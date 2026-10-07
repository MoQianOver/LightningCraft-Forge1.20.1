package org.lightningcraft.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.lightningcraft.ref.RefStrings;

public final class LCRegistry {

	private LCRegistry() {}

	public static final DeferredRegister<Block> BLOCKS =
			DeferredRegister.create(ForgeRegistries.BLOCKS, RefStrings.MODID);
	public static final DeferredRegister<Item> ITEMS =
			DeferredRegister.create(ForgeRegistries.ITEMS, RefStrings.MODID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RefStrings.MODID);
	public static final DeferredRegister<Fluid> FLUIDS =
			DeferredRegister.create(ForgeRegistries.FLUIDS, RefStrings.MODID);
	public static final DeferredRegister<EntityType<?>> ENTITIES =
			DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RefStrings.MODID);
	public static final DeferredRegister<SoundEvent> SOUNDS =
			DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RefStrings.MODID);
	public static final DeferredRegister<Enchantment> ENCHANTMENTS =
			DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, RefStrings.MODID);
	public static final DeferredRegister<MobEffect> MOB_EFFECTS =
			DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, RefStrings.MODID);
	public static final DeferredRegister<Potion> POTIONS =
			DeferredRegister.create(ForgeRegistries.POTIONS, RefStrings.MODID);

	public static final DeferredRegister<MenuType<?>> MENUS =
			DeferredRegister.create(ForgeRegistries.MENU_TYPES, RefStrings.MODID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
			DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, RefStrings.MODID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
			DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, RefStrings.MODID);
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
			DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, RefStrings.MODID);
	public static final DeferredRegister<Feature<?>> FEATURES =
			DeferredRegister.create(ForgeRegistries.FEATURES, RefStrings.MODID);

	public static final DeferredRegister<FluidType> FLUID_TYPES =
			DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, RefStrings.MODID);

	public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
			DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RefStrings.MODID);
	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
			DeferredRegister.create(Registries.STRUCTURE_TYPE, RefStrings.MODID);
	public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES =
			DeferredRegister.create(Registries.STRUCTURE_PIECE, RefStrings.MODID);

	public static final DeferredRegister<com.mojang.serialization.Codec<? extends net.minecraft.world.level.chunk.ChunkGenerator>> CHUNK_GENERATORS =
			DeferredRegister.create(Registries.CHUNK_GENERATOR, RefStrings.MODID);

	public static final DeferredRegister<Codec<? extends net.minecraftforge.common.loot.IGlobalLootModifier>> LOOT_MODIFIERS =
			DeferredRegister.create(
					net.minecraftforge.registries.ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS,
					RefStrings.MODID);

	public static final net.minecraftforge.registries.RegistryObject<
			net.minecraft.world.level.levelgen.feature.Feature<
					net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration>>
			UNDERWORLD_LIGHT = FEATURES.register("underworld_light",
					() -> new org.lightningcraft.worldgen.feature.FeatureUnderworldLight(
							net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.CODEC));

	public static final net.minecraftforge.registries.RegistryObject<
			net.minecraft.world.level.levelgen.feature.Feature<
					net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration>>
			ICE_SHRUB = FEATURES.register("ice_shrub",
					() -> new org.lightningcraft.worldgen.feature.FeatureIceShrub(
							net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.CODEC));

	public static final net.minecraftforge.registries.RegistryObject<
			net.minecraft.world.level.levelgen.feature.Feature<
					net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration>>
			UNDERWORLD_TREE = FEATURES.register("underworld_tree",
					() -> new org.lightningcraft.worldgen.feature.FeatureUnderworldTree(
							net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.CODEC));

	public static void register(IEventBus bus) {
		BLOCKS.register(bus);
		ITEMS.register(bus);
		BLOCK_ENTITIES.register(bus);
		FLUIDS.register(bus);
		FLUID_TYPES.register(bus);
		ENTITIES.register(bus);
		SOUNDS.register(bus);
		ENCHANTMENTS.register(bus);
		MOB_EFFECTS.register(bus);
		POTIONS.register(bus);
		MENUS.register(bus);
		RECIPE_TYPES.register(bus);
		RECIPE_SERIALIZERS.register(bus);
		PARTICLE_TYPES.register(bus);
		FEATURES.register(bus);
		CREATIVE_TABS.register(bus);
		STRUCTURE_TYPES.register(bus);
		STRUCTURE_PIECES.register(bus);
		CHUNK_GENERATORS.register(bus);
		LOOT_MODIFIERS.register("auto_smelt", () -> org.lightningcraft.loot.LootModifierAutoSmelt.CODEC);
		LOOT_MODIFIERS.register(bus);
	}
}
