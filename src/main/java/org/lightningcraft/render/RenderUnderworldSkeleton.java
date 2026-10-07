package org.lightningcraft.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.entities.EntityUnderworldSkeleton;
import org.lightningcraft.ref.RefStrings;

@OnlyIn(Dist.CLIENT)
public class RenderUnderworldSkeleton extends SkeletonRenderer {

	private static final ResourceLocation TEXTURE =
			new ResourceLocation(RefStrings.MODID, "textures/entity/underworld_skeleton.png");

	public RenderUnderworldSkeleton(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractSkeleton e) {
		return TEXTURE;
	}

}