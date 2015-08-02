package com.strangeone101.bendinggui.menus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import com.projectkorra.ProjectKorra.BendingPlayer;
import com.projectkorra.ProjectKorra.Element;
import com.projectkorra.ProjectKorra.GeneralMethods;
import com.projectkorra.ProjectKorra.MultiAbilityManager;
import com.projectkorra.ProjectKorra.SubElement;
import com.projectkorra.ProjectKorra.Ability.AbilityModuleManager;
import com.projectkorra.ProjectKorra.Ability.StockAbilities;
import com.strangeone101.bendinggui.Descriptions;
import com.strangeone101.bendinggui.BendingGUI;
import com.strangeone101.bendinggui.Config;
import com.strangeone101.bendinggui.MenuItem;
import com.strangeone101.bendinggui.PlayerGuiData;
import com.strangeone101.bendinggui.MenuBase;

public class MenuBendingOptions extends MenuBase
{
	/**Had to move to Strings to be compatible with ProjectKorra. That's how moves are handled now.*/
	private List<String> playerMoves = new ArrayList<String>();
	private int movePage = 0;
	
	private String abilityIndex = null; //What ability is currently selected
	private int abilityIndexInt = -1;      //Used only for glow
	private int slotIndex = -1;
	
	protected Mode mode = Mode.NONE;
	
	protected OfflinePlayer thePlayer;
	protected Player openPlayer = null;
	
	protected boolean redirect = false;
	protected boolean hasBeenToggled = false;
	
	
	public MenuBendingOptions(OfflinePlayer player) 
	{
		super("Bending Options", 4);
		this.thePlayer = player;
		if (GeneralMethods.getBendingPlayer(player.getName()).getElements().isEmpty())
		{
			redirect = true;
		}	
	}
	
	@SuppressWarnings("deprecation")
	public MenuItem getItemForMove(OfflinePlayer player, final String move, final int index)
	{
		MaterialData mat = new MaterialData(Material.STAINED_GLASS, (byte)4);
		if (BendingGUI.getAbilityElement(move) == Element.Air) {mat = Config.elementIcons.get(Element.Air);}
		else if (BendingGUI.getAbilityElement(move) == Element.Earth) {mat = Config.elementIcons.get(Element.Earth);}
		else if (BendingGUI.getAbilityElement(move) == Element.Fire) {mat = Config.elementIcons.get(Element.Fire);}
		else if (BendingGUI.getAbilityElement(move) == Element.Water) {mat = Config.elementIcons.get(Element.Water);}
		else if (BendingGUI.getAbilityElement(move) == Element.Chi) {mat = Config.elementIcons.get(Element.Chi);}
		else {mat = Config.avatarIcon;}
		
		if (BendingGUI.getSubElement(move) == SubElement.Bloodbending) mat = Config.subElementIcons.get(SubElement.Bloodbending);
		else if (BendingGUI.getSubElement(move) == SubElement.Icebending) mat = Config.subElementIcons.get(SubElement.Icebending);
		else if (BendingGUI.getSubElement(move) == SubElement.Lavabending) mat = Config.subElementIcons.get(SubElement.Lavabending);
		else if (BendingGUI.getSubElement(move) == SubElement.Sandbending) mat = Config.subElementIcons.get(SubElement.Sandbending);
		else if (BendingGUI.getSubElement(move) == SubElement.Plantbending) mat = Config.subElementIcons.get(SubElement.Plantbending);
		else if (BendingGUI.getSubElement(move) == SubElement.Metalbending) mat = Config.subElementIcons.get(SubElement.Metalbending);
		else if (BendingGUI.getSubElement(move) == SubElement.Combustion) mat = Config.subElementIcons.get(SubElement.Combustion);
		else if (BendingGUI.getSubElement(move) == SubElement.Flight) mat = Config.subElementIcons.get(SubElement.Flight);
		else if (BendingGUI.getSubElement(move) == SubElement.Healing) mat = Config.subElementIcons.get(SubElement.Healing);
		else if (BendingGUI.getSubElement(move) == SubElement.Lightning) mat = Config.subElementIcons.get(SubElement.Lightning);
		else if (BendingGUI.getSubElement(move) == SubElement.SpiritualProjection) mat = Config.subElementIcons.get(SubElement.SpiritualProjection);
		
		final ChatColor c = BendingGUI.getAbilityElement(move) == Element.Air ? ChatColor.GRAY : (BendingGUI.getAbilityElement(move) == Element.Chi ? ChatColor.GOLD : (BendingGUI.getAbilityElement(move) == Element.Earth ? ChatColor.GREEN : (BendingGUI.getAbilityElement(move) == Element.Fire ? ChatColor.RED : (BendingGUI.getAbilityElement(move) == Element.Water ?  ChatColor.BLUE: ChatColor.LIGHT_PURPLE))));
		
		MenuItem item = new MenuItem(c + move.toString(), mat) {

			@Override
			public void onClick(Player player) 
			{
				if (!GeneralMethods.abilityExists(move))
				{
					player.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "Move doesn't exist! Please contact StrangeOne101 about this!");
					BendingGUI.log.warning("[BendingGUI] Error: Move selected with invalid ID/Name. Move name: \"" + move + "\". Please contact StrangeOne101 about this!");
					closeMenu(player);
					return;
				}
				
				if (mode == Mode.INFO && move != null)
				{
					//player.sendMessage(ChatColor.YELLOW + "Info for " + c);
					//String s = BendingGUI.getAbilityElement(move) == Element.Air ? "Air" : (BendingGUI.getAbilityElement(move) == Element.Earth ? "Earth" : (BendingGUI.getAbilityElement(move) == Element.Water ? "Water" : (BendingGUI.getAbilityElement(move) == Element.Fire ? "Fire" : (BendingGUI.getAbilityElement(move) == Element.Chi ? "Chiblocker" : "Other"))));
					//Tools.sendMessage(player, c + s + "." + move.toString());
					player.sendMessage(c + AbilityModuleManager.descriptions.get(move));
					closeMenu(player);
					return;
				}
				if (mode == Mode.DELETE)
				{
					mode = Mode.NONE;
				}
				
				if (slotIndex == -1)
				{
					abilityIndex = move != abilityIndex ? move : null;
					abilityIndexInt = index != abilityIndexInt ? index : -1;
				}
				else
				{
					bindMoveToSlot(player, move, slotIndex);
					slotIndex = -1;
					abilityIndex = null;
					abilityIndexInt = -1;
				}
				update();
			}
		};
		String desc = "";
		String moveDesc = ChatColor.DARK_GRAY + Descriptions.getDescription(move);
		List<String> l = BendingGUI.getDescriptions(moveDesc, ChatColor.DARK_GRAY, 45);
		for (String s : l)
		{
			desc = desc + s + "\n";
		}
		if (move != null && this.mode == Mode.DELETE)
		{
			desc = desc + "\n\n" + ChatColor.RED + ChatColor.BOLD + "TOGGLE THE REMOVAL TOOL BEFORE\n" + ChatColor.RED + ChatColor.BOLD + "REBINDING!" + "\n" + ChatColor.RESET + ChatColor.DARK_GRAY + "You must turn off the unbind tool before you\n" + ChatColor.DARK_GRAY + "can rebind moves again!";
		}
		else if (move != null && this.mode == Mode.INFO)
		{
			desc = desc + "\n\n" + ChatColor.YELLOW + ChatColor.BOLD + "CLICK FOR MORE INFO!" + "\n" + ChatColor.RESET + ChatColor.DARK_GRAY + "Click to display more infomation about this move!";
		}
		else if (this.abilityIndex == move)
		{
			desc = desc + "\n\n" + ChatColor.GREEN + ChatColor.BOLD + "CURRENTLY SELECTED!" + "\n" + ChatColor.RESET + ChatColor.DARK_GRAY + "Click a slot to bind to this move to!";
		}
		item.setDescriptions(Arrays.asList(desc.split("\n")));
		if (abilityIndex == move)
		{
			item.setEnchanted(true);
		}

		return item;
	}
	
	@SuppressWarnings("deprecation")
	public MenuItem getItemForSlot(OfflinePlayer player, final int index)
	{
		MaterialData mat = new MaterialData(Material.STAINED_GLASS_PANE, (byte)15);
		final String move = this.getMoveForSlot(player, index + 1);
		ChatColor c = ChatColor.RED;
		if (move != null && !move.equals("null"))
		{
			c = BendingGUI.getAbilityElement(move) == Element.Air ? ChatColor.GRAY : (BendingGUI.getAbilityElement(move) == Element.Chi ? ChatColor.GOLD : (BendingGUI.getAbilityElement(move) == Element.Earth ? ChatColor.GREEN : (BendingGUI.getAbilityElement(move) == Element.Fire ? ChatColor.RED : (BendingGUI.getAbilityElement(move) == Element.Water ? ChatColor.BLUE : ChatColor.LIGHT_PURPLE))));
			if (BendingGUI.getAbilityElement(move) == Element.Air) {mat.setData((byte)0);}
			else if (BendingGUI.getAbilityElement(move) == Element.Earth) {mat.setData((byte)13);}
			else if (BendingGUI.getAbilityElement(move) == Element.Fire) {mat.setData((byte)14);}
			else if (BendingGUI.getAbilityElement(move) == Element.Water) {mat.setData((byte)11);}
			else if (BendingGUI.getAbilityElement(move) == Element.Chi) {mat.setData((byte)4);}
			else {mat.setData((byte)10);}
		}
		final ChatColor c1 = c;
		
		
		String itemname, desc = "";
		
		if (move == null || move.equals("null"))
		{
			itemname = ChatColor.RED + "Slot " + (index + 1) + ChatColor.DARK_GRAY + " (Empty)" ;
			desc = ChatColor.DARK_GRAY + "Nothing is currently bound to this slot!\n\n"
				 + ChatColor.DARK_GRAY + "Click a move and click a slot to bind!";
		}
		else
		{
			itemname = c + "Slot " + (index + 1) + ChatColor.DARK_GRAY + " (" + move.toString() + ")" ;
			desc = ChatColor.DARK_GRAY + "Currently Bound: " + c + ChatColor.BOLD + move.toString() + "\n\n" + ChatColor.RESET + ChatColor.DARK_GRAY + "To bind a new move, click a move then click\n" + ChatColor.DARK_GRAY + "the slot you want to bind it to.";
		}
		
		if (MultiAbilityManager.playerAbilities.containsKey(player.getName()))
		{
			desc = desc + "\n\n" + ChatColor.RED + ChatColor.BOLD + "YOU CANNOT EDIT YOUR BINDS RIGHT NOW!\n" + ChatColor.RESET + ChatColor.DARK_GRAY + "You are using a multi-ability move and must stop\nusing it before you can bind again!";
		}
		else if (thePlayer instanceof Player)
		{
			if (move != null && mode == Mode.DELETE)
			{
				desc = desc + "\n\n" + ChatColor.RED + ChatColor.BOLD + "CLICK TO REMOVE!" + "\n" + ChatColor.RESET + ChatColor.DARK_GRAY + "Click to remove " + move.toString() + " from this slot!";
			}
			else if (move != null && mode == Mode.INFO)
			{
				desc = desc + "\n\n" + ChatColor.YELLOW + ChatColor.BOLD + "CLICK FOR MOVE INFO!" + "\n" + ChatColor.RESET + ChatColor.DARK_GRAY + "Click to display more infomation about " + move.toString() + "!";
			}
			else if (this.slotIndex == index)
			{
				desc = desc + "\n\n" + ChatColor.GREEN + ChatColor.BOLD + "CURRENTLY SELECTED!" + "\n" + ChatColor.RESET + ChatColor.DARK_GRAY + "Click a move to bind to this slot!";
			}
		}
		else
		{
			desc = desc + "\n\n" + ChatColor.RED + ChatColor.BOLD + "CANNOT MODIFY BENDING OF OFFLINE PLAYERS!" + "\n" + ChatColor.RESET + ChatColor.DARK_GRAY + "You can't modify the bending of players that are offline!";
		}
		
		MenuItem item = new MenuItem(itemname, mat) {

			@Override
			public void onClick(Player player) 
			{
				if (MultiAbilityManager.playerAbilities.containsKey(player.getName()))
				{
					closeMenu(player);
					player.sendMessage(ChatColor.RED + "You cannot modify binds right now!");
				}
				else if (thePlayer instanceof Player)
				{
					if (mode == Mode.DELETE && move != null)
					{
						//GeneralMethods.getBendingPlayer(thePlayer.getName()).setAbility(index, null);
						BendingPlayer bPlayer = GeneralMethods.getBendingPlayer(thePlayer.getName());
						HashMap<Integer, String> abilities = bPlayer.getAbilities();
						abilities.remove(index + 1);
						bPlayer.setAbilities(abilities);
						//GeneralMethods.bindAbility(thePlayer, null, index);
						player.sendMessage(ChatColor.RED + "Removed " + move.toString() + " from Slot " + (index + 1));
						update();
						return;
					}
					else if (mode == Mode.INFO && move != null)
					{
						//player.sendMessage(ChatColor.YELLOW + "Info for " + c1 + move.toString());
						//String s = BendingGUI.getAbilityElement(move) == Element.Air ? "Air" : (BendingGUI.getAbilityElement(move) == Element.Earth ? "Earth" : (BendingGUI.getAbilityElement(move) == Element.Water ? "Water" : (BendingGUI.getAbilityElement(move) == Element.Fire ? "Fire" : (BendingGUI.getAbilityElement(move) == Element.Chi ? "Chiblocker" : "Other"))));
						//Tools.sendMessage(player, c1 + s + "." + move.toString());
						player.sendMessage(c1 + AbilityModuleManager.descriptions.get(move));
						closeMenu(player);
						return;
					}
					
					if (abilityIndex == null)
					{
						slotIndex = index != slotIndex ? index : -1;
					}
					else
					{
						bindMoveToSlot((Player) thePlayer, abilityIndex, index);
						abilityIndex = null;
						slotIndex = -1;
						abilityIndexInt = -1;
					}
					update();
				}
			}
		};
		item.setDescriptions(Arrays.asList(desc.split("\n")));
		if (index == this.slotIndex)
		{
			item.setEnchanted(true);
		}
		return item;
	}
	
	/**Toggles on the removal tool. So players can unbind their bending*/
	public MenuItem getRemoveToolItem(OfflinePlayer player)
	{
		MaterialData material = new MaterialData(Material.BARRIER);
		String s = ChatColor.RED + "Removal Tool " + ChatColor.DARK_GRAY + (this.mode == Mode.DELETE ? "(On)" : "(Off)");
		MenuItem item = new MenuItem(s, material) {

			@Override
			public void onClick(Player player) 
			{
				if (this.isShiftClicked())
				{
					BendingPlayer bPlayer = GeneralMethods.getBendingPlayer(thePlayer.getName());
					HashMap<Integer, String> abilities = new HashMap<Integer, String>();
					bPlayer.setAbilities(abilities);
					player.sendMessage(ChatColor.RED + "Removed all bound moves from slots.");
					mode = mode == Mode.DELETE ? Mode.NONE : mode;
				}
				else
				{
					mode = mode == Mode.DELETE ? Mode.NONE : Mode.DELETE;
				}
				update();
			}
		};
		item.setDescriptions(Arrays.asList(new String[] {ChatColor.GRAY + "Allows you to remove bound moves to your slots.",
		ChatColor.GRAY + "Click to turn " + (this.mode == Mode.DELETE ? "off again" : "on"), ChatColor.GRAY + "SHIFT click "
				+ "to remove all your bound moves"}));
		if (this.mode == Mode.DELETE)
		{
			item.setEnchanted(true);
		}
		return item;
	}
	
	/**Shows lots of info for moves when you click them*/
	public MenuItem getInfoToolItem(OfflinePlayer player)
	{
		MaterialData material = new MaterialData(Material.EMPTY_MAP);
		String s = "Move Help Tool " + ChatColor.DARK_GRAY + (this.mode == Mode.INFO ? "(On)" : "(Off)");
		s = ChatColor.YELLOW + s;
		MenuItem item = new MenuItem(s, material) {

			@Override
			public void onClick(Player player) 
			{
				mode = mode == Mode.INFO ? Mode.NONE : Mode.INFO;
				update();
			}
		};
		item.setDescriptions(Arrays.asList(new String[] {ChatColor.GRAY + "Click for more infomation and on bending moves.",
		ChatColor.GRAY + "Click to turn " + (this.mode == Mode.INFO ? "off again" : "on")}));
		if (this.mode == Mode.INFO)
		{
			item.setEnchanted(true);
		}
		return item;
	}
	
	/**The arrow for moving pages*/
	public MenuItem getPageArrow(OfflinePlayer player, final boolean isRightDirection)
	{
		MaterialData material = new MaterialData(Material.ARROW);
		String s = ChatColor.YELLOW + (isRightDirection ? "Next Page " : "Previous Page ");
		s = s + ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW + (this.movePage + 1) + ChatColor.DARK_GRAY + "/" + ChatColor.YELLOW + this.getMaxPages() + ChatColor.DARK_GRAY + ")";
		MenuItem item = new MenuItem(s, material) {

			
			@Override
			public void onClick(Player player) 
			{
				//Import not to use the player variable from this method. If you do, the menu will change
				//to that players which stops functionality for admins looking at other player's bindings
				if (movePage == getMaxPages() - 1 && isRightDirection) return;
				movePage = isRightDirection ? movePage + 1 : movePage - 1;
				PlayerGuiData.getData(thePlayer).setPage(movePage);
				if (BendingGUI.pageArrowMoveMouse)
				{
					MenuBendingOptions menu = new MenuBendingOptions(thePlayer);
					menu.movePage = movePage;
					getMenu().getInventory().clear();
					closeMenu(player);
					menu.openMenu(player);
					//Has to use normal player here because we want it to show to the player who's using the menu
				}
				else
				{
					update();
				}
			}
		};
		item.setDescriptions(Arrays.asList(new String[] {ChatColor.GRAY + "Click to go to the " + (isRightDirection ? "next" : "previous") + " page of moves."}));
		return item;
	}
	
	public MenuItem getEditElements()
	{
		final MenuBendingOptions instance = this;
		final boolean b = this.openPlayer.hasPermission("bending.admin.add");
		MenuItem item = new MenuItem(ChatColor.YELLOW + (b ? "Add/Remove Elements" : "Change Element"), new MaterialData(Material.NETHER_STAR)) {
			@Override
			public void onClick(Player player) 
			{
				if (b && (player.hasPermission("bending.admin.add") || player.hasPermission("bending.admin.remove")))
				{
					switchMenu(player, new MenuEditElements(thePlayer, instance));
				}
				else if (b)
				{
					player.sendMessage(ChatColor.RED + "You don't have permission to edit this player's bending!");
					closeMenu(player);
				}
				else if (!b && player.hasPermission("bending.command.rechoose"))
				{
					switchMenu(player, new MenuElementSelect(openPlayer, instance));
				}
			}
		};
		String s = this.openPlayer.getName() == this.thePlayer.getName() ? "you" : this.thePlayer.getName();
		if (b)
		{
			item.addDescription(ChatColor.GRAY + "Edit the elements " + s + " can bend");
		}
		else
		{
			item.addDescription(ChatColor.GRAY + "Change your main bending element");
		}
		
		return item;
	}
	
	@SuppressWarnings("deprecation")
	public MenuItem getBendingToggle()
	{
		boolean isToggled = !GeneralMethods.getBendingPlayer(this.thePlayer.getName()).isToggled();
		byte damage = (byte) (isToggled ? 13 : 14);
		final OfflinePlayer p = this.thePlayer;
		MenuItem item = new MenuItem((isToggled ? ChatColor.GREEN + "En" : ChatColor.RED + "Dis") + "able Bending", new MaterialData(Material.STAINED_CLAY, damage)) {
			@Override
			public void onClick(Player player) 
			{
				if (!player.hasPermission("bending.command.toggle"))
				{
					player.sendMessage(ChatColor.RED + "You do not have permission to toggle " + p.getName() + "'s bending!");
					return;
				}
				
				hasBeenToggled = GeneralMethods.getBendingPlayer(p.getName()).isToggled();
				
				if (thePlayer.getName().equals(player.getName()))
				{
					Bukkit.dispatchCommand(player, "bending toggle");
					update();
					return;
				}
				
				BendingPlayer bPlayer = GeneralMethods.getBendingPlayer(p.getName());
				if (!bPlayer.isToggled()) 
				{	
					if (p instanceof Player)
					{
						((Player)p).sendMessage(ChatColor.RED + "Your bending has been toggled off. You will not be able to use most abilities until you toggle it back.");
					}
					if (openPlayer != thePlayer)
					{
						openPlayer.sendMessage(ChatColor.GREEN + "You turned off " + thePlayer.getName() + "'s bending.");
					}
					
				} 
				else 
				{
					if (p instanceof Player)
					{
						((Player)p).sendMessage(ChatColor.GREEN + "You have turned your Bending back on.");
					}
					if (openPlayer != thePlayer)
					{
						openPlayer.sendMessage(ChatColor.GREEN + "You turned on " + thePlayer.getName() + "'s bending.");
					}
				}
				
				//If the player is looking at their own one, use this method instead of reflection.
				//Just simpler AND the preferred method since you should always try avoid reflection
				
				//Force set toggle
				Class<?> c = bPlayer.getClass();
				try 
				{
					Field f = c.getDeclaredField("isToggled");
					f.setAccessible(true);
					f.setBoolean(bPlayer, bPlayer.isToggled() ? false : true);
					f.setAccessible(false);
				} 
				catch (NoSuchFieldException e) {e.printStackTrace();} 
				catch (SecurityException e) {e.printStackTrace();} 
				catch (IllegalArgumentException e) {e.printStackTrace();} 
				catch (IllegalAccessException e) {e.printStackTrace();}
				
				update();
				//Bukkit.getServer().dispatchCommand(p, "bending toggle");
			}
		};
		item.addDescription(ChatColor.GRAY + "Click to " + (isToggled ? "re-en" : "dis") + "able bending!");
		return item;
	}
	
	@SuppressWarnings("deprecation")
	public MenuItem getPlayerStats()
	{
		final MenuBendingOptions menu1 = this;
		String string = thePlayer.getName() != openPlayer.getName() ? ChatColor.YELLOW + thePlayer.getName() + "'s Bending" : ChatColor.YELLOW + "Your bending";
		MenuItem item = new MenuItem(string, new MaterialData(Material.SKULL_ITEM, (byte)3)) {

			@Override
			public void onClick(Player player) 
			{
				if (player.hasPermission("bendinggui.view"))
				{
					switchMenu(player, new MenuPlayers(menu1));
				}
			}
		};
		
		if ((thePlayer instanceof Player && ((Player)thePlayer).hasPermission("bending.avatar")) || GeneralMethods.getBendingPlayer(this.thePlayer.getName()).getElements().size() > 1)
		{
			boolean b = GeneralMethods.getBendingPlayer(this.thePlayer.getName()).hasElement(Element.Earth) || GeneralMethods.getBendingPlayer(this.thePlayer.getName()).hasElement(Element.Air);
			item.addDescription((thePlayer.getName() != openPlayer.getName() ? ChatColor.GRAY + "Currently the " : ChatColor.GRAY + "You are the " ) + GeneralMethods.getAbilityColor("AvatarState") + "Avatar" );
			item.addDescription(ChatColor.DARK_GRAY + (thePlayer.getName() != openPlayer.getName() ? "They" : "You") + " are currently a" + (b ? "n" : "") + ":");
		}
		else
		{
			boolean b = GeneralMethods.getBendingPlayer(this.thePlayer.getName()).hasElement(Element.Earth) || GeneralMethods.getBendingPlayer(this.thePlayer.getName()).hasElement(Element.Air);
			item.addDescription((thePlayer.getName() != openPlayer.getName() ? ChatColor.DARK_GRAY + "They" : ChatColor.GRAY + "You" ) + " are currently a" + (b ? "n" : "") + ":");
		}
		BendingPlayer p = GeneralMethods.getBendingPlayer(thePlayer.getName());
	
		if (p.getElements().contains(Element.Air)) 
		{
			String s = ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Airbender";
			if (thePlayer instanceof Player) 
			{
				if (((Player) thePlayer).hasPermission("bending.air.flight") && Config.showSubElementsOnUser) {s = s + ChatColor.DARK_GRAY + " (Can" + ChatColor.GRAY + " Fly" + ChatColor.DARK_GRAY + ")";}
				item.addDescription(s);
			}
		}
		if (p.getElements().contains(Element.Earth)) 
		{
			String s = ChatColor.DARK_GRAY + "- " + ChatColor.GREEN + "Earthbender";
			if (thePlayer instanceof Player) 
			{
				boolean b = false;
				List<String> list = new ArrayList<String>();
				if (((Player)thePlayer).hasPermission("bending.earth.metalbending") && Config.showSubElementsOnUser) 
				{
					b = true;
					list.add(GeneralMethods.getAbilityColor(StockAbilities.MetalClips.toString()) + "Metalbend");
				}
				if (((Player)thePlayer).hasPermission("bending.earth.lavabending") && Config.showSubElementsOnUser) 
				{
					b = true;
					list.add(GeneralMethods.getAbilityColor(StockAbilities.LavaFlow.toString()) + "Lavabend");
				}
				if (((Player)thePlayer).hasPermission("bending.earth.sandbending") && Config.showSubElementsOnUser) 
				{
					b = true;
					list.add(GeneralMethods.getAbilityColor(StockAbilities.SandSpout.toString()) + "Sandbend");
				}
				if (b && Config.showSubElementsOnUser) 
				{
					s = s + ChatColor.DARK_GRAY + " (Can " + (BendingGUI.INSTANCE.makeListFancy(list).replaceAll("\\,", ChatColor.DARK_GRAY + ",")
							.replaceAll(" and ", ChatColor.DARK_GRAY + " and ") + ChatColor.DARK_GRAY + ")");
				}
				List<String> l = BendingGUI.getDescriptions(s, ChatColor.DARK_GRAY, 65);
				for (String s1 : l) {
					item.addDescription(s1);
				}
			}
		}
		if (p.getElements().contains(Element.Fire)) 
		{
			String s = ChatColor.DARK_GRAY + "- " + ChatColor.RED + "Firebender";
			if (thePlayer instanceof Player) 
			{
				boolean b = false;
				List<String> list = new ArrayList<String>();
				if (((Player)thePlayer).hasPermission("bending.fire.lightningbending")) 
				{
					b = true;
					list.add("Can use " + GeneralMethods.getAbilityColor(StockAbilities.Lightning.toString()) + "Lightning");
				}
				if (((Player)thePlayer).hasPermission("bending.fire.combustionbending")) 
				{
					b = true;
					list.add("Can " + GeneralMethods.getAbilityColor(StockAbilities.Combustion.toString()) + "Combust");
				}
				if (b && Config.showSubElementsOnUser) 
				{
					s = s + ChatColor.DARK_GRAY + " (" + (BendingGUI.INSTANCE.makeListFancy(list).replaceAll("\\,", ChatColor.DARK_GRAY + ",")
							.replaceAll(" and ", ChatColor.DARK_GRAY + " and ") + ChatColor.DARK_GRAY + ")");
				}
				List<String> l = BendingGUI.getDescriptions(s, ChatColor.DARK_GRAY, 65);
				for (String s1 : l) {
					item.addDescription(s1);
				}
			}
			
		}
		if (p.getElements().contains(Element.Water)) 
		{
			String s = ChatColor.DARK_GRAY + "- " + ChatColor.BLUE + "Waterbender";
			if (thePlayer instanceof Player) 
			{
				boolean b = false;
				List<String> list = new ArrayList<String>();
				if (((Player)thePlayer).hasPermission("bending.water.bloodbending")) 
				{
					b = true;
					list.add(GeneralMethods.getAbilityColor(StockAbilities.Bloodbending.toString()) + "Bloodbend");
				}
				if (((Player)thePlayer).hasPermission("bending.water.plantbending")) 
				{
					b = true;
					list.add(GeneralMethods.getAbilityColor(StockAbilities.Surge.toString()) + "Plantbend");
				}
				if (((Player)thePlayer).hasPermission("bending.water.healing")) 
				{
					b = true;
					list.add(GeneralMethods.getAbilityColor(StockAbilities.HealingWaters.toString()) + "Heal");
				}
				if (b && Config.showSubElementsOnUser) 
				{
					s = s + ChatColor.DARK_GRAY + " (Can " + (BendingGUI.INSTANCE.makeListFancy(list).replaceAll("\\,", ChatColor.DARK_GRAY + ",")
							.replaceAll(" and ", ChatColor.DARK_GRAY + " and ") + ChatColor.DARK_GRAY + ")");
				}
				List<String> l = BendingGUI.getDescriptions(s, ChatColor.DARK_GRAY, 65);
				for (String s1 : l) {
					item.addDescription(s1);
				}
			}
			
		}
		if (p.getElements().contains(Element.Chi)) 
		{
			String s = ChatColor.DARK_GRAY + "- " + ChatColor.GOLD + "Chiblocker";
			item.addDescription(s);
		}
		
		if (this.openPlayer.hasPermission("bendinggui.admin"))
		{
			item.addDescription("");
			item.addDescription(ChatColor.GRAY + "" + ChatColor.BOLD + "CLICK TO CHANGE PLAYER");
		}
		else if (this.openPlayer.hasPermission("bending.command.who"))
		{
			item.addDescription("");
			item.addDescription(ChatColor.YELLOW + "" + ChatColor.BOLD + "Click to view other player's bending");
		}
		/*NBTTagCompound tag = item.getNBTData();
		tag.setString("SkullOwner", thePlayer.getName());
		item.setNBTData(tag);*/
		return item;
	}
	
	/**Returns the move based on the slot*/
	public String getMoveForSlot(OfflinePlayer player, int index)
	{
		try {
		return GeneralMethods.getBendingPlayer(player.getName()).getAbilities().get(index);
		} catch (NullPointerException e) {return null;}
	}
	
	/**Bind the move to the slot. Args: Move, slot. Slot should be an int from 0 to 8*/
	public void bindMoveToSlot(Player player, String move, int slot)
	{
		//BendingGUI.log.info("Player: " + player + ", Move: " + move + ", Slot: " + slot);
		if (slot >= 9)
		{
			openPlayer.sendMessage(ChatColor.RED + "Error: Slot binding out of range! Please contact your admin about this!");
			this.closeMenu(openPlayer);
			return;
		}
		GeneralMethods.getBendingPlayer(player.getName()).getAbilities().put(slot + 1, move);
		GeneralMethods.saveAbility(GeneralMethods.getBendingPlayer(player.getName()), slot + 1, move);
		//GeneralMethods.bindAbility(player, move, slot + 1);
		ChatColor c = BendingGUI.getAbilityElement(move) == Element.Air ? ChatColor.GRAY : (BendingGUI.getAbilityElement(move) == Element.Chi ? ChatColor.GOLD : (BendingGUI.getAbilityElement(move) == Element.Earth ? ChatColor.GREEN : (BendingGUI.getAbilityElement(move) == Element.Fire ? ChatColor.RED : (BendingGUI.getAbilityElement(move) == Element.Water ? ChatColor.BLUE : ChatColor.LIGHT_PURPLE))));
		player.sendMessage(c + move.toString() + ChatColor.YELLOW + " bound to slot " + (slot + 1) + "!");
	}
	
	/**Updates the GUI by re-initializing*/
	public void update()
	{
		OfflinePlayer player = this.thePlayer;
		//BendingGUI.log.log(Level.INFO, "Player: " + player.getDisplayName() + ",Page: " + movePage + ", MaxPage: " + this.getMaxPages() + ", MoveSize: " + playerMoves.size());
		//this.getInventory().clear();
		
		for (int i = 0; i < this.getInventory().getContents().length; i++)
		{
			if (this.getInventory().getContents()[i] == null)
			{
				this.getInventory().clear(i);
			}
			else if (this.getInventory().getContents()[i].getType() != Material.SKULL_ITEM)
			{
				this.getInventory().clear(i);
			}
		}
		this.playerMoves.clear();
		
		if (GeneralMethods.getBendingPlayer(player.getName()).getElements() == null || GeneralMethods.getBendingPlayer(player.getName()).getElements().isEmpty()) //Somehow this needs to be here
		{
			this.switchMenu(openPlayer, new MenuElementSelect(thePlayer));
			return;
		}
		else if (!GeneralMethods.getBendingPlayer(player.getName()).isToggled() || this.hasBeenToggled)
		{
			for (int i = 0; i < 18; i++)
			{
				@SuppressWarnings("deprecation")
				MenuItem disabledSlot = new MenuItem(ChatColor.RED + "Bending is disabled!", new MaterialData(Material.STAINED_GLASS_PANE, (byte)7)) {
					@Override
					public void onClick(Player player) {}		
				};
				disabledSlot.setDescriptions(Arrays.asList(new String[] {ChatColor.GRAY + "Enable bending to use again!"}));
				this.addMenuItem(disabledSlot, i);
			}
			//this.addMenuItem(MenuItems.bendingRebind_ToggleBendingGreen, 8, 3);
			this.addMenuItem(this.getBendingToggle(), 8, 3);
		}
		else //If not, start adding what's needed
		{
			//this.addMenuItem(MenuItems.bendingRebind_ToggleBendingRed, 8, 3);
			this.addMenuItem(this.getBendingToggle(), 8, 3);
			
			HashSet<String> checked = new HashSet<String>();
			
			@SuppressWarnings("unchecked")
			Collection<String>[] order = new Collection[] {AbilityModuleManager.airbendingabilities, AbilityModuleManager.flightabilities, AbilityModuleManager.spiritualprojectionabilities, 
					AbilityModuleManager.earthbendingabilities, AbilityModuleManager.sandabilities, AbilityModuleManager.metalabilities, AbilityModuleManager.lavaabilities,
					AbilityModuleManager.firebendingabilities, AbilityModuleManager.combustionabilities, AbilityModuleManager.lightningabilities,
					AbilityModuleManager.waterbendingabilities, AbilityModuleManager.healingabilities, AbilityModuleManager.plantabilities, AbilityModuleManager.iceabilities, AbilityModuleManager.bloodabilities, 
					AbilityModuleManager.chiabilities, AbilityModuleManager.abilities};
			
			for (Collection<String> s : order)
			{
				if (s == AbilityModuleManager.abilities) //Save time and energy by picking out what we have already used
				{
					s = new HashSet<String>();
					s.addAll(AbilityModuleManager.abilities);
					s.removeAll(checked);
				}
				for (int i = 0; i < s.size(); i++)
				{
					String move = (String) s.toArray()[i];
					if (!((s == AbilityModuleManager.airbendingabilities || s == AbilityModuleManager.earthbendingabilities ||
							s == AbilityModuleManager.firebendingabilities || s == AbilityModuleManager.waterbendingabilities) &&
							AbilityModuleManager.subabilities.contains(move)))
					{
						if (((player instanceof Player && BendingGUI.canBind((Player)player, move)) || !(player instanceof Player)) && !this.playerMoves.contains(move))
						{
							this.playerMoves.add(move);
						}
					}
				}
			}
			
			//Hopefully fix bug
			if (this.playerMoves.isEmpty())
			{
				if (!PlayerGuiData.getData(player).abilityData.isEmpty())
				{
					this.playerMoves = PlayerGuiData.getData(player).abilityData;
					PlayerGuiData.updateAbilityData(player);
				}
				else
				{
					BukkitRunnable run = new BukkitRunnable() {

						public void run() 
						{
							update();
						}	
					};
					run.runTaskLater(BendingGUI.INSTANCE, 200L);
					 //Try again
				}
			}
			else
			{
				PlayerGuiData.getData(player).abilityData = this.playerMoves;
			}
			
			int maxPage = this.getMaxPages();
			
			int p = PlayerGuiData.getData(player).getPage();
			if (p > this.getMaxPages() + 1)
			{
				p = this.getMaxPages() + 1;
			}
			movePage = p;
			
			/*int i1 = 0; //Where to start
			int j = 0; //Max moves on the page
			j = maxPage == 1 ? 9 : (maxPage == 2 ? 8 : (movePage == 0 ? 8 : (movePage + 1 == maxPage ? 8 : 7))); //Decide max moves per page based on the max no. of pages
			i1 = ((movePage + 1 == maxPage || j == 7) && j != 9) ? 1 : 0; //If on last page or in mid page, start the index at 1
			
	
			for (int j1 = i1; j1 < j; j1++)
			{
				Abilities move = maxPage == 1 ? this.playerMoves.get(j1 - i1) : this.playerMoves.get(this.movePage * 7 + 1 + j1 - i1);
				MenuItem item = this.getItemForMove(player, move, j1);
				if (abilityIndexInt == j1)
				{
					//Adds the enchanted glow
					this.addGlow(item, j1);
				}
				else //Don't add it with this method if you need custom NBT data.
				{
					this.addMenuItem(item, j1);
				}
			}*/
			
			//The first index for the move to get from the list
			int firstMoveIndex = (movePage == 0 ? 0 : movePage * 7 + 1);
			int firstIndex_ = movePage == 0 ? 0 : 1;
			
			int end; //Last index to use
		    
		    // Find the end of the relevant part of the list.
		    if (movePage >= maxPage - 1) end = this.playerMoves.size();
		    else if (movePage == 0) end = 8;
		    else end = firstMoveIndex + 7;
		    
		    if (end >= this.playerMoves.size()) end = this.playerMoves.size();
		 
		    for (int i = firstMoveIndex; i < end; ++i) {
		    	int slotIndex = movePage == 0 ? i : (i - firstMoveIndex + firstIndex_);
		        String move = this.playerMoves.get(i);
		        MenuItem item = this.getItemForMove(player, move, slotIndex);
				/*if (abilityIndexInt == slotIndex)
				{
					//Adds the enchanted glow
					//this.addGlow(item, slotIndex);
				}
				else //Don't add it with this method if you need custom NBT data.
				{*/
					this.addMenuItem(item, slotIndex);
				//}        
		    }
			
			//Add arrows
			if (this.movePage != 0)
			{
				this.addMenuItem(this.getPageArrow(player, false), 0);
			}
			if (this.movePage != maxPage - 1)
			{
				this.addMenuItem(this.getPageArrow(player, true), 8);
			}
			
			//Add the user slot items
			for (int i = 0; i < 9; i++)
			{
				MenuItem item = this.getItemForSlot(player, i);
				/*if (slotIndex == i)
				{
					//this.addGlow(item, i + 9);
				}
				else
				{*/
					this.addMenuItem(item, i, 1);
				//}
				
			}
		}
		
		MenuItem removeTool = this.getRemoveToolItem(player);
		/*if (this.mode == Mode.DELETE)
		{
			//this.addGlow(removeTool, 3 * 9 + 0); //Y * 9 + X
			//this.addGlow(removeTool, 3 * 9 + 6); //Y * 9 + X
		}
		else
		{*/
			//this.addMenuItem(removeTool, 3 * 9 + 0);
			this.addMenuItem(removeTool, 3 * 9 + 6);
		//}
		
		if (this.openPlayer != null)
		{
			//If they can modify bending, give them the edit elements page
			if (this.openPlayer.hasPermission("bending.admin.add") || this.openPlayer.hasPermission("bending.admin.remove"))
			{
				this.addMenuItem(this.getEditElements(), 3 * 9 + 4);
			}
		}
		
		if (this.getInventory().getContents()[3 * 9 + 7] == null)
		{
			MenuItem infoTool = this.getInfoToolItem(player);
			this.addMenuItem(infoTool, 3 * 9 + 7);
			
			this.addMenuItem(this.getPlayerStats(), 0, 3);
			SkullMeta meta = (SkullMeta) this.getInventory().getItem(27).getItemMeta();
			meta.setOwner(this.thePlayer.getName());
			this.getInventory().getItem(27).setItemMeta(meta);
		}
		
	}
	
	/**Add an enchanted glow to an item based on inv index*/
	/*public void addGlow(MenuItem item, int index)
	{
		net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item.getItemStack());
		NBTTagCompound tag = null;
        if (!stack.hasTag()) {
            tag = new NBTTagCompound();
            stack.setTag(tag);
        }
        if (tag == null) tag = stack.getTag();
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        NBTTagCompound display = new NBTTagCompound();
        display.setString("Name", item.getText());
        NBTTagList lore = new NBTTagList();
        for (String s : item.lore)
        {
        	lore.add(new NBTTagString(s));
        }
        display.set("Lore", lore);
        tag.set("display", display);
        stack.setTag(tag);
		ItemStack stack1 = CraftItemStack.asCraftMirror(stack);
        this.getInventory().setItem(index, stack1);
	}*/
	
	/**Add NBT data to the stack*/
	public void addNBTData(int x, int y, String key, Object data)
	{
		net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(this.getInventory().getItem(y * 9 + x));
		NBTTagCompound tag = null;
        if (!stack.hasTag()) {
            tag = new NBTTagCompound();
            stack.setTag(tag);
        }
        if (tag == null) tag = stack.getTag();
        if (data instanceof String) {tag.setString(key, (String) data);}
        else if (data instanceof Integer) {tag.setInt(key, (Integer) data);}
        else if (data instanceof Boolean) {tag.setBoolean(key, (Boolean) data);}
        else if (data instanceof Byte) {tag.setByte(key, (Byte) data);}
        else if (data instanceof Short) {tag.setShort(key, (Short) data);}
        else if (data instanceof Long) {tag.setLong(key, (Long) data);}
        else {tag.set(key, (NBTBase) data);}
        stack.setTag(tag);
        ItemStack stack1 = CraftItemStack.asCraftMirror(stack);
        this.getInventory().setItem(y * 9 + x, stack1);
	}
	
	@Override
	public void openMenu(Player player) 
	{
		this.openPlayer = player;
		this.playerMoves = new ArrayList<String>();
		if (this.redirect)
		{
			player.sendMessage(ChatColor.GREEN + "You aren't a bender yet! Please choose an element!");
			this.switchMenu(player, new MenuElementSelect(thePlayer));
			return;
		}
		
		int p = PlayerGuiData.getData(player).getPage();
		if (p > this.getMaxPages() + 1)
		{
			p = this.getMaxPages() + 1;
		}
		this.movePage = p;
		
		update();
		super.openMenu(player);
	}
	
	
	@Override
	public void closeMenu(Player player) 
	{
		this.playerMoves = null;
		super.closeMenu(player);
	}
	
	/*public int getMaxPages()
	{
		if (this.playerMoves.size() > 9)
		{
			int i = (this.playerMoves.size() - 8) / 7;
			if ((this.playerMoves.size() - 8) % 7 == 1)
			{
				i--;
			}
			return i + 2;
		}
		return 1;
	}*/
	
	public int getMaxPages() {
	    if (this.playerMoves.size() <=  9) return 1;
	    if (this.playerMoves.size() <= 16) return 2;
	    return ((this.playerMoves.size() - 3) / 7) + 1;
	}
	
	public enum Mode
	{
		NONE, DELETE, INFO
	}
}
