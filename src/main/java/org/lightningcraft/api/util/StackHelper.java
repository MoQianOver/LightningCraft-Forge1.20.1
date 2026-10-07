package org.lightningcraft.api.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.TagParser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.util.LCOreDictionary;
import org.lightningcraft.api.recipes.LightningInfusionRecipe;

public class StackHelper {

	private static Map<String, Integer> oreMap = new HashMap();
	private static Map<String, Integer> subMap = new HashMap();

	public static Random rand = new Random();

	@OnlyIn(Dist.CLIENT)
	public static @Nonnull ItemStack animateItemStackFromString(String stackString, boolean change) {
		int ores;
		if(LCOreDictionary.doesOreNameExist(stackString)) {
			ores = LCOreDictionary.getOres(stackString).size();
		} else {
			ores = 1;
		}
		int oreIndex = 0;
		int subIndex = 0;
		ItemStack result;

		if(oreMap.containsKey(stackString))	oreIndex = oreMap.get(stackString);
		if(change) {
			if(ores > 0) {
				oreIndex = rand.nextInt(ores);
			} else {
				oreIndex = 0;
			}
			oreMap.put(stackString, oreIndex);
		}

		ItemStack primary = makeItemStackFromString(stackString, oreIndex);
		if(primary.isEmpty()) return ItemStack.EMPTY;

		NonNullList<ItemStack> subItems = NonNullList.create();

		subItems.add(primary.getItem().getDefaultInstance());

		if(subMap.containsKey(stackString))	subIndex = subMap.get(stackString);
		if(primary.getDamageValue() == LCOreDictionary.WILDCARD_VALUE && !subItems.isEmpty()) {
			if(change) {
				subIndex = rand.nextInt(subItems.size());
				subMap.put(stackString, subIndex);
			}
			result = subItems.get(subIndex);
		} else {
			result = primary;
		}
		return result;
	}

	private static boolean isStringOreDict(String stackString) {
		if(stackString == LightningInfusionRecipe.nullIdentifier) return false;
		return LCOreDictionary.doesOreNameExist(stackString);
	}

	public static @Nonnull ItemStack makeItemStackFromString(String stackString, int oreIndex) {
		if(stackString == LightningInfusionRecipe.nullIdentifier) return ItemStack.EMPTY;
		try {
			if(!isStringOreDict(stackString)) {
				return ItemStack.of(TagParser.parseTag(stackString));
			} else {

				throw new IllegalStateException("OreDict exists");
			}
		} catch(Exception e) {
			List<ItemStack> list;
			if(isStringOreDict(stackString) && oreIndex < (list = LCOreDictionary.getOres(stackString)).size()) {
				return list.get(oreIndex);
			} else {
				return ItemStack.EMPTY;
			}
		}
	}

	public static @Nonnull ItemStack makeItemStackFromString(String stackString) {
		return makeItemStackFromString(stackString, 0);
	}

	public static int getMetaFromString(String stackString) {
		if(!isStringOreDict(stackString)) {
			ItemStack stack = makeItemStackFromString(stackString);
			return !stack.isEmpty() ? stack.getDamageValue() : 0;
		} else {
			return -1;
		}
	}

	public static String changeStringMeta(String stackString, int newMeta) {
		try {
			if(!isStringOreDict(stackString)) {
				ItemStack stack = makeItemStackFromString(stackString);
				stack.setDamageValue(newMeta);
				return makeStringFromItemStack(stack);
			}
		} catch(NullPointerException e) {}
		return stackString;
	}

	public static String changeStringNBT(String stackString, CompoundTag newTag) {
		try {
			if(!isStringOreDict(stackString)) {
				ItemStack stack = makeItemStackFromString(stackString);
				stack.setTag(newTag);
				return makeStringFromItemStack(stack);
			}
		} catch(NullPointerException e) {}
		return stackString;
	}

	public static String stripMetaFromString(String stackString) {
		return changeStringMeta(stackString, 0);
	}

	public static String stripNBTFromString(String stackString) {
		return changeStringNBT(stackString, null);
	}

	public static String makeStringFromItemStack(@Nonnull Object stack) {
		if(stack instanceof ItemStack) {
			ItemStack s = (ItemStack)stack;
			return s.save(new CompoundTag()).toString();
		} else if(stack instanceof String) {
			return (String)stack;
		} else {
			throw new IllegalArgumentException("Parameter must be a String or an ItemStack, was given " + stack.getClass());
		}
	}

	public static boolean areItemStacksEqualForCrafting(@Nonnull ItemStack... stacks) {
		ItemStack comp = stacks[0];
		if(comp.isEmpty()) return false;
		ItemStack comp1 = comp.copy(); comp1.setCount(1);
		for(int n = 1; n < stacks.length; n++) {
			if(stacks[n].isEmpty()) return false;
			ItemStack comp2 = stacks[n].copy(); comp2.setCount(1);
			if(!ItemStack.isSameItemSameTags(comp1, comp2)) {
				if(comp1.getDamageValue() == LCOreDictionary.WILDCARD_VALUE) {
					comp2.setDamageValue(LCOreDictionary.WILDCARD_VALUE);
					if(!ItemStack.isSameItemSameTags(comp1, comp2)) return false;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean setStackColor(@Nonnull ItemStack stack, Color color) {
		if(!(stack.getItem() instanceof ArmorItem))
			return false;
		Item armor = stack.getItem();

		if (!(armor == Items.LEATHER_BOOTS || armor == Items.LEATHER_LEGGINGS ||
				armor == Items.LEATHER_CHESTPLATE || armor == Items.LEATHER_HELMET)) {
			return false;
		} else {
			CompoundTag nbttagcompound = stack.getTag();
			if(nbttagcompound == null) nbttagcompound = new CompoundTag();
			CompoundTag disp = new CompoundTag();
			disp.putInt("color", color.getRGB());
			nbttagcompound.put("display", disp);
			stack.setTag(nbttagcompound);
			return true;
		}
	}

	public static List<CompoundTag> getEnchantments(@Nonnull ItemStack s) {
		List enchList = new ArrayList<CompoundTag>();

		if(!s.isEmpty() && s.hasTag()) {
			ListTag enchants;
			String ench = s.getItem() == Items.ENCHANTED_BOOK ? "StoredEnchantments" : "ench";
			enchants = (ListTag) s.getTag().get(ench);

			if(enchants != null && enchants.size() > 0) {
				for(int i = 0; i < enchants.size(); i++) {
					CompoundTag enchant = enchants.getCompound(i);
					enchList.add(enchant);
				}
			}
		}

		return enchList;
	}

	public static boolean addEnchantments(@Nonnull ItemStack s, List<CompoundTag> enchList) {
		if(!s.isEmpty()) {
			String ench = s.getItem() == Items.ENCHANTED_BOOK ? "StoredEnchantments" : "ench";
			if(!s.hasTag()) s.setTag(new CompoundTag());
			ListTag currentEnchants = (ListTag) s.getTag().get(ench);

			if(currentEnchants == null) {
				currentEnchants = new ListTag();
			}

			for(int i = 0; i < enchList.size(); i++) {
				currentEnchants.add(enchList.get(i));
			}

			s.getTag().put(ench, currentEnchants);
			return true;
		}
		return false;
	}

	public static boolean oreDictNameStartsWith(@Nonnull ItemStack ore, String start) {
		for(int i : LCOreDictionary.getOreIDs(ore)) {
			if(LCOreDictionary.getOreName(i).startsWith(start)) return true;
		}
		return false;
	}

	public static boolean oreDictNameEquals(@Nonnull ItemStack ore, String equals) {
		for(int i : LCOreDictionary.getOreIDs(ore)) {
			if(LCOreDictionary.getOreName(i).equals(equals)) return true;
		}
		return false;
	}

	public static int[] makeIntArray(@Nonnull ItemStack[] stacks) {
		return makeIntArray(stacks.length);
	}

	public static int[] makeIntArray(int size) {
		int[] ints = new int[size];
		for(int i = 0; i < size; i++) ints[i] = i;
		return ints;
	}

}
