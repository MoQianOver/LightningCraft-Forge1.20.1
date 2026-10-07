package org.lightningcraft.init;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.registry.ILightningCraftBlock;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.blocks.BlockAirTerminal;
import org.lightningcraft.blocks.BlockChargingPlate;
import org.lightningcraft.blocks.BlockDoubleSlabLC;
import org.lightningcraft.blocks.BlockEnchReallocator;
import org.lightningcraft.blocks.BlockEnergyProvider;
import org.lightningcraft.blocks.BlockEnergyReceiver;
import org.lightningcraft.blocks.BlockLight;
import org.lightningcraft.blocks.BlockLightningBreaker;
import org.lightningcraft.blocks.BlockLightningCannon;
import org.lightningcraft.blocks.BlockLightningCell;
import org.lightningcraft.blocks.BlockLightningCrusher;
import org.lightningcraft.blocks.BlockLightningFurnace;
import org.lightningcraft.blocks.BlockLightningInfuser;
import org.lightningcraft.blocks.BlockLightningMiner;
import org.lightningcraft.blocks.BlockLogLC;
import org.lightningcraft.blocks.BlockMetal;
import org.lightningcraft.blocks.BlockPlankLC;
import org.lightningcraft.blocks.BlockSlabLC;
import org.lightningcraft.blocks.BlockStairsLC;
import org.lightningcraft.blocks.BlockStaticGenerator;
import org.lightningcraft.blocks.BlockStone;
import org.lightningcraft.blocks.BlockUnderOre;
import org.lightningcraft.blocks.BlockUnderSand;
import org.lightningcraft.blocks.BlockUnderTNT;
import org.lightningcraft.blocks.BlockWallLC;
import org.lightningcraft.blocks.BlockWeakStone;
import org.lightningcraft.blocks.BlockWireless;
import org.lightningcraft.blocks.PortalUnderworld;
import org.lightningcraft.blocks.base.BlockLC;
import org.lightningcraft.render.BlockColoring;

public class LCBlocks {

	private static final JointList<ILightningCraftBlock> blocks = new JointList<>();

	public static BlockLC metalBlock;
	public static BlockLC stoneBlock;
	public static BlockLogLC woodLog;
	public static BlockLC woodPlank;
	public static BlockSlabLC slabBlock;
	public static BlockSlabLC slabBlockDouble;
	public static BlockStairsLC thunderStairs;
	public static BlockStairsLC demonStairs;
	public static BlockStairsLC underStairs;
	public static BlockStairsLC underPlankStairs;
	public static BlockLC airTerminal;
	public static BlockLC lightningCell;
	public static BlockLC lightningFurnace;
	public static BlockLC lightningCrusher;
	public static BlockLC lightningInfuser;
	public static BlockLC lightningBreaker;
	public static BlockLC lightningMiner;
	public static BlockLC lightningConduit;
	public static BlockLC staticGenerator;
	public static BlockLC wirelessBlock;
	public static BlockChargingPlate chargingPlate;
	public static BlockLC enchReallocator;
	public static PortalUnderworld underPortal;
	public static BlockUnderSand underSand;
	public static BlockLC corruptStone;
	public static BlockWallLC wallBlock;
	public static BlockLC lightBlock;
	public static BlockLC oreBlock;
	public static BlockUnderTNT underTNT;
	public static BlockLC lightningCannon;
	public static BlockLC rfProvider;
	public static BlockLC rfReceiver;

	private static <T extends Block & ILightningCraftBlock> T track(String name, T block) {
		blocks.add(block);

		Class<? extends net.minecraft.world.item.BlockItem> itemClass = block.getItemClass();
		if (itemClass != null) {
			LCRegistry.ITEMS.register(name, () -> createBlockItem(block, itemClass));
		}
		return block;
	}

	private static net.minecraft.world.item.BlockItem createBlockItem(Block block,
			Class<? extends net.minecraft.world.item.BlockItem> itemClass) {
		Object[] args = ((ILightningCraftBlock) block).getItemClassArgs();

		if (itemClass == org.lightningcraft.items.blocks.ItemBlockMeta.class) {

			int count = block instanceof org.lightningcraft.blocks.base.BlockVariant bv
					? bv.getSubBlockCount() : 1;
			return new org.lightningcraft.items.blocks.ItemBlockMeta(block, count);
		}
		if (itemClass == org.lightningcraft.items.blocks.ItemBlockRarity.class) {
			return new org.lightningcraft.items.blocks.ItemBlockRarity(block,
					(net.minecraft.world.item.Rarity) args[0]);
		}

		try {
			return itemClass.getConstructor(Block.class, net.minecraft.world.item.Item.Properties.class)
					.newInstance(block, new net.minecraft.world.item.Item.Properties());
		} catch (NoSuchMethodException ignored) {

		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("构造方块物品失败 " + itemClass, e);
		}
		try {
			return itemClass.getConstructor(Block.class).newInstance(block);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("无法为方块 " + block + " 构造物品 " + itemClass
					+ "（既没有 (Block, Item.Properties) 也没有 (Block) 构造器）", e);
		}
	}

	public static void createBlocks() {
		LCRegistry.BLOCKS.register("metal_block", () -> track("metal_block", metalBlock = new BlockMetal()));
		LCRegistry.BLOCKS.register("stone_block", () -> track("stone_block", stoneBlock = new BlockStone()));
		LCRegistry.BLOCKS.register("wood_log", () -> track("wood_log", woodLog = new BlockLogLC()));
		LCRegistry.BLOCKS.register("wood_plank", () -> track("wood_plank", woodPlank = new BlockPlankLC()));
		LCRegistry.BLOCKS.register("slab_block", () -> track("slab_block", slabBlock = new BlockSlabLC()));

		LCRegistry.BLOCKS.register("slab_block_double", () -> track("slab_block_double", slabBlockDouble = new BlockDoubleSlabLC()));
		LCRegistry.BLOCKS.register("thunder_stairs", () -> track("thunder_stairs", thunderStairs = new BlockStairsLC()));
		LCRegistry.BLOCKS.register("demon_stairs", () -> track("demon_stairs", demonStairs = new BlockStairsLC()));
		LCRegistry.BLOCKS.register("under_stairs", () -> track("under_stairs", underStairs = new BlockStairsLC()));
		LCRegistry.BLOCKS.register("under_plank_stairs", () -> {
			underPlankStairs = new BlockStairsLC(woodPlank);
			return track("under_plank_stairs", underPlankStairs);
		});
		LCRegistry.BLOCKS.register("air_terminal", () -> track("air_terminal", airTerminal = new BlockAirTerminal()));
		LCRegistry.BLOCKS.register("lightning_cell", () -> track("lightning_cell", lightningCell = new BlockLightningCell()));
		LCRegistry.BLOCKS.register("lightning_furnace", () -> track("lightning_furnace", lightningFurnace = new BlockLightningFurnace()));
		LCRegistry.BLOCKS.register("lightning_crusher", () -> track("lightning_crusher", lightningCrusher = new BlockLightningCrusher()));
		LCRegistry.BLOCKS.register("lightning_infuser", () -> track("lightning_infuser", lightningInfuser = new BlockLightningInfuser()));
		LCRegistry.BLOCKS.register("lightning_breaker", () -> track("lightning_breaker", lightningBreaker = new BlockLightningBreaker()));
		LCRegistry.BLOCKS.register("lightning_miner", () -> track("lightning_miner", lightningMiner = new BlockLightningMiner()));
		LCRegistry.BLOCKS.register("static_generator", () -> track("static_generator", staticGenerator = new BlockStaticGenerator()));
		LCRegistry.BLOCKS.register("wireless_block", () -> track("wireless_block", wirelessBlock = new BlockWireless()));
		LCRegistry.BLOCKS.register("charging_plate", () -> track("charging_plate", chargingPlate = new BlockChargingPlate()));
		LCRegistry.BLOCKS.register("ench_reallocator", () -> track("ench_reallocator", enchReallocator = new BlockEnchReallocator()));
		LCRegistry.BLOCKS.register("under_portal", () -> track("under_portal", underPortal = new PortalUnderworld()));
		LCRegistry.BLOCKS.register("under_sand", () -> track("under_sand", underSand = new BlockUnderSand()));
		LCRegistry.BLOCKS.register("corrupt_stone", () -> track("corrupt_stone", corruptStone = new BlockWeakStone()));
		LCRegistry.BLOCKS.register("wall_block", () -> track("wall_block", wallBlock = new BlockWallLC()));
		LCRegistry.BLOCKS.register("light_block", () -> track("light_block", lightBlock = new BlockLight()));
		LCRegistry.BLOCKS.register("ore_block", () -> track("ore_block", oreBlock = new BlockUnderOre()));
		LCRegistry.BLOCKS.register("under_tnt", () -> track("under_tnt", underTNT = new BlockUnderTNT()));
		LCRegistry.BLOCKS.register("lightning_cannon", () -> track("lightning_cannon", lightningCannon = new BlockLightningCannon()));
		LCRegistry.BLOCKS.register("rf_provider", () -> track("rf_provider", rfProvider = new BlockEnergyProvider()));
		LCRegistry.BLOCKS.register("rf_receiver", () -> track("rf_receiver", rfReceiver = new BlockEnergyReceiver()));
	}

	public static void registerBlocks() {

	}

	@OnlyIn(Dist.CLIENT)
	public static void registerRendering() {

	}

	@OnlyIn(Dist.CLIENT)
	public static void registerBlockColors() {
		for(ILightningCraftBlock block : blocks) BlockColoring.registerBlock(block);
	}

}
