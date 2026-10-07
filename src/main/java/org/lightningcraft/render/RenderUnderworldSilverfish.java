package org.lightningcraft.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SilverfishRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.entities.EntityUnderworldSilverfish;
import org.lightningcraft.ref.RefStrings;

@OnlyIn(Dist.CLIENT)
public class RenderUnderworldSilverfish extends SilverfishRenderer {

	private static final ResourceLocation TEXTURE =
			new ResourceLocation(RefStrings.MODID, "textures/entity/underworld_silverfish.png");

	public RenderUnderworldSilverfish(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected void scale(Silverfish entity, PoseStack poseStack, float partialTick) {
		poseStack.scale(2.0F, 2.0F, 2.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(Silverfish e) {
		return TEXTURE;
	}

}