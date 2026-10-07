package org.lightningcraft.util;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;

public class WeatherUtils {

	public static List<LightningBolt> getLightningBoltsWithinAABB(Level level, AABB box) {

		return level.getEntitiesOfClass(LightningBolt.class, box);
	}

}
