package com.strangeone101.bendinggui;

import me.simplicitee.project.addons.BoardManager;
import me.simplicitee.project.addons.ProjectAddons;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.jedk1.jedcore.configuration.JedCoreConfig;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;

public class BendingBoard 
{
	public enum BBPlugin {
		
		JEDCORE, PROJECT_ADDONS, NONE;
		
		public Plugin getPlugin() {
			if (this == PROJECT_ADDONS) return Bukkit.getPluginManager().getPlugin("ProjectAddons");
			if (this == JEDCORE) return Bukkit.getPluginManager().getPlugin("JedCore");
			return null;
		}
	};
	
	private static BBPlugin boardPlugin = BBPlugin.NONE;

	private static Field PROJECT_ADDONS_DISABLED_SET;
	
	/**Checks all BendingBoard plugins supported*/
	public static void checkPlugins() {
		if (BBPlugin.JEDCORE.getPlugin() != null) {
			if (JedCoreConfig.board.getConfig().getBoolean("Settings.Enabled")) {
				boardPlugin = BBPlugin.JEDCORE;
			}
		}
		
		if (boardPlugin == BBPlugin.NONE && BBPlugin.PROJECT_ADDONS.getPlugin() != null) {
			if (ProjectAddons.instance.isBoardEnabled()) {
				boardPlugin = BBPlugin.PROJECT_ADDONS;

				try {
					PROJECT_ADDONS_DISABLED_SET = BoardManager.class.getDeclaredField("disabled");
					PROJECT_ADDONS_DISABLED_SET.setAccessible(true);
					Set<UUID> set = (Set<UUID>) PROJECT_ADDONS_DISABLED_SET.get(ProjectAddons.instance.getBoardManager());
				} catch (NoSuchFieldException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**Returns whether the current board is toggled or not*/
	public static boolean isToggled(Player player) {
		if (boardPlugin == BBPlugin.PROJECT_ADDONS) {
			try {
				Set<UUID> set = (Set<UUID>) PROJECT_ADDONS_DISABLED_SET.get(ProjectAddons.instance.getBoardManager());
				return set.contains(player.getUniqueId());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		} else if (boardPlugin == BBPlugin.JEDCORE) {
			return com.jedk1.jedcore.scoreboard.BendingBoard.disabled.contains(player.getUniqueId());
		}
		
		return false;
	}
	
	/**Toggle the current bending board, if it's in use.*/
	public static void toggle(Player player) {
		if (boardPlugin == BBPlugin.PROJECT_ADDONS) {
			ProjectAddons.instance.getBoardManager().toggleDisabled(player);
			ProjectAddons.instance.getBoardManager().update(player);
		} else if (boardPlugin == BBPlugin.JEDCORE) {
			com.jedk1.jedcore.scoreboard.BendingBoard.toggle(player);
		}
	}
	
	/**Returns whether there is a bendingboard that can be used on the server*/
	public static boolean isBoardEnabled() {
		return boardPlugin != BBPlugin.NONE && boardPlugin != null;
	}
}
