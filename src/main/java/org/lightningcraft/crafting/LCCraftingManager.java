package org.lightningcraft.crafting;

import org.lightningcraft.util.LCStack;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.api.util.LCOreDictionary;
import org.lightningcraft.blocks.BlockLight;
import org.lightningcraft.blocks.BlockSlabLC;
import org.lightningcraft.blocks.BlockStone;
import org.lightningcraft.blocks.BlockUnderOre;
import org.lightningcraft.blocks.BlockUnderTNT;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.integration.energy.EnergyApiHelper;
import org.lightningcraft.recipes.LCFuelHandler;
import org.lightningcraft.ref.Material;
import org.lightningcraft.ref.Metal;
import org.lightningcraft.ref.Metal.Dust;
import org.lightningcraft.ref.Metal.Ingot;
import org.lightningcraft.ref.Metal.MBlock;
import org.lightningcraft.ref.Metal.Nugget;
import org.lightningcraft.ref.Metal.Plate;
import org.lightningcraft.ref.Metal.Rod;
import org.lightningcraft.ref.RefStrings;

public class LCCraftingManager {

	public static void setOreDictionary() {

		for(int meta = 0; meta < Ingot.count; meta++) {
			LCOreDictionary.registerOre(MBlock.getBlockFromMeta(meta), LCStack.of(LCBlocks.metalBlock, 1, meta));
		}

		for(int meta = 0; meta < Ingot.count; meta++) {
			LCOreDictionary.registerOre(Ingot.getIngotFromMeta(meta), LCStack.of(LCItems.ingot, 1, meta));
		}

		for(int meta = 0; meta < Ingot.count; meta++) {
			LCOreDictionary.registerOre(Nugget.getNuggetFromMeta(meta), LCStack.of(LCItems.nugget, 1, meta));
		}

		for(int meta = 0; meta < Ingot.count; meta++) {
			LCOreDictionary.registerOre(Dust.getDustFromMeta(meta), LCStack.of(LCItems.dust, 1, meta));
		}

		for(int meta = 0; meta < Rod.count; meta++) {
			LCOreDictionary.registerOre(Rod.getRodFromMeta(meta), LCStack.of(LCItems.rod, 1, meta));
		}

		for(int meta = 0; meta < Rod.count; meta++) {
			LCOreDictionary.registerOre(Plate.getPlateFromMeta(meta), LCStack.of(LCItems.plate, 1, meta));
		}

		LCOreDictionary.registerOre("logWood", new ItemStack(LCBlocks.woodLog));
		LCOreDictionary.registerOre("plankWood", new ItemStack(LCBlocks.woodPlank));
		LCOreDictionary.registerOre("slabWood", LCStack.of(LCBlocks.slabBlock, 1, BlockSlabLC.UNDER_PLANK));
		LCOreDictionary.registerOre("stairWood", new ItemStack(LCBlocks.underPlankStairs));

		LCOreDictionary.registerOre("oreIron", LCStack.of(LCBlocks.oreBlock, 1, BlockUnderOre.IRON));
		LCOreDictionary.registerOre("oreGold", LCStack.of(LCBlocks.oreBlock, 1, BlockUnderOre.GOLD));
		LCOreDictionary.registerOre("oreDiamond", LCStack.of(LCBlocks.oreBlock, 1, BlockUnderOre.DIAMOND));
		LCOreDictionary.registerOre("oreEmerald", LCStack.of(LCBlocks.oreBlock, 1, BlockUnderOre.EMERALD));

		LCOreDictionary.registerOre("nuggetNetherStar", LCStack.of(LCItems.material, 1, Material.NETHER_NUGGET));
		LCOreDictionary.registerOre("dustDiamond", LCStack.of(LCItems.material, 1, Material.DIAMOND_DUST));
		LCOreDictionary.registerOre("dustEmerald", LCStack.of(LCItems.material, 1, Material.EMERALD_DUST));
		LCOreDictionary.registerOre("dustQuartz", LCStack.of(LCItems.material, 1, Material.QUARTZ_DUST));
	}

	public static void addCraftingRecipes() {

		for(int meta = 0; meta < Ingot.count; meta++) {
			RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.metalBlock, 1, meta), "XXX","XXX","XXX", 'X',Ingot.getIngotFromMeta(meta));
			RecipeHelper.addShapelessRecipe(LCStack.of(LCItems.ingot, 9, meta), LCStack.of(LCBlocks.metalBlock, 1, meta));
		}

		for(int meta = 0; meta < Ingot.count; meta++) {
			RecipeHelper.addShapedRecipe(LCStack.of(LCItems.ingot, 1, meta), "XXX","XXX","XXX", 'X',Nugget.getNuggetFromMeta(meta));
			RecipeHelper.addShapelessRecipe(LCStack.of(LCItems.nugget, 9, meta), LCStack.of(LCItems.ingot, 1, meta));
		}

		for(int meta = 0; meta < Rod.count; meta++) {
			RecipeHelper.addShapedRecipe(LCStack.of(LCItems.rod, 1, meta), "  X"," X ","X  ", 'X',Rod.getIngotFromMeta(meta));
		}

		for(int meta = 0; meta < Rod.count; meta++) {
			RecipeHelper.addShapedRecipe(LCStack.of(LCItems.plate, 3, meta), "XXX", 'X',Rod.getIngotFromMeta(meta));
		}

		for(int meta = 0; meta < Rod.count; meta++) {
			RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.airTerminal, 1, meta), " I "," X ","PIP",
					'I',Rod.getIngotFromMeta(meta), 'X',Rod.getRodFromMeta(meta), 'P',Plate.getPlateFromMeta(meta));
		}

		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.golfClub), " II"," X ","I  ", 'X',"rodIron", 'I',"ingotIron");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.golfClubGold), " II"," X ","I  ", 'X',"rodGold", 'I',"ingotGold");

		RecipeHelper.addShapedRecipe(new ItemStack(Items.NETHER_STAR), "XXX","XXX","XXX", 'X',"nuggetNetherStar");
		RecipeHelper.addShapelessRecipe(LCStack.of(LCItems.material, 9, Material.NETHER_NUGGET), Items.NETHER_STAR);

		RecipeHelper.addShapelessRecipe(LCStack.of(LCItems.material, 3, Material.UNDER_MEAL), LCStack.of(LCItems.material, 1, Material.UNDER_BONE));
		RecipeHelper.addShapelessRecipe(LCStack.of(Items.RED_DYE, 1, 0), LCStack.of(LCItems.material, 1, Material.DEMON_BLOOD));
		RecipeHelper.addShapelessRecipe(LCStack.of(Items.CYAN_DYE, 1, 0), LCStack.of(LCItems.material, 1, Material.UNDER_MEAL));

		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.stoneBlock, 4, BlockStone.THUNDER), "QXQ","XOX","QXQ",
				'X',"cobblestone", 'O',Blocks.OBSIDIAN, 'Q',"gemQuartz");
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.stoneBlock, 4, BlockStone.THUNDER_BRICK), "XX","XX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.THUNDER));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.THUNDER_BRICK_CHISELED), "X","X",
				'X',LCStack.of(LCBlocks.slabBlock, 1, BlockSlabLC.THUNDER));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.stoneBlock, 4, BlockStone.THUNDER_BRICK_FANCY), "XX","XX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.THUNDER_BRICK_CHISELED));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.stoneBlock, 4, BlockStone.THUNDER_BRICK_FANCY_2), "XX","XX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.THUNDER_BRICK));

		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.slabBlock, 6, BlockSlabLC.THUNDER), "XXX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.THUNDER_BRICK));
		RecipeHelper.addShapedRecipe(new ItemStack(LCBlocks.thunderStairs, 4), "X  ","XX ","XXX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.THUNDER_BRICK));

		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.stoneBlock, 4, BlockStone.DEMON_BRICK), "XX","XX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.DEMON));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.DEMON_BRICK_CHISELED), "X","X",
				'X',LCStack.of(LCBlocks.slabBlock, 1, BlockSlabLC.DEMON));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.stoneBlock, 4, BlockStone.DEMON_BRICK_FANCY), "XX","XX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.DEMON_BRICK_CHISELED));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.stoneBlock, 4, BlockStone.DEMON_BRICK_FANCY_2), "XX","XX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.DEMON_BRICK));

		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.slabBlock, 6, BlockSlabLC.DEMON), "XXX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.DEMON_BRICK));
		RecipeHelper.addShapedRecipe(new ItemStack(LCBlocks.demonStairs, 4), "X  ","XX ","XXX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.DEMON_BRICK));

		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.stoneBlock, 4, BlockStone.UNDER_BRICK), "XX","XX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.UNDER));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.UNDER_BRICK_CHISELED), "X","X",
				'X',LCStack.of(LCBlocks.slabBlock, 1, BlockSlabLC.UNDER));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.stoneBlock, 4, BlockStone.UNDER_BRICK_FANCY), "XX","XX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.UNDER_BRICK_CHISELED));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.stoneBlock, 4, BlockStone.UNDER_BRICK_FANCY_2), "XX","XX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.UNDER_BRICK));

		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.slabBlock, 6, BlockSlabLC.UNDER), "XXX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.UNDER_BRICK));
		RecipeHelper.addShapedRecipe(new ItemStack(LCBlocks.underStairs, 4), "X  ","XX ","XXX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.UNDER_BRICK));

		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.lightBlock, 4, BlockLight.UNDER_LAMP), "RBR","BLB","RBR",
				'R',Blocks.GLOWSTONE, 'B',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.UNDER_BRICK_CHISELED),
				'L',LCStack.of(LCBlocks.lightBlock, 1, BlockLight.UNDER));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.lightBlock, 4, BlockLight.UNDER_LAMP_FANCY), "AB","BA",
				'A',LCStack.of(LCBlocks.lightBlock, 1, BlockLight.UNDER), 'B',LCStack.of(LCBlocks.lightBlock, 1, BlockLight.UNDER_LAMP));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.lightBlock, 4, BlockLight.UNDER_LAMP_FANCY_2), "AA","AA",
				'A',LCStack.of(LCBlocks.lightBlock, 1, BlockLight.UNDER_LAMP_FANCY));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.lightBlock, 4, BlockLight.UNDER_LAMP_FANCY_3), "AA","AA",
				'A',LCStack.of(LCBlocks.lightBlock, 1, BlockLight.UNDER_LAMP_FANCY_2));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.lightBlock, 4, BlockLight.UNDER_LAMP_FANCY), "AA","AA",
				'A',LCStack.of(LCBlocks.lightBlock, 1, BlockLight.UNDER_LAMP_FANCY_3));

		RecipeHelper.addShapedRecipe(new ItemStack(LCBlocks.wallBlock, 6), "BBB","BBB", 'B',LCBlocks.corruptStone);

		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.underTNT, 1, BlockUnderTNT.LIGHTNING), "GSG","SGS","GSG",
				'G',LCStack.of(LCItems.material, 1, Material.UNDER_POWDER_2), 'S',LCBlocks.underSand);
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.underTNT, 1, BlockUnderTNT.MYSTIC), "GSG","TGT","GSG",
				'G',LCStack.of(LCItems.material, 1, Material.DIVINE_DUST), 'S',LCStack.of(LCBlocks.underTNT, 1, BlockUnderTNT.LIGHTNING), 'T',Blocks.TNT);

		RecipeHelper.addShapelessRecipe(new ItemStack(LCBlocks.woodPlank, 4), LCBlocks.woodLog);
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.slabBlock, 6, BlockSlabLC.UNDER_PLANK), "XXX", 'X',LCBlocks.woodPlank);
		RecipeHelper.addShapedRecipe(new ItemStack(LCBlocks.underPlankStairs, 4), "X  ","XX ","XXX", 'X',LCBlocks.woodPlank);

		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.elecHammer), "PPP","XIX"," I ", 'X',"ingotElectricium", 'I',"rodElectricium", 'P',"plateElectricium");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.elecSword), " X ","PXP"," I ", 'X',"ingotElectricium", 'I',"rodElectricium", 'P',"plateElectricium");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.elecPick), "XXX"," I "," I ", 'X',"ingotElectricium", 'I',"rodElectricium");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.elecAxe), "XX","XI"," I", 'X',"ingotElectricium", 'I',"rodElectricium");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.elecShovel), "X","I","I", 'X',"ingotElectricium", 'I',"rodElectricium");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.elecHoe), "XX"," I"," I", 'X',"ingotElectricium", 'I',"rodElectricium");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.skyHammer), "PPP","XIX"," I ", 'X',"ingotSkyfather", 'I',"rodSkyfather", 'P',"plateSkyfather");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.skySword), " X ","PXP"," I ", 'X',"ingotSkyfather", 'I',"rodSkyfather", 'P',"plateSkyfather");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.skyPick), "XXX"," I "," I ", 'X',"ingotSkyfather", 'I',"rodSkyfather");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.skyAxe), "XX","XI"," I", 'X',"ingotSkyfather", 'I',"rodSkyfather");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.skyShovel), "X","I","I", 'X',"ingotSkyfather", 'I',"rodSkyfather");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.skyHoe), "XX"," I"," I", 'X',"ingotSkyfather", 'I',"rodSkyfather");

		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.elecHelm), "XXX","X X", 'X',"ingotElectricium");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.elecChest), "X X","XXX","XXX", 'X',"ingotElectricium");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.elecLegs), "XXX","X X","X X", 'X',"ingotElectricium");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.elecBoots), "X X","X X", 'X',"ingotElectricium");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.skyHelm), "XXX","X X", 'X',"ingotSkyfather");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.skyChest), "X X","XXX","XXX", 'X',"ingotSkyfather");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.skyLegs), "XXX","X X","X X", 'X',"ingotSkyfather");
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.skyBoots), "X X","X X", 'X',"ingotSkyfather");

		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.kineticSword), "RCR","RAR","RER",
				'A',Items.GOLDEN_SWORD, 'E',"plateElectricium", 'R',"dustRedstone", 'C',Items.COMPARATOR);
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.kineticPick), "RCR","RAR","RER",
				'A',Items.GOLDEN_PICKAXE, 'E',"plateElectricium", 'R',"dustRedstone", 'C',Items.COMPARATOR);
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.kineticAxe), "RCR","RAR","RER",
				'A',Items.GOLDEN_AXE, 'E',"plateElectricium", 'R',"dustRedstone", 'C',Items.COMPARATOR);
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.kineticShovel), "RCR","RAR","RER",
				'A',Items.GOLDEN_SHOVEL, 'E',"plateElectricium", 'R',"dustRedstone", 'C',Items.COMPARATOR);

		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.kineticHelm), "RCR","RAR","RER",
				'A',Items.GOLDEN_HELMET, 'E',"plateElectricium", 'R',"dustRedstone", 'C',Items.COMPARATOR);
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.kineticChest), "RCR","RAR","RER",
				'A',Items.GOLDEN_CHESTPLATE, 'E',"plateElectricium", 'R',"dustRedstone", 'C',Items.COMPARATOR);
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.kineticLegs), "RCR","RAR","RER",
				'A',Items.GOLDEN_LEGGINGS, 'E',"plateElectricium", 'R',"dustRedstone", 'C',Items.COMPARATOR);
		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.kineticBoots), "RCR","RAR","RER",
				'A',Items.GOLDEN_BOOTS,  'E',"plateElectricium", 'R',"dustRedstone", 'C',Items.COMPARATOR);

		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.lightningCell, 1, Ingot.ELEC), "XAX","IBI","XAX",
				'B',"plateGold", 'X',"rodIron", 'I',"plateElectricium", 'A',"dustRedstone");
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.lightningCell, 1, Ingot.SKY), "XAX","IBI","XAX",
				'B',LCStack.of(LCBlocks.lightningCell, 1, Ingot.ELEC), 'X',"rodGold", 'I',"plateSkyfather", 'A',Items.COMPARATOR);
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.lightningCell, 1, Ingot.MYSTIC), "XAX","IBI","XAX",
				'B',LCStack.of(LCBlocks.lightningCell, 1, Ingot.SKY), 'X',"rodSkyfather", 'I',"plateMystic", 'A',"plateElectricium");

		ItemStack lUpgrade = LCStack.of(LCItems.material, 1, Material.UPGRADE);
		RecipeHelper.addShapedRecipe(lUpgrade, " R ","RXR"," R ", 'X',"plateSkyfather", 'R',"dustRedstone");

		RecipeHelper.addShapedRecipe(new ItemStack(LCBlocks.lightningFurnace), "XCX","XAX","XIX",
				'X',"cobblestone", 'I',"plateIron", 'A',Blocks.FURNACE, 'C',Items.COMPARATOR);
		RecipeHelper.addShapedRecipe(new ItemStack(LCBlocks.lightningCrusher), "ZCZ","XAX","ZIZ",
				'Z',Blocks.IRON_BARS, 'X',Blocks.OBSIDIAN, 'I',"plateElectricium", 'A',LCBlocks.lightningFurnace, 'C',Items.COMPARATOR);
		RecipeHelper.addShapedRecipe(new ItemStack(LCBlocks.lightningInfuser), "XCX","XAX","XIX",
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.THUNDER), 'I',"plateGold", 'A',"rodElectricium", 'C',Items.COMPARATOR);
		RecipeHelper.addShapedRecipe(new ItemStack(LCBlocks.lightningBreaker), "XCX","GAG","XIX",
				'X',"plateIron", 'G',"plateGold", 'I',Items.DIAMOND_PICKAXE, 'A',"blockIron", 'C',Items.COMPARATOR);
		RecipeHelper.addShapedRecipe(new ItemStack(LCBlocks.lightningMiner), "XCX","GAG","XIX",
				'X',"plateGold", 'G',"plateSkyfather", 'I',LCItems.elecPick, 'A',new ItemStack(LCBlocks.lightningBreaker), 'C',"plateElectricium");
		RecipeHelper.addShapedRecipe(new ItemStack(LCBlocks.staticGenerator), "XCX","COC","XCX",
				'C',Items.COMPARATOR, 'X',Blocks.QUARTZ_BLOCK, 'O',LCStack.of(LCBlocks.lightningCell, 1, Ingot.ELEC));
		RecipeHelper.addShapedRecipe(new ItemStack(LCBlocks.enchReallocator), "TCT","XAX","XEX",
				'A',LCStack.of(LCItems.material, 1, Material.ENSORCELLED), 'E',Blocks.ENCHANTING_TABLE,
				'T',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.UNDER_BRICK_CHISELED), 'C',Items.COMPARATOR,
				'X',LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.DEMON_BRICK_CHISELED));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.lightningCannon, 1, 1), "ERE","ICI","ETE",
				'C',LCStack.of(LCItems.material, 1, Material.CANNON_CORE), 'I',"rodIron", 'E',"rodElectricium", 'T',Blocks.TNT, 'R',Items.COMPARATOR);
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.lightningCannon, 1, 2), "EQE","ICI","EDE",
				'C',LCStack.of(LCBlocks.lightningCannon, 1, 1), 'I',"rodMystic", 'E',"rodSkyfather", 'Q',lUpgrade, 'D',"dustMystic");

		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.wirelessBlock, 1, 0), " T ","XOX","XCX", 'C',"dustDiamond",
				'X',"plateIron", 'O',lUpgrade, 'T',LCStack.of(LCBlocks.airTerminal, 1, Rod.ELEC));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.wirelessBlock, 1, 1), " T ","XOX","XCX", 'C',Items.COMPARATOR,
				'X',"rodIron", 'O',LCStack.of(LCBlocks.wirelessBlock, 1, 0), 'T',LCStack.of(LCBlocks.airTerminal, 1, Rod.SKY));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.wirelessBlock, 1, 2), " T ","XOX","XCX", 'C',"plateElectricium",
				'X',"blockIron", 'O',LCStack.of(LCBlocks.wirelessBlock, 1, 1), 'T',LCStack.of(LCBlocks.airTerminal, 1, Rod.MYSTIC));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.wirelessBlock, 1, 3), " T ","XOX","XCX", 'C',"dustRedstone",
				'X',"plateGold", 'O',lUpgrade, 'T',LCStack.of(LCBlocks.airTerminal, 1, Rod.ELEC));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.wirelessBlock, 1, 4), " T ","XOX","XCX", 'C',Items.COMPARATOR,
				'X',"rodGold", 'O',LCStack.of(LCBlocks.wirelessBlock, 1, 3), 'T',LCStack.of(LCBlocks.airTerminal, 1, Rod.SKY));
		RecipeHelper.addShapedRecipe(LCStack.of(LCBlocks.wirelessBlock, 1, 5), " T ","XOX","XCX", 'C',"blockRedstone",
				'X',"blockGold", 'O',LCStack.of(LCBlocks.wirelessBlock, 1, 4), 'T',LCStack.of(LCBlocks.airTerminal, 1, Rod.MYSTIC));

		if(EnergyApiHelper.rfOrTeslaLoaded) {
			RecipeHelper.addShapedRecipe(new ItemStack(LCBlocks.rfProvider), "BRB","CAC","BCB",
					'B',"plateElectricium", 'A',"blockIron", 'R',Items.COMPARATOR, 'C',"plateIron");
			RecipeHelper.addShapedRecipe(new ItemStack(LCBlocks.rfReceiver), "BRB","CAC","BCB",
					'B',"blockRedstone", 'A',"blockGold", 'R',Items.COMPARATOR, 'C',"plateIron");
		}

		RecipeHelper.addShapedRecipe(new ItemStack(LCItems.wirelessMarker, 1), " C ","EPE"," U ",
				'P',Items.PAPER, 'E',"dustElectricium", 'U',lUpgrade, 'C',Items.COMPARATOR);
		RecipeHelper.addShapelessRecipe(LCStack.of(LCItems.wirelessMarker, 1, 0), LCStack.of(LCItems.wirelessMarker, 1, 1));

		RecipeHelper.addShapedRecipe(LCStack.of(LCItems.battery, 1, 0), "ICI","IBI","IRI",
				'B',"plateElectricium", 'I',"rodIron", 'R',"dustRedstone", 'C',Items.COMPARATOR);
		RecipeHelper.addShapedRecipe(LCStack.of(LCItems.battery, 1, 1), "RUR","RBR","RIR",
				'B',LCStack.of(LCItems.battery, 1, 0), 'U',lUpgrade, 'R',"dustRedstone", 'I',"rodElectricium");
		RecipeHelper.addShapedRecipe(LCStack.of(LCItems.battery, 1, 2), "RUR","RBR","RIR",
				'B',LCStack.of(LCItems.battery, 1, 1), 'U',"ingotSkyfather", 'R',"blockRedstone", 'I',"blockElectricium");
		RecipeHelper.addShapedRecipe(new ItemStack(LCBlocks.chargingPlate), "RCR","RPR","RER", 'E',"plateElectricium",
				'P',Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'R',"dustRedstone", 'C',Items.COMPARATOR);

		RecipeHelper.addShapedRecipe(LCStack.of(LCItems.itemMagnet, 1, 0), "INN","  N","INN", 'I',"plateIron", 'N',"blockRedstone");
		RecipeHelper.addShapedRecipe(LCStack.of(LCItems.itemMagnet, 1, 1), "IEE"," ME","IEE",
				'I',"plateIron", 'M',LCStack.of(LCItems.itemMagnet, 1, 0), 'E',"ingotIron");
		RecipeHelper.addShapedRecipe(LCStack.of(LCItems.itemMagnet, 1, 2), "IEE"," ME","IEE",
				'I',"plateIron", 'M',LCStack.of(LCItems.itemMagnet, 1, 1), 'E',"ingotGold");
		RecipeHelper.addShapedRecipe(LCStack.of(LCItems.itemMagnet, 1, 3), "IEE"," ME","IEE",
				'I',"plateIron", 'M',LCStack.of(LCItems.itemMagnet, 1, 2), 'E',"ingotElectricium");

	}

	public static void addSmeltingRecipes() {

	}

}
