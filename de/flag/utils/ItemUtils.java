package de.flag.utils;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
public class ItemUtils {
	public static ItemStack getItemWithName(Material mat, String name) {
		ItemStack stack = new ItemStack(mat);
		
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		
		return stack;
	}
	public static ItemStack getItemWithName(Material mat, String name, Enchantment ench, int level) {
		ItemStack stack = new ItemStack(mat);
		
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		meta.addEnchant(ench, level, true);
		stack.setItemMeta(meta);
		
		return stack;
	}
	public static ItemStack getItemWithEnchantment(Material mat,Enchantment ench, int level) {
		ItemStack stack = new ItemStack(mat);
		
		ItemMeta meta = stack.getItemMeta();
		meta.addEnchant(ench, level, true);
		stack.setItemMeta(meta);
		
		return stack;
	}
	public static ItemStack getPotion(PotionType e, int level, String name) {
		Potion pot = new Potion(e, level);
		ItemStack item =  pot.toItemStack(1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack getColoredItemStack(ItemStack stack,Color color) {
		try {
	          LeatherArmorMeta meta = (LeatherArmorMeta)stack.getItemMeta();
	          meta.setColor(color);
	          stack.setItemMeta(meta);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return stack;
 	}
	public static ItemStack getRandomNBT(ItemStack stack, String name) {
		ItemMeta meta = stack.getItemMeta();
		String rdm = "";
		for(int i = 0; i < 10; i++)
			rdm+="§" + new Random().nextInt(9);
		meta.setDisplayName(rdm + name);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static void clearArmorSlots(Player p) {
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
	}
	
	public static ItemStack getItemWithID(int mat, int id) {
		@SuppressWarnings("deprecation")
		ItemStack stack = new ItemStack(mat);
		stack.setDurability((short) id);
		
		return stack;
	}
	
	public static ItemStack getBook(final String title, final String author, final String displayName, final String... pages) {
        final ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        final BookMeta meta = (BookMeta)book.getItemMeta();
        meta.setTitle(title);
        meta.setDisplayName(author);
        meta.setAuthor(displayName);
        meta.setPages(pages);
        book.setItemMeta((ItemMeta)meta);
        return book;
    }

}
