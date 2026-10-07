package org.lightningcraft.blocks;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;

import net.minecraft.world.item.BlockItem;

import java.util.List;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import org.lightningcraft.api.util.JointList;
import org.lightningcraft.blocks.base.BlockMeta;
import org.lightningcraft.items.blocks.ItemBlockAirTerminal;
import org.lightningcraft.ref.Metal.Rod;

public class BlockAirTerminal extends BlockMeta {

	public static enum EnumType implements StringRepresentable {
	    IRON(Rod.IRON),
	    STEEL(Rod.STEEL),
	    LEAD(Rod.LEAD),
	    TIN(Rod.TIN),
	    ALUM(Rod.ALUM),
	    GOLD(Rod.GOLD),
	    COPPER(Rod.COPPER),
	    ELEC(Rod.ELEC),
	    SKY(Rod.SKY),
	    MYSTIC(Rod.MYSTIC);

	    private static final EnumType DEFAULT = IRON;
	    private int ID;
	    private String name;

	    private EnumType(int meta) {
	        this.ID = meta;
	        this.name = Rod.getNameFromMeta(meta).toLowerCase();
	    }

	    public static EnumType getTypeFromMeta(int meta) {
			for(EnumType m : EnumType.values()) {
				if(m.ID == meta) return m;
			}
			return DEFAULT;
		}

	    @Override
		public String getSerializedName() {
			return getName();
		}

		public String getName() {
	        return name;
	    }

	    @Override
	    public String toString() {
	        return getName();
	    }

	    public int getID() {
	        return ID;
	    }
	}

	protected Property type;

	public BlockAirTerminal(float hardness, float resistance) {

		super(Blocks.IRON_BLOCK, Rod.count, hardness, resistance, true, true);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter source, BlockPos pos, CollisionContext context) {
	return Shapes.box(0.2F, 0.0F, 0.2F, 0.8F, 0.9375F, 0.8F);
	}

	public BlockAirTerminal() {
		this(3.0f, 30.0f);
	}

	@Override
	public Class<? extends BlockItem> getItemClass() {
		return ItemBlockAirTerminal.class;
	}

	public double getEfficiency(BlockState state) {
		switch(getMetaFromState(state)) {
		case Rod.IRON:
			return 0.30;
		case Rod.STEEL:
			return 0.35;
		case Rod.LEAD:
			return 0.40;
		case Rod.TIN:
			return 0.50;
		case Rod.ALUM:
			return 0.60;
		case Rod.GOLD:
			return 0.70;
		case Rod.COPPER:
			return 0.75;
		case Rod.ELEC:
			return 0.90;
		case Rod.SKY:
			return 1.00;
		case Rod.MYSTIC:
			return 1.10;
		default:
			return 0.25;
		}
	}

}
