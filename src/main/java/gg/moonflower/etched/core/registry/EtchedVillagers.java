package gg.moonflower.etched.core.registry;

import com.google.common.collect.ImmutableSet;
import gg.moonflower.etched.core.Etched;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;
//TODO: FIX
public class EtchedVillagers {

	private static Set<BlockState> getBlockStates(Block block) {
        return ImmutableSet.copyOf((Collection<BlockState>)block.getStateDefinition().getPossibleStates());
    }
    public static final PoiType BARD_POI = PointOfInterestHelper.register(new ResourceLocation(Etched.MOD_ID,"bard"),1,1,getBlockStates(EtchedBlocks.ETCHING_TABLE.get()));
    public static final VillagerProfession BARD_PROFESSION = Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, (new ResourceLocation(Etched.MOD_ID,"bard")), new VillagerProfession(Etched.MOD_ID + ":bard", poi -> poi.value().equals(BARD_POI), poi -> poi.value().equals(BARD_POI), ImmutableSet.of(), ImmutableSet.of(), null));

    public static void register(){
        TradeOfferHelper.registerVillagerOffers(BARD_PROFESSION,1,itemListings -> {
            itemListings.add(new VillagerTrades.EmeraldForItems(Items.MUSIC_DISC_13,8,4,20));
            itemListings.add(new VillagerTrades.EmeraldForItems(Items.MUSIC_DISC_11,8,4,20));
            itemListings.add(new VillagerTrades.EmeraldForItems(Items.MUSIC_DISC_CAT,8,4,20));
            itemListings.add(new VillagerTrades.EmeraldForItems(Items.MUSIC_DISC_OTHERSIDE,8,4,20));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(Items.NOTE_BLOCK,1, 2, 16, 2));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(EtchedItems.MUSIC_LABEL.get()), 4, 2, 16, 1));
        });
        TradeOfferHelper.registerVillagerOffers(BARD_PROFESSION,2,itemListings -> {
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(EtchedItems.BLANK_MUSIC_DISC.get(), 1), 12, 2, 12, 15));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(EtchedBlocks.ETCHING_TABLE.get(), 1), 12, 2, 12, 15));
        });
        TradeOfferHelper.registerVillagerOffers(BARD_PROFESSION,3,itemListings -> {
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(Blocks.CLAY), 6, 1, 16, 2));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(Blocks.HAY_BLOCK), 12, 1, 8, 2));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(Blocks.WHITE_WOOL), 8, 1, 32, 4));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(Blocks.BONE_BLOCK), 24, 1, 8, 4));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(Blocks.PACKED_ICE), 36, 1, 4, 8));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(Blocks.GOLD_BLOCK), 48, 1, 2, 10));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.JUKEBOX), 26, 1, 4, 30));
        });

        TradeOfferHelper.registerVillagerOffers(BARD_PROFESSION,4,itemListings -> {
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(EtchedItems.JUKEBOX_MINECART.get()), 28, 1, 4, 30));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(EtchedBlocks.ALBUM_JUKEBOX.get()), 30, 1, 4, 30));
        });
        TradeOfferHelper.registerVillagerOffers(BARD_PROFESSION,5,itemListings -> {
            itemListings.add(new VillagerTrades.EmeraldForItems(Items.DIAMOND, 1, 8, 40));
            itemListings.add(new VillagerTrades.EmeraldForItems(Items.AMETHYST_SHARD,  1, 8, 40));
        });
    }
}