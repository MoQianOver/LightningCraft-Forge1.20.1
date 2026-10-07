package org.lightningcraft.recipes;

import org.lightningcraft.util.LCStack;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.api.util.LCOreDictionary;
import org.lightningcraft.api.recipes.LightningInfusionRecipe;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.api.util.StackHelper;
import org.lightningcraft.blocks.BlockStone;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.init.LCPotions;
import org.lightningcraft.ref.Material;
import org.lightningcraft.ref.Metal.Ingot;
import org.lightningcraft.util.LCMisc;

public class LightningInfusionRecipes {

	private static LightningInfusionRecipes instance = new LightningInfusionRecipes();
	private int recipeIndex;
	private JointList<LightningInfusionRecipe> recipes = new JointList();
	private int lastResultCost = -2;

	public static LightningInfusionRecipes instance() {
		return instance;
	}

	private LightningInfusionRecipes() {
		recipeIndex = 0;
		addDefaultRecipes();
	}

	private static final int ANY = LCOreDictionary.WILDCARD_VALUE;

	private void addDefaultRecipes() {
		ItemStack thunderstone = LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.THUNDER);
		ItemStack demonBlood = LCStack.of(LCItems.material, 1, Material.DEMON_BLOOD);
		ItemStack ichor = LCStack.of(LCItems.material, 1, Material.ICHOR);
		ItemStack prismarine = LCStack.of(Blocks.PRISMARINE, 1, ANY);
		ItemStack wool = LCStack.of(Blocks.WHITE_WOOL, 1, ANY);
		String dust = "dustMystic";

		addRecipe(new ItemStack(Blocks.DEAD_BUSH), 10, "treeSapling", Blocks.SAND);
		addRecipe(new ItemStack(Blocks.CLAY, 3), 15, Blocks.DIRT, Blocks.GRAVEL, Blocks.SAND, Items.POTION);
		addRecipe(new ItemStack(Blocks.SPONGE), 10, wool, "slimeball");
		addRecipe(new ItemStack(Blocks.PACKED_ICE), 15, Blocks.ICE, LCMisc.makeArray(Blocks.ICE, 3));
		addRecipe(new ItemStack(Blocks.MOSSY_COBBLESTONE), 10, "cobblestone", Blocks.VINE);
		addRecipe(new ItemStack(Blocks.BOOKSHELF, 2), 10, "logWood", LCMisc.makeArray(Items.BOOK, 4));
		addRecipe(new ItemStack(Items.QUARTZ), 40, LCStack.of(Blocks.STONE, 1, 1), LCMisc.makeArray(LCStack.of(Blocks.STONE, 1, 3), 2));
		addRecipe(new ItemStack(Items.DIAMOND), 50, Blocks.COAL_BLOCK, LCMisc.makeArray(Items.COAL, 4));
		addRecipe(new ItemStack(Items.EMERALD), 75, prismarine, LCMisc.makeArray(Items.PRISMARINE_CRYSTALS, 4));

		addRecipe(LCStack.of(LCItems.ingot, 1, Ingot.ELEC), 30, "ingotIron", "ingotGold", "gemDiamond");
		addRecipe(LCStack.of(LCBlocks.metalBlock, 1, Ingot.ELEC), 270, "blockIron", "blockGold", "blockDiamond");

		int spCost = 90;
		addRecipe(new ItemStack(LCItems.soulSword), spCost, Items.STONE_SWORD, LCMisc.makeArray(Blocks.SOUL_SAND, 4));
		addRecipe(new ItemStack(LCItems.zombieSword), spCost, Items.STONE_SWORD, LCMisc.makeArray(Items.ROTTEN_FLESH, 4));
		addRecipe(new ItemStack(LCItems.featherSword), spCost, Items.STONE_SWORD, LCMisc.makeArray(Items.FEATHER, 4));
		addRecipe(new ItemStack(LCItems.enderSword), spCost, Items.STONE_SWORD, LCMisc.makeArray(Items.ENDER_PEARL, 4));
		addRecipe(new ItemStack(LCItems.blazeSword), spCost, Items.STONE_SWORD, LCMisc.makeArray(Items.BLAZE_ROD, 4));
		addRecipe(new ItemStack(LCItems.iceSword), spCost, Items.STONE_SWORD, LCMisc.makeArray(Blocks.PACKED_ICE, 4));

		addRecipe(LCStack.of(LCItems.ingot, 1, Ingot.SKY), 85, "ingotElectricium", thunderstone, Items.ENDER_EYE, thunderstone, Items.ENDER_EYE);

		addRecipe(new ItemStack(Items.LAVA_BUCKET), 40, Items.WATER_BUCKET, LCMisc.makeArray(demonBlood, 4));
		addRecipe(new ItemStack(Blocks.NETHERRACK, 2), 15, "cobblestone", demonBlood);
		addRecipe(new ItemStack(Blocks.NETHER_BRICKS), 25, Blocks.BRICKS, demonBlood);
		addRecipe(new ItemStack(Items.NETHER_WART, 2), 20, Items.WHEAT_SEEDS, demonBlood);

		addRecipe(LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.DEMON), 35, LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.THUNDER), demonBlood, demonBlood);

		addRecipe(LCStack.of(LCItems.material, 1, Material.UNDER_CHARGE), 100, Items.FIRE_CHARGE, "dustDiamond", demonBlood, Blocks.OBSIDIAN);
		addRecipe(LCStack.of(LCItems.material, 1, Material.UNDER_CHARGE), 40, Blocks.OBSIDIAN,
				demonBlood, LCStack.of(LCItems.material, 1, Material.UNDER_POWDER_2));

		addRecipe(LCStack.of(LCItems.material, 1, Material.ENSORCELLED), 250, LCStack.of(LCItems.material, 1, Material.UPGRADE),
				"dustDiamond", demonBlood, LCStack.of(LCItems.material, 1, Material.UNDER_MEAL), demonBlood);

		addRecipe(LCStack.of(LCItems.material, 4, Material.UNDER_POWDER_2), 50, "dustElectricium",
				LCMisc.makeArray(LCStack.of(LCItems.material, 1, Material.UNDER_POWDER), 4));
		addRecipe(LCStack.of(LCItems.material, 4, Material.DIVINE_DUST), 100, "dustMystic",
				LCMisc.makeArray(LCStack.of(LCItems.material, 1, Material.UNDER_POWDER_2), 4));

		ItemStack mundane = LCPotions.getPotionWithType(Potions.MUNDANE);
		ItemStack normalDemon = LCPotions.getPotionWithType(LCPotions.demonFriendNormal);
		ItemStack extendedDemon = LCPotions.getPotionWithType(LCPotions.demonFriendExtended);
		addRecipe(normalDemon, 50, mundane, demonBlood, Items.GOLDEN_CARROT, demonBlood, Items.GOLDEN_CARROT).setNBTSensitive();
		addRecipe(extendedDemon, 25, normalDemon, LCMisc.makeArray("dustRedstone", 4)).setNBTSensitive();

		addRecipe(LCStack.of(LCItems.ingot, 2, Ingot.MYSTIC), 160, "ingotSkyfather", "nuggetNetherStar", demonBlood, ichor, demonBlood);

		if(LCConfig.mysticGear) {

			addRecipe(new ItemStack(LCItems.mysticHammer), 360, LCItems.skyHammer, LCMisc.makeArray(dust, 4));
			addRecipe(new ItemStack(LCItems.mysticSword), 300, LCItems.skySword, LCMisc.makeArray(dust, 4));
			addRecipe(new ItemStack(LCItems.mysticPick), 240, LCItems.skyPick, LCMisc.makeArray(dust, 4));
			addRecipe(new ItemStack(LCItems.mysticAxe), 240, LCItems.skyAxe, LCMisc.makeArray(dust, 4));
			addRecipe(new ItemStack(LCItems.mysticShovel), 240, LCItems.skyShovel, LCMisc.makeArray(dust, 4));
			addRecipe(new ItemStack(LCItems.mysticHoe), 200, LCItems.skyHoe, LCMisc.makeArray(dust, 4));

			addRecipe(new ItemStack(LCItems.mysticHelm), 240, LCItems.skyHelm, LCMisc.makeArray(dust, 4));
			addRecipe(new ItemStack(LCItems.mysticChest), 360, LCItems.skyChest, LCMisc.makeArray(dust, 4));
			addRecipe(new ItemStack(LCItems.mysticLegs), 300, LCItems.skyLegs, LCMisc.makeArray(dust, 4));
			addRecipe(new ItemStack(LCItems.mysticBoots), 240, LCItems.skyBoots, LCMisc.makeArray(dust, 4));
		}
	}

	public LightningInfusionRecipe addRecipe(LightningInfusionRecipe r) {
		recipes.add(r);
		recipeIndex++;
		return r;
	}

	public LightningInfusionRecipe addRecipe(@Nonnull ItemStack output, int cost, Object infuse, Object... surrounding) {
		return addRecipe(new LightningInfusionRecipe(output, cost, infuse, surrounding));
	}

	private boolean hasResult(@Nonnull ItemStack infuse, String item, List<String> itemOres, boolean nbtSensitive) {
		String useItem = StackHelper.makeStringFromItemStack(infuse);

		if(!nbtSensitive) {
			useItem = StackHelper.stripNBTFromString(useItem);
			item = StackHelper.stripNBTFromString(item);
		}

		if(item.equals(useItem) || itemOres.contains(useItem)) {
			return true;
		}

		if(StackHelper.getMetaFromString(item) == LCOreDictionary.WILDCARD_VALUE) {
			String newInfuse = StackHelper.stripMetaFromString(item);
			if(newInfuse.equals(StackHelper.stripMetaFromString(useItem))) return true;
		}

		for(String s : itemOres) {
			if(StackHelper.getMetaFromString(s) == LCOreDictionary.WILDCARD_VALUE) {
				String cutInfuseOre = StackHelper.stripMetaFromString(s);
				if(cutInfuseOre.equals(StackHelper.stripMetaFromString(useItem))) return true;
			}
		}

		return false;
	}

	public boolean hasBaseResult(@Nonnull ItemStack infuse1) {
		if(infuse1.isEmpty()) return false;
		ItemStack infuse = infuse1.copy(); infuse.setCount(1);
		for(LightningInfusionRecipe recipe : recipes) {
			if(hasResult(infuse, recipe.getInfuseItem(), recipe.getInfuseItemAsOre(), recipe.getNBTSensitive())) return true;
		}
		return false;
	}

	public @Nonnull ItemStack getInfusionResult(@Nonnull ItemStack infuse1, @Nonnull ItemStack... stacks1) {

		if(stacks1.length != LightningInfusionRecipe.itemsNeeded) return ItemStack.EMPTY;

		ItemStack infuse = infuse1.copy(); infuse.setCount(1);
		List<ItemStack> stacks = new JointList();
		for(int s = 0; s < stacks1.length; s++) {
			if(!stacks1[s].isEmpty()) {
				ItemStack current = stacks1[s].copy(); current.setCount(1);
				stacks.add(current);
			} else {
				stacks.add(ItemStack.EMPTY);
			}
		}

		for(LightningInfusionRecipe recipe : recipes) {

			int valid = 0;

			List<String> itemList = new JointList();
			List<List<String>> oreList = new JointList();
			for(String item : recipe.getItems()) {
				item = StackHelper.stripNBTFromString(item);
				itemList.add(item);
			}
			for(List list : recipe.getItemsAsOres()) {
				oreList.add(list);
			}

			if(hasResult(infuse, recipe.getInfuseItem(), recipe.getInfuseItemAsOre(), recipe.getNBTSensitive())) {
				valid++;
			}

			for(ItemStack stack : stacks) {
				String stackString;
				List matchList;
				if(stack.isEmpty()) {
					stackString = LightningInfusionRecipe.nullIdentifier;
				} else {
					stackString = StackHelper.makeStringFromItemStack(stack);
				}
				String modStackString = StackHelper.changeStringMeta(stackString, LCOreDictionary.WILDCARD_VALUE);
				modStackString = StackHelper.stripNBTFromString(modStackString);
				if(itemList.contains(stackString)) {
					itemList.remove(stackString);
					valid++;
				} else if(itemList.contains(modStackString)) {
					itemList.remove(modStackString);
					valid++;
				} else if((matchList = LCMisc.listListContains(oreList, stackString)) != null) {
					oreList.remove(matchList);
					valid++;
				} else if((matchList = LCMisc.listListContains(oreList, modStackString)) != null) {
					oreList.remove(matchList);
					valid++;
				}
			}

			if(valid == 1 + LightningInfusionRecipe.itemsNeeded) {
				lastResultCost = recipe.getCost();
				return recipe.getOutput().copy();
			}
		}
		lastResultCost = -2;
		return ItemStack.EMPTY;
	}

	public int getLastResultCost() {
		return lastResultCost;
	}

	public final List<LightningInfusionRecipe> getRecipeList() {
		return recipes;
	}

}
