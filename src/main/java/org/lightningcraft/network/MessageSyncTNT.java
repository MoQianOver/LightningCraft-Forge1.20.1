package org.lightningcraft.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.lightningcraft.entities.EntityLCTNTPrimed;
import org.lightningcraft.ref.RefMisc;

public class MessageSyncTNT {

	private int uid, variant;

	public MessageSyncTNT() { }

	public MessageSyncTNT(EntityLCTNTPrimed entity) {
		this.uid = entity.getId();
		this.variant = entity.variant;
	}

	public MessageSyncTNT(FriendlyByteBuf buf) {
		CompoundTag tag = buf.readNbt();
		if (tag != null) {
			this.uid = tag.getInt("uid");
			this.variant = tag.getInt("variant");
		}
	}

	public void encode(FriendlyByteBuf buf) {
		CompoundTag tag = new CompoundTag();
		tag.putInt("uid", this.uid);
		tag.putInt("variant", this.variant);
		buf.writeNbt(tag);
	}

	public static void handle(MessageSyncTNT m, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> handleClient(m));
		ctx.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.CLIENT)
	private static void handleClient(MessageSyncTNT m) {
		if(RefMisc.DEBUG) System.out.println("Syncing TNT type for rendering");
		if (Minecraft.getInstance().level == null) return;
		Entity e = Minecraft.getInstance().level.getEntity(m.uid);
		if (e instanceof EntityLCTNTPrimed t) {
			t.variant = m.variant;
		}
	}

}