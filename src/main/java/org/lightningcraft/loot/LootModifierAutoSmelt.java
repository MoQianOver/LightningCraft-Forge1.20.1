package org.lightningcraft.loot;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import org.lightningcraft.api.IMysticGear;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.items.ItemSkyAxe;
import org.lightningcraft.items.ItemSkyPick;
import org.lightningcraft.items.ItemSkySpade;

public class LootModifierAutoSmelt extends LootModifier {

	public static final int DEF_BONUS = 1;

	public static final int MYSTIC_BONUS = DEF_BONUS + 1;

	public static final Codec<LootModifierAutoSmelt> CODEC = RecordCodecBuilder.create(
			inst -> codecStart(inst).apply(inst, LootModifierAutoSmelt::new));

	@Override
	public Codec<? extends net.minecraftforge.common.loot.IGlobalLootModifier> codec() {
		return CODEC;
	}

	public LootModifierAutoSmelt(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> loot, LootContext ctx) {
		ItemStack tool = ctx.getParamOrNull(LootContextParams.TOOL);
		BlockState state = ctx.getParamOrNull(LootContextParams.BLOCK_STATE);
		ServerLevel level = ctx.getLevel();
		Entity entity = ctx.getParamOrNull(LootContextParams.THIS_ENTITY);
		net.minecraft.world.phys.Vec3 originVec = ctx.getParamOrNull(LootContextParams.ORIGIN);

		if (tool == null || tool.isEmpty() || state == null
				|| !(entity instanceof Player player)
				) {
			return loot;
		}

		boolean isMystic = tool.getItem() instanceof IMysticGear;
		boolean isSky = tool.getItem() instanceof ItemSkyPick
				|| tool.getItem() instanceof ItemSkyAxe
				|| tool.getItem() instanceof ItemSkySpade;
		if (!isMystic && !isSky) {
			return loot;
		}

		if (player.isShiftKeyDown() || EnchantmentHelper.hasSilkTouch(tool)) {
			return loot;
		}

		if (isSky && LCConfig.autoSmelt) {
			ItemStack input = new ItemStack(state.getBlock());
			var found = level.getRecipeManager().getRecipeFor(
					RecipeType.SMELTING, new SimpleContainer(input), level);
			if (found.isPresent()) {
				ItemStack out = found.get().getResultItem(level.registryAccess()).copy();

				if (!out.isEmpty() && !(out.getItem() instanceof BlockItem)) {
					int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
					int levels = fortune + DEF_BONUS + (isMystic ? 1 : 0);
					int n = levels > 0
							? 1 + level.getRandom().nextInt(levels + 1)
							: 1;
					loot.clear();
					for (int i = 0; i < n; i++) {
						loot.add(out.copy());
					}
					return loot;
				}
			}
		}

		if (isMystic && originVec != null) {
			BlockPos origin = BlockPos.containing(originVec);
			ItemStack boosted = tool.copy();
			int base = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
			var enchants = EnchantmentHelper.getEnchantments(boosted);
			enchants.put(Enchantments.BLOCK_FORTUNE, base + MYSTIC_BONUS);
			EnchantmentHelper.setEnchantments(enchants, boosted);

			BlockEntity be = level.getBlockEntity(origin);
			List<ItemStack> reroll = Block.getDrops(state, level, origin, be, player, boosted);
			loot.clear();
			loot.addAll(reroll);
			return loot;
		}

		return loot;
	}
}
