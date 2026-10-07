package org.lightningcraft.integration.chisel;

import org.lightningcraft.blocks.BlockLight;
import org.lightningcraft.blocks.BlockStone;
import org.lightningcraft.config.LCConfig;
import org.lightningcraft.init.LCBlocks;
import org.lightningcraft.ref.Log;
import team.chisel.api.carving.CarvingUtils;

public class ChiselIntegration {

	public static void mainRegistry() {
		try {

			CarvingUtils.getChiselRegistry().addGroup(new CarvingGroup("thunder", new CarvingVariationList(
					new CarvingVariation(LCBlocks.stoneBlock, BlockStone.THUNDER),
					new CarvingVariation(LCBlocks.stoneBlock, BlockStone.THUNDER_BRICK),
					new CarvingVariation(LCBlocks.stoneBlock, BlockStone.THUNDER_BRICK_CHISELED),
					new CarvingVariation(LCBlocks.stoneBlock, BlockStone.THUNDER_BRICK_FANCY),
					new CarvingVariation(LCBlocks.stoneBlock, BlockStone.THUNDER_BRICK_FANCY_2)
					)));
			CarvingUtils.getChiselRegistry().addGroup(new CarvingGroup("demon", new CarvingVariationList(
					new CarvingVariation(LCBlocks.stoneBlock, BlockStone.DEMON),
					new CarvingVariation(LCBlocks.stoneBlock, BlockStone.DEMON_BRICK),
					new CarvingVariation(LCBlocks.stoneBlock, BlockStone.DEMON_BRICK_CHISELED),
					new CarvingVariation(LCBlocks.stoneBlock, BlockStone.DEMON_BRICK_FANCY),
					new CarvingVariation(LCBlocks.stoneBlock, BlockStone.DEMON_BRICK_FANCY_2)
					)));
			CarvingUtils.getChiselRegistry().addGroup(new CarvingGroup("under", new CarvingVariationList(
					new CarvingVariation(LCBlocks.stoneBlock, BlockStone.UNDER),
					new CarvingVariation(LCBlocks.stoneBlock, BlockStone.UNDER_BRICK),
					new CarvingVariation(LCBlocks.stoneBlock, BlockStone.UNDER_BRICK_CHISELED),
					new CarvingVariation(LCBlocks.stoneBlock, BlockStone.UNDER_BRICK_FANCY),
					new CarvingVariation(LCBlocks.stoneBlock, BlockStone.UNDER_BRICK_FANCY_2)
					)));
			CarvingUtils.getChiselRegistry().addGroup(new CarvingGroup("lamp", new CarvingVariationList(
					new CarvingVariation(LCBlocks.lightBlock, BlockLight.UNDER_LAMP_FANCY),
					new CarvingVariation(LCBlocks.lightBlock, BlockLight.UNDER_LAMP_FANCY_2),
					new CarvingVariation(LCBlocks.lightBlock, BlockLight.UNDER_LAMP_FANCY_3)
					)));

			if(LCConfig.chiselCorruptStone) {
				CarvingUtils.getChiselRegistry().addVariation("stonebrick", new CarvingVariation(LCBlocks.corruptStone, 0));
			}

			Log.logger.info("Chisel integration complete.");
		} catch(Exception e) {
			Log.logger.error("Chisel integration failed!");
			e.printStackTrace();
		}
	}

}
