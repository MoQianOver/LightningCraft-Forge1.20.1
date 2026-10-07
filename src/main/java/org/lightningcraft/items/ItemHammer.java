package org.lightningcraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;

import net.minecraft.world.item.Tier;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.google.common.collect.Multimap;

import org.lightningcraft.init.LCEnchantments;
import org.lightningcraft.ref.LCText;
import org.lightningcraft.util.Effect;

public class ItemHammer extends ItemChargedSword {

	protected double attackSpeed;

	public ItemHammer(Tier mat) {
		super(mat);
		this.attackSpeed = -3.5;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		list.add(Component.literal(LCText.getChargedLore()));
		list.add(Component.literal(LCText.getSummonerLore()));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if(hand == InteractionHand.MAIN_HAND) {
			player.startUsingItem(hand);
			ItemStack stack = player.getItemInHand(hand);
	        return new InteractionResultHolder(InteractionResult.SUCCESS, stack);
		} else {
			return super.use(level, player, hand);
		}
	}

	@Override
	public void onUseTick(Level level, LivingEntity player, ItemStack stack, int count) {

		int j = EnchantmentHelper.getItemEnchantmentLevel(LCEnchantments.handOfThor, stack);

		if(random.nextDouble() <= (0.05 + j * 0.02)) {
			if(Effect.lightning(player, true));
			stack.hurtAndBreak(10 - j * 2, player, e -> {});
		}
	}

	@Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

	@Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
		Multimap<Attribute, AttributeModifier> multimap = super.getDefaultAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EquipmentSlot.MAINHAND) {

			Multimap<Attribute, AttributeModifier> modifiable =
					com.google.common.collect.HashMultimap.create(multimap);
			modifiable.removeAll(Attributes.ATTACK_SPEED);
			modifiable.put(Attributes.ATTACK_SPEED,
					new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", this.attackSpeed,
							AttributeModifier.Operation.ADDITION));
			return modifiable;
		}

		return multimap;
	}

}
