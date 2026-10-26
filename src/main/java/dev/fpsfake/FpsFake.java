package dev.fpsfake;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public final class FpsFake {

	/** Centro do FPS falso e o quanto ele varia pra cima/baixo. Edite à vontade. */
	private static final int BASE_FPS = 5_000_000;
	private static final int SPREAD = 1_000_000;
	/** A cada quantos ms o número muda. */
	private static final long CHANGE_EVERY_MS = 100;

	private static final Path CONFIG = FabricLoader.getInstance().getConfigDir().resolve("fpsfake.properties");

	private static volatile boolean enabled = true;
	private static int current = BASE_FPS;
	private static long nextChangeAt = 0;

	private FpsFake() {}

	public static boolean isEnabled() {
		return enabled;
	}

	public static void setEnabled(boolean value) {
		enabled = value;
		save();
	}

	public static boolean toggle() {
		setEnabled(!enabled);
		return enabled;
	}

	public static int nextFakeFps() {
		long now = System.currentTimeMillis();
		if (now >= nextChangeAt) {
			nextChangeAt = now + CHANGE_EVERY_MS;
			current = BASE_FPS + ThreadLocalRandom.current().nextInt(-SPREAD, SPREAD + 1);
		}
		return current;
	}

	public static void load() {
		if (!Files.exists(CONFIG)) return;
		try (InputStream in = Files.newInputStream(CONFIG)) {
			Properties props = new Properties();
			props.load(in);
			enabled = Boolean.parseBoolean(props.getProperty("enabled", "true"));
		} catch (IOException ignored) {
		}
	}

	private static void save() {
		try (OutputStream out = Files.newOutputStream(CONFIG)) {
			Properties props = new Properties();
			props.setProperty("enabled", Boolean.toString(enabled));
			props.store(out, "FPS Fake");
		} catch (IOException ignored) {
		}
	}
}
