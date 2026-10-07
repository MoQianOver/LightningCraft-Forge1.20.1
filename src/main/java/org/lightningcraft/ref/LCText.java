package org.lightningcraft.ref;

import java.text.DecimalFormat;

public class LCText {

	public static final String secSign = "\u00a7";
	public static final DecimalFormat df = new DecimalFormat("0.0");
	public static final DecimalFormat af = new DecimalFormat("#.#");

	static {
		af.setDecimalSeparatorAlwaysShown(false);
	}

	public static String getInventoryLEUserLore() {
		return secSign + "e" + Lang.tr("lore." + RefStrings.MODID + ":inventoryleuser");
	}

	public static String getInvincibilityLore() {
		return secSign + "f" + Lang.tr("lore." + RefStrings.MODID + ":invincibility");
	}

	public static String getAngerGodsChatLore() {
		return secSign + "c" + Lang.tr("lore." + RefStrings.MODID + ":angergods") + secSign + "r";
	}

	public static String getAutoRepairLore() {
		return secSign + "a" + Lang.tr("lore." + RefStrings.MODID + ":autorepair") + " I";
	}

	public static String getAutoRepair2Lore() {
		return secSign + "a" + Lang.tr("lore." + RefStrings.MODID + ":autorepair") + " II";
	}

	public static String getSkyHelmLore() {
		return secSign + "f" + Lang.tr("lore." + RefStrings.MODID + ":skyhelm");
	}

	public static String getMysticHelmLore() {
		return secSign + "b" + Lang.tr("lore." + RefStrings.MODID + ":mystichelm");
	}

	public static String getSkyChestLore() {
		return secSign + "4" + Lang.tr("lore." + RefStrings.MODID + ":skychest");
	}

	public static String getMysticChestLore() {
		return secSign + "4" + Lang.tr("lore." + RefStrings.MODID + ":mysticchest");
	}

	public static String getSkyLegsLore() {
		return secSign + "6" + Lang.tr("lore." + RefStrings.MODID + ":skylegs");
	}

	public static String getMysticLegsLore() {
		return secSign + "2" + Lang.tr("lore." + RefStrings.MODID + ":mysticlegs");
	}

	public static String getSkyBootsLore() {
		return secSign + "e" + Lang.tr("lore." + RefStrings.MODID + ":skyboots");
	}

	public static String getMysticBootsLore() {
		return secSign + "e" + Lang.tr("lore." + RefStrings.MODID + ":mysticboots");
	}

	public static String getChargedLore() {
		return secSign + "b" + Lang.tr("lore." + RefStrings.MODID + ":charged");
	}

	public static String getSummonerLore() {
		return secSign + "e" + Lang.tr("lore." + RefStrings.MODID + ":summoner");
	}

	public static String getAutoSmeltLore() {
		return secSign + "c" + Lang.tr("lore." + RefStrings.MODID + ":autosmelt");
	}

	public static String getFortuneBonusLore() {
		return secSign + "9" + Lang.tr("lore." + RefStrings.MODID + ":fortunebonus");
	}

	public static String getQuickFarmLore() {
		return secSign + "2" + Lang.tr("lore." + RefStrings.MODID + ":quickfarm");
	}

	public static String getSoulSwordLore() {
		return secSign + "6" + Lang.tr("lore." + RefStrings.MODID + ":soulsword");
	}

	public static String getZombieSwordLore() {
		return secSign + "4" + Lang.tr("lore." + RefStrings.MODID + ":zombiesword");
	}

	public static String getFeatherSwordLore() {
		return secSign + "e" + Lang.tr("lore." + RefStrings.MODID + ":feathersword");
	}

	public static String getEnderSwordLore() {
		return secSign + "5" + Lang.tr("lore." + RefStrings.MODID + ":endersword");
	}

	public static String getBlazeSwordLore() {
		return secSign + "c" + Lang.tr("lore." + RefStrings.MODID + ":blazesword");
	}

	public static String getIceSwordLore() {
		return secSign + "9" + Lang.tr("lore." + RefStrings.MODID + ":icesword");
	}

	public static String getDemonBloodLore() {
		return secSign + "4" + secSign + "o" + Lang.tr("lore." + RefStrings.MODID + ":demonblood") + secSign + "r";
	}

	public static String getUnderworldChargeLore() {
		return secSign + "3" + secSign + "o" + Lang.tr("lore." + RefStrings.MODID + ":underworldcharge") + secSign + "r";
	}

	public static String getHeavenSwordLore() {
		return secSign + "d" + Lang.tr("lore." + RefStrings.MODID + ":heavensword");
	}

}
