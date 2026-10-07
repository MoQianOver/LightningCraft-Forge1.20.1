package org.lightningcraft.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.lightningcraft.ref.RefStrings;

public class LCNetwork {

	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(RefStrings.MODID, "main"),
			() -> "1", "1"::equals, "1"::equals);

	public static int id;

	public static void mainRegistry() {
		id = 0;
		registerNetwork();
	}

	private static void registerNetwork() {
		CHANNEL.messageBuilder(org.lightningcraft.network.MessageSpawnParticle.class, id++,
						net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT)
				.encoder(org.lightningcraft.network.MessageSpawnParticle::encode)
				.decoder(org.lightningcraft.network.MessageSpawnParticle::new)
				.consumerMainThread(org.lightningcraft.network.MessageSpawnParticle::handle)
				.add();
		CHANNEL.messageBuilder(org.lightningcraft.network.MessageLightningUpgrade.class, id++,
						net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT)
				.encoder(org.lightningcraft.network.MessageLightningUpgrade::encode)
				.decoder(org.lightningcraft.network.MessageLightningUpgrade::new)
				.consumerMainThread(org.lightningcraft.network.MessageLightningUpgrade::handle)
				.add();
		CHANNEL.messageBuilder(org.lightningcraft.network.MessageSyncTNT.class, id++,
						net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT)
				.encoder(org.lightningcraft.network.MessageSyncTNT::encode)
				.decoder(org.lightningcraft.network.MessageSyncTNT::new)
				.consumerMainThread(org.lightningcraft.network.MessageSyncTNT::handle)
				.add();
	}

}