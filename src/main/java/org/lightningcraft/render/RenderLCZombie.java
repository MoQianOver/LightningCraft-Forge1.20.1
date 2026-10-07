package org.lightningcraft.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lightningcraft.entities.EntityLCZombie;

@OnlyIn(Dist.CLIENT)
public class RenderLCZombie extends ZombieRenderer {

	public RenderLCZombie(EntityRendererProvider.Context context) {
		super(context);
	}

}