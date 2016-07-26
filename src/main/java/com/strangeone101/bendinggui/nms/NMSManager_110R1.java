package com.strangeone101.bendinggui.nms;

import net.minecraft.server.v1_10_R1.NBTTagCompound;

import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NMSManager_110R1 implements INMSManager 
{
	public ItemStack addGlow(ItemStack itemstack) 
	{
		net.minecraft.server.v1_10_R1.ItemStack stack = this.toNMSStack(itemstack);
		NBTTagCompound tag = new NBTTagCompound();
		if (stack.getTag() != null)
		{
			tag = stack.getTag();
		}
	
		tag.set("ench", new NBTTagCompound());
		return this.toBukkitStack(stack);
	}
	
	private net.minecraft.server.v1_10_R1.ItemStack toNMSStack(ItemStack stack)
	{
		return CraftItemStack.asNMSCopy(stack);
	}
	
	private ItemStack toBukkitStack(net.minecraft.server.v1_10_R1.ItemStack stack)
	{
		return CraftItemStack.asCraftMirror(stack);
	}

}
