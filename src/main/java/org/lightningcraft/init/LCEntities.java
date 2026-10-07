package org.lightningcraft.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import org.lightningcraft.entities.EntityDemonSoldier;
import org.lightningcraft.entities.EntityLCElectricAttack;
import org.lightningcraft.entities.EntityLCLightningBolt;
import org.lightningcraft.entities.EntityLCTNTPrimed;
import org.lightningcraft.entities.EntityLCZombie;
import org.lightningcraft.entities.EntityUnderworldCreeper;
import org.lightningcraft.entities.EntityUnderworldGhast;
import org.lightningcraft.entities.EntityUnderworldSilverfish;
import org.lightningcraft.entities.EntityUnderworldSkeleton;
import org.lightningcraft.entities.EntityUnderworldSlime;

public class LCEntities {

	public static final RegistryObject<EntityType<EntityLCZombie>> HELPER_ZOMBIE =
		LCRegistry.ENTITIES.register("helper_zombie", () -> EntityType.Builder
			.<EntityLCZombie>of(EntityLCZombie::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(80).build("helper_zombie"));

	public static final RegistryObject<EntityType<EntityDemonSoldier>> DEMON_SOLDIER =
		LCRegistry.ENTITIES.register("demon_soldier", () -> EntityType.Builder
			.<EntityDemonSoldier>of(EntityDemonSoldier::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(80).build("demon_soldier"));

	public static final RegistryObject<EntityType<EntityUnderworldSlime>> UNDERWORLD_SLIME =
		LCRegistry.ENTITIES.register("underworld_slime", () -> EntityType.Builder
			.of(EntityUnderworldSlime::new, MobCategory.MONSTER).sized(0.52F, 0.52F).clientTrackingRange(80).build("underworld_slime"));

	public static final RegistryObject<EntityType<EntityUnderworldSkeleton>> UNDERWORLD_SKELETON =
		LCRegistry.ENTITIES.register("underworld_skeleton", () -> EntityType.Builder
			.of(EntityUnderworldSkeleton::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(80).build("underworld_skeleton"));

	public static final RegistryObject<EntityType<EntityUnderworldSilverfish>> UNDERWORLD_SILVERFISH =
		LCRegistry.ENTITIES.register("underworld_silverfish", () -> EntityType.Builder
			.of(EntityUnderworldSilverfish::new, MobCategory.MONSTER).sized(0.4F, 0.3F).clientTrackingRange(80).build("underworld_silverfish"));

	public static final RegistryObject<EntityType<EntityUnderworldGhast>> UNDERWORLD_GHAST =
		LCRegistry.ENTITIES.register("underworld_ghast", () -> EntityType.Builder
			.of(EntityUnderworldGhast::new, MobCategory.MONSTER).sized(4.0F, 4.0F).clientTrackingRange(80).build("underworld_ghast"));

	public static final RegistryObject<EntityType<EntityUnderworldCreeper>> UNDERWORLD_CREEPER =
		LCRegistry.ENTITIES.register("underworld_creeper", () -> EntityType.Builder
			.of(EntityUnderworldCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(80).build("underworld_creeper"));

	public static final RegistryObject<EntityType<EntityLCTNTPrimed>> LC_TNT_PRIMED =
		LCRegistry.ENTITIES.register("lc_tnt_primed", () -> EntityType.Builder
			.<EntityLCTNTPrimed>of(EntityLCTNTPrimed::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(160).updateInterval(10).build("lc_tnt_primed"));

	public static final RegistryObject<EntityType<EntityLCElectricAttack>> LC_ELECTRIC_ATTACK =
		LCRegistry.ENTITIES.register("lc_electric_attack", () -> EntityType.Builder
			.<EntityLCElectricAttack>of(EntityLCElectricAttack::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(160).updateInterval(10).build("lc_electric_attack"));

	public static final RegistryObject<EntityType<EntityLCLightningBolt>> LC_LIGHTNING_BOLT =
		LCRegistry.ENTITIES.register("lc_lightning_bolt", () -> EntityType.Builder
			.<EntityLCLightningBolt>of(EntityLCLightningBolt::new, MobCategory.MISC).sized(0.0F, 0.0F).clientTrackingRange(160).updateInterval(10).build("lc_lightning_bolt"));

	@net.minecraftforge.eventbus.api.SubscribeEvent
	public static void onAttributeCreation(net.minecraftforge.event.entity.EntityAttributeCreationEvent event) {
		event.put(HELPER_ZOMBIE.get(), EntityLCZombie.createAttributes().build());
		event.put(DEMON_SOLDIER.get(), EntityDemonSoldier.createAttributes().build());
		event.put(UNDERWORLD_SLIME.get(), EntityUnderworldSlime.createAttributes().build());
		event.put(UNDERWORLD_SKELETON.get(), EntityUnderworldSkeleton.createAttributes().build());
		event.put(UNDERWORLD_SILVERFISH.get(), EntityUnderworldSilverfish.createAttributes().build());
		event.put(UNDERWORLD_GHAST.get(), EntityUnderworldGhast.createAttributes().build());
		event.put(UNDERWORLD_CREEPER.get(), EntityUnderworldCreeper.createAttributes().build());
	}

	public static final RegistryObject<EntityType<org.lightningcraft.entities.EntityLCItem>> LC_ITEM =
		LCRegistry.ENTITIES.register("lc_item", () -> EntityType.Builder
			.<org.lightningcraft.entities.EntityLCItem>of(org.lightningcraft.entities.EntityLCItem::new, MobCategory.MISC)
			.sized(0.25F, 0.25F).clientTrackingRange(64).updateInterval(20).build("lc_item"));

	public static final RegistryObject<net.minecraft.world.item.Item> DEMON_SOLDIER_EGG =
			LCRegistry.ITEMS.register("demon_soldier_spawn_egg",
					() -> new net.minecraftforge.common.ForgeSpawnEggItem(DEMON_SOLDIER, 0x282828, 0xE07220,
							new net.minecraft.world.item.Item.Properties()));

	public static final RegistryObject<net.minecraft.world.item.Item> UNDERWORLD_SLIME_EGG =
			LCRegistry.ITEMS.register("underworld_slime_spawn_egg",
					() -> new net.minecraftforge.common.ForgeSpawnEggItem(UNDERWORLD_SLIME, 0x002040, 0x20A0D3,
							new net.minecraft.world.item.Item.Properties()));

	public static final RegistryObject<net.minecraft.world.item.Item> UNDERWORLD_SKELETON_EGG =
			LCRegistry.ITEMS.register("underworld_skeleton_spawn_egg",
					() -> new net.minecraftforge.common.ForgeSpawnEggItem(UNDERWORLD_SKELETON, 0x006161, 0x40D3D3,
							new net.minecraft.world.item.Item.Properties()));

	public static final RegistryObject<net.minecraft.world.item.Item> UNDERWORLD_SILVERFISH_EGG =
			LCRegistry.ITEMS.register("underworld_silverfish_spawn_egg",
					() -> new net.minecraftforge.common.ForgeSpawnEggItem(UNDERWORLD_SILVERFISH, 0x400000, 0xC02020,
							new net.minecraft.world.item.Item.Properties()));

	public static final RegistryObject<net.minecraft.world.item.Item> UNDERWORLD_GHAST_EGG =
			LCRegistry.ITEMS.register("underworld_ghast_spawn_egg",
					() -> new net.minecraftforge.common.ForgeSpawnEggItem(UNDERWORLD_GHAST, 0x282828, 0xC0C0C0,
							new net.minecraft.world.item.Item.Properties()));

	public static final RegistryObject<net.minecraft.world.item.Item> UNDERWORLD_CREEPER_EGG =
			LCRegistry.ITEMS.register("underworld_creeper_spawn_egg",
					() -> new net.minecraftforge.common.ForgeSpawnEggItem(UNDERWORLD_CREEPER, 0x003C50, 0x40A0AF,
							new net.minecraft.world.item.Item.Properties()));

	public static void mainRegistry() {
	}

	public static void registerRendering() {

	}

}