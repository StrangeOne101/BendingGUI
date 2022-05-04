package com.strangeone101.bendinggui.command;

import java.util.List;

import com.strangeone101.bendinggui.LangBuilder;
import com.strangeone101.bendinggui.config.ConfigStandard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        Player player = (Player) sender;
        if (args.size() == 0) {
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
            if (!(sender instanceof Player)) {
                sender.sendMessage(new LangBuilder("Chat.Command.PlayerOnly").toString());
                return false;
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

            player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.NoPermission").toString());

        } else if (args.get(0).equalsIgnoreCase("reload") || args.get(0).equalsIgnoreCase("r") || args.get(0).equalsIgnoreCase("rel")) {
            if (sender.hasPermission("bendinggui.reload")) {
                BendingGUI.INSTANCE.reload();
                player.sendMessage(ChatColor.YELLOW + new LangBuilder("Chat.Command.Reload").toString());
                return true;
            }

            player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.NoPermission").toString());

        } else if (args.get(0).equalsIgnoreCase("help") || args.get(0).equalsIgnoreCase("h")) {
            sender.sendMessage(ChatColor.YELLOW + new LangBuilder("Chat.Command.Usage").toString());
        } else {
            Player playero = Bukkit.getPlayer(args.get(0));

            if (playero == null && player.hasPermission("bendinggui.admin")) {
                sender.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.NoPlayer").toString());
                return true;
            } else if (playero != null && !player.hasPermission("bendinggui.admin")) {

                player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.NoEditPermission").toString());
            } else if (playero == null) {

                sender.sendMessage(ChatColor.RED + new LangBuilder("Chat.Command.Usage").toString());
            } else {

                MenuBendingOptions menu = new MenuBendingOptions(playero);
                menu.openMenu(player);
            }
            return true;

        }
        return true;
    }
}
