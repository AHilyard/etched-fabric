package gg.moonflower.etched.core;

import com.tterrag.registrate.Registrate;
import gg.moonflower.etched.api.sound.download.SoundSourceManager;
import gg.moonflower.etched.common.network.EtchedMessages;
import gg.moonflower.etched.common.sound.download.BandcampSource;
import gg.moonflower.etched.common.sound.download.SoundCloudSource;
import gg.moonflower.etched.core.fabric.EtchedConfig;
import gg.moonflower.etched.core.registry.*;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Etched {

    public static final String MOD_ID = "etched";
    public static final Registrate REGISTRATE = Registrate.create(MOD_ID);
    public static final Logger LOGGER = LogManager.getLogger("Etched/General");


    public Etched() {

    }

    public static void init() {
        //i guess following is needed to preload classes before registration or they will
        // not be registered
        EtchedTags.register();
        EtchedMessages.init();
        EtchedBlocks.register();
        EtchedEntities.register();
        EtchedItems.register();
        EtchedMenus.register();
        EtchedRecipes.register();
        EtchedSounds.register();

        REGISTRATE.register();
        EtchedConfig.HANDLER.load();
        SoundSourceManager.registerSource(new SoundCloudSource());
        SoundSourceManager.registerSource(new BandcampSource());

        // register villagers after REGISTRATE so stuff is not air i guess
        EtchedVillagers.register();

        CauldronInteraction.WATER.put(EtchedItems.BLANK_MUSIC_DISC.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.put(EtchedItems.MUSIC_LABEL.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.put(EtchedItems.COMPLEX_MUSIC_LABEL.get(), (state, level, pos, player, hand, stack) -> {
            if (!level.isClientSide()) {
                stack.removeTagKey("Label");
                ItemStack newStack = new ItemStack(EtchedItems.MUSIC_LABEL.get());
                newStack.setCount(stack.getCount());
                newStack.setTag(stack.getTag());

                player.setItemInHand(hand, newStack);
                player.awardStat(Stats.CLEAN_ARMOR);
                LayeredCauldronBlock.lowerFillLevel(state, level, pos);
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        });
    }

}

