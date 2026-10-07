package org.lightningcraft.items;

import net.minecraft.network.chat.Component;
import org.lightningcraft.util.LCStack;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.util.StackHelper;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.items.base.ItemLC;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.ref.Metal.Rod;
import org.lightningcraft.util.Effect;
import org.lightningcraft.util.SkyUtils;

public class ItemGolfClub extends ItemLC {

	public ItemGolfClub() {
		this(new Item.Properties().durability(6));
	}

	protected ItemGolfClub(Item.Properties properties) {
		super(Rarity.COMMON, properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		list.add(Component.literal(LCText.getSummonerLore()));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if(!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
			ItemStack stack = player.getItemInHand(hand);
			SkyUtils.damageStack(2, stack, player, EquipmentSlot.MAINHAND, true);
			Effect.lightning(player, false);

		}
		return super.use(level, player, hand);
	}

	@Override
	public boolean isValidRepairItem(ItemStack a, ItemStack b) {
		ItemStack repairWith = LCStack.of(LCItems.rod, 0, Rod.IRON);
		if(StackHelper.areItemStacksEqualForCrafting(repairWith, b)){
			return true;
		} else {
			return false;
		}
	}

}
