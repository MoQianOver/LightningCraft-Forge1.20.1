package org.lightningcraft.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.lightningcraft.tiles.TileEntityChargingPlate;
import org.lightningcraft.tiles.TileEntityEnchReallocator;
import org.lightningcraft.tiles.TileEntityEnergyProvider;
import org.lightningcraft.tiles.TileEntityEnergyReceiver;
import org.lightningcraft.tiles.TileEntityLightningBreaker;
import org.lightningcraft.tiles.TileEntityLightningCannon;
import org.lightningcraft.tiles.TileEntityLightningCell;
import org.lightningcraft.tiles.TileEntityLightningCrusher;
import org.lightningcraft.tiles.TileEntityLightningFurnace;
import org.lightningcraft.tiles.TileEntityLightningInfuser;
import org.lightningcraft.tiles.TileEntityLightningMiner;
import org.lightningcraft.tiles.TileEntityLightningReceiver;
import org.lightningcraft.tiles.TileEntityLightningTransmitter;
import org.lightningcraft.tiles.TileEntityStaticGenerator;

public class LCTileEntities {

	public static final DeferredRegister<BlockEntityType<?>> TILES = LCRegistry.BLOCK_ENTITIES;

	public static final RegistryObject<BlockEntityType<TileEntityLightningCell>> LIGHTNING_CELL =
		TILES.register("lightning_cell", () -> BlockEntityType.Builder
			.of(TileEntityLightningCell::new, LCBlocks.lightningCell).build(null));

	public static final RegistryObject<BlockEntityType<TileEntityLightningFurnace>> LIGHTNING_FURNACE =
		TILES.register("lightning_furnace", () -> BlockEntityType.Builder
			.of(TileEntityLightningFurnace::new, LCBlocks.lightningFurnace).build(null));

	public static final RegistryObject<BlockEntityType<TileEntityLightningCrusher>> LIGHTNING_CRUSHER =
		TILES.register("lightning_crusher", () -> BlockEntityType.Builder
			.of(TileEntityLightningCrusher::new, LCBlocks.lightningCrusher).build(null));

	public static final RegistryObject<BlockEntityType<TileEntityLightningInfuser>> LIGHTNING_INFUSER =
		TILES.register("lightning_infuser", () -> BlockEntityType.Builder
			.of(TileEntityLightningInfuser::new, LCBlocks.lightningInfuser).build(null));

	public static final RegistryObject<BlockEntityType<TileEntityLightningBreaker>> LIGHTNING_BREAKER =
		TILES.register("lightning_breaker", () -> BlockEntityType.Builder
			.of(TileEntityLightningBreaker::new, LCBlocks.lightningBreaker).build(null));

	public static final RegistryObject<BlockEntityType<TileEntityLightningMiner>> LIGHTNING_MINER =
		TILES.register("lightning_miner", () -> BlockEntityType.Builder
			.of(TileEntityLightningMiner::new, LCBlocks.lightningMiner).build(null));

	public static final RegistryObject<BlockEntityType<TileEntityStaticGenerator>> STATIC_GENERATOR =
		TILES.register("static_generator", () -> BlockEntityType.Builder
			.of(TileEntityStaticGenerator::new, LCBlocks.staticGenerator).build(null));

	public static final RegistryObject<BlockEntityType<TileEntityChargingPlate>> CHARGING_PLATE =
		TILES.register("charging_plate", () -> BlockEntityType.Builder
			.of(TileEntityChargingPlate::new, LCBlocks.chargingPlate).build(null));

	public static final RegistryObject<BlockEntityType<TileEntityEnchReallocator>> ENCH_REALLOCATOR =
		TILES.register("ench_reallocator", () -> BlockEntityType.Builder
			.of(TileEntityEnchReallocator::new, LCBlocks.enchReallocator).build(null));

	public static final RegistryObject<BlockEntityType<TileEntityLightningCannon>> LIGHTNING_CANNON =
		TILES.register("lightning_cannon", () -> BlockEntityType.Builder
			.of(TileEntityLightningCannon::new, LCBlocks.lightningCannon).build(null));

	public static final RegistryObject<BlockEntityType<TileEntityLightningTransmitter>> LIGHTNING_TRANSMITTER =
		TILES.register("lightning_transmitter", () -> BlockEntityType.Builder
			.of(TileEntityLightningTransmitter::new, LCBlocks.wirelessBlock).build(null));

	public static final RegistryObject<BlockEntityType<TileEntityLightningReceiver>> LIGHTNING_RECEIVER =
		TILES.register("lightning_receiver", () -> BlockEntityType.Builder
			.of(TileEntityLightningReceiver::new, LCBlocks.wirelessBlock).build(null));

	public static final RegistryObject<BlockEntityType<TileEntityEnergyProvider>> ENERGY_PROVIDER =
		TILES.register("energy_provider", () -> BlockEntityType.Builder
			.of(TileEntityEnergyProvider::new, LCBlocks.rfProvider).build(null));

	public static final RegistryObject<BlockEntityType<TileEntityEnergyReceiver>> ENERGY_RECEIVER =
		TILES.register("energy_receiver", () -> BlockEntityType.Builder
			.of(TileEntityEnergyReceiver::new, LCBlocks.rfReceiver).build(null));

	public static void mainRegistry() {

	}
}
