package org.lightningcraft.entities;
import net.minecraft.world.entity.EntityType;

import java.awt.Color;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.Level;
import org.lightningcraft.api.util.StackHelper;
import org.lightningcraft.ref.RefStrings;

public class EntityUnderworldSkeleton extends Skeleton {

	public EntityUnderworldSkeleton(EntityType<EntityUnderworldSkeleton> type, Level level ) {
		super(type, level);
	}

	public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
		return net.minecraft.world.entity.monster.Monster.createMonsterAttributes()
				.add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0D)
				.add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.325D)
				.add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 5.0D);
	}

	@Override
	protected void populateDefaultEquipmentSlots(net.minecraft.util.RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(this.random, difficulty);
        boolean hasBow = this.random.nextBoolean();
        if(!hasBow) {
        	this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
        } else {
        	this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
        }

        ItemStack leather_helmet = new ItemStack(Items.LEATHER_HELMET); StackHelper.setStackColor(leather_helmet, new Color(1,0,0));
        ItemStack leather_chestplate = new ItemStack(Items.LEATHER_CHESTPLATE); StackHelper.setStackColor(leather_chestplate, new Color(1,0,0));
        ItemStack leather_leggings = new ItemStack(Items.LEATHER_LEGGINGS); StackHelper.setStackColor(leather_leggings, new Color(1,0,0));
        ItemStack leather_boots = new ItemStack(Items.LEATHER_BOOTS); StackHelper.setStackColor(leather_boots, new Color(1,0,0));

        this.setItemSlot(EquipmentSlot.HEAD, leather_helmet);
        this.setItemSlot(EquipmentSlot.CHEST, leather_chestplate);
        this.setItemSlot(EquipmentSlot.LEGS, leather_leggings);
        this.setItemSlot(EquipmentSlot.FEET, leather_boots);
    }

}