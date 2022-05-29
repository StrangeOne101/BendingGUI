package com.strangeone101.bendinggui.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.PassiveAbility;
import com.strangeone101.bendinggui.BendingGUI;
import com.strangeone101.bendinggui.api.ChooseSupport;
import com.strangeone101.bendinggui.api.ElementSupport;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigLanguage extends ConfigBase {
	
	private static ConfigLanguage INSTANCE;

	public ConfigLanguage() {
		super("language.yml");
		
		this.load();
		
		INSTANCE = this;
	}

	@Override
	public Map<String, Object> addDefaults() {
		Map<String, Object> defaults = new HashMap<>();

		defaults.put("Item.Title", "&aConfigure Bending");
		defaults.put("Item.Lore", "&7Right click to configure your bending!");

		defaults.put("Generic.Yourself", "yourself");
		defaults.put("Generic.You", "you");
		defaults.put("Generic.Your", "your");
		defaults.put("Generic.Their", "their");
		defaults.put("Generic.NoElementBending", " element");
		defaults.put("Generic.A", "a");
		defaults.put("Generic.An", "an");
		defaults.put("Generic.They", "they");
		defaults.put("Generic.List1", "{item}");
		defaults.put("Generic.List2", "{item} and {item}");
		defaults.put("Generic.List3", "{item}, {item} and {item}");
		defaults.put("Generic.List4", "{item}, {item}, {item} and {item}");

		defaults.put("Display.Choose.Title", "Please select an element!");
		defaults.put("Display.Choose.NoPerm", "You don't have permission to choose this element!");
		defaults.put("Display.Choose.PermRemoved.Self", "You cannot choose an element because your bending has been permanently removed!");
		defaults.put("Display.Choose.PermRemoved.Admin", "This player has had their bending permanently removed!");
		defaults.put("Display.Choose.Confirm.Title", "Choose {element}?");
		defaults.put("Display.Choose.Confirm.Yes.Title", "&a&lYES");
		defaults.put("Display.Choose.Confirm.Yes.Lore", "&7Are you sure you want to choose {elementcolor}{element}&7? This cannot be changed!");
		defaults.put("Display.Choose.Confirm.No.Title", "&c&lNO");
		defaults.put("Display.Choose.Confirm.No.Lore", "&7Return to the previous menu");
		defaults.put("Display.Choose.Fire.Title", "&cChoose &4&l{ELEMENT}");
		defaults.put("Display.Choose.Fire.Lore", "&7Firebenders can create fire with their bare fists. "
				+ "They are more prone to fire based damage and in some cases are completely fire resistant. "
				+ "Some skilled firebenders can even create lightning, making them very dangerous.");
		defaults.put("Display.Choose.Water.Title", "&9Choose &1&l{ELEMENT}");
		defaults.put("Display.Choose.Water.Lore", "&7Waterbenders can manipulate anything with water in them, "
				+ "including ice, plants and of course, water. They can freeze and thaw ice at will, and can "
				+ "also swim extremely fast in water.");
		defaults.put("Display.Choose.Earth.Title", "&aChoose &2&l{ELEMENT}");
		defaults.put("Display.Choose.Earth.Lore", "&7Earthbenders can manipulate almost anything natrual from "
				+ "the earth. Earthebenders take no fall damage while landing on blocks they can bend and some"
				+ " skilled benders can even bend sand, metal or lava!");
		defaults.put("Display.Choose.Air.Title", "&fChoose &7&l{ELEMENT}");
		defaults.put("Display.Choose.Air.Lore", "&7Airbenders can manipulate air at will, making them extremely "
				+ "fast and agile and have a powerful connection with spirits. They can also jump higher, run "
				+ "faster and they take no fall damage when they hit the ground.");
		defaults.put("Display.Choose.Chi.Title", "&eChoose &6&lCHIBLOCKER");
		defaults.put("Display.Choose.Chi.Lore", "&7Chiblocking isn't stricly an element but it is a form of art "
				+ "that makes the user faster and more agile than other benders. Chiblockers can paralyze other "
				+ "benders, take their bending away and can fight extremely well.");

		defaults.put("Display.Players.Title", "Other Player's Bending");
		defaults.put("Display.Players.Back.Title", "&eReturn to bending menu");
		defaults.put("Display.Players.Back.Lore", "&7Click to return to bending menu");
		defaults.put("Display.Players.You", "&e{player} &8(You)");
		defaults.put("Display.Players.Online", "&e{player}");
		defaults.put("Display.Players.Offline", "&7{player}");
		defaults.put("Display.Players.NonBender", "&7(Non-bender)");
		defaults.put("Display.Players.Admin.Click", "&c&lCLICK TO VIEW BENDING");
		defaults.put("Display.Players.Admin.ChooseClick", "&c&lCLICK TO CHOOSE THEIR BENDING");
		defaults.put("Display.Players.ToggleOffline.On.Title", "&cShow Offline Players");
		defaults.put("Display.Players.ToggleOffline.Off.Title", "&cHide Offline Players");
		defaults.put("Display.Players.ToggleOffline.On.Lore", "&7Click to show offline players");
		defaults.put("Display.Players.ToggleOffline.Off.Lore", "&7Click to hide offline players");

		defaults.put("Display.Presets.Title", "Preset Selector");
		defaults.put("Display.Presets.Slot.Full.Title", "{abilitycolor}Slot {slot} &7({abilitycolor}{ability}&7)");
		defaults.put("Display.Presets.Slot.Full.Lore", "&7Currently bound: {abilitycolor}{ability}");
		defaults.put("Display.Presets.Slot.Empty.Title", "&cSlot {slot} &7(Empty)");
		defaults.put("Display.Presets.Slot.Empty.Lore", "&7Nothing is currently bound to this slot!");
		defaults.put("Display.Presets.Preset.Title", "&c{presetcolor}{preset}");
		defaults.put("Display.Presets.Preset.Lore", "&e> {slot1}\\n&e> {slot2}\\n&e> {slot3}\\n&e> {slot4}\\n&e> {slot5}\\n&e> {slot6}\\n&e> {slot7}\\n&e> {slot8}\\n&e> {slot9}");
		defaults.put("Display.Presets.Preset.BindLore", "&e&lClick to bind this preset!");
		defaults.put("Display.Presets.Preset.DeleteLore", "&c&lClick to delete this preset!");
		defaults.put("Display.Presets.Preset.BoundLore", "&a&lCurrently bound");
		defaults.put("Display.Presets.Preset.Empty", "&8(Empty)");
		defaults.put("Display.Presets.Global.Title", "&9Server Preset: &c{presetcolor}{preset}");
		defaults.put("Display.Presets.Global.Lore", "&7This is a preset defined by the server\\n\\n&e> {slot1}\\n&e> {slot2}\\n&e> {slot3}\\n&e> {slot4}\\n&e> {slot5}\\n&e> {slot6}\\n&e> {slot7}\\n&e> {slot8}\\n&e> {slot9}");
		defaults.put("Display.Presets.Global.Empty", "&8(Empty)");
		defaults.put("Display.Presets.Global.BindLore", "&e&lClick to bind this preset!");
		defaults.put("Display.Presets.Global.DeleteLore", "&cYou can't delete a global preset!");
		defaults.put("Display.Presets.Global.BoundLore", "&aCurrently bound");
		defaults.put("Display.Presets.Create.Title", "&eCreate a new preset");
		defaults.put("Display.Presets.Create.Lore", "&7Make a new preset based on {their|your} current binds");
		defaults.put("Display.Presets.Create.Confirm.Title", "Create preset '{preset}'?");
		defaults.put("Display.Presets.Create.Confirm.Yes.Title", "&a&lYES");
		defaults.put("Display.Presets.Create.Confirm.Yes.Lore", "&7This will create a new preset using {their|your} current binds");
		defaults.put("Display.Presets.Create.Confirm.No.Title", "&c&lNO");
		defaults.put("Display.Presets.Create.Confirm.No.Lore", "&7Return to the preset menu");
		defaults.put("Display.Presets.Delete.On.Title", "&cDelete a preset &7(&eOn&7)");
		defaults.put("Display.Presets.Delete.On.Lore", "&7Delete an existing preset forever\\n\\n&c&lCLICK A PRESET TO DELETE");
		defaults.put("Display.Presets.Delete.Off.Title", "&cDelete a preset &7(Off)");
		defaults.put("Display.Presets.Delete.Off.Lore", "&7Delete an existing preset forever\\n\\n&eClick this and then click a preset to delete it");
		defaults.put("Display.Presets.Delete.Confirm.Title", "Delete preset '{preset}'?");
		defaults.put("Display.Presets.Delete.Confirm.Yes.Title", "&a&lYES");
		defaults.put("Display.Presets.Delete.Confirm.Yes.Lore", "&7This will delete the preset forever! (A very long time!)");
		defaults.put("Display.Presets.Delete.Confirm.No.Title", "&c&lNO");
		defaults.put("Display.Presets.Delete.Confirm.No.Lore", "&7Return to the preset menu");
		defaults.put("Display.Presets.Empty.Title", "&7No Presets :(");
		defaults.put("Display.Presets.Empty.Lore", "&8You don't have any presets. Want to make some?");
		defaults.put("Display.Presets.Max.Title", "&eCreate Preset");
		defaults.put("Display.Presets.Max.Lore", "&7You cannot create anymore presets! You must\\n&7delete an existing one before another one\\n&7can be created");

		defaults.put("Display.Edit.Title", "Edit {player|your} elements");
		defaults.put("Display.Edit.Element.Title", "{elementcolor}&l{element}");
		defaults.put("Display.Edit.Element.Lore.Has", "&7Click to take away &e{player|your} {elementcolor}{element}{bending}&7!");
		defaults.put("Display.Edit.Element.Lore.HasNot", "&7Click to make &e{player|yourself} &7{a} {elementcolor}{element}{bender}&7!");
		defaults.put("Display.Edit.Back.Title", "&eBack");
		defaults.put("Display.Edit.Back.Lore", "&7Return to the previous menu");
		defaults.put("Display.Edit.RemoveAll.Title", "&cRemove All Elements");
		defaults.put("Display.Edit.RemoveAll.Lore", "&eThis will remove all {player|your} existing elements");
		defaults.put("Display.Edit.RemoveAll.Confirm.Title", "Are you sure?");
		defaults.put("Display.Edit.RemoveAll.Confirm.Yes.Title", "&a&lYES");
		defaults.put("Display.Edit.RemoveAll.Confirm.Yes.Lore", "&7Are you sure you want to remove all {player|your} elements?");
		defaults.put("Display.Edit.RemoveAll.Confirm.No.Title", "&c&lNO");
		defaults.put("Display.Edit.RemoveAll.Confirm.No.Lore", "&7Return to the element menu");
		
		defaults.put("Display.Main.Title", "Bending Options");
		defaults.put("Display.Main.Info.On.Title", "&eMove Help Tool &7(On)");
		defaults.put("Display.Main.Info.Off.Title", "&eMove Help Tool &7(Off)");
		defaults.put("Display.Main.Info.On.Lore", "&7When toggled on, click on an ability for more information.\\n&7Click to turn off again");
		defaults.put("Display.Main.Info.Off.Lore", "&7When toggled on, click on an ability for more information.\\n&7Click to turn on");
		defaults.put("Display.Main.Remove.AbilityLore", "&c&lTOGGLE THE REMOVAL TOOL BEFORE\\nREBINDING!\\n&r&7You must turn off the unbind tool before you\\ncan rebind moves again");
		defaults.put("Display.Main.Remove.On.Title", "&cRemoval Tool &7(On)");
		defaults.put("Display.Main.Remove.Off.Title", "&cRemoval Tool &7(Off)");
		defaults.put("Display.Main.Remove.On.Lore", "&7Allows you to remove bound moves to {their|your} slots.\\n&7Click to turn off again\\n&7SHIFT click to remove all your bound moves");
		defaults.put("Display.Main.Remove.Off.Lore", "&7Allows you to remove bound moves to {their|your} slots.\\n&7Click to turn on\\n&7SHIFT click to remove all your bound moves");
		defaults.put("Display.Main.Bind.AbilityLore", "&a&lCURRENTLY SELECTED\\n&r&7Click a slot to bind to this move to!");
		defaults.put("Display.Main.Edit.Title", "&eAdd/Remove Elements");
		defaults.put("Display.Main.Edit.Lore", "&7Edit the elements {player|you} can bend");
		defaults.put("Display.Main.Change.Title", "&eChange Element");
		defaults.put("Display.Main.Change.Lore", "&7Change your main bending element");
		defaults.put("Display.Main.Change.LoreTime", "&7You can change your element in: {time}");

		defaults.put("Display.Main.Combos.Title", "&eView Combos");
		defaults.put("Display.Main.Combos.Lore", "&7View the available combos");
		defaults.put("Display.Main.Presets.Title", "&eView Presets");
		defaults.put("Display.Main.Presets.Lore", "&7View, bind or create presets for {their|your} binds");
		defaults.put("Display.Main.ComboAbility.Title", "{abilitycolor}{ability} (Combo)");
		defaults.put("Display.Main.Toggle.On.Title", "&cDisable Bending");
		defaults.put("Display.Main.Toggle.Off.Title", "&aEnable Bending");
		defaults.put("Display.Main.Toggle.On.Lore", "&7Click to disable {their|your} bending");
		defaults.put("Display.Main.Toggle.Off.Lore", "&7Click to re-enable {their|your} bending");
		defaults.put("Display.Main.Board.On.Title", "&aToggle BendingBoard &7(ACTIVE)");
		defaults.put("Display.Main.Board.Off.Title", "&cToggle BendingBoard &7(INACTIVE)");
		defaults.put("Display.Main.Board.On.Lore", "&7Click to toggle the bending board");
		defaults.put("Display.Main.Board.Off.Lore", "&7Click to toggle the bending board");
		defaults.put("Display.Main.Overview.Title.Self", "&eYour Bending");
		defaults.put("Display.Main.Overview.Title.Others", "&e{player}'{s} Bending");
		defaults.put("Display.Main.Overview.Lore.Who", "&e&lClick to view other players bending");
		defaults.put("Display.Main.Overview.Lore.AdminWho", "&e&lClick to view and edit other \\n&e&lplayers bending");
		defaults.put("Display.Main.Overview.Lore.AvatarSelf", "&5You are the avatar!");
		defaults.put("Display.Main.Overview.Lore.Avatar", "&5They are the avatar!");
		defaults.put("Display.Main.Overview.Lore.ElementPrefix", "&7{they|you} are currently {a}:");
		defaults.put("Display.Main.Overview.Lore.SubList", "(Can {list})");
		defaults.put("Display.Main.Overview.Element.Air", "&7Airbender");
		defaults.put("Display.Main.Overview.Element.Earth", "&aEarthbender");
		defaults.put("Display.Main.Overview.Element.Water", "&9Waterbender");
		defaults.put("Display.Main.Overview.Element.Fire", "&cFirebender");
		defaults.put("Display.Main.Overview.Element.Chi", "&6Chiblocker");
		defaults.put("Display.Main.Overview.Element.Flight", "Fly");
		defaults.put("Display.Main.Overview.Element.SpiritualProjection", "Spiritually Project");
		defaults.put("Display.Main.Overview.Element.Sand", "Sandbend");
		defaults.put("Display.Main.Overview.Element.Metal", "Metalbend");
		defaults.put("Display.Main.Overview.Element.Lava", "Lavabend");
		defaults.put("Display.Main.Overview.Element.Lightning", "use Lightning");
		defaults.put("Display.Main.Overview.Element.Combustion", "Combust");
		defaults.put("Display.Main.Overview.Element.BlueFire", "use Blue Fire");
		defaults.put("Display.Main.Overview.Element.Blood", "Bloodbend");
		defaults.put("Display.Main.Overview.Element.Plant", "Plantbend");
		defaults.put("Display.Main.Overview.Element.Ice", "");
		defaults.put("Display.Main.Overview.Element.Healing", "Heal");

		defaults.put("Display.Main.Slot.Full.Title", "{abilitycolor}Slot {slot} &7({abilitycolor}{ability}&7)");
		defaults.put("Display.Main.Slot.Full.Lore", "&7Currently bound: {abilitycolor}{ability}\\n\\n&7To bind a new move, click a move then click\\n&7the slot you want to bind it to.");
		defaults.put("Display.Main.Slot.Empty.Title", "&cSlot {slot} &7(Empty)");
		defaults.put("Display.Main.Slot.Empty.Lore", "&7Nothing is currently bound to this slot!\\n\\n&7Click a move and click a slot to bind!");
		defaults.put("Display.Main.Slot.Disabled.Multi", "&c&lYOU CANNOT EDIT YOUR BINDS RIGHT NOW!\\n&r&7You are using a multi-ability move and must stop\\n&7using it before you can bind again!");
		defaults.put("Display.Main.Slot.Disabled.Toggled", "&cBending is disabled!");
		defaults.put("Display.Main.Slot.Disabled.ToggledLore", "&7Enable bending to use again!");

		defaults.put("Display.Main.Slot.Info", "&e&lCLICK FOR MORE INFO!\\n&r&7Click to display more information about this move!");
		defaults.put("Display.Main.Slot.Remove", "&c&lCLICK TO REMOVE!\\n&r&7Click to remove {ability} from this slot!");
		defaults.put("Display.Main.Slot.Selected", "&a&lCURRENTLY SELECTED!\\n&r&7Click a move to bind to this slot!");
		defaults.put("Display.Main.Slot.Offline", "&c&lCANNOT MODIFY BENDING OF OFFLINE PLAYERS!\\n&r&7You can't modify the bending of players that are offline!");
		defaults.put("Display.Main.Ability.Selected", "&a&lCURRENTLY SELECTED\\n&r&7Click a slot to bind to this move to!");
		defaults.put("Display.Main.Ability.Remove", "&c&lTOGGLE THE REMOVAL TOOL BEFORE\\n&c&lREBINDING!\\n&r&7You must turn off the unbind tool before you\\n&7can rebind moves again");
		defaults.put("Display.Main.Ability.Info", "&e&lCLICK FOR MORE INFO!\\n&r&7Click to display more information about this move!");

		defaults.put("Display.Common.Page.Next.Title", "&eNext Page &7(&e{current}&7/&e{max}&7)");
		defaults.put("Display.Common.Page.Next.Lore", "&7Click to go to the next page of moves");
		defaults.put("Display.Common.Page.Previous.Title", "&ePrevious Page &7(&e{current}&7/&e{max}&7)");
		defaults.put("Display.Common.Page.Previous.Lore", "&7Click to go to the previous page of moves");
		defaults.put("Display.Common.Page.Back.Title", "&eReturn to bending menu");
		defaults.put("Display.Common.Page.Back.Lore", "&7Click to return to the bending menu");

		//Generic plugin errors
		defaults.put("Display.Errors.NoAbilName", "&4Error: &cMove doesn't exist! Please contact StrangeOne101 about this!");
		defaults.put("Display.Errors.SlotOutOfRange", "&cError: Slot binding out of range! Please contact StrangeOne101 about this!");
		defaults.put("Display.Errors.FailedToOpen", "&cAn error occurred while trying to open the bending interface. Please report this to your admin or the plugin developer!");
		defaults.put("Display.Errors.NoAbilityDescription", "&7\u272F \u272F \u272F This move doesn't have a description set! Bug your server owner about it! \u272F \u272F \u272F");
		defaults.put("Display.Errors.ClickEvent", "&cAn error occurred while processing the clickevent. Please report this to your admin or the plugin developer!");
		defaults.put("Display.Errors.CantEditNow", "&cYou cannot edit your binds right now!");
		defaults.put("Display.Errors.Disabled", "&cThere is a problem with BendingGUI at the moment. Please contact your admin!");
		defaults.put("Display.Errors.NoTouchy", "&cThe bending gui cannot be tampered with!");

		defaults.put("Display.Errors.DupePreset", "&cYou already have a preset named {preset}!");
		defaults.put("Display.Errors.MaxPresets", "&cYou cannot create anymore than {max} presets!");
		defaults.put("Display.Errors.NoBinds", "&cYou cannot create a preset without any binds!");
		defaults.put("Display.Errors.InvalidPresetName", "&cInvalid preset name! It must have no spaces or special characters!");

		defaults.put("Chat.Choose.CantChoose", "&cYou must have an element to modify your bending!");
		defaults.put("Chat.Choose.ChooseNow", "&aYou aren't a bender yet! Please choose an element!");
		defaults.put("Chat.Choose.Success.Self", "&eYou are now {a} {elementcolor}{element}{bender}&e!");
		defaults.put("Chat.Choose.Success.Admin", "&e{player} is now {a} {element}{bender}&e!");
		defaults.put("Chat.Choose.Rechoose.NoPermission", "&cYou don't have permission to change your element!");
		defaults.put("Chat.Choose.NoPermissionElement", "&cYou don't have permission to choose this element!");
		defaults.put("Chat.Choose.PermaRemoved", "&cYou cannot choose an element because your bending has been permanently removed!");
		defaults.put("Chat.Choose.ChangeCooldown", "&cYou cannot change your element for another {time}!");
		defaults.put("Chat.Choose.Admin.PermaRemoved", "&cThis player has had their bending permanently removed!");
		defaults.put("Chat.Choose.Admin.NoPermission", "&cYou don't have permission to choose this player's bending!");
		defaults.put("Chat.Board.Offline", "&aCan't toggle an offline player's bending board.");
		defaults.put("Chat.Toggle.Admin.NoPermission", "&cYou don't have permission to toggle this player's bending!");
		defaults.put("Chat.Toggle.Admin.On", "&aYou toggled {player}'{s} bending on!");
		defaults.put("Chat.Toggle.Admin.Off", "&cYou toggled {player}'{s} bending off!");
		defaults.put("Chat.Toggle.Player.On", "&aYour bending has been toggled back on.");
		defaults.put("Chat.Toggle.Player.Off", "&cYour bending has been toggled off. You will not be able to use most abilities until you toggle it back.");
		defaults.put("Chat.Toggle.NoPermission", "&cYou don't have permission to toggle your bending bending!");
		defaults.put("Chat.Combo.Help", "{abilitycolor}{ability} (Combo) -");
		defaults.put("Chat.Bind.Ability", "{abilitycolor}{ability} &ebound to slot {slot}!");
		defaults.put("Chat.Bind.Remove", "&cRemoved {abilitycolor}{ability}&c from slot {slot}!");
		defaults.put("Chat.Bind.RemoveAll", "&cRemoved all bound moves from slots!");
		defaults.put("Chat.Edit.Add.Self", "&eYou are now {a} {elementcolor}{element}{bender}&e!");
		defaults.put("Chat.Edit.Add.Admin", "&e{player} is now {a} {elementcolor}{element}{bender}&e!");
		defaults.put("Chat.Edit.Remove.Self", "&eYour {elementcolor}{element}{bending}&e has been removed!");
		defaults.put("Chat.Edit.Remove.Admin", "&e{player} is no longer {a} {elementcolor}{element}{bender}&e!");
		defaults.put("Chat.Edit.RemoveAll.Self", "&cYour elements have been removed! You will need to choose a new element to bend again!");
		defaults.put("Chat.Edit.RemoveAll.Admin", "&c{player}'{s} elements have been removed!");
		defaults.put("Chat.Edit.NoPermission", "&cYou don't have permission to edit this player's bending!");
		defaults.put("Chat.Edit.Admin.Offline", "&cYou can't edit an offline players bending right now!");
		defaults.put("Chat.Presets.Bind", "&eYou have bound your {presetcolor}{preset} &epreset!");
		defaults.put("Chat.Presets.BindFailed", "&eYou have bound your {presetcolor}{preset} &epreset!");
		defaults.put("Chat.Presets.Delete", "&eYou deleted {player|your} {presetcolor}{preset} &epreset!");
		defaults.put("Chat.Presets.Create.Prompt", "&ePlease enter the name for {player|your} new preset bellow, or type \"cancel\"!");
		defaults.put("Chat.Presets.Create.CancelInput", "cancel");
		defaults.put("Chat.Presets.Create.Success", "&aNew preset &e{presetcolor}{preset}&a created!");
		defaults.put("Chat.Presets.Create.Timeout", "&cPreset creation canceled due to timeout/inactivity");
		defaults.put("Chat.Presets.DupePreset", "&cYou already have a preset named {preset}!");
		defaults.put("Chat.Presets.MaxPresets", "&cYou cannot create anymore than {max} presets!");
		defaults.put("Chat.Presets.NoBinds", "&cYou cannot create a preset without any binds!");
		defaults.put("Chat.Presets.InvalidPresetName", "&cInvalid preset name! It must have no spaces or special characters!");
		defaults.put("Chat.Presets.CantBind", "&cCannot bind a preset filled with abilities you cannot use! Did you change element?");
		defaults.put("Chat.Command.PlayerOnly", "&cSorry bud! Only players can run this command!");
		defaults.put("Chat.Command.NoPermission", "&cYou don't have permission to run this command!");
		defaults.put("Chat.Command.NoEditPermission", "&cYou don't have permission to edit other players bending!");
		defaults.put("Chat.Command.GiveItem", "&aRight click the compass to configure your bending!");
		defaults.put("Chat.Command.NoPlayer", "&cError while finding player!");
		defaults.put("Chat.Command.Reload", "&eBendingGUI Reloaded!");
		defaults.put("Chat.Command.Usage", "&eCommand usage is /gui or /gui <choose/version/reload> or /gui [player]");
		defaults.put("Chat.Command.AlreadyChosen", "&cYou have already chosen an element!");
		defaults.put("Chat.Command.Version", "&eBendingGUI is version {version}, for ProjectKorra version {pkversion}");
		defaults.put("Chat.Command.VersionWarning", "&cSupport for this version of ProjectKorra is not guaranteed.");

		defaults.put("Staff.Developer", "&5ProjectKorra Developer");
		defaults.put("Staff.Contributor", "&5ProjectKorra Contributor");
		defaults.put("Staff.Admin", "&4ProjectKorra Administrator");
		defaults.put("Staff.Mist", "&4ProjectKorra Founder");
		
		Map<String, String> abilities = new HashMap<>();

		abilities.put("AirBlast", "Releases a blast of air that pushing all mobs and items");
		abilities.put("AirBubble", "Allows the user to keep a bubble of air around them while traveling underwater");
		abilities.put("AcrobatStance", "Makes the users faster and stronger but uses more energy in the process");
		abilities.put("AirBurst", "Creates a powerful gust of air that can blow away your enemies");
		abilities.put("AirScooter", "Allows the user to ride a ball of air to scale across terrain fast");
		abilities.put("AirSpout", "Allows the user to walk on a spout of air");
		abilities.put("AirSuction", "Pulls all mobs and items towards the the user");
		abilities.put("AirSwipe", "Releases a wider gust of air, pushing mobs and items");
		abilities.put("AirShield", "Shield yourself from everything using air");
		abilities.put("Flight", "Allows the user to fly freely in the sky");
		abilities.put("Suffocate", "Bends the air right out of another player's lungs");
		abilities.put("Tornado", "Creates a tornado of air that will misplace other users");	
		
		abilities.put("Bloodbending", "Allows the user to manipulate other players and mobs with their bending");
		abilities.put("HealingWaters", "Allows the user to heal with water");	
		abilities.put("IceBlast", "Blasts a chunk of ice towards your target");	
		abilities.put("IceSpike", "Pulls a spike of ice up from bellow your enemy");	
		abilities.put("OctopusForm", "Creates a octopus of water around the user");	
		abilities.put("PhaseChange", "Allows the user to freeze and melt water");
		abilities.put("Surge", "Creates a wave or a shield of water and ice");	
		abilities.put("Torrent", "Creates a ring of water around the user which can be used to freeze targets or push them away");	
		abilities.put("WaterArms", "Allows the user to use water to create arms with lots of different abilities");
		abilities.put("WaterBubble", "Allows the user to travel underwater with a bubble of air around them");
		abilities.put("WaterManipulation", "Allows the user to fire water at a target");
		abilities.put("WaterSpout", "Allows the user to stand on a spout of water");
		
		abilities.put("Blaze", "Releases a powerful ring of fire around you");
		abilities.put("Combustion", "Creates a fire-like projectile with their the user's mind");
		abilities.put("FireBlast", "Blasts a ball of fire towards your enemy");	
		abilities.put("FireBurst", "Sets fire to everything around you");	
		abilities.put("FireJet", "Allows the user to fly for short distances with fire");	
		abilities.put("FireShield", "Allows the user to create a shield of fire in front of them");	
		abilities.put("HeatControl", "Allows the user to put out fire and not burn");	
		abilities.put("Illumination", "Allows the bender to see by holding a flame");	
		abilities.put("Lightning", "Allows the user to release a strike of lightning");
		
		abilities.put("Catapult", "Allows the user to catapult themselves through the air by launching themselves up from the earth");
		abilities.put("Collapse", "Collapses or pulls down the earth");
		abilities.put("EarthArmor", "Uses earth as armor for short periods of time");
		abilities.put("EarthBlast", "Blasts a chunk of earth wherever the user wants it to go");
		abilities.put("EarthGrab", "Allows the user to trap other mobs and players in earth");
		abilities.put("EarthSmash", "Allows the user to grab a huge chunk of earth and throw it");
		abilities.put("EarthTunnel", "Allows the user to tunnel through the earth");	
		abilities.put("RaiseEarth", "Creates walls or columns of earth in front of the user");
		abilities.put("SandSpout", "Creates a spout of sand for the user to stand on while blinding users bellow");
		abilities.put("Shockwave", "Releases a powerful shockwave of earth that sends targets flying");
		abilities.put("Tremorsense", "Allows the user to see nearby airpockets (caves)");	
		
		abilities.put("Extraction", "Allows the user to extract metals directly from the ore");
		abilities.put("LavaFlow", "Turns earth into a pool or lava or creates a ring of lava around the user");	
		abilities.put("MetalClips", "Allows the user to fire slices of metal at a target and capture them");	
		
		abilities.put("HighJump", "Makes the user jump high in the air");	
		abilities.put("Paralyze", "Allows the user to paralyze other benders and block their bending");	
		abilities.put("QuickStrike", "Allows the user to attack quickly with a chance to chi block the target");	
		abilities.put("RapidPunch", "Allows the user to attack faster while dealing more damage");	
		abilities.put("Smokescreen", "Releases smoke and blinds all nearby players");	
		abilities.put("SwiftKick", "Damages the target with a high chance of blocking their chi, if the user is in the air");	
		abilities.put("WarriorStance", "Makes the user's attacks more powerful but also makes the user more vulnerable");
		
		abilities.put("AvatarState", "Users in the Avatar State are much more powerful than normal benders. Their bending power is multiplied heavily and they take far less damage from attacks.");
		
		//Jedcore Abilities
		abilities.put("AirBlade", "Creates a powerful blade of air that damages mobs and players");
		abilities.put("AirBreath", "Releases a powerful breath of air that knocks back your target");
		abilities.put("AirGlide", "Allows the user to glide down safely instead of falling");
		abilities.put("AirPunch", "Allows the user to punch in rapid succession with blasts of air");
		abilities.put("Meditate", "Allows the user to become stronger for short periods of time after meditating");
		abilities.put("SonicBlast", "Creates an ear-piecing sonicblast that does high damage to other mobs and players");
		abilities.put("FartBlast", "Allows the user to stink other players out by farting on them");
		
		abilities.put("EarthKick", "Allows the user to kick the earth and hurl chunks at their opponent");
		abilities.put("EarthLine", "Creates a line of risen earth that inflicts damage on all mobs and players that it hits");
		abilities.put("EarthShard", "Allows the user to pickup multiple chunks of earth and hurl them towards their target");
		abilities.put("EarthSurf", "Allows the user to ride on earth to scale the terrain fast");
		abilities.put("EarthPillar", "Pulls a pillar of earth out in whatever direction you look");
		abilities.put("MudSurge", "Hurls mud towards a target");
		abilities.put("SandBlast", "Blasts sand towards an enemy, temporarily blinding them");
		abilities.put("SandShift", "Allows the user to change sandstone without the need for crafting it");
		abilities.put("StatePhase", "Allows the bender to create sand from earth and vise versa");
		abilities.put("MetalFragments", "Allows the bender to shoot fragments of metal towards a target");
		abilities.put("MetalShred", "Allows the user to shred a metal wall to gain access through it");
		abilities.put("MetalHook", "Allows the bender to use metalbending to hook or grapple onto a surface and pull themselves in");
		abilities.put("MagnetShield", "Repels all metal objects hurling towards the user");
		abilities.put("LavaFlux", "Creates a wave of lava that can be hurled towards a target");
		abilities.put("LavaDisc", "Melts a block of earth into a disk of lava that can be thrown");
		abilities.put("Fissure", "Creates a line of lava that can swallow up mobs");
		abilities.put("LavaThrow", "Allows the user to hurl waves of lava at a target");
		
		abilities.put("FireBall", "Creates a fireball that can be cast towards a user");
		abilities.put("FireBreath", "Allows the user to breathe a powerful breath of fire");
		abilities.put("FirePunch", "Allows the user to damage and set fire to their enemy with just one punch");
		abilities.put("FireComet", "Charge up the power of an entire comet to throw towards your enemies");
		abilities.put("FireShots", "Allows the user to cast multiple and short fireballs towards a target");
		abilities.put("WallOfFire", "Creates wall of fire that blocks all incoming projectiles and mobs");
		abilities.put("Discharge", "Creates a powerful bolt of electricity to fry your targets");
		abilities.put("LightningBurst", "Allows the user to charge lightning and release it in all directions at once");
		
		abilities.put("Maelstrom", "Creates a whirlpool that sucks all mobs and players into it");
		abilities.put("Drain", "Fill up all bottles in your inventory from the water in plants around you!");
		abilities.put("WakeFishing", "Allows the user to use waterbending to fish");
		abilities.put("WaterGimbal", "Creates wall of fire that blocks all incoming projectiles and mobs");
		abilities.put("PlantDrain", "Drains all nearby plants of water to create water for waterbending");
		abilities.put("PlantWhip", "Creates a vine of leaves that can quickly whip a target");
		abilities.put("PlantArmor", "Allows the user to create basic armor for themselves with leaves");
		abilities.put("PlantBlast", "Allows the user to blast quick, plant projectiles at their target");
		abilities.put("IceClaws", "Creates ice on your hand that can damage and slow your target");
		abilities.put("IceWall", "Creates a wall of ice in front of the user");
		abilities.put("IceStream", "Creates an stream-like projectile that freezes and slows targets on impact");
		abilities.put("FrostBreath", "Allows the user to freeze their breath, damaging and slowing their targets");
		abilities.put("BloodPuppet", "Allows the manipulation of mobs and players to make them damage each other");
		
		abilities.put("DaggerThrow", "Allows the user to rapidfire arrows towards a target");
		abilities.put("Backstab", "Hit the user in their back to block their chi and deal a lot of damage");
		abilities.put("WallRun", "Click rapidly to run up walls");

		abilities.put("SpiritBeam", "Releases a powerful blast as the spirit of the avatar. The user must be in the AvatarState to use this move.");
		abilities.put("ElementSphere", "Creates a sphere of all the elements around the user, allowing them to use all four at once");
		
		abilities.put("LoonyBlast", "Use Loonergy to blast away enemies with extreme power!");
		abilities.put("ToonShield", "Shield yourself from any attack with the power of Loonergy!");
		abilities.put("BanBlaze", "Charge up a deadly admin-blast that 'bans' any user it hits!");
		abilities.put("OwnerState", "The avatar state, buffed with the power of an admin to the extreme!");

		//Spirit Element
		abilities.put("Agility", "Dash with left click or soar with sneak!");
		abilities.put("Possess", "Jump inside the body of another to possess and harm them");
		abilities.put("Vanish", "Vanish into thin air and reappear a distance away");
		abilities.put("Combo-Phase", "Dematerialize yourself to be able to phase through any kind of wall");

		//Light Spirits
		abilities.put("Shelter", "Create a safe haven for you or another player for a while");
		abilities.put("Alleviate", "Heal yourself or others by clearing all negative effects and giving regen and night vision");
		abilities.put("Orb", "Place an orb of positive energy that harms anything that crosses it");
		abilities.put("Combo-Rejuvenate", "Mark the ground with positive energy that heals others but damages dark spirits");

		//Dark Spirits
		abilities.put("Shackle", "Trap your attacker in a field of dark energy while you get away");
		abilities.put("Intoxicate", "Sacrifice your health to imbue your target with extremely toxic energy");
		abilities.put("Strike", "Rush up to your target and bite them. This will make you appear above the target instantly");
		abilities.put("Combo-Infest", "Mark the ground with chaotic energy that buffs monsters and harms everything else");

		//Water Spirit Abilities
		abilities.put("Corrupt", "Use waterbending to corrupt a light spirit");
		abilities.put("Purify", "Use waterbending to purify a dark spirit");

		//Spirits Complete Pack - Spirit
		abilities.put("Float", "Use your spirit powers to float in the air");
		abilities.put("Combo-Skyrocket", "Skyrocket yourself into the air before slamming back to earth with a deadly impact");

		//Spirits Complete Pack - Light
		abilities.put("Wish", "Use your positive energy to create a wish that will heal you later on");
		abilities.put("LightBeam", "Charge up your positive energy to release a deadly beam");
		abilities.put("Safeguard", "Create a personal shield to protect you as you move");
		abilities.put("Enlightenment", "Enlighten you and other light spirits in the area to power everyone up");
		abilities.put("Combo-Awakening", "Summon Light Spirits to your aid that will assist you against other Dark Spirits");
		abilities.put("Combo-Sanctuary", "Create a protective barrier that also buffs your fellow Light Spirits");

		//Spirits Complete Pack - Dark
		abilities.put("DarkBeam", "Charge up your negative energy to release a deadly beam");
		abilities.put("Onslaught", "Charge towards your target while corrupting them with your dark energy");
		abilities.put("Shadow", "Transform yourself into the darkness to evade everything as you blip through the darkness instantly");
		abilities.put("Corruption", "Summon Dark Spirits to spread corruption to the land and other spirits");
		abilities.put("Combo-Nightmare", "Infect everything around you with negative energy to debuff them for a while");
		abilities.put("Combo-Pandemonium", "Corrupts the free will of all entities around you by pulling them towards the center of this ability");

		abilities.put("CactusBlast", "Fight in the desert by throwing cactus!");
		abilities.put("Sandstorm", "Allows the user to whip up a sandstorm!");
		abilities.put("EarthBurrow", "Allows the user to burrow themselves in the ground");
		abilities.put("BloodRip", "Rip the blood out of living things with bloodbending");

		//JedCore combo abilities
		abilities.put("Combo-AirSlam", "Kick your enemy up in the air then kick them away!");
		abilities.put("Combo-AirStream", "Creates a current of air that can be controlled by the player for a while");
		abilities.put("Combo-AirSweep", "Create a sweeping current of air that can sweep enemies off their feet!");
		abilities.put("Combo-Twister", "Create a twister to suck up and blow away your opponents!");
		abilities.put("Combo-SwiftStream", "Pull all enemies along with you as you fly!");
		
		abilities.put("Combo-Crevice", "Creates a crevice in the ground that can swallow players!");
		abilities.put("Combo-MagmaBlast", "Fire balls of magma at your enemies!");
		abilities.put("Combo-EarthDome", "Surround yourself (or others) in earth for protection");
		abilities.put("Combo-EarthPillars", "Send players flying into the air by raising the earth bellow them suddenly");

		abilities.put("Combo-FireKick", "Create a small arc of fire from your feet!");
		abilities.put("Combo-FireSpin", "Create a huge ring of fire around you that does damage and huge knockback!");
		abilities.put("Combo-FireWheel", "Hurl a spinning wheel of fire towards your enemies!");
		abilities.put("Combo-JetBlaze", "Blaze it up as you launch from your powerful FireJet!");
		abilities.put("Combo-JetBlast", "Launch with a boom as your launch with a powerful FireJet!");
		
		abilities.put("Combo-WaterFlow", "Create a huge torrent of water that can sweep away your enemies!");
		abilities.put("Combo-Maelstrom", "Suck everyone and everything up in a deep whirlpool!");
		abilities.put("Combo-WaterGimbal", "Control two torrents of water at once!");
		abilities.put("Combo-IceBullet", "Make a dome of ice and that can shoot shards of ice!");
		abilities.put("Combo-IceWave", "Freeze your WaterWave to damage enemies that you hit!");
		
		abilities.put("Combo-Immobilize", "Freeze your enemies for a few seconds!");

		for (CoreAbility ability : CoreAbility.getAbilities()) {
			if (ability instanceof PassiveAbility || ability.isHiddenAbility()) continue;

			String name = ability.getName();

			if (ability instanceof ComboAbility) name = "Combo-" + name;

			if (!abilities.containsKey(name)) {
				defaults.put("Abilities." + name, ability.getName() + " placeholder here");
			} else {
				defaults.put("Abilities." + name, abilities.get(name));
			}
		}

		for (Element customSupport : BendingGUI.INSTANCE.getSupportedElements()) {
			ElementSupport support = BendingGUI.INSTANCE.getSupportedElement(customSupport);

			defaults.put("Display.Main.Overview.Element." + support.getElement().getName(), support.getLangOverviewName());

			if (support instanceof ChooseSupport) {
				defaults.put("Display.Choose." + support.getElement().getName() + ".Title", ((ChooseSupport) support).getLangChooseTitle());
				defaults.put("Display.Choose." + support.getElement().getName() + ".Lore", ((ChooseSupport) support).getLangChooseLore());
			}

		}
		
		return defaults;
	}
	
	public static ConfigLanguage getInstance() {
		return INSTANCE;
	}
	
	public void addDefaultDescription(CoreAbility ability, String description) {
		String name = ability.getName();
		if (ability instanceof ComboAbility) name = "Combo-" + name;
		
		addDefault("Abilities." + name, description);
	}

	@Override
	public void load() {
		if (!getFile().exists()) {
			File oldlang = new File(getFile().getParentFile(), "lang.yml");
			if (oldlang.exists()) {
				super.load();
				YamlConfiguration oldConfig = YamlConfiguration.loadConfiguration(oldlang);
				for (String key : oldConfig.getKeys(false)) {
					this.set("Abilities." + key, oldConfig.get(key));
				}
				try {
					BendingGUI.INSTANCE.getLogger().info("Imported old lang.yml data!");
					this.config.save(getFile());

					oldlang.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		super.load();
	}
}
