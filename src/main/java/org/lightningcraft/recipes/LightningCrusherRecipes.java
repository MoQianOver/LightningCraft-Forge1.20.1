package org.lightningcraft.recipes;

import org.lightningcraft.util.LCStack;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import org.lightningcraft.api.util.LCOreDictionary;
import org.lightningcraft.api.recipes.LightningCrusherRecipe;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.api.util.StackHelper;
import org.lightningcraft.blocks.BlockStone;
import org.lightningcraft.blocks.BlockUnderOre;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.ref.Material;

public class LightningCrusherRecipes {

	private static LightningCrusherRecipes instance = new LightningCrusherRecipes();
	private int recipeIndex;
	private JointList<LightningCrusherRecipe> recipes = new JointList();

	public static LightningCrusherRecipes instance() {
		return instance;
	}

	private LightningCrusherRecipes() {
		recipeIndex = 0;
		addDefaultRecipes();
	}

	private void addDefaultRecipes() {

		addRecipe(new ItemStack(Blocks.GRAVEL), LCStack.of(Blocks.STONE, 1, LCOreDictionary.WILDCARD_VALUE));
		addRecipe(new ItemStack(Blocks.GRAVEL), "cobblestone");
		addRecipe(new ItemStack(Blocks.SAND), new ItemStack(Blocks.GRAVEL));
		addRecipe(new ItemStack(Blocks.SAND), "blockGlass");
		addRecipe(new ItemStack(Blocks.SAND), "sandstone");
		addRecipe(new ItemStack(Items.GLOWSTONE_DUST, 4), "glowstone");
		addRecipe(new ItemStack(Items.STICK, 16), "logWood");
		addRecipe(new ItemStack(Items.STICK, 4), "plankWood");
		addRecipe(new ItemStack(Items.STICK, 2), "treeSapling");
		addRecipe(new ItemStack(Items.SUGAR, 4), new ItemStack(Items.SUGAR_CANE));
		addRecipe(new ItemStack(Items.LEATHER, 5), new ItemStack(Items.SADDLE));
		addRecipe(new ItemStack(Items.SNOWBALL, 4), new ItemStack(Blocks.SNOW));
		addRecipe(new ItemStack(Items.SNOWBALL, 6), new ItemStack(Blocks.ICE));
		addRecipe(new ItemStack(Items.SNOWBALL, 8), new ItemStack(Blocks.PACKED_ICE));
		addRecipe(new ItemStack(Items.CLAY_BALL, 4), new ItemStack(Blocks.CLAY));
		addRecipe(new ItemStack(Items.PRISMARINE_SHARD, 4), LCStack.of(Blocks.PRISMARINE, 1, 0));
		addRecipe(new ItemStack(Items.PRISMARINE_SHARD, 9), LCStack.of(Blocks.PRISMARINE, 1, 1));
		addRecipe(new ItemStack(Items.PRISMARINE_SHARD, 8), LCStack.of(Blocks.PRISMARINE, 1, 2));
		addRecipe(new ItemStack(Items.PRISMARINE_CRYSTALS), new ItemStack(Items.PRISMARINE_SHARD));
		addRecipe(new ItemStack(Items.PRISMARINE_CRYSTALS, 4), new ItemStack(Blocks.SEA_LANTERN));
		addRecipe(new ItemStack(Items.BRICK, 4), new ItemStack(Blocks.BRICKS));
		addRecipe(new ItemStack(Items.NETHER_BRICK, 4), new ItemStack(Blocks.NETHER_BRICKS));
		addRecipe(new ItemStack(Items.GUNPOWDER, 3), new ItemStack(Items.FIRE_CHARGE));
		addRecipe(new ItemStack(Items.BLAZE_POWDER, 3), new ItemStack(Items.BLAZE_ROD));
		addRecipe(LCStack.of(Items.WHITE_DYE, 6, 0), new ItemStack(Items.BONE));

		addRecipe(new ItemStack(Items.ENDER_EYE), "nuggetSkyfather");
		addRecipe(LCStack.of(LCItems.material, 1, Material.DEMON_BLOOD), "nuggetMystic");
		addRecipe(LCStack.of(LCItems.material, 6, Material.UNDER_MEAL), LCStack.of(LCItems.material, 1, Material.UNDER_BONE));
		addRecipe(LCStack.of(Items.WHITE_DYE, 6, 0), LCStack.of(LCItems.material, 1, Material.UNDER_MEAL));

		addRecipe(new ItemStack(Items.REDSTONE, 6), "oreRedstone");
		addRecipe(LCStack.of(Items.BLUE_DYE, 9, 0), "oreLapis");
		addRecipe(LCStack.of(LCItems.material, 3, Material.DIAMOND_DUST), "oreDiamond", LCStack.of(LCBlocks.oreBlock, 1, BlockUnderOre.DIAMOND));
		addRecipe(LCStack.of(LCItems.material, 3, Material.EMERALD_DUST), "oreEmerald", LCStack.of(LCBlocks.oreBlock, 1, BlockUnderOre.EMERALD));
		addRecipe(LCStack.of(LCItems.material, 4, Material.QUARTZ_DUST), "oreQuartz");

		addRecipe(LCStack.of(LCItems.material, 6, Material.DIAMOND_DUST), LCStack.of(LCBlocks.oreBlock, 1, BlockUnderOre.DIAMOND));
		addRecipe(LCStack.of(LCItems.material, 6, Material.EMERALD_DUST), LCStack.of(LCBlocks.oreBlock, 1, BlockUnderOre.EMERALD));

		addRecipe(new ItemStack(Items.GUNPOWDER, 2), LCStack.of(LCItems.material, 1, Material.UNDER_POWDER));
		addRecipe(new ItemStack(LCBlocks.underSand), new JointList().join(
			LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.UNDER),
			LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.UNDER_BRICK),
			LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.UNDER_BRICK_CHISELED),
			LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.UNDER_BRICK_FANCY),
			LCStack.of(LCBlocks.stoneBlock, 1, BlockStone.UNDER_BRICK_FANCY_2)
		));

		addDustRecipes();
	}

	private void addDustRecipes() {
		for(String n : LCOreDictionary.getOreNames()) {
			boolean ore = n.length() >= 3 && n.substring(0, 3).equals("ore");
			boolean ingot = n.length() >= 5 && n.substring(0, 5).equals("ingot");
			boolean gem = n.length() >= 3 && n.substring(0, 3).equals("gem");
			if(ore || ingot || gem) {
				if(recipeExists(n)) continue;
				String type = ore || gem ? n.substring(3, n.length()) : n.substring(5, n.length());
				for(String m : LCOreDictionary.getOreNames()) {
					if(m.length() >= 5 && m.substring(0, 4).equals("dust") && m.substring(4, m.length()).equals(type)) {
						try {
							ItemStack result = LCOreDictionary.getOres(m).get(0).copy();
							if(ore) result.setCount(2);
							addRecipe(result, n);
						} catch(Exception e) {}
					}
				}
			}
		}
	}

	public boolean recipeExists(Object input) {
		for(LightningCrusherRecipe r : recipes) {
			if(r.getInputRaw().equals(input)) return true;
		}
		return false;
	}

	public void addRecipe(LightningCrusherRecipe r) {
		recipes.add(r);
		recipeIndex++;
	}

	public void addRecipe(@Nonnull ItemStack out, Object in) {
		addRecipe(new LightningCrusherRecipe(out, in));
	}

	public void addRecipe(@Nonnull ItemStack out, Object in, @Nonnull ItemStack... excluding) {
		addRecipe(new LightningCrusherRecipe(out, in, excluding));
	}

	public @Nonnull ItemStack getCrushingResult(@Nonnull ItemStack input) {
		for(LightningCrusherRecipe r : recipes) {
			for(ItemStack s : r.getInput()) {
				if(StackHelper.areItemStacksEqualForCrafting(s, input)) {
					return r.getOutput();
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public List<LightningCrusherRecipe> getRecipeList() {
		return recipes;
	}

}
