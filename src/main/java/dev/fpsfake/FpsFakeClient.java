package dev.fpsfake;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.Command;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class FpsFakeClient implements ClientModInitializer {

	public static final String MOD_ID = "fpsfake";

	private static final KeyMapping.Category CATEGORY =
			KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"));

	private static KeyMapping toggleKey;

	@Override
	public void onInitializeClient() {
		FpsFake.load();

		// Tecla K (dá pra mudar em Opções > Controles)
		toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.fpsfake.toggle",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_K,
				CATEGORY
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleKey.consumeClick()) {
				boolean now = FpsFake.toggle();
				if (client.player != null) {
					client.player.displayClientMessage(status(now), true);
				}
			}
		});

		// /fpsfake  |  /fpsfake on  |  /fpsfake off
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				dispatcher.register(ClientCommandManager.literal("fpsfake")
						.executes(ctx -> {
							ctx.getSource().sendFeedback(status(FpsFake.toggle()));
							return Command.SINGLE_SUCCESS;
						})
						.then(ClientCommandManager.literal("on").executes(ctx -> {
							FpsFake.setEnabled(true);
							ctx.getSource().sendFeedback(status(true));
							return Command.SINGLE_SUCCESS;
						}))
						.then(ClientCommandManager.literal("off").executes(ctx -> {
							FpsFake.setEnabled(false);
							ctx.getSource().sendFeedback(status(false));
							return Command.SINGLE_SUCCESS;
						}))));
	}

	private static Component status(boolean on) {
		return Component.literal("FPS Fake: ").withStyle(ChatFormatting.GRAY)
				.append(Component.literal(on ? "LIGADO" : "DESLIGADO")
						.withStyle(on ? ChatFormatting.GREEN : ChatFormatting.RED));
	}
}
