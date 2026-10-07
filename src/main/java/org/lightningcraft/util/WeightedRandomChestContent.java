package org.lightningcraft.util;

import net.minecraft.util.RandomSource;

import java.util.List;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

public class WeightedRandomChestContent
{

    public ItemStack theItemId;

    public int minStackSize;

    public int maxStackSize;

    public int itemWeight;

    public WeightedRandomChestContent(Item item, int meta, int minimumChance, int maximumChance, int itemWeightIn)
    {
        this.itemWeight = itemWeightIn;

        this.theItemId = (item == null) ? ItemStack.EMPTY : LCStack.of(item, 1, meta);
        this.minStackSize = minimumChance;
        this.maxStackSize = maximumChance;
    }

    public static void generateChestContents(net.minecraft.util.RandomSource rand, List<WeightedRandomChestContent> listIn, ChestBlockEntity dispenser, int max)
    {
        for (int i = 0; i < max; ++i)
        {
                        WeightedRandomChestContent wrcc = getRandomItem(rand, listIn);
            if (wrcc == null) return;

            if (wrcc.theItemId == null || wrcc.theItemId.isEmpty()) continue;
            int j = wrcc.minStackSize + rand.nextInt(wrcc.maxStackSize - wrcc.minStackSize + 1);

            if (wrcc.theItemId.getMaxStackSize() >= j)
            {
                ItemStack itemstack1 = wrcc.theItemId.copy();
                itemstack1.setCount(j);
                dispenser.setItem(rand.nextInt(dispenser.getContainerSize()), itemstack1);
            }
            else
            {
                for (int k = 0; k < j; ++k)
                {
                    ItemStack itemstack = wrcc.theItemId.copy();
                    itemstack.setCount(1);
                    dispenser.setItem(rand.nextInt(dispenser.getContainerSize()), itemstack);
                }
            }
        }
    }

    private static WeightedRandomChestContent getRandomItem(net.minecraft.util.RandomSource rand, List<WeightedRandomChestContent> list)
    {
        int total = 0;
        for (WeightedRandomChestContent c : list) total += c.itemWeight;
        if (total <= 0) return null;
        int roll = rand.nextInt(total);
        for (WeightedRandomChestContent c : list)
        {
            roll -= c.itemWeight;
            if (roll < 0) return c;
        }
        return list.get(list.size() - 1);
    }
}