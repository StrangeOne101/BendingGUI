package com.strangeone101.bendinggui;

import java.lang.reflect.Method;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.projectkorra.projectkorra.util.ReflectionHandler;
import com.projectkorra.projectkorra.util.ReflectionHandler.PackageType;



public class Util 
{
	/*public static ItemStack addGlow(ItemStack stack)
	{
		try {
			Object tag = ReflectionHandler.getMethod(PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftItemStack"), "getTag").invoke(getNMSItemStack(stack));
			if (tag != null)
			{
				Constructor<?> newTag = ReflectionHandler.getConstructor("NBTTagCompound", PackageType.MINECRAFT_SERVER);
				Method setTag = ReflectionHandler.getMethod("NBTTagCompound", PackageType.MINECRAFT_SERVER, "setTag", String.class, newTag.getClass());
				setTag.invoke(tag, "ench", newTag.newInstance());
			}
		} catch (ReflectiveOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stack;
		
	}*/
	
	public static Object getNMSItemStack(ItemStack item) {
		try {
			Method asNMSCopy = ReflectionHandler.getMethod(PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftItemStack"), "asNMSCopy", ItemStack.class);
			return asNMSCopy.invoke(PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftItemStack"), item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getStaff(UUID id) {
		if (id.toString().equals("8621211e-283b-46f5-87bc-95a66d68880e")) return ChatColor.DARK_RED + "ProjectKorra Founder"; // MistPhizzle

		else if (id.toString().equals("a197291a-cd78-43bb-aa38-52b7c82bc68c")) return ChatColor.DARK_PURPLE + "ProjectKorra Head Developer"; // OmniCypher
		else if (id.toString().equals("15d1a5a7-76ef-49c3-b193-039b27c47e30")) return ChatColor.DARK_RED + "ProjectKorra Administrator"; // Kiam
		
		else if (id.toString().equals("96f40c81-dd5d-46b6-9afe-365114d4a082")) return ChatColor.DARK_PURPLE + "ProjectKorra Developer"; // Coolade
		else if (id.toString().equals("833a7132-a9ec-4f0a-ad9c-c3d6b8a1c7eb")) return ChatColor.DARK_PURPLE + "ProjectKorra Developer"; // Jacklin213
		//else if (id.toString().equals("4eb6315e-9dd1-49f7-b582-c1170e497ab0")) return ChatColor.DARK_PURPLE + "ProjectKorra Developer"; // jedk1
		else if (id.toString().equals("d7757be8-86de-4898-ab4f-2b1b2fbc3dfa")) return ChatColor.DARK_PURPLE + "ProjectKorra Developer"; // StrangeOne101
		else if (id.toString().equals("3b5bdfab-8ae1-4794-b160-4f33f31fde99")) return ChatColor.DARK_PURPLE + "ProjectKorra Developer"; // kingbirdy
		//else if (id.toString().equals("dedf335b-d282-47ab-8ffc-a80121661cd1")) return ChatColor.DARK_PURPLE + "ProjectKorra Developer"; // grasshopperMatt
		//else if (id.toString().equals("679a6396-6a31-4898-8130-044f34bef743")) return ChatColor.DARK_PURPLE + "ProjectKorra Developer"; // savior67
		//else if (id.toString().equals("7bb267eb-cf0b-4fb9-a697-27c2a913ed92")) return ChatColor.DARK_PURPLE + "ProjectKorra Developer"; // Finn
		else if (id.toString().equals("1c30007f-f8ef-4b4e-aff0-787aa1bc09a3")) return ChatColor.DARK_PURPLE + "ProjectKorra Developer"; // Sorin
			
		else if (id.toString().equals("623df34e-9cd4-438d-b07c-1905e1fc46b6")) return ChatColor.DARK_PURPLE + "ProjectKorra Head Concept Designer"; // Loony
		else if (id.toString().equals("3c484e61-7876-46c0-98c9-88c7834dc96c")) return ChatColor.DARK_PURPLE + "ProjectKorra Concept Designer"; // SamuraiSnowman (Zmeduna)
		//else if (id.toString().equals("1d4a8a47-1f3b-40a6-b412-c15d874491b8")) return ChatColor.DARK_PURPLE + "ProjectKorra Concept Designer"; // Fyf 
		
		else if (id.toString().equals("1553482a-5e86-4270-9262-b57c11151074")) return ChatColor.GOLD + "ProjectKorra Head Moderator"; // Pickle9775
		else if (id.toString().equals("3d5bc713-ab8b-4125-b5ba-a1c1c2400b2c")) return ChatColor.GOLD + "ProjectKorra Moderator"; // Gold
		else if (id.toString().equals("38217173-8a32-4ba7-9fe1-dd4fed031a74")) return ChatColor.GOLD + "ProjectKorra Moderator"; // Easte
	
		//Retired staff
		else if (id.toString().equals("5031c4e3-8103-49ea-b531-0d6ae71bad69")) return ChatColor.DARK_PURPLE + "ProjectKorra Developer";  //Simp
		else if (id.toString().equals("80f9072f-e37e-4adc-8675-1ba6af87d63b")) return ChatColor.DARK_PURPLE + "ProjectKorra Concept Designer";  // Cross
		/*else if (id.toString().equals("57205eec-96bd-4aa3-b73f-c6627429beb2")) return ChatColor.DARK_PURPLE + "Previous PK Concept Designer";  // ashe36
		else if (id.toString().equals("7daead36-d285-4640-848a-2f105334b792")) return ChatColor.DARK_PURPLE + "Previous PK Concept Designer";  // Fuzzy
		else if (id.toString().equals("f30c871e-cd60-446b-b219-e31e00e16857")) return ChatColor.DARK_PURPLE + "Previous PK Concept Designer";  // Gangksta
		else if (id.toString().equals("38217173-8a32-4ba7-9fe1-dd4fed031a74")) return ChatColor.DARK_PURPLE + "Previous PK Concept Designer";  //Fly
		else if (id.toString().equals("2ab334d1-9691-4994-a624-209c7b4f220b")) return ChatColor.DARK_PURPLE + "Previous PK Digital Team";  // Austygen
		else if (id.toString().equals("929b14fc-aaf1-4f0f-84c2-f20c55493f53")) return ChatColor.DARK_PURPLE + "Previous PK Head Concept Designer"; //Vidcom
		*/else if (id.toString().equals("81adae76-d647-4b41-bfb0-8166516fa189")) return ChatColor.DARK_PURPLE + "ProjectKorra Developer";  //AlexTheCoder
		/*else if (id.toString().equals("0fd77ff6-07fb-4a7d-ba87-ae6f802ed1f9")) return ChatColor.DARK_PURPLE + "Previous PK Concept Designer";  //Hit_Manx
		else if (id.toString().equals("b2d82a84-ce22-4518-a8fc-1b28aeda0c0b")) return ChatColor.DARK_PURPLE + "Previous PK Concept Designer";  //Shunky
		*/else if (id.toString().equals("9c18ff57-04b3-4841-9726-9d64373d0d65")) return ChatColor.BLUE + "ProjectKorra Digital Team";  //coastyo
		/*else if (id.toString().equals("c364ffe2-de9e-4117-9735-6d14bde038f6")) return ChatColor.DARK_PURPLE + "Previous PK Developer";  //Carbogen
		else if (id.toString().equals("5aad055c-6b3b-489e-a2f0-9a711135ea66")) return ChatColor.DARK_PURPLE + "Previous PK Concept Designer";  //Zolteex
		else if (id.toString().equals("95b92e81-d8ae-4b99-9482-8e5b3dbde5cc")) return ChatColor.DARK_PURPLE + "Previous PK Concept Designer";  //Majorite
		*/else if (id.toString().equals("9636d66a-bff8-48e4-993e-68f0e7891c3b")) return ChatColor.DARK_PURPLE + "ProjectKorra Developer";  //Runefist
		/*else if (id.toString().equals("ff01d5c1-9be2-40c2-b708-0c0971039a45")) return ChatColor.DARK_PURPLE + "Previous PK Digital Team";  //UnlitedOwns*/

		return "";
	}
	
	@SuppressWarnings("deprecation")
	public static String getStaff(String playerName) {
		return getStaff(Bukkit.getOfflinePlayer(playerName).getUniqueId());
	}
}
