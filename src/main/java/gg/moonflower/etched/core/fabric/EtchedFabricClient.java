package gg.moonflower.etched.core.fabric;

import gg.moonflower.etched.client.render.item.AlbumCoverItemRenderer;
import gg.moonflower.etched.common.item.BoomboxItem;
import gg.moonflower.etched.common.item.EtchedMusicDiscItem;
import gg.moonflower.etched.common.network.EtchedMessages;
import net.fabricmc.api.ClientModInitializer;
import gg.moonflower.etched.core.Etched;
//import gg.moonflower.etched.common.entity.MinecartJukebox;
import gg.moonflower.etched.core.EtchedClient;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.event.client.player.ClientPickBlockGatherCallback;
import net.fabricmc.fabric.mixin.object.builder.client.ModelPredicateProviderRegistryAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
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

		// Register the playing property for boomboxes.
		ModelPredicateProviderRegistryAccessor.callRegister(new ResourceLocation("etched", "playing"),
			(itemStack, world, entity, seed) -> {
				if (!(entity instanceof Player)) {
                    return 0;
                }
                InteractionHand hand = BoomboxItem.getPlayingHand(entity);
                return hand != null && itemStack == entity.getItemInHand(hand) ? 1 : 0;
			});

		// Register our custom models.
		ModelLoadingPlugin.register(new EtchedModelLoadingPlugin());
    }

	public class EtchedModelLoadingPlugin implements ModelLoadingPlugin {

		@Override
		public void onInitializeModelLoader(Context pluginContext)
		{
			ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
			String folder = "models/item/" + AlbumCoverItemRenderer.FOLDER_NAME;
			pluginContext.addModels(new ModelResourceLocation(new ResourceLocation(Etched.MOD_ID, "boombox_in_hand"), "inventory"));
			for (ResourceLocation location : resourceManager.listResources(folder, name -> name.getPath().endsWith(".json")).keySet())
			{
				pluginContext.addModels(new ModelResourceLocation(new ResourceLocation(location.getNamespace(), location.getPath().substring(12, location.getPath().length() - 5)), "inventory"));
			}
		}

	}
}
