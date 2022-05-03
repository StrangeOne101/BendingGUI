package com.strangeone101.bendinggui.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.strangeone101.bendinggui.LangBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.Element.SubElement;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager;
import com.projectkorra.projectkorra.ability.util.ComboManager.AbilityInformation;
import com.projectkorra.projectkorra.ability.util.ComboManager.ComboAbilityInfo;
import com.projectkorra.projectkorra.ability.util.MultiAbilityManager;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.strangeone101.bendinggui.BendingBoard;
import com.strangeone101.bendinggui.BendingGUI;
import com.strangeone101.bendinggui.Config;
import com.strangeone101.bendinggui.Descriptions;
import com.strangeone101.bendinggui.DynamicUpdater;
import com.strangeone101.bendinggui.MenuBase;
import com.strangeone101.bendinggui.MenuItem;
import com.strangeone101.bendinggui.Util;

public class MenuBendingOptions extends MenuBase
{
	/**Had to move to Strings to be compatible with ProjectKorra. That's how moves are handled now.*/
	private List<String> playerMoves = new ArrayList<String>();
	private List<String> playerCombos = new ArrayList<String>();
	int movePage = 0;
	
	private String abilityIndex = null; //What ability is currently selected
	private int abilityIndexInt = -1;      //Used only for glow
	private int slotIndex = -1;
	
	protected Mode mode = Mode.NONE;
	
	protected OfflinePlayer thePlayer;
	protected Player openPlayer = null;
	
	protected boolean redirect = false;
	protected boolean hasBeenToggled = false;
	
	protected boolean combos = false;
	
	
	public MenuBendingOptions(OfflinePlayer player) 
	{
		super(new LangBuilder("Display.Main.Title").toString(), 4);
		this.thePlayer = player;
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		if (bPlayer != null && bPlayer.getElements().isEmpty())
		{
			redirect = true;
		}	
	}

	public MenuItem getItemForMove(OfflinePlayer player, final String move, final int index)
	{
		Material mat;
		Element mainElement = CoreAbility.getAbility(move).getElement();
		if (mainElement == Element.AVATAR) {
			mat = Config.avatarIcon;
		} else {
			mat = Config.elementIcons.get(mainElement);
		}
		
		if (mainElement instanceof SubElement) 
		{
			mainElement = ((SubElement)mainElement).getParentElement();
		}
		
		final ChatColor c = mainElement == Element.AIR ? ChatColor.GRAY : (mainElement == Element.CHI ? ChatColor.GOLD : (mainElement == Element.EARTH ? ChatColor.GREEN : (mainElement == Element.FIRE ? ChatColor.RED : (mainElement == Element.WATER ?  ChatColor.BLUE : (mainElement == Element.AVATAR ? ChatColor.LIGHT_PURPLE : mainElement.getColor())))));
		
		MenuItem item = new MenuItem(c + move, mat) {

			@Override
			public void onClick(Player player) 
			{
				if (!GeneralMethods.abilityExists(move))
				{
					player.sendMessage(new LangBuilder("Display.Errors.NoAbilName").toString());
					BendingGUI.log.warning("[BendingGUI] Error: Move selected with invalid ID/Name. Move name: \"" + move + "\". Please contact StrangeOne101 about this!");
					closeMenu(player);
					return;
				}
				
				if (mode == Mode.INFO && move != null)
				{
					//player.sendMessage(ChatColor.YELLOW + "Info for " + c);
					//String s = CoreAbility.getAbility(move).getElement() == Element.Air ? "Air" : (CoreAbility.getAbility(move).getElement() == Element.Earth ? "Earth" : (CoreAbility.getAbility(move).getElement() == Element.Water ? "Water" : (CoreAbility.getAbility(move).getElement() == Element.Fire ? "Fire" : (CoreAbility.getAbility(move).getElement() == Element.Chi ? "Chiblocker" : "Other"))));
					//Tools.sendMessage(player, c + s + "." + move.toString());
					player.sendMessage(c + CoreAbility.getAbility(move).getDescription());
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
				DynamicUpdater.updateMenu(thePlayer, getInstance());
			}
		};
		String desc = "";
		String moveDesc = GRAY + LangBuilder.getAbilityDescription(CoreAbility.getAbility(move)).toString();
		List<String> l = BendingGUI.getDescriptions(moveDesc, GRAY, 45);
		for (String s : l)
		{
			desc = desc + s + "\n";
		}
		if (move != null && this.mode == Mode.DELETE)
		{
			desc = desc + "\n\n" + new LangBuilder("Display.Main.Items.Remove.AbilityLore").toString();
		}
		else if (move != null && this.mode == Mode.INFO)
		{
			desc = desc + "\n\n" + new LangBuilder("Display.Main.Items.Info.AbilityLore").toString();
		}
		else if (this.abilityIndex == move)
		{
			desc = desc + "\n\n" + new LangBuilder("Display.Main.Items.Bind.AbilityLore").toString();
		}
		item.setDescriptions(Arrays.asList(desc.split("\n")));
		if (abilityIndex == move)
		{
			item.setEnchanted(true);
		}

		return item;
	}

	public MenuItem getItemForSlot(OfflinePlayer player, final int index)
	{
		Material mat = Material.BLACK_STAINED_GLASS_PANE;
		final String move = this.getMoveForSlot(player, index + 1);
		ChatColor c = ChatColor.RED;
		if (move != null && !move.equals("null"))
		{
			Element element = CoreAbility.getAbility(move).getElement();
			if (element instanceof SubElement) element = ((SubElement)element).getParentElement();
			c = element == Element.AIR ? ChatColor.GRAY : (element == Element.CHI ? ChatColor.GOLD : (element == Element.EARTH ? ChatColor.GREEN : (element == Element.FIRE ? ChatColor.RED : (element == Element.WATER ? ChatColor.BLUE : (element == Element.AVATAR ? ChatColor.LIGHT_PURPLE : element.getColor())))));
			if (element == Element.AIR) {mat = Material.WHITE_STAINED_GLASS_PANE;}
			else if (element == Element.EARTH) {mat = Material.LIME_STAINED_GLASS_PANE;}
			else if (element == Element.FIRE) {mat = Material.RED_STAINED_GLASS_PANE;}
			else if (element == Element.WATER) {mat = Material.BLUE_STAINED_GLASS_PANE;}
			else if (element == Element.CHI) {mat = Material.YELLOW_STAINED_GLASS_PANE;}
			else {mat = Material.PURPLE_STAINED_GLASS_PANE;}
		}
		final ChatColor c1 = c;
		
		
		String itemname, desc = "";
		
		if (move == null || move.equals("null"))
		{
			itemname = new LangBuilder("Display.Main.Items.Slot.Empty.Title").slot(index + 1).toString();
			desc = new LangBuilder("Display.Main.Items.Slot.Empty.Lore").slot(index + 1).toString();
		}
		else
		{
			itemname = new LangBuilder("Display.Main.Items.Slot.Full.Title").slot(index + 1).ability(CoreAbility.getAbility(move)).toString();
			desc = new LangBuilder("Display.Main.Items.Slot.Full.Lore").slot(index + 1).ability(CoreAbility.getAbility(move)).toString();
		}
		

		if (thePlayer instanceof Player)
		{
			if (MultiAbilityManager.playerAbilities.containsKey((Player)player))
			{
				desc = desc + "\n\n" + new LangBuilder("Display.Main.Items.Slot.Disabled.Multi");
			}
			else if (move != null && mode == Mode.DELETE)
			{
				desc = desc + "\n\n" + new LangBuilder("Display.Main.All.Lore.Remove").ability(CoreAbility.getAbility(move)).slot(index + 1);
			}
			else if (move != null && mode == Mode.INFO)
			{
				desc = desc + "\n\n" + new LangBuilder("Display.Main.All.Lore.Info").ability(CoreAbility.getAbility(move)).toString();
			}
			else if (this.slotIndex == index)
			{
				desc = desc + "\n\n" + new LangBuilder("Display.Main.All.Lore.Selected").slot(index + 1);
			}
		}
		else
		{
			desc = desc + "\n\n" + new LangBuilder("Display.Main.All.Lore.Offline").player(thePlayer);
		}
		
		MenuItem item = new MenuItem(itemname, mat) {

			@Override
			public void onClick(Player player) 
			{
				if (MultiAbilityManager.playerAbilities.containsKey(player))
				{
					closeMenu(player);
					player.sendMessage(new LangBuilder("Display.Errors.CantEditNow").toString());
				}
				else if (thePlayer instanceof Player)
				{
					if (mode == Mode.DELETE && move != null)
					{
						//BendingPlayer.getBendingPlayer(thePlayer.getName()).setAbility(index, null);
						BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(thePlayer.getName());
						HashMap<Integer, String> abilities = bPlayer.getAbilities();
						abilities.remove(index + 1);
						bPlayer.setAbilities(abilities);
						GeneralMethods.saveAbility(bPlayer, index + 1, null);
						//GeneralMethods.bindAbility(thePlayer, null, index);
						player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Bind.Remove").ability(CoreAbility.getAbility(move)).slot(index + 1).toString());
						update();
						return;
					}
					else if (mode == Mode.INFO && move != null)
					{
						//player.sendMessage(ChatColor.YELLOW + "Info for " + c1 + move.toString());
						//String s = CoreAbility.getAbility(move).getElement() == Element.Air ? "Air" : (CoreAbility.getAbility(move).getElement() == Element.Earth ? "Earth" : (CoreAbility.getAbility(move).getElement() == Element.Water ? "Water" : (CoreAbility.getAbility(move).getElement() == Element.Fire ? "Fire" : (CoreAbility.getAbility(move).getElement() == Element.Chi ? "Chiblocker" : "Other"))));
						//Tools.sendMessage(player, c1 + s + "." + move.toString());
						player.sendMessage(c1 + CoreAbility.getAbility(move).getDescription());
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
					DynamicUpdater.updateMenu(thePlayer, getInstance());
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
		Material material = Material.BARRIER;

		String key = "Display.Main.Items.Remove." + (this.mode == Mode.DELETE ? "On" : "Off");
		MenuItem item = new MenuItem(ChatColor.RED + new LangBuilder(key + ".Title").toString(), material) {

			@Override
			public void onClick(Player player) 
			{
				if (this.isShiftClicked())
				{
					BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(thePlayer.getName());
					HashMap<Integer, String> abilities = new HashMap<Integer, String>();
					bPlayer.setAbilities(abilities);
					player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Bind.RemoveAll").toString());
					mode = mode == Mode.DELETE ? Mode.NONE : mode;
					for (int i = 1; i <= 9; i++) {
						GeneralMethods.saveAbility(bPlayer, i, null);
					}
				}
				else
				{
					mode = mode == Mode.DELETE ? Mode.NONE : Mode.DELETE;
				}
				update();
				DynamicUpdater.updateMenu(thePlayer, getInstance());
			}
		};
		item.addDescription(new LangBuilder(key + ".Lore").toString());
		if (this.mode == Mode.DELETE)
		{
			item.setEnchanted(true);
		}
		return item;
	}
	
	/**Toggles the bending board*/
	public MenuItem getBBToggle(Player player)
	{
		final boolean b = !BendingBoard.isToggled(player);
		Material material = b ? Material.LIME_DYE : Material.GRAY_DYE;
		String key = "Display.Main.Items.Board." + (b ? "On" : "Off") + ".";
		//String s = (b ? ChatColor.GREEN : ChatColor.RED) + "Toggle BendingBoard " + GRAY + (b ? "(ACTIVE)" : "(INACTIVE)");
		MenuItem item = new MenuItem(new LangBuilder(key + "Title").toString(), material) {

			@Override
			public void onClick(Player player) 
			{
				if (thePlayer instanceof Player) {
					//BendingBoard.toggle((Player) thePlayer);
					BendingBoard.toggle((Player) thePlayer);
					update();
					DynamicUpdater.updateMenu(thePlayer, getInstance());
				}
				else {
					player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Board.Offline").player(thePlayer).toString());
				}
				
			}
		};
		item.addDescription(ChatColor.GRAY + new LangBuilder(key + "Lore").toString());
		return item;
	}
	
	/**Shows lots of info for moves when you click them*/
	public MenuItem getInfoToolItem(OfflinePlayer player)
	{
		Material material = Material.getMaterial("SIGN");
		if (material == null)
		{
			material = Material.getMaterial("OAK_SIGN");
		}

		String key = "Display.Main.Items.Info." + (this.mode == Mode.INFO ? "On" : "Off");
		MenuItem item = new MenuItem(ChatColor.YELLOW + new LangBuilder(key + ".Title").toString(), material) {

			@Override
			public void onClick(Player player) 
			{
				mode = mode == Mode.INFO ? Mode.NONE : Mode.INFO;
				update();
				DynamicUpdater.updateMenu(thePlayer, getInstance());
			}
		};
		item.addDescription(ChatColor.GRAY + new LangBuilder(key + ".Lore").toString());
		if (this.mode == Mode.INFO)
		{
			item.setEnchanted(true);
		}
		return item;
	}
	
	/**The arrow for moving pages*/
	public MenuItem getPageArrow(OfflinePlayer player, final boolean isRightDirection)
	{
		Material material = Material.ARROW;
		String baseKey = "Display.Main.Items.Page." + (isRightDirection ? "Next" : "Previous");
		String s = new LangBuilder(baseKey + ".Title").page(this.movePage + 1, this.getMaxPages()).toString();
		//String s = ChatColor.YELLOW + (isRightDirection ? "Next Page " : "Previous Page ");

		//s = s + GRAY + "(" + ChatColor.YELLOW + (this.movePage + 1) + GRAY + "/" + ChatColor.YELLOW + this.getMaxPages() + GRAY + ")";
		MenuItem item = new MenuItem(s, material) {

			
			@Override
			public void onClick(Player player) 
			{
				//Import not to use the player variable from this method. If you do, the menu will change
				//to that players which stops functionality for admins looking at other player's bindings
				if (movePage == getMaxPages() - 1 && isRightDirection) return;
				movePage = isRightDirection ? movePage + 1 : movePage - 1;
				if (isShiftClicked) {
					movePage = isRightDirection ? getMaxPages() - 1 : 0;
				}
				if (combos) DynamicUpdater.setComboPage(thePlayer, movePage);
				else DynamicUpdater.setPage(thePlayer, movePage);
				
				//player.sendMessage("Combos: " + DynamicUpdater.getComboPage(thePlayer));
				//player.sendMessage("Moves: " + DynamicUpdater.getPage(thePlayer));
				//DynamicUpdater.getData(thePlayer).setPage(movePage);
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
				DynamicUpdater.updateMenu(thePlayer, getInstance());
			}
		};
		item.setDescriptions(Arrays.asList(new LangBuilder(baseKey + ".Lore").toString().split("\\n")));
		return item;
	}
	
	public MenuItem getEditElements()
	{
		final MenuBendingOptions instance = this;
		final boolean b = this.openPlayer.hasPermission("bending.admin.add");
		String key = "Display.Main.Items." + (b ? "Edit" : "Change");
		MenuItem item = new MenuItem(ChatColor.YELLOW + new LangBuilder(key + ".Title").toString(), Material.NETHER_STAR) {
			@Override
			public void onClick(Player player) 
			{
				if (b && (player.hasPermission("bending.admin.add") || player.hasPermission("bending.admin.remove")))
				{
					switchMenu(player, new MenuEditElements(thePlayer, instance));
				}
				else if (b)
				{
					player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Edit.Admin.NoPermission").toString());
					closeMenu(player);
				}
				else if (!b && player.hasPermission("bending.command.rechoose"))
				{
					switchMenu(player, new MenuSelectElement(thePlayer, instance));
				}
				DynamicUpdater.updateMenu(thePlayer, getInstance());
			}
		};
		if (b)
		{
			item.addDescription(ChatColor.GRAY + new LangBuilder("Display.Main.Items.Edit.Lore").yourOrPlayer(thePlayer, openPlayer).toString());
		}
		else
		{
			item.addDescription(ChatColor.GRAY + new LangBuilder("Display.Main.Items.Change.Lore").toString());
		}
		
		return item;
	}
	
	public MenuItem getComboItem()
	{
		MenuItem item = new MenuItem(ChatColor.YELLOW + new LangBuilder("Display.Main.Items.Combos.Title").toString(), Material.MAGMA_CREAM) {
			@Override
			public void onClick(Player player) 
			{
				combos = !combos;
				movePage = combos ? DynamicUpdater.getComboPage(thePlayer) : DynamicUpdater.getPage(thePlayer);
				update();
			}
		};
		item.addDescription(ChatColor.GRAY + new LangBuilder("Display.Main.Items.Combos.Lore").toString());
		if (combos) item.setEnchanted(true);
		return item;
	}

	public MenuItem getBendingToggle()
	{
		boolean isToggled = !BendingPlayer.getBendingPlayer(this.thePlayer).isToggled();
		Material mat = isToggled ? Material.LIME_WOOL : Material.RED_WOOL;
		final OfflinePlayer p = this.thePlayer;
		String key = "Display.Main.Items.Toggle." + (isToggled ? "Off" : "On") + ".";
		MenuItem item = new MenuItem(new LangBuilder(key + "Title").toString(), mat) {
			@Override
			public void onClick(Player player)
			{
				if (openPlayer != thePlayer)
				{
					if (!player.hasPermission("bending.admin.toggle"))
					{
						player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Toggle.Admin.NoPermission").toString());
						return;
					}
					BendingPlayer.getBendingPlayer(thePlayer).toggleBending();

					if (isToggled) {
						player.sendMessage(ChatColor.GREEN + new LangBuilder("Chat.Toggle.Admin.On").player(thePlayer).plural(thePlayer.getName()).toString());
					} else {
						player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Toggle.Admin.Off").player(thePlayer).plural(thePlayer.getName()).toString());
					}
					return;
				}


				
				hasBeenToggled = BendingPlayer.getBendingPlayer(p.getName()).isToggled();
				
				BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(p.getName());

				if (isToggled) {
					player.sendMessage(ChatColor.GREEN + new LangBuilder("Chat.Toggle.Player.On").player(thePlayer).plural(thePlayer.getName()).toString());
				} else {
					player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Toggle.Player.Off").player(thePlayer).plural(thePlayer.getName()).toString());
				}

				bPlayer.toggleBending();
				
				update();
				DynamicUpdater.updateMenu(thePlayer, getInstance());
				//Bukkit.getServer().dispatchCommand(p, "bending toggle");
			}
		};
		item.addDescription(ChatColor.GRAY + new LangBuilder(key + "Lore").toString());
		return item;
	}

	public MenuItem getPlayerStats()
	{
		final MenuBendingOptions menu1 = this;
		//String string = thePlayer.getName() != openPlayer.getName() ? ChatColor.YELLOW + thePlayer.getName() + "'s Bending" : ChatColor.YELLOW + "Your bending";
		String key = "Display.Main.Items.Overview";
		MenuItem item = new MenuItem(ChatColor.YELLOW + new LangBuilder(key + ".Title." + (thePlayer == openPlayer ? "Self" : "Other")).player(thePlayer).plural(thePlayer.getName()).toString(), Material.PLAYER_HEAD) {

			@Override
			public void onClick(Player player) 
			{
				if (player.hasPermission("bendinggui.view"))
				{
					switchMenu(player, new MenuPlayers(menu1));
				}
			}
		};
		
		BendingPlayer p = BendingPlayer.getBendingPlayer(thePlayer.getName());
		if ((thePlayer instanceof Player && ((Player)thePlayer).hasPermission("bending.avatar")) || p.hasElement(Element.AVATAR))
		{
			boolean b = p.hasElement(Element.EARTH) || p.hasElement(Element.AIR);
			item.addDescription(Element.AVATAR.getColor() + new LangBuilder(key + ".Lore.Avatar" + (thePlayer == openPlayer ?  "Self" : "")).player(thePlayer).plural(thePlayer.getName()).toString());
			item.addDescription(GRAY + new LangBuilder(key + ".Lore.AvatarElements").yourOrPlayer(thePlayer, openPlayer).anOrA(b ? "airOrEarth" : "").capitalizeFirst().toString());
			//item.addDescription(GRAY + (thePlayer.getName() != openPlayer.getName() ? "They" : "You") + " are currently a" + (b ? "n" : "") + ":");
		}
		/*else
		{
			boolean b = BendingPlayer.getBendingPlayer(this.thePlayer.getName()).hasElement(Element.EARTH) || BendingPlayer.getBendingPlayer(this.thePlayer.getName()).hasElement(Element.AIR);
			item.addDescription((thePlayer.getName() != openPlayer.getName() ? GRAY + "They" : ChatColor.GRAY + "You" ) + " are currently a" + (b ? "n" : "") + ":");
		}*/
		
	
		if (p.getElements().contains(Element.AIR)) 
		{
			String s = GRAY + "- " + ChatColor.GRAY + new LangBuilder("Display.Main.Items.Overview.Element.Air");
			if (Config.showSubElementsOnUser) 
			{
				List<String> list = new ArrayList<String>();
				if (p.hasSubElement(SubElement.FLIGHT)) {
					list.add(new LangBuilder("Display.Main.Items.Overview.Element.Flight").toString());
				}
				if (p.hasSubElement(SubElement.SPIRITUAL)) {
					list.add(new LangBuilder("Display.Main.Items.Overview.Element.SpiritualProjection").toString()); //Spiritually
				}
				for (SubElement sub : Element.getAddonSubElements(Element.AIR)) {
					if (p.hasElement(sub)) {
						list.add(new LangBuilder("Display.Main.Items.Overview.Element." + sub.getName()).toString());
					}
				}
				if (list.size() > 0)
					s = s + GRAY + " " + new LangBuilder("Display.Main.Items.Overview.Lore.ElementList").list(list.toArray(new String[0]));
				List<String> l = BendingGUI.getDescriptions(s, GRAY, 65);
				for (String s1 : l) {
					item.addDescription(s1);
				}
			} else {
				item.addDescription(s);
			}
		}
		if (p.getElements().contains(Element.EARTH)) 
		{
			String s = GRAY + "- " + ChatColor.GREEN + new LangBuilder("Display.Main.Items.Overview.Element.Earth");
			if (Config.showSubElementsOnUser) 
			{
				List<String> list = new ArrayList<String>();
				if (p.hasSubElement(Element.METAL)) 
				{
					list.add(new LangBuilder("Display.Main.Items.Overview.Element.Metal").toString());
				}
				if (p.hasSubElement(Element.LAVA)) 
				{
					list.add(new LangBuilder("Display.Main.Items.Overview.Element.Lava").toString());
				}
				if (p.hasSubElement(Element.SAND)) 
				{
					list.add(new LangBuilder("Display.Main.Items.Overview.Element.Sand").toString());
				}
				for (SubElement sub : Element.getAddonSubElements(Element.EARTH)) {
					if (p.hasElement(sub)) {
						list.add(new LangBuilder("Display.Main.Items.Overview.Element." + sub.getName()).toString());
					}
				}
				if (list.size() > 0)
					s = s + GRAY + " " + new LangBuilder("Display.Main.Items.Overview.Lore.ElementList").list(list.toArray(new String[0]));
				
				List<String> l = BendingGUI.getDescriptions(s, GRAY, 65);
				for (String s1 : l) {
					item.addDescription(s1);
				}
			} else {
				item.addDescription(s);
			}
		}
		if (p.getElements().contains(Element.FIRE)) 
		{
			String s = GRAY + "- " + ChatColor.RED + new LangBuilder("Display.Main.Items.Overview.Element.Fire");
			if (Config.showSubElementsOnUser) 
			{
				List<String> list = new ArrayList<String>();
				if (p.hasSubElement(Element.LIGHTNING)) 
				{
					list.add(new LangBuilder("Display.Main.Items.Overview.Element.Lightning").toString());
				}
				if (p.hasSubElement(Element.COMBUSTION)) 
				{
					list.add(new LangBuilder("Display.Main.Items.Overview.Element.Combustion").toString());
				}
				if (Config.hasBlueFire && p.hasSubElement(Element.BLUE_FIRE))
				{
					list.add(new LangBuilder("Display.Main.Items.Overview.Element.BlueFire").toString());
				}
				for (SubElement sub : Element.getAddonSubElements(Element.FIRE)) {
					if (p.hasElement(sub)) {
						list.add(new LangBuilder("Display.Main.Items.Overview.Element." + sub.getName()).toString());
					}
				}
				if (list.size() > 0)
					s = s + GRAY + " " + new LangBuilder("Display.Main.Items.Overview.Lore.ElementList").list(list.toArray(new String[0]));

				List<String> l = BendingGUI.getDescriptions(s, GRAY, 65);
				for (String s1 : l) {
					item.addDescription(s1);
				}
			} else {
				item.addDescription(s);
			}
			
		}
		if (p.getElements().contains(Element.WATER)) 
		{
			String s = GRAY + "- " + ChatColor.BLUE + new LangBuilder("Display.Main.Items.Overview.Element.Water");
			if (Config.showSubElementsOnUser) 
			{
				List<String> list = new ArrayList<String>();
				if (p.hasSubElement(Element.BLOOD)) 
				{
					list.add(new LangBuilder("Display.Main.Items.Overview.Element.Blood").toString());
				}
				if (p.hasSubElement(Element.PLANT)) 
				{
					list.add(new LangBuilder("Display.Main.Items.Overview.Element.Plant").toString());
				}
				if (p.hasSubElement(Element.HEALING)) 
				{
					list.add(new LangBuilder("Display.Main.Items.Overview.Element.Healing").toString());
				}
				for (SubElement sub : Element.getAddonSubElements(Element.WATER)) {
					if (p.hasElement(sub)) {
						list.add(new LangBuilder("Display.Main.Items.Overview.Element." + sub.getName()).toString());
					}
				}
				if (list.size() > 0)
					s = s + GRAY + " " + new LangBuilder("Display.Main.Items.Overview.Lore.ElementList").list(list.toArray(new String[0]));

				List<String> l = BendingGUI.getDescriptions(s, GRAY, 65);
				for (String s1 : l) {
					item.addDescription(s1);
				}
			} else {
				item.addDescription(s);
			}
			
		}
		if (p.getElements().contains(Element.CHI)) 
		{
			String s = GRAY + "- " + ChatColor.GOLD + new LangBuilder("Display.Main.Items.Overview.Element.Chi").toString();
			item.addDescription(s);
		}
		for (Element element : Element.getAddonElements()) {
			String s = GRAY + "- " + new LangBuilder("Display.Main.Items.Overview.Element." + element.getName()).toString();
			item.addDescription(s);
		}
		if (!Util.getStaff(thePlayer.getUniqueId()).equals("")) {
			item.addDescription(Util.getStaff(thePlayer.getUniqueId()));
		}
		
		if (this.openPlayer.hasPermission("bendinggui.admin"))
		{
			item.addDescription("");
			item.addDescription(ChatColor.YELLOW + "" + new LangBuilder("Display.Main.Items.Overview.Lore.AdminWho"));
		}
		else if (this.openPlayer.hasPermission("bending.command.who"))
		{
			item.addDescription("");
			item.addDescription(ChatColor.YELLOW + "" + new LangBuilder("Display.Main.Items.Overview.Lore.Who"));
		}
		/*NBTTagCompound tag = item.getNBTData();
		tag.setString("SkullOwner", thePlayer.getName());
		item.setNBTData(tag);*/
		return item;
	}

	public MenuItem getItemForCombo(OfflinePlayer player, final String move, final int index)
	{
		Material mat;
		Element mainElement = getComboElement(move);

		if (mainElement == Element.AVATAR) {
			mat = Config.avatarIcon;
		} else {
			mat = Config.elementIcons.get(mainElement);
		}

		if (mainElement instanceof SubElement) 
		{
			mainElement = ((SubElement)mainElement).getParentElement();
		}
		
		final ChatColor c = mainElement == Element.AIR ? ChatColor.GRAY : (mainElement == Element.CHI ? ChatColor.GOLD : (mainElement == Element.EARTH ? ChatColor.GREEN : (mainElement == Element.FIRE ? ChatColor.RED : (mainElement == Element.WATER ?  ChatColor.BLUE : (mainElement == Element.AVATAR ? ChatColor.LIGHT_PURPLE : mainElement.getColor())))));
		CoreAbility coreAbility = CoreAbility.getAbility(move);

		MenuItem item = new MenuItem(new LangBuilder("Display.Main.Items.ComboAbility.Title").ability(CoreAbility.getAbility(move)).toString(), mat) {

			@Override
			public void onClick(Player player) 
			{
				if (!GeneralMethods.abilityExists(move))
				{
					player.sendMessage(ChatColor.DARK_RED + new LangBuilder("Display.Errors.NoAbilName").toString());
					BendingGUI.log.warning("[BendingGUI] Error: Move selected with invalid ID/Name. Move name: \"" + move + "\". Please contact StrangeOne101 about this!");
					closeMenu(player);
					return;
				}
				

				ChatColor color = coreAbility != null ? coreAbility.getElement().getColor() : null;
				String usage = ConfigManager.languageConfig.get().getString("Commands.Help.Usage");
				player.sendMessage(new LangBuilder("Chat.Combo.Help").ability(coreAbility).toString());
				player.sendMessage(color + ComboManager.getDescriptions().get(move));
				player.sendMessage(ChatColor.GOLD + usage + ComboManager.getInstructions().get(move));
				closeMenu(player);
				return;
			}
		};
		String desc = "";
		String moveDesc = GRAY + LangBuilder.getAbilityDescription(coreAbility).toString();
		List<String> l = BendingGUI.getDescriptions(moveDesc, GRAY, 45);
		
		String desc2 = ComboManager.getInstructions().get(move);
		if (desc2 != null) {
			for (String s : ChatColor.stripColor(desc2).split(" > ")) {
				l.add(ChatColor.GRAY + "> " + s);
			}
		}
		
		for (String s : l)
		{
			desc = desc + s + "\n";
		}
		desc = desc + "\n" + new LangBuilder("Display.Main.All.Lore.Info").ability(coreAbility);
		
		item.setDescriptions(Arrays.asList(desc.split("\n")));
		
		
		return item;
	}
	
	/**Returns the move based on the slot*/
	public String getMoveForSlot(OfflinePlayer player, int index)
	{
		try {
		return BendingPlayer.getBendingPlayer(player).getAbilities().get(index);
		} catch (NullPointerException e) {return null;}
	}
	
	/**Bind the move to the slot. Args: Move, slot. Slot should be an int from 0 to 8*/
	public void bindMoveToSlot(Player player, String move, int slot)
	{
		//BendingGUI.log.info("Player: " + player + ", Move: " + move + ", Slot: " + slot);
		if (slot >= 9)
		{
			openPlayer.sendMessage(ChatColor.RED + new LangBuilder("Display.Errors.SlotOutOfRange").toString());
			this.closeMenu(openPlayer);
			return;
		}
		
		BendingPlayer.getBendingPlayer(player.getName()).getAbilities().put(slot + 1, move);
		GeneralMethods.saveAbility(BendingPlayer.getBendingPlayer(player.getName()), slot + 1, move);
		//GeneralMethods.bindAbility(player, move, slot + 1);
		Element e = CoreAbility.getAbility(move).getElement();
		if (e instanceof SubElement) e = ((SubElement)e).getParentElement();
		//ChatColor c = e == Element.AIR ? ChatColor.GRAY : (e == Element.CHI ? ChatColor.GOLD : (e == Element.EARTH ? ChatColor.GREEN : (e == Element.FIRE ? ChatColor.RED : (e == Element.WATER ? ChatColor.BLUE : ChatColor.LIGHT_PURPLE))));
		player.sendMessage(new LangBuilder("Chat.Bind.Ability").ability(CoreAbility.getAbility(move)).slot(slot + 1).toString());
		BendingBoard.updateBoard(player);
		//player.sendMessage(c + move.toString() + ChatColor.YELLOW + " bound to slot " + (slot + 1) + "!");
	}
	
	
	/**Updates the GUI by re-initializing*/
	public void update()
	{
		OfflinePlayer player = this.thePlayer;
		if (combos && mode == Mode.INFO) mode = Mode.NONE;
		
		
		//BendingGUI.log.log(Level.INFO, "Player: " + player.getDisplayName() + ",Page: " + movePage + ", MaxPage: " + this.getMaxPages() + ", MoveSize: " + playerMoves.size());
		//this.getInventory().clear();
		
		for (int i = 0; i < this.getInventory().getContents().length; i++)
		{
			if (this.getInventory().getContents()[i] == null)
			{
				this.getInventory().clear(i);
			}
			else if (this.getInventory().getContents()[i].getType() != Material.PLAYER_HEAD)
			{
				this.getInventory().clear(i);
			}
		}
		this.playerMoves.clear();
		this.playerCombos.clear();
		
		if (BendingPlayer.getBendingPlayer(player).getElements() == null || BendingPlayer.getBendingPlayer(player).getElements().isEmpty()) //Somehow this needs to be here
		{
			this.switchMenu(openPlayer, new MenuSelectElement(thePlayer));
			return;
		}
		else if (!BendingPlayer.getBendingPlayer(player).isToggled() || this.hasBeenToggled)
		{
			for (int i = 0; i < 18; i++)
			{
				MenuItem disabledSlot = new MenuItem(ChatColor.RED + new LangBuilder("Display.Main.Items.Slot.Disabled.Toggled").toString(), Material.GRAY_STAINED_GLASS_PANE) {
					@Override
					public void onClick(Player player) {}		
				};
				disabledSlot.addDescription(ChatColor.GRAY + new LangBuilder("Display.Main.Items.Slot.Disabled.ToggledLore").toString());
				this.addMenuItem(disabledSlot, i);
			}
			//this.addMenuItem(MenuItems.bendingRebind_ToggleBendingGreen, 8, 3);
			this.addMenuItem(this.getBendingToggle(), 8, 3);
		}
		else //If not, start adding what's needed
		{
			//this.addMenuItem(MenuItems.bendingRebind_ToggleBendingRed, 8, 3);
			this.addMenuItem(this.getBendingToggle(), 8, 3);
			
			
			if (combos) {
				HashMap<Element, List<String>> abilities = new HashMap<Element, List<String>>();
				
				for (String name : ComboManager.getComboAbilities().keySet())
				{
					if ((thePlayer instanceof Player) || ((!(thePlayer instanceof Player)) && !this.playerCombos.contains(name)))
					{
						//this.playerMoves.add(move);
						Element element = getComboElement(name);
						if (element == null) continue;
						if (!abilities.containsKey(element))
						{
							abilities.put(element, new ArrayList<String>());
						}
						if (canBendCombo(name, (Player) thePlayer)) {
							abilities.get(element).add(name);
						}
						
					}
				}
				
				for (Element element : BendingGUI.elementOrder)
				{
					List<String> abilities_ = abilities.get(element);
					if (abilities_ == null || abilities_.isEmpty()) continue;
					for (String ab : abilities_)
					{
						this.playerCombos.add(ab);
					}
				}
			} else {
				HashMap<Element, List<CoreAbility>> abilities = new HashMap<Element, List<CoreAbility>>();
				
				mainloop: for (CoreAbility ability : CoreAbility.getAbilities())
				{
					if ((player instanceof Player && BendingPlayer.getBendingPlayer(player).canBind(ability)) || ((!(player instanceof Player)) && !this.playerMoves.contains(ability.getName())))
					{
						//this.playerMoves.add(move);
						if (ability.isHiddenAbility()) continue;
						if (ComboManager.getComboAbilities().containsKey(ability.getName()) || ability instanceof ComboAbility) continue;
						if (!abilities.containsKey(ability.getElement()))
						{
							abilities.put(ability.getElement(), new ArrayList<CoreAbility>());
						}
						
						for (CoreAbility ability2 : abilities.get(ability.getElement())) {
							if (ability2.getName().equals(ability.getName())) {
								continue mainloop;
							}
						}
						abilities.get(ability.getElement()).add(ability);
					}
				}
				
				for (Element element : BendingGUI.elementOrder)
				{
					List<CoreAbility> abilities_ = abilities.get(element);
					if (abilities_ == null || abilities_.isEmpty()) continue;
					List<String> abilitylist = new ArrayList<String>();
					for (CoreAbility ab : abilities_)
					{
						abilitylist.add(ab.getName());
					}
					
					Collections.sort(abilitylist);
					this.playerMoves.addAll(abilitylist);
					
					
				}
				
				//Hopefully fix bug
				if (this.playerMoves.isEmpty())
				{
					/*if (!DynamicUpdater.getData(player).abilityData.isEmpty())
					{
						//this.playerMoves = DynamicUpdator.getData(player).abilityData;
						//DynamicUpdator.updateAbilityData(player);
					}
					else
					{
						*/BukkitRunnable run = new BukkitRunnable() {

							public void run() 
							{
								update();
							}	
						};
						run.runTaskLater(BendingGUI.INSTANCE, 200L);
						 //Try again
					//}
				} 
				/*else
				{
					DynamicUpdater.getData(player).abilityData = this.playerMoves;
				}*/
				
				
				
				
			}
			
			
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
			
			int maxPage = this.getMaxPages();
			
			int p = combos ? DynamicUpdater.getComboPage(thePlayer) : DynamicUpdater.getPage(thePlayer);
			if (p > this.getMaxPages())
			{
				p = this.getMaxPages() - 1;
			}
			movePage = p;
			
			//The first index for the move to get from the list
			int firstMoveIndex = (movePage == 0 ? 0 : movePage * 7 + 1);
			int firstIndex_ = movePage == 0 ? 0 : 1;
			
			int end; //Last index to use
			
			int size = combos ? this.playerCombos.size() : this.playerMoves.size();
		    
		    // Find the end of the relevant part of the list.
		    if (movePage >= maxPage - 1) end = size;
		    else if (movePage == 0) end = 8;
		    else end = firstMoveIndex + 7;
		    
		    if (end >= size) end = size;
		 
		    for (int i = firstMoveIndex; i < end; ++i) {
		    	int slotIndex = movePage == 0 ? i : (i - firstMoveIndex + firstIndex_);
		    	
		    	MenuItem item;
		    	if (combos) {
		    		String move = this.playerCombos.get(i);
			        item = this.getItemForCombo(player, move, slotIndex);
		    	} else {
		    		String move = this.playerMoves.get(i);
			        item = this.getItemForMove(player, move, slotIndex);
		    	}
		        
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
			if (this.openPlayer.hasPermission("bending.command.add") || this.openPlayer.hasPermission("bending.command.remove") || this.openPlayer.hasPermission("bending.command.rechoose"))
			{
				this.addMenuItem(this.getEditElements(), 3 * 9 + 4);
			}
		}
		
		this.addMenuItem(this.getComboItem(), 1, 3);
		
		if (this.getInventory().getContents()[3 * 9 + 7] == null)
		{
			MenuItem infoTool = this.getInfoToolItem(player);
			this.addMenuItem(infoTool, 3 * 9 + 7);
			
			this.addMenuItem(this.getPlayerStats(), 0, 3);
			SkullMeta meta = (SkullMeta) this.getInventory().getItem(27).getItemMeta();
			meta.setOwningPlayer(this.thePlayer);
			this.getInventory().getItem(27).setItemMeta(meta);
		}
		
		if (BendingBoard.isBoardEnabled() && thePlayer instanceof Player) {
			this.addMenuItem(getBBToggle((Player) thePlayer), 3, 3);
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
	/*public void addNBTData(int x, int y, String key, Object data)
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
	}*/
	
	@Override
	public void openMenu(Player player) 
	{
		this.openPlayer = player;
		this.playerMoves = new ArrayList<String>();
		this.playerCombos = new ArrayList<String>();
		
		if (this.redirect)
		{
			if (!player.hasPermission("bending.command.choose")) 
			{
				player.sendMessage(new LangBuilder("Chat.Choose.CantChoose").toString());
				return;
			}
			player.sendMessage(new LangBuilder("Chat.Choose.ChooseNow").toString());
			this.switchMenu(player, new MenuSelectElement(thePlayer));
			return;
		}
		
		int p = combos ? DynamicUpdater.getComboPage(thePlayer) : DynamicUpdater.getPage(thePlayer);
		if (p > this.getMaxPages())
		{
			p = this.getMaxPages();
		}
		this.movePage = p;
		
		update();
		super.openMenu(player);
	}
	
	
	@Override
	public void closeMenu(Player player) 
	{
		//this.playerMoves = null;
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
		int i = combos ? this.playerCombos.size() : this.playerMoves.size();
	    if (i <=  9) return 1;
	    if (i <= 16) return 2;
	    return ((i - 3) / 7) + 1;
	}
	
	public MenuBendingOptions getInstance()
	{
		return this;
	}
	
	public OfflinePlayer getOpenPlayer()
	{
		return openPlayer;
	}
	
	public OfflinePlayer getMenuPlayer()
	{
		return thePlayer;
	}
	
	public enum Mode
	{
		NONE, DELETE, INFO
	}
	
	/**Transfers important information over from the argument menu to this current menu*/
	public void updateFromMenu(MenuBendingOptions menu)
	{
		this.abilityIndex = menu.abilityIndex;
		this.abilityIndexInt = menu.abilityIndexInt;
		this.hasBeenToggled = menu.hasBeenToggled;
		this.mode = menu.mode;
		this.movePage = menu.movePage;
		this.combos = menu.combos;
		this.slotIndex = menu.slotIndex;
	}
	
	public static Element getComboElement(String combo) {
		if (!ComboManager.getDescriptions().containsKey(combo)) return null; //Hidden Combo
		ComboAbilityInfo info = ComboManager.getComboAbilities().get(combo);
		
		CoreAbility abil = CoreAbility.getAbility(info.getAbilities().get(0).getAbilityName());
		if (abil == null) return null;
		Element element = abil.getElement();
		
		for (int i = 1; i < info.getAbilities().size(); i++) {
			CoreAbility curAbil = CoreAbility.getAbility(info.getAbilities().get(1).getAbilityName());
			if (curAbil == null) break;
			if (curAbil.getElement() instanceof SubElement && !(element instanceof SubElement)) {
				element = curAbil.getElement();
			} else if (curAbil.getElement() instanceof SubElement && element instanceof SubElement) {
				SubElement sub1 = (SubElement) curAbil.getElement();
				SubElement sub2 = (SubElement) element;
				if (sub1.getParentElement() != sub1.getParentElement()) {
					element = Element.AVATAR;
					break;
				} else if (sub1 != sub2) {
					element = sub1.getParentElement();
				}
			} else if (!(curAbil.getElement() instanceof SubElement) && !(element instanceof SubElement) && element != curAbil.getElement()) {
				element = Element.AVATAR;
				break;
			}
		}
		return element;
	}
	
	public static boolean canBendCombo(String combo, Player player) {
		if (!ComboManager.getDescriptions().containsKey(combo)) return false; //Hidden Combo
		ComboAbilityInfo info = ComboManager.getComboAbilities().get(combo);
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		for (AbilityInformation abi : info.getAbilities()) {
			if (CoreAbility.getAbility(abi.getAbilityName()) == null || !bPlayer.canBind(CoreAbility.getAbility(abi.getAbilityName()))) {
				return false;
			}
		}
		return true;
	}
}
