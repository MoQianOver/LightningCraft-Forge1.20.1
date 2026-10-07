package org.lightningcraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import net.minecraft.world.item.Tier;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.items.base.ItemMeta;
import org.lightningcraft.util.ISimpleLEUser;
import org.lightningcraft.util.InventoryLE;
import org.lightningcraft.util.InventoryLE.LECharge;

public class ItemMagnet extends ItemMeta implements ISimpleLEUser {

	public static final double lpPerItemPerTick = 0.1D;
	public static final double t1Range = 8;
	public static final int nMagnets = 4;

	public ItemMagnet() {
		super(nMagnets, Rarity.COMMON, new Item.Properties().stacksTo(1));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		LECharge charge = new LECharge();
		boolean charged = InventoryLE.appendHoverText(stack, level, list, flag, charge);

		list.add(Component.translatable("tooltip.lightningcraft.magnet_range", (int)getRange(stack)));
	}

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    	player.startUsingItem(hand);
    	return new InteractionResultHolder(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

	public static double getRange(ItemStack stack) {
		return stack.isEmpty() ? 0 : (stack.getDamageValue() + 1) * t1Range;
	}

    @Override
    public void onUseTick(Level level, LivingEntity player, ItemStack stack, int count) {
    	if(!level.isClientSide) {

    		double range = getRange(stack);
    		AABB box =
    				new AABB(player.getX() - range, player.getY() - range, player.getZ() - range, player.getX() + range, player.getY() + range, player.getZ() + range);
    		List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, box);

    		for(ItemEntity item : items) {

    			ItemStack battery;
    			if((battery = getLESource((Player)player, lpPerItemPerTick)).isEmpty()) {
    				break;
    			} else {
    				ItemBattery.addStoredPower(battery, -lpPerItemPerTick);
    			}

    			double dx = player.getX() - item.getX();
    			double dy = player.getY() - item.getY();
    			double dz = player.getZ() - item.getZ();
    			float dm = Mth.sqrt((float)(dx * dx + dy * dy + dz * dz));
    			double vel = 0.3D;

			item.setDeltaMovement(vel * dx / dm, vel * dy / dm, vel * dz / dm);
    			item.hurtMarked = true;
    		}

    	}
    }

    @Override
	public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
	public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

}
