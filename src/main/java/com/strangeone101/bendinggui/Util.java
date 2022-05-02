package com.strangeone101.bendinggui;

import java.util.UUID;

import org.bukkit.Bukkit;

public class Util {
	
	public static String getStaff(UUID id) {
		String key = switch (id.toString()) {
			case "8621211e-283b-46f5-87bc-95a66d68880e" -> "Staff.Mist"; // MistPhizzle
			case "a197291a-cd78-43bb-aa38-52b7c82bc68c" -> "Staff.Admin"; // OmniCypher

			//else if (id.toString().equals("15d1a5a7-76ef-49c3-b193-039b27c47e30")) return ChatColor.DARK_RED + "ProjectKorra Administrator"; // Kiam
			case "96f40c81-dd5d-46b6-9afe-365114d4a082",    // Coolade
					"833a7132-a9ec-4f0a-ad9c-c3d6b8a1c7eb", // Jacklin213
					"4eb6315e-9dd1-49f7-b582-c1170e497ab0", // jedk1
					"d7757be8-86de-4898-ab4f-2b1b2fbc3dfa", // StrangeOne101
					"3b5bdfab-8ae1-4794-b160-4f33f31fde99", // kingbirdy
					"1c30007f-f8ef-4b4e-aff0-787aa1bc09a3", // Sorin
					"623df34e-9cd4-438d-b07c-1905e1fc46b6", // Loony
					"1553482a-5e86-4270-9262-b57c11151074", // Pickle9775
					"3d5bc713-ab8b-4125-b5ba-a1c1c2400b2c", // Gold
					"38217173-8a32-4ba7-9fe1-dd4fed031a74", // Easte
					"4f7cf9cd-ee04-4480-8ca0-7bca9b1db302", // plasmarob
					"dd578a4f-d35e-4fed-94db-9d5a627ff962", // Sobki
					"80f9072f-e37e-4adc-8675-1ba6af87d63b", // Cross
					"57205eec-96bd-4aa3-b73f-c6627429beb2", // ashe36
					"929b14fc-aaf1-4f0f-84c2-f20c55493f53", // Vidcom
					"9c18ff57-04b3-4841-9726-9d64373d0d65", // coastyo
					"c364ffe2-de9e-4117-9735-6d14bde038f6", // Carbogen
					"9636d66a-bff8-48e4-993e-68f0e7891c3b"  // Runefist
					-> "Staff.Contributor";
			case "592fb564-701a-4a5e-9d65-13f7ed0acf59",    // Vahagn
					"e98a2f7d-d571-4900-a625-483cbe6774fe", // Aztl
					"7bb267eb-cf0b-4fb9-a697-27c2a913ed92", // Finn
					"81adae76-d647-4b41-bfb0-8166516fa189", // AlexTheCoder
					"5031c4e3-8103-49ea-b531-0d6ae71bad69"  // Simplicitee
					-> "Staff.Developer ";
			default -> null;
			//else if (id.toString().equals("3c484e61-7876-46c0-98c9-88c7834dc96c")) return ChatColor.DARK_PURPLE + "ProjectKorra Concept Designer"; // SamuraiSnowman (Zmeduna)
			//else if (id.toString().equals("1d4a8a47-1f3b-40a6-b412-c15d874491b8")) return ChatColor.DARK_PURPLE + "ProjectKorra Concept Designer"; // Fyf



			//Retired staff//

			//else if (id.toString().equals("2ab334d1-9691-4994-a624-209c7b4f220b")) return ChatColor.BLUE + "ProjectKorra Contributor";  // Austygen

			/*else if (id.toString().equals("0fd77ff6-07fb-4a7d-ba87-ae6f802ed1f9")) return ChatColor.DARK_PURPLE + "Previous PK Concept Designer";  //Hit_Manx
			else if (id.toString().equals("b2d82a84-ce22-4518-a8fc-1b28aeda0c0b")) return ChatColor.DARK_PURPLE + "Previous PK Concept Designer";  //Shunky
			*/

			//else if (id.toString().equals("5aad055c-6b3b-489e-a2f0-9a711135ea66")) return ChatColor.DARK_PURPLE + "Previous PK Concept Designer";  //Zolteex
			//else if (id.toString().equals("95b92e81-d8ae-4b99-9482-8e5b3dbde5cc")) return ChatColor.DARK_PURPLE + "Previous PK Concept Designer";  //Majorite

					/*else if (id.toString().equals("ff01d5c1-9be2-40c2-b708-0c0971039a45")) return ChatColor.DARK_PURPLE + "Previous PK Digital Team";  //UnlitedOwns*/
		};

		if (key != null) return new LangBuilder(key).toString();

		return "";
	}
	
	@SuppressWarnings("deprecation")
	public static String getStaff(String playerName) {
		return getStaff(Bukkit.getOfflinePlayer(playerName).getUniqueId());
	}
}
