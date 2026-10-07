package org.lightningcraft.gui.server;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import org.lightningcraft.api.capabilities.ILightningUpgradable;
import org.lightningcraft.gui.ShortSender;
import org.lightningcraft.init.LCCapabilities;
import org.lightningcraft.tiles.TileEntityLightningUser;

public abstract class ContainerLightningUser extends AbstractContainerMenu {

	protected static final int INFO_OFFSET = 6;

	protected final TileEntityLightningUser tile;

	private final ContainerData data;

	private Short lowStored = null;
	private Short highStored = null;
	private Short lowMax = null;
	private Short highMax = null;

	protected ContainerLightningUser(MenuType<?> type, int windowId, Inventory player,
			TileEntityLightningUser tile) {
		super(type, windowId);
		this.tile = tile;
		this.data = new ContainerData() {
			@Override
			public int get(int index) {
				return readData(index);
			}

			@Override
			public void set(int index, int value) {
				writeData(index, value);
			}

			@Override
			public int getCount() {
				return dataCount();
			}
		};
		this.addDataSlots(this.data);
	}

	private int readData(int index) {
		int cellPower = (int) (this.tile.cellPower * 10D);
		int maxPower = (int) this.tile.maxPower;
		return switch (index) {
			case 0 -> ShortSender.getLowShort(cellPower);
			case 1 -> ShortSender.getHighShort(cellPower);
			case 2 -> ShortSender.getLowShort(maxPower);
			case 3 -> ShortSender.getHighShort(maxPower);
			case 4 -> (int) (this.tile.getEfficiency() * 1000D);
			default -> readInfo(index);
		};
	}

	private void writeData(int index, int value) {
		switch (index) {
			case 0 -> lowStored = (short) value;
			case 1 -> highStored = (short) value;
			case 2 -> lowMax = (short) value;
			case 3 -> highMax = (short) value;
			case 4 -> this.tile.setEfficiency(value / 1000D);
			default -> writeInfo(index, value);
		}
		if (lowStored != null && highStored != null) {
			this.tile.cellPower = ShortSender.getInt(lowStored, highStored) / 10D;
		}
		if (lowMax != null && highMax != null) {
			this.tile.maxPower = ShortSender.getInt(lowMax, highMax);
		}
	}

	protected int readInfo(int index) {
		return 0;
	}

	protected void writeInfo(int index, int value) {

	}

	protected int dataCount() {
		return INFO_OFFSET;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.tile.isUseableByPlayer(player);
	}

	public static abstract class Upgradable extends ContainerLightningUser {

		protected Upgradable(MenuType<?> type, int windowId, Inventory player, TileEntityLightningUser tile) {
			super(type, windowId, player, tile);
		}

		@Override
		protected int readInfo(int index) {
			if (index == 5) {
				ILightningUpgradable cap = this.tile
						.getCapability(LCCapabilities.LIGHTNING_UPGRADABLE).orElse(null);
				return cap != null && cap.isUpgraded() ? 1 : 0;
			}
			return super.readInfo(index);
		}

		@Override
		protected void writeInfo(int index, int value) {
			if (index == 5) {
				ILightningUpgradable cap = this.tile
						.getCapability(LCCapabilities.LIGHTNING_UPGRADABLE).orElse(null);
				if (cap != null) {
					cap.setUpgraded(value > 0);
				}
				return;
			}
			super.writeInfo(index, value);
		}

		@Override
		protected int dataCount() {
			return Math.max(super.dataCount(), INFO_OFFSET);
		}

	}

}
