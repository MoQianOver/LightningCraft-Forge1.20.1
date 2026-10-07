package org.lightningcraft.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.entities.EntityLCElectricAttack;

@OnlyIn(Dist.CLIENT)
public class RenderLCElectricAttack extends EntityRenderer<EntityLCElectricAttack> {

	public RenderLCElectricAttack(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityLCElectricAttack e) {
		return null;
	}

}