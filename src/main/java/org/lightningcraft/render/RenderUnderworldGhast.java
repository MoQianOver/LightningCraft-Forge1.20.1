package org.lightningcraft.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GhastRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.entities.EntityUnderworldGhast;
import org.lightningcraft.ref.RefStrings;

@OnlyIn(Dist.CLIENT)
public class RenderUnderworldGhast extends GhastRenderer {

	private static final ResourceLocation TEXTURE =
			new ResourceLocation(RefStrings.MODID, "textures/entity/underworld_ghast.png");
	private static final ResourceLocation TEXTURE_SHOOTING =
			new ResourceLocation(RefStrings.MODID, "textures/entity/underworld_ghast_shooting.png");

	public RenderUnderworldGhast(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected void scale(Ghast entity, PoseStack poseStack, float partialTick) {
		poseStack.scale(3.0F, 3.0F, 3.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(Ghast e) {
		return e.isCharging() ? TEXTURE_SHOOTING : TEXTURE;
	}

}