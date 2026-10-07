package org.lightningcraft.render;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.entities.EntityDemonSoldier;
import org.lightningcraft.ref.RefStrings;

@OnlyIn(Dist.CLIENT)
public class RenderDemonSoldier
		extends HumanoidMobRenderer<EntityDemonSoldier, HumanoidModel<EntityDemonSoldier>> {

	private static final ResourceLocation TEXTURE =
			new ResourceLocation(RefStrings.MODID, "textures/entity/demon_soldier.png");

	public RenderDemonSoldier(EntityRendererProvider.Context context) {

		super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
		this.addLayer(new HumanoidArmorLayer<>(this,
				new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
				new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
				context.getModelManager()));
		this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(),
				context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(EntityDemonSoldier e) {
		return TEXTURE;
	}

}