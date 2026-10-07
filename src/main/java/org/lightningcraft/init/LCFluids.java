package org.lightningcraft.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;
import org.lightningcraft.fluids.BlockFluidLC;
import org.lightningcraft.fluids.LCFluidType;
import org.lightningcraft.fluids.ModFluid;
import org.lightningcraft.ref.RefStrings;

public class LCFluids {

	public static final List<MoltenFluid> ALL = new ArrayList<>();

	private static final ResourceLocation STILL_TEXTURE =
			new ResourceLocation(RefStrings.MODID, "block/molten_metal_still");
	private static final ResourceLocation FLOWING_TEXTURE =
			new ResourceLocation(RefStrings.MODID, "block/molten_metal_flow");

	public static MoltenFluid moltenElectricium;
	public static MoltenFluid moltenSkyfather;
	public static MoltenFluid moltenMystic;

	public static void createFluids() {
		ALL.clear();
		moltenElectricium = addFluid("electricium", 2000, 10000, 800, 12, 0xFF20FAFA);
		moltenSkyfather    = addFluid("skyfather",  2000, 10000, 900, 12, 0xFF343434);
		moltenMystic       = addFluid("mystic",     2000, 10000, 1000, 12, 0xFFB40000);
	}

	private static MoltenFluid addFluid(String name, int density, int viscosity,
			int temperature, int luminosity, int tintColor) {
		MoltenFluid fluid = new MoltenFluid(name, density, viscosity, temperature, luminosity, tintColor);
		ALL.add(fluid);
		return fluid;
	}

	public static void registerRendering() {

	}

	public static final class MoltenFluid {

		public final RegistryObject<FluidType> type;

		public final RegistryObject<Fluid> still;

		public final RegistryObject<Fluid> flowing;

		public RegistryObject<LiquidBlock> block;
		public RegistryObject<Item> bucket;
		public ForgeFlowingFluid.Properties properties;

		private MoltenFluid(String name, int density, int viscosity,
				int temperature, int luminosity, int tintColor) {

			this.type = LCRegistry.FLUID_TYPES.register(name,
					() -> new LCFluidType(STILL_TEXTURE, FLOWING_TEXTURE, tintColor,
							density, viscosity, temperature, luminosity));

			this.still = LCRegistry.FLUIDS.register(name,
					() -> new ModFluid.Source(this.properties));
			this.flowing = LCRegistry.FLUIDS.register(name + "_flowing",
					() -> new ModFluid.Flowing(this.properties));

			this.properties = new ForgeFlowingFluid.Properties(this.type, this.still, this.flowing)
					.block(() -> this.block.get())
					.bucket(() -> this.bucket.get())
					.slopeFindDistance(4)
					.levelDecreasePerBlock(1)
					.tickRate(10)
					.explosionResistance(100.0F);

			this.block = LCRegistry.BLOCKS.register(name,
					() -> new BlockFluidLC((FlowingFluid) this.still.get()));

			this.bucket = LCRegistry.ITEMS.register(name + "_bucket",
					() -> new BucketItem(this.still.get(),
							new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
		}

		public Fluid still() {
			return this.still.get();
		}

		public LiquidBlock block() {
			return this.block.get();
		}
	}
}
