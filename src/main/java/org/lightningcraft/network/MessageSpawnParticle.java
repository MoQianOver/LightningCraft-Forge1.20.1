package org.lightningcraft.network;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.lightningcraft.init.LCParticles;
import org.lightningcraft.ref.RefMisc;

public class MessageSpawnParticle {

	private String name;
	private double x, y, z;
	private double vx, vy, vz;

	public MessageSpawnParticle() { }

	public MessageSpawnParticle(String name, double x, double y, double z, double vx, double vy, double vz) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
	}

	public MessageSpawnParticle(FriendlyByteBuf buf) {
		CompoundTag tag = buf.readNbt();
		if (tag != null) {
			this.name = tag.getString("name");
			this.x = tag.getDouble("x");
			this.y = tag.getDouble("y");
			this.z = tag.getDouble("z");
			this.vx = tag.getDouble("vx");
			this.vy = tag.getDouble("vy");
			this.vz = tag.getDouble("vz");
		}
	}

	public void encode(FriendlyByteBuf buf) {
		CompoundTag tag = new CompoundTag();
		tag.putString("name", this.name);
		tag.putDouble("x", this.x);
		tag.putDouble("y", this.y);
		tag.putDouble("z", this.z);
		tag.putDouble("vx", this.vx);
		tag.putDouble("vy", this.vy);
		tag.putDouble("vz", this.vz);
		buf.writeNbt(tag);
	}

	public static void handle(MessageSpawnParticle m, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if(RefMisc.DEBUG) System.out.println("Spawning particle");
			LCParticles.spawnParticle(m.name, m.x, m.y, m.z, m.vx, m.vy, m.vz);
		});
		ctx.get().setPacketHandled(true);
	}

}