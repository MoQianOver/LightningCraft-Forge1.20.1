package org.lightningcraft.util;

import org.lightningcraft.api.util.JointList;

public class IntList extends JointList<Integer> {

	public IntList(){
		super();
	}

	public int average() {
		int a = 0;
		for(int i : this) {
			a += i;
		}
		return a / this.size();
	}

	public int min() {
		int a = Integer.MAX_VALUE;
		for(int i : this) {
			a = Math.min(a, i);
		}
		return a;
	}

	public int max() {
		int a = Integer.MIN_VALUE;
		for(int i : this) {
			a = Math.max(a, i);
		}
		return a;
	}

	public int variance() {
		return this.max() - this.min();
	}

	public int averageLowBias() {
		return (this.average() + this.min()) / 2;
	}

	public int averageHighBias() {
		return (this.average() + this.max()) / 2;
	}

}
