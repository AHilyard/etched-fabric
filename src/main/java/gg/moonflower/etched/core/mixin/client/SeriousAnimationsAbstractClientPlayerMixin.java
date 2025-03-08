package gg.moonflower.etched.core.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gg.moonflower.etched.common.item.BoomboxItem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;

import net.mcvader.seriousplayeranimations.torsoPosGetter;

@Mixin(value = AbstractClientPlayer.class, priority = 1001)
public class SeriousAnimationsAbstractClientPlayerMixin
{
	@Inject(method = "animate", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 0, shift = At.Shift.BEFORE, remap = true))
	private void seriousAnimationsBoomboxSupportMainHand(CallbackInfo info)
	{
		AbstractClientPlayer self = (AbstractClientPlayer)(Object)this;
		InteractionHand playingHand = BoomboxItem.getPlayingHand(self);
		if (playingHand == InteractionHand.MAIN_HAND && self instanceof torsoPosGetter posGetter)
		{
			posGetter.disableMainArmB(true);
		}
	}

	@Inject(method = "animate", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 1, shift = At.Shift.BEFORE, remap = true))
	private void seriousAnimationsBoomboxSupportOffHand(CallbackInfo info)
	{
		AbstractClientPlayer self = (AbstractClientPlayer)(Object)this;
		InteractionHand playingHand = BoomboxItem.getPlayingHand(self);
		if (playingHand == InteractionHand.OFF_HAND && self instanceof torsoPosGetter posGetter)
		{
			posGetter.disableOffArmB(true);
		}
	}
}
