package org.lightningcraft.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.lightningcraft.entities.EntityLCTNTPrimed;
import org.lightningcraft.init.LCBlocks;

@OnlyIn(Dist.CLIENT)
public class RenderLCTNTPrimed extends EntityRenderer<EntityLCTNTPrimed> {

	public RenderLCTNTPrimed(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5F;
	}

	@Override
	public void render(EntityLCTNTPrimed entity, float entityYaw, float partialTicks, PoseStack poseStack,
			MultiBufferSource buffer, int packedLight) {
		BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

		poseStack.pushPose();
		poseStack.translate(0.0D, 0.5D, 0.0D);

		if (entity.fuse - partialTicks + 1.0F < 10.0F) {
			float f = 1.0F - (entity.fuse - partialTicks + 1.0F) / 10.0F;
			f = Mth.clamp(f, 0.0F, 1.0F);
			f = f * f;
			f = f * f;
			float f1 = 1.0F + f * 0.3F;
			poseStack.scale(f1, f1, f1);
		}

		float alpha = (1.0F - (entity.fuse - partialTicks + 1.0F) / 100.0F) * 0.8F;
		poseStack.translate(-0.5D, -0.5D, 0.5D);

		BlockState state = LCBlocks.underTNT.getStateFromMeta(entity.variant);
		dispatcher.renderSingleBlock(state, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);

		poseStack.translate(0.0D, 0.0D, -1.0D);

		if (entity.fuse / 5 % 2 == 0) {
			dispatcher.renderSingleBlock(state, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY,
					ModelData.EMPTY, RenderType.entityTranslucent(TextureAtlas.LOCATION_BLOCKS));
		}

		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityLCTNTPrimed entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

}
