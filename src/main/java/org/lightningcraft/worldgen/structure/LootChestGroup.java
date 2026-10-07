package org.lightningcraft.worldgen.structure;

import net.minecraft.util.RandomSource;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.util.IntList;
import org.lightningcraft.util.WeightedRandomChestContent;

public class LootChestGroup {

	private int numChests;
	private JointList<Boolean> isPlaced = new JointList();
	private JointList<Boolean> isTrapped = new JointList();
	private JointList<WeightedRandomChestContent> defaultContents = new JointList();
	private JointList<JointList<WeightedRandomChestContent>> chestContents = new JointList();
	private IntList minChestStacks = new IntList();
	private IntList maxChestStacks = new IntList();
	private int minDefaultStacks;
	private int maxDefaultStacks;

	public LootChestGroup(int numChests, int minStacks, int maxStacks, WeightedRandomChestContent[] chestContents) {
		this.numChests = numChests;
		this.isPlaced.join(false, numChests);
		this.minChestStacks.join(minDefaultStacks = minStacks, numChests);
		this.maxChestStacks.join(maxDefaultStacks = maxStacks, numChests);
		this.defaultContents = new JointList<WeightedRandomChestContent>().join(chestContents);
		this.chestContents.join(defaultContents, numChests);
		this.isTrapped.join(false, numChests);
	}

	public void setChestContents(WeightedRandomChestContent[] chestContents) {
		this.defaultContents = new JointList<WeightedRandomChestContent>().join(chestContents);
	}

	public void setChestContents(int index, WeightedRandomChestContent[] chestContents) {
		JointList<WeightedRandomChestContent> singleContents = new JointList<WeightedRandomChestContent>().join(chestContents);
		this.chestContents.set(index, singleContents);
	}

	public List<WeightedRandomChestContent> getChestContents() {
		return defaultContents;
	}

	public List<WeightedRandomChestContent> getChestContents(int index) {
		return chestContents.get(index);
	}

	public void setChestPlaced(int index, boolean status) {
		if(index < isPlaced.size()) {
			this.isPlaced.set(index, status);
		}
	}

	public boolean getChestPlaced(int index) {
		if(index < isPlaced.size()) {
			return this.isPlaced.get(index);
		} else {
			return false;
		}
	}

	public void setStackMinMax(int index, int min, int max) {
		minChestStacks.set(index, min);
		maxChestStacks.set(index, max);
	}

	public int getStackCount(net.minecraft.util.RandomSource rand, int index) {
		return rand.nextInt(maxChestStacks.get(index) - minChestStacks.get(index) + 1) + minChestStacks.get(index);
	}

	public int getStackCount(net.minecraft.util.RandomSource rand) {
		return rand.nextInt(maxDefaultStacks - minDefaultStacks + 1) + minDefaultStacks;
	}

	public boolean getIsTrapped(int index) {
		return this.isTrapped.get(index);
	}

	public void setIsTrapped(int index, boolean status) {
		this.isTrapped.set(index, status);
	}

	public void writeToNBT(CompoundTag tag) {
		for(int i = 0; i < numChests; i++) {
			tag.putBoolean("placedChest" + i, this.isPlaced.get(i));
		}
	}

	public void readFromNBT(CompoundTag tag) {
		for(int i = 0; i < numChests; i++) {
			this.isPlaced.set(i, tag.getBoolean("placedChest" + i));
		}
	}

}
