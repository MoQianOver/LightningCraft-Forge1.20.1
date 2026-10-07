package org.lightningcraft.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;
import org.lightningcraft.blocks.base.BlockMeta;
import org.lightningcraft.items.blocks.ItemBlockMetal;
import org.lightningcraft.ref.Metal.Ingot;

public class BlockMetal extends BlockMeta {

	public static enum EnumType implements StringRepresentable {
		ELEC(Ingot.ELEC),
		SKY(Ingot.SKY),
		MYSTIC(Ingot.MYSTIC);

		private static final EnumType DEFAULT = ELEC;
		private final int ID;
		private final String name;

		EnumType(int meta) {
			this.ID = meta;
			this.name = Ingot.getNameFromMeta(meta).toLowerCase();
		}

		public static EnumType getTypeFromMeta(int meta) {
			for (EnumType m : EnumType.values()) {
				if (m.ID == meta) return m;
			}
			return DEFAULT;
		}

		@Override
		public String getSerializedName() {
			return name;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}

		public int getID() {
			return ID;
		}
	}

	public BlockMetal(float hardness, float resistance) {
		super(Blocks.IRON_BLOCK, Ingot.count, hardness, resistance, false);
	}

	public BlockMetal() {
		this(6.5f, 40.0f);
	}

	@Override
	public Class<? extends BlockItem> getItemClass() {
		return ItemBlockMetal.class;
	}

}
