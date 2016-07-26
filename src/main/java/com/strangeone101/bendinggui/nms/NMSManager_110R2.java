package com.strangeone101.bendinggui.nms;

import org.bukkit.inventory.ItemStack;

public class NMSManager_110R2 implements INMSManager 
{
	public ItemStack addGlow(ItemStack itemstack) 
	{
		/*net.minecraft.server.v1_10_R2.ItemStack stack = this.toNMSStack(itemstack);
		NBTTagCompound tag = new NBTTagCompound();
		if (stack.getTag() != null)
		{
			tag = stack.getTag();
		}
	
		tag.set("ench", new NBTTagCompound());
		return this.toBukkitStack(stack);*/
		return itemstack;
	}
	
	/*private net.minecraft.server.v1_10_R2.ItemStack toNMSStack(ItemStack stack)
	{
		return CraftItemStack.asNMSCopy(stack);
	}
	
	private ItemStack toBukkitStack(net.minecraft.server.v1_10_R2.ItemStack stack)
	{
		return CraftItemStack.asCraftMirror(stack);
	}*/

}
