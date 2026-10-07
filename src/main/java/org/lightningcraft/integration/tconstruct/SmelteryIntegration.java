package org.lightningcraft.integration.tconstruct;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.lightningcraft.init.LCFluids;

public class SmelteryIntegration {

	public static void registerFluids() {
		registerTinkersFluid(LCFluids.moltenElectricium, "Electricium");
		registerTinkersFluid(LCFluids.moltenSkyfather, "Skyfather");
		registerTinkersFluid(LCFluids.moltenMystic, "Mystic");
	}

	private static void registerTinkersFluid(Fluid fluid, String oreSuffix) {

		CompoundTag tag = new CompoundTag();
		tag.putString("fluid", fluid.getName());
		tag.putString("ore", oreSuffix);
		tag.putBoolean("toolforge", true);

		FMLInterModComms.sendMessage("tconstruct", "integrateSmeltery", tag);
	}

}
