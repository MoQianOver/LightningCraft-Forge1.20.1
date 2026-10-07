package org.lightningcraft.integration.chisel;

import java.util.List;

import org.lightningcraft.api.util.JointList;
import team.chisel.api.carving.ICarvingVariation;

public class CarvingVariationList extends JointList<CarvingVariation> {

	public CarvingVariationList(CarvingVariation... vars) {
		for(CarvingVariation v : vars) {
			this.join(v);
		}
	}

	public List<ICarvingVariation> getGeneralizedList() {
		List<ICarvingVariation> list = new JointList();
			for(CarvingVariation v : this) {
				list.add(v);
			}
		return list;
	}

}
