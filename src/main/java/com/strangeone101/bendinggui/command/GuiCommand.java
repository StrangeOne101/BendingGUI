package com.strangeone101.bendinggui.command;

import java.util.HashMap;
import java.util.List;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.strangeone101.bendinggui.LangBuilder;
import com.strangeone101.bendinggui.config.ConfigPresets;
import com.strangeone101.bendinggui.config.ConfigStandard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.command.PKCommand;
import com.strangeone101.bendinggui.BendingGUI;
import com.strangeone101.bendinggui.menus.MenuBendingOptions;
import com.strangeone101.bendinggui.menus.MenuSelectElement;
import org.bukkit.inventory.ItemStack;

public class GuiCommand extends PKCommand {

    public GuiCommand() {
        super("gui", "/bending gui [help/version/reload]", "Opens a menu that allows users to configure bending without commands", new String[]{"gui", "menu"});
    }

    public void execute(CommandSender arg0, List<String> arg1) {
        executeCommand(arg0, arg1);
    }

    public static boolean executeCommand(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player) && args.size() == 0) {
            sender.sendMessage(new LangBuilder("Chat.Command.PlayerOnly").toString());
            return true;
        }

        if (!sender.hasPermission("bendinggui.command")) {
            sender.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.NoPermission").toString());
            return true;
        }

        if (args.size() == 0) {
            Player player = (Player) sender;
            if (ConfigStandard.getInstance().doUseItem()) {
                ItemStack stack = ConfigStandard.getInstance().getItem();
                if (player.getInventory().contains(stack)) {
                    player.getInventory().remove(stack);
                }
                player.getInventory().addItem(stack);
                player.sendMessage(ChatColor.GREEN + new LangBuilder("Chat.Command.GiveItem").toString());
            } else {
                MenuBendingOptions menu = new MenuBendingOptions(player);
                menu.openMenu(player);
            }

            return true;
        } else if (args.get(0).equalsIgnoreCase("choose") || args.get(0).equalsIgnoreCase("c") || args.get(0).equalsIgnoreCase("ch")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(new LangBuilder("Chat.Command.PlayerOnly").toString());
                return false;
            }
            Player player = (Player) sender;
            if (args.size() == 2) {
                if (player.hasPermission("bending.admin.choose")) {
                    Player playero = Bukkit.getPlayer(args.get(1));
                    if (playero != null) {
                        MenuSelectElement menu = new MenuSelectElement(playero);
                        menu.openMenu(player);
                    } else {
                        player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.NoPlayer").toString());
                    }
                } else {
                    player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.NoPermission").toString());
                }
                return true;
            }

            if (BendingPlayer.getBendingPlayer(player).getElements().isEmpty() && player.hasPermission("bending.command.rechoose")) {
                MenuSelectElement menu = new MenuSelectElement(player);
                menu.openMenu(player);
            } else if (!player.hasPermission("bending.command.rechoose")) {
                player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.AlreadyChosen").toString());
            } else if (!player.hasPermission("bending.command.choose")) {
                player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.NoPermission").toString());
            }
        } else if (args.get(0).equalsIgnoreCase("version") || args.get(0).equalsIgnoreCase("v") || args.get(0).equalsIgnoreCase("ver")) {
            if (sender.hasPermission("bendinggui.version")) {
                sender.sendMessage(ChatColor.YELLOW + new LangBuilder("Chat.Command.Version").version(BendingGUI.INSTANCE.getDescription().getVersion()).toString());

                if (!BendingGUI.versionInfo.equals("")) {
                    sender.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.VersionWarning").toString());
                }
                return true;
            }

            sender.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.NoPermission").toString());

        } else if (args.get(0).equalsIgnoreCase("reload") || args.get(0).equalsIgnoreCase("r") || args.get(0).equalsIgnoreCase("rel")) {
            if (sender.hasPermission("bendinggui.reload")) {
                BendingGUI.INSTANCE.reload();
                sender.sendMessage(ChatColor.YELLOW + new LangBuilder("Chat.Command.Reload").toString());
                return true;
            }

            sender.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.NoPermission").toString());

        } else if (args.get(0).equalsIgnoreCase("help") || args.get(0).equalsIgnoreCase("h")) {
            sender.sendMessage(ChatColor.YELLOW + new LangBuilder("Chat.Command.Usage").toString());
        } else if (args.get(0).equalsIgnoreCase("debug")) {
            if (!sender.hasPermission("bendinggui.admin")) {
                sender.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.NoEditPermission").toString());
                return true;
            }
            if (args.size() < 3) {
                sender.sendMessage(ChatColor.RED + "/gui debug <lang/config/pconfig> [path]");
                return true;
            }

            if (args.get(1).equalsIgnoreCase("lang")) {
                String key = args.get(2);

                LangBuilder builder = new LangBuilder(key).ability(CoreAbility.getAbility("FireBlast"))
                        .time(13 * 1000).slot(4).element(Element.WATER).list("Apples", "Bananas", "a monkey")
                        .page(1, 4).preset("firearena", new HashMap<>());
                if (sender instanceof Player) builder = builder.player((OfflinePlayer) sender)
                        .yourOrPlayer((OfflinePlayer) sender, (OfflinePlayer) sender);

                sender.sendMessage(ChatColor.YELLOW + "Lang: '" + ChatColor.DARK_AQUA + builder.toString() + "'");
            } else if (args.get(1).equalsIgnoreCase("config")) {
                String key = args.get(2);

                Object o = ConfigStandard.getInstance().get(key);
                if (o instanceof String) o = "\"" + o + "\"";

                sender.sendMessage(ChatColor.YELLOW + "Config: '" + ChatColor.DARK_AQUA + o.toString() + "'");
            } else if (args.get(1).equalsIgnoreCase("pconfig")) {
                String key = args.get(2);

                Object o = ConfigPresets.getInstance().get(key);
                if (o instanceof String) o = "\"" + o + "\"";

                sender.sendMessage(ChatColor.YELLOW + "PresetConfig: '" + ChatColor.DARK_AQUA + o.toString() + "'");
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(new LangBuilder("Chat.Command.PlayerOnly").toString());
                return false;
            }

            Player playero = Bukkit.getPlayer(args.get(0));

            if (playero == null && sender.hasPermission("bendinggui.admin")) {
                sender.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.NoPlayer").toString());
                return true;
            } else if (playero != null && !sender.hasPermission("bendinggui.admin")) {
                sender.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.NoEditPermission").toString());
            } else if (playero == null) {
                sender.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.Usage").toString());
            } else {
                MenuBendingOptions menu = new MenuBendingOptions(playero);
                menu.openMenu((Player)sender);
            }
            return true;

        }
        return true;
    }
}
