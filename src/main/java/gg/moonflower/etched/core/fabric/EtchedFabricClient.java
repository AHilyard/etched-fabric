package gg.moonflower.etched.core.fabric;

import gg.moonflower.etched.common.item.EtchedMusicDiscItem;
import gg.moonflower.etched.common.network.EtchedMessages;
import net.fabricmc.api.ClientModInitializer;

//import gg.moonflower.etched.common.entity.MinecartJukebox;
import gg.moonflower.etched.core.EtchedClient;
import net.fabricmc.fabric.api.event.client.player.ClientPickBlockGatherCallback;
import net.fabricmc.fabric.mixin.object.builder.client.ModelPredicateProviderRegistryAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;

public class EtchedFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        EtchedMessages.init();
        EtchedMessages.initClient();
        EtchedClient.registerItemGroups();

        ClientPickBlockGatherCallback.EVENT.register((player, result) -> {
            if (result.getType() == HitResult.Type.ENTITY && player.getAbilities().instabuild) {
                //FIXME
                //Entity entity = ((EntityHitResult) result).getEntity();
                //if (entity instanceof MinecartJukebox minecart) {
                //    return new ItemStack(minecart.getDropItem());
                //}
            }
            return ItemStack.EMPTY;
        });
        EtchedClient.registerItemColors();

		// Register the pattern property for music discs.
		ModelPredicateProviderRegistryAccessor.callRegister(new ResourceLocation("etched", "pattern"), 
			(itemStack, world, entity, seed) -> { return EtchedMusicDiscItem.getPattern(itemStack).ordinal() / 10.0f; });
    }
}
