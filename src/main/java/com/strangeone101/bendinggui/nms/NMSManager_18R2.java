package com.strangeone101.bendinggui.nms;

import net.minecraft.server.v1_8_R2.NBTTagCompound;

import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NMSManager_18R2 implements INMSManager 
{
	public ItemStack addGlow(ItemStack itemstack) 
	{
		net.minecraft.server.v1_8_R2.ItemStack stack = this.toNMSStack(itemstack);
		NBTTagCompound tag = new NBTTagCompound();
		if (stack.getTag() != null)
		{
			tag = stack.getTag();
		}

		tag.set("ench", new NBTTagCompound());
		return this.toBukkitStack(stack);
	}
	
	private net.minecraft.server.v1_8_R2.ItemStack toNMSStack(ItemStack stack)
	{
		return CraftItemStack.asNMSCopy(stack);
	}
	
	private ItemStack toBukkitStack(net.minecraft.server.v1_8_R2.ItemStack stack)
	{
		return CraftItemStack.asCraftMirror(stack);
	}

}
