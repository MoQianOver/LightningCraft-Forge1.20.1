package org.lightningcraft.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lightningcraft.gui.client.GuiEnchReallocator;
import org.lightningcraft.gui.client.GuiLightningCannon;
import org.lightningcraft.gui.client.GuiLightningCell;
import org.lightningcraft.gui.client.GuiLightningCrusher;
import org.lightningcraft.gui.client.GuiLightningFurnace;
import org.lightningcraft.gui.client.GuiLightningInfuser;
import org.lightningcraft.gui.client.GuiLightningMiner;
import org.lightningcraft.gui.client.GuiLightningReceiver;
import org.lightningcraft.gui.client.GuiRFProvider;
import org.lightningcraft.gui.client.GuiRFReceiver;
import org.lightningcraft.gui.client.GuiStaticGenerator;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.init.LCEntities;
import org.lightningcraft.init.LCFluids;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.init.LCMenus;
import org.lightningcraft.registry.ClientRegistryHelper;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		super.preInit();
		MinecraftForge.EVENT_BUS.register(new ClientRegistryHelper());

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterRenderers);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
	}

	private void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(this::registerScreens);
		event.enqueueWork(this::registerItemProperties);
	}

	private void registerItemProperties() {
		for (net.minecraftforge.registries.RegistryObject<net.minecraft.world.item.Item> entry
				: org.lightningcraft.init.LCRegistry.ITEMS.getEntries()) {
			net.minecraft.world.item.Item item = entry.get();
			if (item instanceof org.lightningcraft.items.base.ItemMeta
					|| item instanceof org.lightningcraft.items.blocks.ItemBlockMeta) {
				net.minecraft.client.renderer.item.ItemProperties.register(item, META_PROPERTY,
						(stack, level, entity, seed) -> stack.getDamageValue());
			}
		}
	}

	private static final net.minecraft.resources.ResourceLocation META_PROPERTY =
			new net.minecraft.resources.ResourceLocation(
					org.lightningcraft.ref.RefStrings.MODID, "meta");

	private void onRegisterRenderers(net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(LCEntities.HELPER_ZOMBIE.get(), org.lightningcraft.render.RenderLCZombie::new);
		event.registerEntityRenderer(LCEntities.DEMON_SOLDIER.get(), org.lightningcraft.render.RenderDemonSoldier::new);
		event.registerEntityRenderer(LCEntities.UNDERWORLD_SLIME.get(), org.lightningcraft.render.RenderUnderworldSlime::new);
		event.registerEntityRenderer(LCEntities.UNDERWORLD_SKELETON.get(), org.lightningcraft.render.RenderUnderworldSkeleton::new);
		event.registerEntityRenderer(LCEntities.UNDERWORLD_SILVERFISH.get(), org.lightningcraft.render.RenderUnderworldSilverfish::new);
		event.registerEntityRenderer(LCEntities.UNDERWORLD_GHAST.get(), org.lightningcraft.render.RenderUnderworldGhast::new);
		event.registerEntityRenderer(LCEntities.UNDERWORLD_CREEPER.get(), org.lightningcraft.render.RenderUnderworldCreeper::new);
		event.registerEntityRenderer(LCEntities.LC_TNT_PRIMED.get(), org.lightningcraft.render.RenderLCTNTPrimed::new);
		event.registerEntityRenderer(LCEntities.LC_ELECTRIC_ATTACK.get(), org.lightningcraft.render.RenderLCElectricAttack::new);

		event.registerEntityRenderer(LCEntities.LC_LIGHTNING_BOLT.get(),
				net.minecraft.client.renderer.entity.LightningBoltRenderer::new);
		event.registerEntityRenderer(LCEntities.LC_ITEM.get(),
				net.minecraft.client.renderer.entity.ItemEntityRenderer::new);
	}

	private void registerScreens() {
		net.minecraft.client.gui.screens.MenuScreens.register(LCMenus.LIGHTNING_CELL.get(), GuiLightningCell::new);
		net.minecraft.client.gui.screens.MenuScreens.register(LCMenus.LIGHTNING_FURNACE.get(), GuiLightningFurnace::new);
		net.minecraft.client.gui.screens.MenuScreens.register(LCMenus.LIGHTNING_CRUSHER.get(), GuiLightningCrusher::new);
		net.minecraft.client.gui.screens.MenuScreens.register(LCMenus.LIGHTNING_INFUSER.get(), GuiLightningInfuser::new);
		net.minecraft.client.gui.screens.MenuScreens.register(LCMenus.STATIC_GENERATOR.get(), GuiStaticGenerator::new);
		net.minecraft.client.gui.screens.MenuScreens.register(LCMenus.ENCH_REALLOCATOR.get(), GuiEnchReallocator::new);
		net.minecraft.client.gui.screens.MenuScreens.register(LCMenus.LIGHTNING_RECEIVER.get(), GuiLightningReceiver::new);
		net.minecraft.client.gui.screens.MenuScreens.register(LCMenus.LIGHTNING_CANNON.get(), GuiLightningCannon::new);
		net.minecraft.client.gui.screens.MenuScreens.register(LCMenus.LIGHTNING_MINER.get(), GuiLightningMiner::new);
		net.minecraft.client.gui.screens.MenuScreens.register(LCMenus.RF_PROVIDER.get(), GuiRFProvider::new);
		net.minecraft.client.gui.screens.MenuScreens.register(LCMenus.RF_RECEIVER.get(), GuiRFReceiver::new);
	}

	@Override
	public void onInit() {
		super.onInit();
		LCBlocks.registerBlockColors();
	}

	@Override
	public void postInit() {
		super.postInit();
	}

	@Override
	public void registerItemModels() {
		super.registerItemModels();
		LCBlocks.registerRendering();
		LCItems.registerRendering();
		LCFluids.registerRendering();
	}

}
