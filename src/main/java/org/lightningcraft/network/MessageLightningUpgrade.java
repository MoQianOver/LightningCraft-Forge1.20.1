package org.lightningcraft.network;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.client.Minecraft;
import org.lightningcraft.api.capabilities.ILightningUpgradable;
import org.lightningcraft.init.LCCapabilities;
import org.lightningcraft.ref.RefMisc;

public class MessageLightningUpgrade {

	private BlockPos pos;
	private boolean upgraded;

	public MessageLightningUpgrade() {}

	public MessageLightningUpgrade(BlockPos pos) {
		this(pos, true);
	}

	public MessageLightningUpgrade(BlockPos pos, boolean upgraded) {
		this.pos = pos;
		this.upgraded = upgraded;
	}

	public MessageLightningUpgrade(FriendlyByteBuf buf) {
		CompoundTag tag = buf.readNbt();
		if (tag != null) {
			pos = BlockPos.of(tag.getLong("pos"));
			upgraded = tag.getBoolean("upgraded");
		}
	}

	public void encode(FriendlyByteBuf buf) {
		CompoundTag tag = new CompoundTag();
		tag.putLong("pos", pos.asLong());
		tag.putBoolean("upgraded", upgraded);
		buf.writeNbt(tag);
	}

	public static void handle(MessageLightningUpgrade m, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> handleClient(m));
		ctx.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.CLIENT)
	private static void handleClient(MessageLightningUpgrade m) {
		Level level = Minecraft.getInstance().level;
		if (level == null) return;
		BlockEntity tile = level.getBlockEntity(m.pos);
		if (tile == null) return;
		ILightningUpgradable cap = tile.getCapability(LCCapabilities.LIGHTNING_UPGRADABLE).orElse(null);
		if (cap != null) {
			cap.setUpgraded(m.upgraded);
			if(RefMisc.DEBUG) System.out.println("Machine upgraded at " + m.pos);
		}
	}

}