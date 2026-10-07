package org.lightningcraft.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkHooks;
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

public class LCGuiHandler {

	public static final int lightningCellGui = 0;
	public static final int lightningFurnaceGui = 1;
	public static final int lightningCrusherGui = 2;
	public static final int lightningInfuserGui = 3;
	public static final int staticGeneratorGui = 4;
	public static final int enchReallocatorGui = 5;
	public static final int lightningRXGui = 6;
	public static final int lightningCannonGui = 7;
	public static final int lightningMinerGui = 8;

	public static final int lightningLEtoRFGui = 100;
	public static final int lightningRFtoLEGui = 101;

	public static void open(Player player, int guiId, BlockPos pos) {
		if (player.level().isClientSide()) return;
		if (!(player instanceof ServerPlayer serverPlayer)) return;

		NetworkHooks.openScreen(serverPlayer,
				new SimpleMenuProvider(
						(id, inv, p) -> createMenu(guiId, id, inv, pos),
						Component.empty()),
				buf -> buf.writeBlockPos(pos));
	}

	private static AbstractContainerMenu createMenu(int guiId, int windowId, Inventory inv, BlockPos pos) {
		return switch (guiId) {
			case lightningCellGui -> new ContainerLightningCell(windowId, inv, pos);
			case lightningFurnaceGui -> new ContainerLightningFurnace(windowId, inv, pos);
			case lightningCrusherGui -> new ContainerLightningCrusher(windowId, inv, pos);
			case lightningInfuserGui -> new ContainerLightningInfuser(windowId, inv, pos);
			case staticGeneratorGui -> new ContainerStaticGenerator(windowId, inv, pos);
			case enchReallocatorGui -> new ContainerEnchReallocator(windowId, inv, pos);
			case lightningRXGui -> new ContainerLightningReceiver(windowId, inv, pos);
			case lightningCannonGui -> new ContainerLightningCannon(windowId, inv, pos);
			case lightningMinerGui -> new ContainerLightningMiner(windowId, inv, pos);
			case lightningLEtoRFGui -> new ContainerRFProvider(windowId, inv, pos);
			case lightningRFtoLEGui -> new ContainerRFReceiver(windowId, inv, pos);
			default -> null;
		};
	}

}
