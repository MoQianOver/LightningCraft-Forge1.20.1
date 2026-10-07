package org.lightningcraft.items;

import net.minecraft.network.chat.Component;
import org.lightningcraft.util.LCStack;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.IInventoryLEUser;
import org.lightningcraft.init.LCCreativeTabs;
import org.lightningcraft.items.base.ItemMeta;
import org.lightningcraft.ref.LCText;

public class ItemBattery extends ItemMeta {

	public static final int nIcons = 1;

	public static final int nGrades = 3;

	public static final int nSubtypes = nIcons * nGrades;

	public ItemBattery() {
		super(nSubtypes, IInventoryLEUser.ILERarity, new Item.Properties().stacksTo(1));
	}

	@Override
	public void inventoryTick(@Nonnull ItemStack stack, Level level, Entity entity, int par4, boolean par5) {
		if(stack.getCount() < 1) stack = ItemStack.EMPTY;
	}

	public static double getStoredPower(@Nonnull ItemStack stack) {
		return stack.isEmpty() || !stack.hasTag() ? 0 : stack.getTag().getDouble("StoredEnergy");
	}

	public static boolean setStoredPower(@Nonnull ItemStack stack, double power) {
		if(!stack.isEmpty()) {
			if(!stack.hasTag()) stack.setTag(new CompoundTag());
			stack.getTag().putDouble("StoredEnergy", power);
			return true;
		} else {
			return false;
		}
	}

	public static boolean addStoredPower(@Nonnull ItemStack stack, double add) {
		return setStoredPower(stack, getStoredPower(stack) + add);
	}

	public static double getMaxPower(@Nonnull ItemStack stack) {
		return 50D * (stack.getDamageValue() + 1D);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		double power = getStoredPower(stack);
		double maxpower = getMaxPower(stack);
		int charge = (int) (100D * (power / maxpower));
		list.add(Component.translatable("tooltip.lightningcraft.stored_energy", LCText.df.format(power), LCText.df.format(maxpower), charge));
	}

}
