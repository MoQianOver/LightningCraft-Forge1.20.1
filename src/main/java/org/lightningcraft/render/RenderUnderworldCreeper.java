package org.lightningcraft.render;

import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.entities.EntityUnderworldCreeper;
import org.lightningcraft.ref.RefStrings;

@OnlyIn(Dist.CLIENT)
public class RenderUnderworldCreeper extends CreeperRenderer {

	private static final ResourceLocation TEXTURE =
			new ResourceLocation(RefStrings.MODID, "textures/entity/underworld_creeper.png");

	public RenderUnderworldCreeper(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Creeper e) {
		return TEXTURE;
	}

}