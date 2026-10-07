package org.lightningcraft.util;

import net.minecraft.core.registries.BuiltInRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.lightningcraft.init.LCItems;
import org.lightningcraft.ref.Metal.Ingot;

public class LCMisc {

	public static SidedInvWrapper[] makeInvWrapper(WorldlyContainer inv) {
		SidedInvWrapper[] wrapper = new SidedInvWrapper[6];
		for(int i = 0; i < 6; i++) {
			wrapper[i] = new SidedInvWrapper(inv, Direction.from3DDataValue(i));
		}
		return wrapper;
	}

	public static Object[] makeArray(Object obj, int amt) {
		Object[] array = new Object[amt];
		Arrays.fill(array, obj);
		return array;
	}

	public static Block[] getBlocksFromRegs(String[] regList) {
		Block[] blocks = new Block[regList.length];
		for(int i = 0; i < regList.length; i++) {
			blocks[i] = BuiltInRegistries.BLOCK.get(new ResourceLocation(regList[i]));
		}
		return blocks;
	}

	public static Rarity getRarityFromStack(@Nonnull ItemStack stack) {
		if(stack.isEmpty()) return Rarity.COMMON;
		if(ItemStack.isSameItemSameTags(stack, LCStack.of(LCItems.ingot, 1, Ingot.ELEC))) {
			return Rarity.UNCOMMON;
		} else if(ItemStack.isSameItemSameTags(stack, LCStack.of(LCItems.ingot, 1, Ingot.SKY))) {
			return Rarity.RARE;
		} else if(ItemStack.isSameItemSameTags(stack, LCStack.of(LCItems.ingot, 1, Ingot.MYSTIC))) {
			return Rarity.EPIC;
		} else {
			return Rarity.COMMON;
		}
	}

	public static <T> List listListContains(List<List<T>> list, T check) {
		for(List l : list) {
			if(l.contains(check)) return l;
		}
		return null;
	}

	public static int posInInventory(Player player, Item item, List<Integer> skip) {
		List<ItemStack> mainInventory = player.getInventory().items;
		for (Integer i = 0; i < mainInventory.size(); i++) {
            if (!skip.contains(i) && !mainInventory.get(i).isEmpty() && mainInventory.get(i).getItem() == item) {
                return i;
            }
        }
		return -1;
	}

	public static int posInInventory(Player player, Item item) {
		IntList nothing = new IntList();
		return posInInventory(player, item, nothing);
	}

	public static boolean mouseColl(int mouseX, int mouseY, AABB box) {
		if (mouseX >= box.minX && mouseX <= box.maxX && mouseY >= box.minY && mouseY <= box.maxY) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean inArray(Object obj, Object[] array) {
		for(Object i : array) {
			if(obj == i) return true;
		}
		return false;
	}

	public static int log2(int chunkHeight) {
		return chunkHeight == 256 ? 8 : 7;
	}

	public static int rotate(int n, int rot, int max) {
		int result = n + rot;
		while(result > max) result -= max;
		return result;
	}

	public static int rotate(int n, int rot, int min, int max) {
		int result = n + rot;
		while(result > max) result -= max;
		while(result < min) result += min;
		return result;
	}

    public static int getMetadataWithOffsetStairs(Direction facing, int meta) {
        if (facing == Direction.SOUTH) {
            if (meta == 2) {
                return 3;
            }
            if (meta == 3) {
                return 2;
            }
        } else if (facing == Direction.WEST) {
            if (meta == 0) {
                return 2;
            }
            if (meta == 1) {
                return 3;
            }
            if (meta == 2) {
                return 0;
            }
            if (meta == 3) {
                return 1;
            }
        } else if (facing == Direction.EAST) {
            if (meta == 0) {
                return 2;
            }
            if (meta == 1) {
                return 3;
            }
            if (meta == 2) {
                return 1;
            }
            if (meta == 3) {
                return 0;
            }
        }
        return meta;
    }

    public static int getMetadataWithOffsetChest(Direction facing, int meta) {
        if (facing == Direction.NORTH) {
            if (meta == 2) {
                return 3;
            }
            if (meta == 3) {
                return 2;
            }
        } else if(facing == Direction.WEST) {
            if (meta == 4) {
                return 2;
            }
            if (meta == 5) {
                return 3;
            }
            if (meta == 2) {
                return 5;
            }
            if (meta == 3) {
                return 4;
            }
        } else if(facing == Direction.EAST) {
            if (meta == 4) {
                return 2;
            }
            if (meta == 5) {
                return 3;
            }
            if (meta == 2) {
                return 4;
            }
            if (meta == 3) {
                return 5;
            }
        }
        return meta;
    }

    public static List<CompoundTag> getEnchantments(@Nonnull ItemStack s) {
    	List enchList = new ArrayList<CompoundTag>();

    	if(!s.isEmpty() && s.hasTag()) {
    		ListTag enchants;
    		String ench = s.getItem() == Items.ENCHANTED_BOOK ? "StoredEnchantments" : "ench";
    		enchants = (ListTag) s.getTag().get(ench);

    		if(enchants != null && enchants.size() > 0) {
    			for(int i = 0; i < enchants.size(); i++) {
    				CompoundTag enchant = enchants.getCompound(i);
    				enchList.add(enchant);
    			}
    		}
    	}

    	return enchList;
    }

    public static boolean addEnchantments(@Nonnull ItemStack s, List<CompoundTag> enchList) {
    	if(!s.isEmpty()) {
	    	String ench = s.getItem() == Items.ENCHANTED_BOOK ? "StoredEnchantments" : "ench";
	    	if(!s.hasTag()) s.setTag(new CompoundTag());
	    	ListTag currentEnchants = (ListTag) s.getTag().get(ench);

	    	if(currentEnchants == null) {
	    		currentEnchants = new ListTag();
	    	}

	    	for(int i = 0; i < enchList.size(); i++) {
	    		currentEnchants.add(enchList.get(i));
	    	}

	    	s.getTag().put(ench, currentEnchants);
	    	return true;
    	}
    	return false;
    }
}
