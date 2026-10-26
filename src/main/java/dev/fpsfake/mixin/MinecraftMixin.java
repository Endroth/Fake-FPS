package dev.fpsfake.mixin;

import dev.fpsfake.FpsFake;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	/** O F3 lê o FPS por aqui; trocamos só o número mostrado. */
	@Inject(method = "getFps", at = @At("HEAD"), cancellable = true)
	private static void fpsfake$fakeFps(CallbackInfoReturnable<Integer> cir) {
		if (FpsFake.isEnabled()) {
			cir.setReturnValue(FpsFake.nextFakeFps());
		}
	}
}
