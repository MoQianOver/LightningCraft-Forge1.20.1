package org.lightningcraft.integration.tconstruct;

import java.awt.Color;
import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.init.LCFluids;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.ref.Log;
import org.lightningcraft.ref.Metal.Ingot;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.traits.TraitAutosmelt;

public class ToolIntegration {

	private static List<Material> matList;

	public static void mainRegistry() {
		matList = new JointList();
		addMaterials();
	}

	public static Material electricium;
	public static Material skyfather;
	public static Material mystic;

	public static AbstractTrait lightning1 = new TraitLightning(1);
	public static AbstractTrait lightning2 = new TraitLightning(2);
	public static AbstractTrait repairTrait = new TraitRepair();

	private static void addMaterials() {

		Class traitHolder = null;
		try {
			traitHolder = Class.forName("slimeknights.tconstruct.tools.TinkerTraits");
		} catch (ClassNotFoundException e1) {
			try {
				traitHolder = Class.forName("slimeknights.tconstruct.tools.TinkerMaterials");
			} catch (ClassNotFoundException e2) {}
		}
		AbstractTrait autosmelt = null;
		try {
			autosmelt = (AbstractTrait)traitHolder.getField("autosmelt").get(null);
		} catch (Exception e) {}

		if(autosmelt != null) {
			electricium = addMaterial("electricium", LCFluids.moltenElectricium, "Electricium", lightning1);
			skyfather = addMaterial("skyfather", LCFluids.moltenSkyfather, "Skyfather", lightning2, autosmelt);
			mystic = addMaterial("mystic", LCFluids.moltenMystic, "Mystic", lightning2, autosmelt, repairTrait);

			TinkerRegistry.addMaterialStats(electricium,
					new HeadMaterialStats(800, 8.00f, 7.00f, 4),
					new HandleMaterialStats(0.90f, 50),
					new ExtraMaterialStats(100));

			TinkerRegistry.addMaterialStats(skyfather,
					new HeadMaterialStats(1000, 12.00f, 9.00f, 5),
					new HandleMaterialStats(0.90f, 60),
					new ExtraMaterialStats(100));

			TinkerRegistry.addMaterialStats(mystic,
					new HeadMaterialStats(1200, 15.00f, 11.00f, 6),
					new HandleMaterialStats(0.90f, 70),
					new ExtraMaterialStats(100));

		} else {
			Log.logger.error("Unable to find Tinker's Construct trait class, aborting tool integration.");
		}
	}

	private static Material addMaterial(String name, Fluid fluid, String oreSuffix, AbstractTrait... traits) {
		Material mat = new Material(name, fluid.getColor());
		mat.setFluid(fluid);
		mat.addCommonItems(oreSuffix);
		mat.setCraftable(false);
		mat.setCastable(true);
		for(AbstractTrait t : traits) {
			if(t instanceof TraitAutosmelt && !LCConfig.autoSmelt) continue;
			if(t instanceof TraitRepair && !LCConfig.autoRepair) continue;
			mat.addTrait(t);
		}
		TinkerRegistry.addMaterial(mat);
		matList.add(mat);
		return mat;
	}

	public static void registerMaterials() {
		for(Material m : matList) {
			TinkerSmeltery.registerToolpartMeltingCasting(m);
		}
	}

}
