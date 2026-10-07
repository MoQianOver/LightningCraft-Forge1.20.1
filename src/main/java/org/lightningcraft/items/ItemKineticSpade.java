package org.lightningcraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Tier;

import java.util.List;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import org.lightningcraft.api.IKineticGear;
import org.lightningcraft.items.base.ItemSpadeLC;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.util.ISimpleLEUser;
import org.lightningcraft.util.InventoryLE;
import org.lightningcraft.util.InventoryLE.LECharge;

public class ItemKineticSpade extends ItemSpadeLC implements ISimpleLEUser, IKineticGear {

	public static final double attackDamage = 3;

	public ItemKineticSpade(Tier mat) {
		super(mat);
	}

	@Override
	public void inventoryTick(ItemStack a, Level level, Entity player, int par4, boolean par5) {

		if(!a.hasTag()) a.setTag(new CompoundTag());
		InventoryLE.updateToolPower(a, (Player)player);
	}

	@Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity user) {
		InventoryLE.hitEntity(stack, entity, user, attackDamage, false);
    	return super.hurtEnemy(stack, entity, user);
    }

	@Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
		float base = super.getDestroySpeed(stack, state);
		return InventoryLE.getEfficiency(stack, base);
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState block, BlockPos pos, LivingEntity user) {
		float base = super.getDestroySpeed(stack, level.getBlockState(pos));
		InventoryLE.onBlockBreak(stack, (Player)user, base);
		return super.mineBlock(stack, level, block, pos, user);
	}

	@Override
	public Multimap getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return HashMultimap.create();
    }

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		LECharge charge = new LECharge();
		boolean charged = InventoryLE.appendHoverText(stack, level, list, flag, charge);
		double damage = 0;

		if(charged && charge.getCharge() > 0) {
			list.add(Component.literal(LCText.getAutoRepair2Lore()));
			damage = Math.min(charge.getCharge(), attackDamage);
		}

		list.add(Component.literal(""));
		list.add(Component.literal(LCText.secSign + "9+" + LCText.af.format(damage) + " Attack Damage"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Rarity getRarity(ItemStack stack) {
		return ILERarity;
	}

}
