package org.lightningcraft.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.lightningcraft.gui.server.ContainerEnchReallocator;
import org.lightningcraft.gui.server.ContainerLightningCannon;
import org.lightningcraft.gui.server.ContainerLightningCell;
import org.lightningcraft.gui.server.ContainerLightningCrusher;
import org.lightningcraft.gui.server.ContainerLightningFurnace;
import org.lightningcraft.gui.server.ContainerLightningInfuser;
import org.lightningcraft.gui.server.ContainerLightningMiner;
import org.lightningcraft.gui.server.ContainerLightningReceiver;
import org.lightningcraft.gui.server.ContainerRFProvider;
import org.lightningcraft.gui.server.ContainerRFReceiver;
import org.lightningcraft.gui.server.ContainerStaticGenerator;

public class LCMenus {

	public static final DeferredRegister<MenuType<?>> MENUS = LCRegistry.MENUS;

	@SuppressWarnings("unchecked")
	public static <T> T tileAt(net.minecraft.world.entity.player.Inventory player, net.minecraft.core.BlockPos pos) {
		return (T) player.player.level().getBlockEntity(pos);
	}

	public static final RegistryObject<MenuType<ContainerLightningCell>> LIGHTNING_CELL =
		MENUS.register("lightning_cell", () -> IForgeMenuType.create(
			(id, inv, buf) -> new ContainerLightningCell(id, inv, buf.readBlockPos())));

	public static final RegistryObject<MenuType<ContainerLightningFurnace>> LIGHTNING_FURNACE =
		MENUS.register("lightning_furnace", () -> IForgeMenuType.create(
			(id, inv, buf) -> new ContainerLightningFurnace(id, inv, buf.readBlockPos())));

	public static final RegistryObject<MenuType<ContainerLightningCrusher>> LIGHTNING_CRUSHER =
		MENUS.register("lightning_crusher", () -> IForgeMenuType.create(
			(id, inv, buf) -> new ContainerLightningCrusher(id, inv, buf.readBlockPos())));

	public static final RegistryObject<MenuType<ContainerLightningInfuser>> LIGHTNING_INFUSER =
		MENUS.register("lightning_infuser", () -> IForgeMenuType.create(
			(id, inv, buf) -> new ContainerLightningInfuser(id, inv, buf.readBlockPos())));

	public static final RegistryObject<MenuType<ContainerLightningMiner>> LIGHTNING_MINER =
		MENUS.register("lightning_miner", () -> IForgeMenuType.create(
			(id, inv, buf) -> new ContainerLightningMiner(id, inv, buf.readBlockPos())));

	public static final RegistryObject<MenuType<ContainerStaticGenerator>> STATIC_GENERATOR =
		MENUS.register("static_generator", () -> IForgeMenuType.create(
			(id, inv, buf) -> new ContainerStaticGenerator(id, inv, buf.readBlockPos())));

	public static final RegistryObject<MenuType<ContainerEnchReallocator>> ENCH_REALLOCATOR =
		MENUS.register("ench_reallocator", () -> IForgeMenuType.create(
			(id, inv, buf) -> new ContainerEnchReallocator(id, inv, buf.readBlockPos())));

	public static final RegistryObject<MenuType<ContainerLightningCannon>> LIGHTNING_CANNON =
		MENUS.register("lightning_cannon", () -> IForgeMenuType.create(
			(id, inv, buf) -> new ContainerLightningCannon(id, inv, buf.readBlockPos())));

	public static final RegistryObject<MenuType<ContainerLightningReceiver>> LIGHTNING_RECEIVER =
		MENUS.register("lightning_receiver", () -> IForgeMenuType.create(
			(id, inv, buf) -> new ContainerLightningReceiver(id, inv, buf.readBlockPos())));

	public static final RegistryObject<MenuType<ContainerRFProvider>> RF_PROVIDER =
		MENUS.register("rf_provider", () -> IForgeMenuType.create(
			(id, inv, buf) -> new ContainerRFProvider(id, inv, buf.readBlockPos())));

	public static final RegistryObject<MenuType<ContainerRFReceiver>> RF_RECEIVER =
		MENUS.register("rf_receiver", () -> IForgeMenuType.create(
			(id, inv, buf) -> new ContainerRFReceiver(id, inv, buf.readBlockPos())));

	public static void mainRegistry() {

	}
}