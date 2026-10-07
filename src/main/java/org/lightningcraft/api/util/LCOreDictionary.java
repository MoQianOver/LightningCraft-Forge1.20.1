package org.lightningcraft.api.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public final class LCOreDictionary {

	private LCOreDictionary() {}

	public static final int WILDCARD_VALUE = 32767;

	private static final Map<String, NonNullList<ItemStack>> ORES = new LinkedHashMap<>();

	private static final Map<String, Integer> NAME_TO_ID = new LinkedHashMap<>();
	private static final List<String> ID_TO_NAME = new ArrayList<>();

	public static void registerOre(String name, ItemStack stack) {
		if (name == null || stack.isEmpty()) return;
		ORES.computeIfAbsent(name, k -> {
			int id = ID_TO_NAME.size();
			NAME_TO_ID.put(k, id);
			ID_TO_NAME.add(k);
			return NonNullList.create();
		}).add(stack.copy());
	}

	public static boolean doesOreNameExist(String name) {
		return name != null && ORES.containsKey(name);
	}

	public static NonNullList<ItemStack> getOres(String name) {
		NonNullList<ItemStack> list = ORES.get(name);
		return list == null ? NonNullList.create() : list;
	}

	public static Collection<String> getOreNames() {
		return ORES.keySet();
	}

	public static int[] getOreIDs(ItemStack stack) {
		if (stack.isEmpty()) return new int[0];
		List<Integer> ids = new ArrayList<>();
		for (Map.Entry<String, NonNullList<ItemStack>> e : ORES.entrySet()) {
			for (ItemStack ore : e.getValue()) {
				if (itemMatches(ore, stack, false)) {
					ids.add(NAME_TO_ID.get(e.getKey()));
					break;
				}
			}
		}
		int[] out = new int[ids.size()];
		for (int i = 0; i < out.length; i++) out[i] = ids.get(i);
		return out;
	}

	public static String getOreName(int id) {
		return (id >= 0 && id < ID_TO_NAME.size()) ? ID_TO_NAME.get(id) : "";
	}

	public static boolean itemMatches(ItemStack target, ItemStack input, boolean strict) {
		if (target.isEmpty() || input.isEmpty()) return false;
		if (target.getItem() != input.getItem()) return false;
		if (target.getDamageValue() != WILDCARD_VALUE
				&& target.getDamageValue() != input.getDamageValue()) return false;
		return !strict || ItemStack.isSameItemSameTags(target, input);
	}

	public static void clear() {
		ORES.clear();
		NAME_TO_ID.clear();
		ID_TO_NAME.clear();
	}
}
