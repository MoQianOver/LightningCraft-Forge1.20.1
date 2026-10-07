package org.lightningcraft.gui;

public class ShortSender {

	public static short getLowShort(int value) {
		return (short)(value >> 16);
	}

	public static short getHighShort(int value) {
		return (short)(value & 0xFFFF);
	}

	public static int getInt(short lowShort, short highShort) {
		return lowShort << 16 | (highShort & 0xFFFF);
	}

}
