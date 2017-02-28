package com.strangeone101.bendinggui.nms;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NMSManager_111R1 implements INMSManager 
{
	public ItemStack addGlow(ItemStack itemstack) 
	{
		if(itemstack == null) return null;
		itemstack.addUnsafeEnchantment(Enchantment.LUCK, 1);
        ItemMeta  meta = itemstack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemstack.setItemMeta(meta);
        return itemstack;
	}
}
