package org.lightningcraft.config;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import org.lightningcraft.integration.energy.EnergyApiHelper;
import org.lightningcraft.ref.Log;

public class LCConfig {

	public static boolean portalEnabled = true;
	public static int portalCooldown = 200;
	public static boolean demonSoldiersAlwaysNeutral = false;
	public static boolean demonSoldiersInNether = true;
	public static boolean demonSoldiersFullPower = false;
	public static boolean useVanillaGhastSounds = false;

	public static boolean upgradeEnabled = true;
	public static String[] minerFillerBlocks = defaultFillerBlocks();
	public static int minerMaxRetries = 256;

	public static boolean autoSmelt = true;
	public static boolean autoRepair = true;
	public static boolean mysticGear = true;

	public static int underworldDimensionID = -9;
	public static boolean JEIIntegration = true;
	public static boolean RFIntegration = true;
	public static int RFtoLEConversion = 50;
	public static boolean tinkersIntegration = true;
	public static boolean disableOtherRods = true;
	public static boolean chiselIntegration = true;
	public static boolean chiselCorruptStone = true;

	public static final ForgeConfigSpec SPEC;

	private static final ForgeConfigSpec.BooleanValue PORTAL_ENABLED;
	private static final ForgeConfigSpec.IntValue PORTAL_COOLDOWN;
	private static final ForgeConfigSpec.BooleanValue DEMON_SOLDIERS_ALWAYS_NEUTRAL;
	private static final ForgeConfigSpec.BooleanValue DEMON_SOLDIERS_IN_NETHER;
	private static final ForgeConfigSpec.BooleanValue DEMON_SOLDIERS_FULL_POWER;
	private static final ForgeConfigSpec.BooleanValue USE_VANILLA_GHAST_SOUNDS;

	private static final ForgeConfigSpec.BooleanValue UPGRADE_ENABLED;
	private static final ForgeConfigSpec.ConfigValue<List<? extends String>> MINER_FILLER_BLOCKS;
	private static final ForgeConfigSpec.IntValue MINER_MAX_RETRIES;

	private static final ForgeConfigSpec.BooleanValue AUTO_SMELT;
	private static final ForgeConfigSpec.BooleanValue AUTO_REPAIR;
	private static final ForgeConfigSpec.BooleanValue MYSTIC_GEAR;

	private static final ForgeConfigSpec.IntValue UNDERWORLD_DIMENSION_ID;

	private static final ForgeConfigSpec.IntValue UNDERWORLD_NOISE_SCALE;
	private static final ForgeConfigSpec.BooleanValue JEI_INTEGRATION;
	private static final ForgeConfigSpec.BooleanValue RF_INTEGRATION;
	private static final ForgeConfigSpec.IntValue RF_TO_LE_CONVERSION;
	private static final ForgeConfigSpec.BooleanValue TINKERS_INTEGRATION;
	private static final ForgeConfigSpec.BooleanValue DISABLE_OTHER_RODS;
	private static final ForgeConfigSpec.BooleanValue CHISEL_INTEGRATION;
	private static final ForgeConfigSpec.BooleanValue CHISEL_CORRUPT_STONE;

	static {
		ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

		b.comment("General").push("general");
		PORTAL_ENABLED = b.comment("Set to false to disable default portal creation.")
				.define("portalEnabled", true);
		PORTAL_COOLDOWN = b.comment("The cooldown time for the underworld portal. Increase if repeated teleporting occurs.")
				.defineInRange("portalCooldown", 200, 0, Integer.MAX_VALUE);
		DEMON_SOLDIERS_ALWAYS_NEUTRAL = b.comment("Set to true if Demon Soldiers should only attack the player if they are attacked first")
				.define("demonSoldiersAlwaysNeutral", false);
		DEMON_SOLDIERS_IN_NETHER = b.comment("Set to false if Demon Soldiers should not spawn in the Nether")
				.define("demonSoldiersInNether", true);
		DEMON_SOLDIERS_FULL_POWER = b.comment("Set to true if Demon Soldiers should NOT be weaker outside of the Underworld")
				.define("demonSoldiersFullPower", false);
		USE_VANILLA_GHAST_SOUNDS = b.comment("Whether or not to use the vanilla ghast sounds for the Underghast instead of the provided ones")
				.define("useVanillaGhastSounds", false);
		b.pop();

		b.comment("Machines").push("machines");
		UPGRADE_ENABLED = b.comment("If false, right clicking machines with the Lightning Upgrade will do nothing")
				.define("upgradeEnabled", true);
		MINER_FILLER_BLOCKS = b.comment("This is the list of blocks the miner considers filler. "
						+ "It will optionally use these to fill its mined area, and won't mine these blocks.")
				.defineList("minerFillerBlocks", List.of(defaultFillerBlocks()), o -> o instanceof String);
		MINER_MAX_RETRIES = b.comment("The max amount of mining retries. Decrease if StackOverflowExceptions occur.")
				.defineInRange("minerMaxRetries", 256, 0, Integer.MAX_VALUE);
		b.pop();

		b.comment("Tools and Armor").push("tools");
		AUTO_SMELT = b.comment("Whether or not Skyfather and Mystic tools will auto-smelt mined blocks.")
				.define("autoSmelt", true);
		AUTO_REPAIR = b.comment("Whether or not Mystic tools will auto-repair.")
				.define("autoRepair", true);
		MYSTIC_GEAR = b.comment("If false, Mystic gear will not be able to be created.")
				.define("mysticGear", true);
		b.pop();

		b.comment("Worldgen").push("worldgen");
		UNDERWORLD_DIMENSION_ID = b.comment("The ID for the Underworld dimension")
				.defineInRange("underworldDimensionID", -9, Integer.MIN_VALUE, Integer.MAX_VALUE);

		UNDERWORLD_NOISE_SCALE = b.defineInRange("underworldNoiseScale", 2048, 0, 65536);
		b.pop();

		b.comment("Mod Integration").push("integration");
		JEI_INTEGRATION = b.comment("Enable JEI integration?").define("JEIIntegration", true);
		RF_INTEGRATION = b.comment("Enable LE <-> RF / TESLA conversion machines?").define("RFIntegration", true);
		RF_TO_LE_CONVERSION = b.comment("This amount of RF / TESLA is equal to 1 LE (for 1 LE -> x RF conversion). "
						+ "The reverse will be 10 times costlier.")
				.defineInRange("RFtoLEConversion", 50, 1, Integer.MAX_VALUE);
		TINKERS_INTEGRATION = b.comment("Enable smeltery support for electricium, skyfather, and mystic metals")
				.define("tinkersIntegration", true);
		DISABLE_OTHER_RODS = b.comment("Will remove the crafting recipes of all other mods' rods that LightningCraft has its own versions of.")
				.define("disableOtherRods", true);
		CHISEL_INTEGRATION = b.comment("Enable support for chiseling several LightningCraft block types")
				.define("chiselIntegration", true);
		CHISEL_CORRUPT_STONE = b.comment("Should one be able to chisel stone and stone bricks to corrupt stone? "
						+ "Will only apply if chisel integration is enabled.")
				.define("chiselCorruptStone", true);
		b.pop();

		SPEC = b.build();
	}

	@net.minecraftforge.eventbus.api.SubscribeEvent
	public static void onConfigEvent(net.minecraftforge.fml.event.config.ModConfigEvent event) {
		if (event.getConfig().getSpec() == SPEC) {
			load();
		}
	}

	public static void load() {
		portalEnabled = PORTAL_ENABLED.get();
		portalCooldown = PORTAL_COOLDOWN.get();
		demonSoldiersAlwaysNeutral = DEMON_SOLDIERS_ALWAYS_NEUTRAL.get();
		demonSoldiersInNether = DEMON_SOLDIERS_IN_NETHER.get();
		demonSoldiersFullPower = DEMON_SOLDIERS_FULL_POWER.get();
		useVanillaGhastSounds = USE_VANILLA_GHAST_SOUNDS.get();

		upgradeEnabled = UPGRADE_ENABLED.get();
		minerFillerBlocks = MINER_FILLER_BLOCKS.get().toArray(new String[0]);
		minerMaxRetries = MINER_MAX_RETRIES.get();

		autoSmelt = AUTO_SMELT.get();
		autoRepair = AUTO_REPAIR.get();
		mysticGear = MYSTIC_GEAR.get();

		underworldDimensionID = UNDERWORLD_DIMENSION_ID.get();

		JEIIntegration = JEI_INTEGRATION.get();
		RFIntegration = RF_INTEGRATION.get();
		if (!RFIntegration) EnergyApiHelper.rfOrTeslaLoaded = false;
		RFtoLEConversion = RF_TO_LE_CONVERSION.get();
		tinkersIntegration = TINKERS_INTEGRATION.get();
		disableOtherRods = DISABLE_OTHER_RODS.get();
		chiselIntegration = CHISEL_INTEGRATION.get();
		chiselCorruptStone = CHISEL_CORRUPT_STONE.get();

		Log.logger.info("Config loaded.");
	}

	private static String[] defaultFillerBlocks() {
		return new String[] {
				BuiltInRegistries.BLOCK.getKey(Blocks.COBBLESTONE).toString(),
				BuiltInRegistries.BLOCK.getKey(Blocks.DIRT).toString(),
				BuiltInRegistries.BLOCK.getKey(Blocks.SAND).toString()
		};
	}

	public static int getUnderworldNoiseScale() {
		try {
			return UNDERWORLD_NOISE_SCALE.get();
		} catch (Throwable t) {
			return 2048;
		}
	}

}
