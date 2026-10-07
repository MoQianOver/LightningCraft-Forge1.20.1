package org.lightningcraft.init;

import org.lightningcraft.util.LCStack;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Tier;
import org.lightningcraft.ref.LCTier;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.api.registry.ILightningCraftItem;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.items.ItemBattery;
import org.lightningcraft.items.ItemBlazeSword;
import org.lightningcraft.items.ItemChargedSword;
import org.lightningcraft.items.ItemElecArmor;
import org.lightningcraft.items.ItemEnderSword;
import org.lightningcraft.items.ItemFeatherSword;
import org.lightningcraft.items.ItemGoldenClub;
import org.lightningcraft.items.ItemGolfClub;
import org.lightningcraft.items.ItemHammer;
import org.lightningcraft.items.ItemIceSword;
import org.lightningcraft.items.ItemKineticArmor;
import org.lightningcraft.items.ItemKineticAxe;
import org.lightningcraft.items.ItemKineticPick;
import org.lightningcraft.items.ItemKineticSpade;
import org.lightningcraft.items.ItemKineticSword;
import org.lightningcraft.items.ItemLightningGuide;
import org.lightningcraft.items.ItemMagnet;
import org.lightningcraft.items.ItemMaterial;
import org.lightningcraft.items.ItemMetalDust;
import org.lightningcraft.items.ItemMetalIngot;
import org.lightningcraft.items.ItemMetalNugget;
import org.lightningcraft.items.ItemMetalPlate;
import org.lightningcraft.items.ItemMetalRod;
import org.lightningcraft.items.ItemMysticArmor;
import org.lightningcraft.items.ItemMysticAxe;
import org.lightningcraft.items.ItemMysticHammer;
import org.lightningcraft.items.ItemMysticHoe;
import org.lightningcraft.items.ItemMysticPick;
import org.lightningcraft.items.ItemMysticSpade;
import org.lightningcraft.items.ItemMysticSword;
import org.lightningcraft.items.ItemSkyArmor;
import org.lightningcraft.items.ItemSkyAxe;
import org.lightningcraft.items.ItemSkyHammer;
import org.lightningcraft.items.ItemSkyHoe;
import org.lightningcraft.items.ItemSkyPick;
import org.lightningcraft.items.ItemSkySpade;
import org.lightningcraft.items.ItemSkySword;
import org.lightningcraft.items.ItemSoulSword;
import org.lightningcraft.items.ItemWirelessMarker;
import org.lightningcraft.items.ItemZombieSword;
import org.lightningcraft.items.base.ItemArmorLC;
import org.lightningcraft.items.base.ItemAxeLC;
import org.lightningcraft.items.base.ItemHoeLC;
import org.lightningcraft.items.base.ItemLC;
import org.lightningcraft.items.base.ItemPickaxeLC;
import org.lightningcraft.items.base.ItemSpadeLC;
import org.lightningcraft.items.base.ItemSwordLC;
import org.lightningcraft.ref.Metal.Ingot;
import org.lightningcraft.ref.RefStrings;
import org.lightningcraft.util.ArmorHelper;
import org.lightningcraft.util.ToolHelper;

public class LCItems {

	private static final JointList<ILightningCraftItem> items = new JointList<>();

	public static void createItems() {
		setupMaterials();
		addItems();

	}

	public static Tier elecMat;
	public static Tier elecHammerMat;
	public static Tier skyMat;
	public static Tier skyHammerMat;
	public static Tier mysticMat;
	public static Tier mysticHammerMat;
	public static Tier kineticMat;
	public static Tier soulMat;
	public static Tier zombieMat;
	public static Tier featherMat;
	public static Tier enderMat;
	public static Tier blazeMat;
	public static Tier iceMat;
	public static ArmorMaterial elecArmor;
	public static ArmorMaterial skyArmor;
	public static ArmorMaterial mysticArmor;
	public static ArmorMaterial kineticArmor;

	private static void setupMaterials() {

		elecMat = new LCTier("Electricium", 4, 3000, 14.0f, 5.0f, 22);
		elecHammerMat = new LCTier("Electricium Hammer", 4, 3500, 16.0f, 9.0f, 22);
		skyMat = new LCTier("Skyfather", 5, 3500, 18.0f, 6.5f, 30);
		skyHammerMat = new LCTier("Skyfather Hammer", 5, 3500, 18.0f, 12.0f, 30);
		mysticMat = new LCTier("Mystic", 6, 4000, 18.0f, 8.0f, 30);
		mysticHammerMat = new LCTier("Mystic Hammer", 6, 4000, 18.0f, 15.0f, 30);
		kineticMat = new LCTier("Kinetic Shard", 3, 100, 1.0f, 0.0f, 1);

		soulMat = new LCTier("Soul", 1, 150, 4.0F, 1.5F, 10);
		zombieMat = new LCTier("Zombie", 1, 131, 4.0F, 1.0F, 10);
		featherMat = new LCTier("Feather", 1, 131, 4.0F, 1.0F, 10);
		enderMat = new LCTier("Ender", 1, 200, 4.0F, 1.5F, 10);
		blazeMat = new LCTier("Blaze", 1, 150, 4.0F, 1.0F, 10);
		iceMat = new LCTier("Ice", 1, 100, 4.0F, 0.5F, 10);

		elecArmor = ArmorHelper.addArmorMaterial("Electricium", RefStrings.MODID + ":elec_armor", 40, new int[]{3, 6, 8, 4}, 25, SoundEvents.ARMOR_EQUIP_GOLD, 2F);
		skyArmor = ArmorHelper.addArmorMaterial("Skyfather", RefStrings.MODID + ":sky_armor", 50, new int[]{3, 7, 8, 4}, 20, SoundEvents.ARMOR_EQUIP_GOLD, 2.5F);
		mysticArmor = ArmorHelper.addArmorMaterial("Mystic", RefStrings.MODID + ":mystic_armor", 60, new int[]{4, 7, 8, 4}, 15, SoundEvents.ARMOR_EQUIP_GOLD, 3F);
		kineticArmor = ArmorHelper.addArmorMaterial("Kinetic", RefStrings.MODID + ":kinetic_armor", 4, new int[]{1, 3, 5, 2}, 1, SoundEvents.ARMOR_EQUIP_GOLD, 0F);
	}

	public static void finalizeMaterials() {

		ToolHelper.setRepairStack(elecMat, LCStack.of(ingot, 1, Ingot.ELEC));
		ToolHelper.setRepairStack(elecHammerMat, LCStack.of(ingot, 1, Ingot.ELEC));
		ToolHelper.setRepairStack(skyMat, LCStack.of(ingot, 1, Ingot.SKY));
		ToolHelper.setRepairStack(skyHammerMat, LCStack.of(ingot, 1, Ingot.SKY));
		ToolHelper.setRepairStack(mysticMat, LCStack.of(ingot, 1, Ingot.MYSTIC));
		ToolHelper.setRepairStack(mysticHammerMat, LCStack.of(ingot, 1, Ingot.MYSTIC));

		ToolHelper.setRepairStack(soulMat, new ItemStack(Blocks.SOUL_SAND));
		ToolHelper.setRepairStack(zombieMat, new ItemStack(Items.ROTTEN_FLESH));
		ToolHelper.setRepairStack(featherMat, new ItemStack(Items.FEATHER));
		ToolHelper.setRepairStack(enderMat, new ItemStack(Items.ENDER_PEARL));
		ToolHelper.setRepairStack(blazeMat, new ItemStack(Items.BLAZE_ROD));
		ToolHelper.setRepairStack(iceMat, new ItemStack(Blocks.PACKED_ICE));

		ArmorHelper.setRepairStack(elecArmor, LCStack.of(ingot, 1, Ingot.ELEC));
		ArmorHelper.setRepairStack(skyArmor, LCStack.of(ingot, 1, Ingot.SKY));
		ArmorHelper.setRepairStack(mysticArmor, LCStack.of(ingot, 1, Ingot.MYSTIC));

		buildCreativeTabs();
	}

	private static void buildCreativeTabs() {
		org.lightningcraft.creativetabs.LCCreativeTab.BLOCK_ENTRIES.clear();
		org.lightningcraft.creativetabs.LCCreativeTab.ITEM_ENTRIES.clear();

		for (net.minecraftforge.registries.RegistryObject<net.minecraft.world.item.Item> entry
				: LCRegistry.ITEMS.getEntries()) {
			net.minecraft.world.item.Item item = entry.get();

			if (item instanceof net.minecraft.world.item.BlockItem blockItem) {

				net.minecraft.world.level.block.Block b = blockItem.getBlock();
				if (item instanceof org.lightningcraft.items.blocks.ItemBlockUnderTNT meta) {
					meta.addCreativeTabEntries();
				} else if (b instanceof org.lightningcraft.blocks.base.BlockVariant variant) {
					int n = Math.max(1, variant.getSubBlockCount());
					for (int meta = 0; meta < n; ++meta) {
						org.lightningcraft.creativetabs.LCCreativeTab.addBlockEntry(
								org.lightningcraft.util.LCStack.of(item, 1, meta));
					}
				} else {
					org.lightningcraft.creativetabs.LCCreativeTab.addBlockEntry(new ItemStack(item));
				}
			} else {
				if (item instanceof org.lightningcraft.items.base.ItemMeta meta) {
					meta.addCreativeTabEntries();
				} else {
					org.lightningcraft.creativetabs.LCCreativeTab.addItemEntry(new ItemStack(item));
				}
			}
		}

		org.lightningcraft.ref.Log.logger.info("Creative tabs built: "
				+ org.lightningcraft.creativetabs.LCCreativeTab.BLOCK_ENTRIES.size() + " blocks, "
				+ org.lightningcraft.creativetabs.LCCreativeTab.ITEM_ENTRIES.size() + " items.");
	}

	public static ItemLC guide;
	public static ItemLC golfClub;
	public static ItemLC golfClubGold;
	public static ItemLC ingot;
	public static ItemLC nugget;
	public static ItemLC dust;
	public static ItemLC rod;
	public static ItemLC plate;
	public static ItemLC material;
	public static ItemLC battery;
	public static ItemLC itemMagnet;
	public static ItemLC wirelessMarker;
	public static ItemSwordLC elecHammer, elecSword;
	public static ItemPickaxeLC elecPick;
	public static ItemAxeLC elecAxe;
	public static ItemSpadeLC elecShovel;
	public static ItemHoeLC elecHoe;
	public static ItemArmorLC elecHelm, elecChest, elecLegs, elecBoots;
	public static ItemSwordLC soulSword, zombieSword, featherSword, enderSword, blazeSword, iceSword;
	public static ItemSwordLC skyHammer, skySword;
	public static ItemPickaxeLC skyPick;
	public static ItemAxeLC skyAxe;
	public static ItemSpadeLC skyShovel;
	public static ItemHoeLC skyHoe;
	public static ItemArmorLC skyHelm, skyChest, skyLegs, skyBoots;
	public static ItemSwordLC kineticSword;
	public static ItemPickaxeLC kineticPick;
	public static ItemAxeLC kineticAxe;
	public static ItemSpadeLC kineticShovel;
	public static ItemArmorLC kineticHelm, kineticChest, kineticLegs, kineticBoots;
	public static ItemSwordLC mysticHammer, mysticSword;
	public static ItemPickaxeLC mysticPick;
	public static ItemAxeLC mysticAxe;
	public static ItemSpadeLC mysticShovel;
	public static ItemHoeLC mysticHoe;
	public static ItemArmorLC mysticHelm, mysticChest, mysticLegs, mysticBoots;

		private static void addItems() {
		LCRegistry.ITEMS.register("guide", () -> track(guide = new ItemLightningGuide()));
		LCRegistry.ITEMS.register("golf_club", () -> track(golfClub = new ItemGolfClub()));
		LCRegistry.ITEMS.register("golf_club_gold", () -> track(golfClubGold = new ItemGoldenClub()));
		LCRegistry.ITEMS.register("ingot", () -> track(ingot = new ItemMetalIngot()));
		LCRegistry.ITEMS.register("nugget", () -> track(nugget = new ItemMetalNugget()));
		LCRegistry.ITEMS.register("dust", () -> track(dust = new ItemMetalDust()));
		LCRegistry.ITEMS.register("rod", () -> track(rod = new ItemMetalRod()));
		LCRegistry.ITEMS.register("plate", () -> track(plate = new ItemMetalPlate()));
		LCRegistry.ITEMS.register("material", () -> track(material = new ItemMaterial()));
		LCRegistry.ITEMS.register("battery", () -> track(battery = new ItemBattery()));
		LCRegistry.ITEMS.register("item_magnet", () -> track(itemMagnet = new ItemMagnet()));
		LCRegistry.ITEMS.register("wireless_marker", () -> track(wirelessMarker = new ItemWirelessMarker()));
		LCRegistry.ITEMS.register("elec_hammer", () -> track(elecHammer = new ItemHammer(elecHammerMat)));
		LCRegistry.ITEMS.register("elec_sword", () -> track(elecSword = new ItemChargedSword(elecMat)));
		LCRegistry.ITEMS.register("elec_pick", () -> track(elecPick = new ItemPickaxeLC(elecMat)));
		LCRegistry.ITEMS.register("elec_axe", () -> track(elecAxe = new ItemAxeLC(elecMat)));
		LCRegistry.ITEMS.register("elec_shovel", () -> track(elecShovel = new ItemSpadeLC(elecMat)));
		LCRegistry.ITEMS.register("elec_hoe", () -> track(elecHoe = new ItemHoeLC(elecMat)));
		LCRegistry.ITEMS.register("elec_helm", () -> track(elecHelm = new ItemElecArmor(elecArmor, EquipmentSlot.HEAD)));
		LCRegistry.ITEMS.register("elec_chest", () -> track(elecChest = new ItemElecArmor(elecArmor, EquipmentSlot.CHEST)));
		LCRegistry.ITEMS.register("elec_legs", () -> track(elecLegs = new ItemElecArmor(elecArmor, EquipmentSlot.LEGS)));
		LCRegistry.ITEMS.register("elec_boots", () -> track(elecBoots = new ItemElecArmor(elecArmor, EquipmentSlot.FEET)));
		LCRegistry.ITEMS.register("soul_sword", () -> track(soulSword = new ItemSoulSword(soulMat)));
		LCRegistry.ITEMS.register("zombie_sword", () -> track(zombieSword = new ItemZombieSword(zombieMat)));
		LCRegistry.ITEMS.register("feather_sword", () -> track(featherSword = new ItemFeatherSword(featherMat)));
		LCRegistry.ITEMS.register("ender_sword", () -> track(enderSword = new ItemEnderSword(enderMat)));
		LCRegistry.ITEMS.register("blaze_sword", () -> track(blazeSword = new ItemBlazeSword(blazeMat)));
		LCRegistry.ITEMS.register("ice_sword", () -> track(iceSword = new ItemIceSword(iceMat)));
		LCRegistry.ITEMS.register("sky_hammer", () -> track(skyHammer = new ItemSkyHammer(skyHammerMat)));
		LCRegistry.ITEMS.register("sky_sword", () -> track(skySword = new ItemSkySword(skyMat)));
		LCRegistry.ITEMS.register("sky_pick", () -> track(skyPick = new ItemSkyPick(skyMat)));
		LCRegistry.ITEMS.register("sky_axe", () -> track(skyAxe = new ItemSkyAxe(skyMat)));
		LCRegistry.ITEMS.register("sky_shovel", () -> track(skyShovel = new ItemSkySpade(skyMat)));
		LCRegistry.ITEMS.register("sky_hoe", () -> track(skyHoe = new ItemSkyHoe(skyMat)));
		LCRegistry.ITEMS.register("sky_helm", () -> track(skyHelm = new ItemSkyArmor(skyArmor, EquipmentSlot.HEAD)));
		LCRegistry.ITEMS.register("sky_chest", () -> track(skyChest = new ItemSkyArmor(skyArmor, EquipmentSlot.CHEST)));
		LCRegistry.ITEMS.register("sky_legs", () -> track(skyLegs = new ItemSkyArmor(skyArmor, EquipmentSlot.LEGS)));
		LCRegistry.ITEMS.register("sky_boots", () -> track(skyBoots = new ItemSkyArmor(skyArmor, EquipmentSlot.FEET)));
		LCRegistry.ITEMS.register("kinetic_sword", () -> track(kineticSword = new ItemKineticSword(kineticMat)));
		LCRegistry.ITEMS.register("kinetic_pick", () -> track(kineticPick = new ItemKineticPick(kineticMat)));
		LCRegistry.ITEMS.register("kinetic_axe", () -> track(kineticAxe = new ItemKineticAxe(kineticMat)));
		LCRegistry.ITEMS.register("kinetic_shovel", () -> track(kineticShovel = new ItemKineticSpade(kineticMat)));
		LCRegistry.ITEMS.register("kinetic_helm", () -> track(kineticHelm = new ItemKineticArmor(kineticArmor, EquipmentSlot.HEAD)));
		LCRegistry.ITEMS.register("kinetic_chest", () -> track(kineticChest = new ItemKineticArmor(kineticArmor, EquipmentSlot.CHEST)));
		LCRegistry.ITEMS.register("kinetic_legs", () -> track(kineticLegs = new ItemKineticArmor(kineticArmor, EquipmentSlot.LEGS)));
		LCRegistry.ITEMS.register("kinetic_boots", () -> track(kineticBoots = new ItemKineticArmor(kineticArmor, EquipmentSlot.FEET)));
		LCRegistry.ITEMS.register("mystic_hammer", () -> track(mysticHammer = new ItemMysticHammer(mysticHammerMat)));
		LCRegistry.ITEMS.register("mystic_sword", () -> track(mysticSword = new ItemMysticSword(mysticMat)));
		LCRegistry.ITEMS.register("mystic_pick", () -> track(mysticPick = new ItemMysticPick(mysticMat)));
		LCRegistry.ITEMS.register("mystic_axe", () -> track(mysticAxe = new ItemMysticAxe(mysticMat)));
		LCRegistry.ITEMS.register("mystic_shovel", () -> track(mysticShovel = new ItemMysticSpade(mysticMat)));
		LCRegistry.ITEMS.register("mystic_hoe", () -> track(mysticHoe = new ItemMysticHoe(mysticMat)));
		LCRegistry.ITEMS.register("mystic_helm", () -> track(mysticHelm = new ItemMysticArmor(mysticArmor, EquipmentSlot.HEAD)));
		LCRegistry.ITEMS.register("mystic_chest", () -> track(mysticChest = new ItemMysticArmor(mysticArmor, EquipmentSlot.CHEST)));
		LCRegistry.ITEMS.register("mystic_legs", () -> track(mysticLegs = new ItemMysticArmor(mysticArmor, EquipmentSlot.LEGS)));
		LCRegistry.ITEMS.register("mystic_boots", () -> track(mysticBoots = new ItemMysticArmor(mysticArmor, EquipmentSlot.FEET)));
	}

	private static <T extends net.minecraft.world.item.Item & ILightningCraftItem> T track(T item) {
		items.add(item);
		return item;
	}

	public static void registerItems() {

	}

	@OnlyIn(Dist.CLIENT)
	public static void registerRendering() {

	}

}
