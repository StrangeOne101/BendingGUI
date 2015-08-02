package com.strangeone101.bendinggui.nms;

import net.minecraft.server.v1_8_R4.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NMSManager_RC4 implements INMSManager 
{
	public ItemStack addGlow(ItemStack itemstack) 
	{
		net.minecraft.server.v1_8_R4.ItemStack stack = this.toNMSStack(itemstack);
		NBTTagCompound tag = new NBTTagCompound();
		if (stack.getTag() != null)
		{
			tag = stack.getTag();
		}
		/*NBTTagCompound display = new NBTTagCompound();
        display.setString("Name", stack.getName());
        NBTTagList lore = new NBTTagList();
        for (String s : stack.)
        {
        	lore.add(new NBTTagString(s));
        }
        display.set("Lore", lore);
        tag.set("display", display);*/
		tag.set("ench", new NBTTagCompound());
		return this.toBukkitStack(stack);
	}
	
	private net.minecraft.server.v1_8_R4.ItemStack toNMSStack(ItemStack stack)
	{
		return CraftItemStack.asNMSCopy(stack);
	}
	
	private ItemStack toBukkitStack(net.minecraft.server.v1_8_R4.ItemStack stack)
	{
		return CraftItemStack.asCraftMirror(stack);
	}

}
