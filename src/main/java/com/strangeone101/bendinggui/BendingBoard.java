package com.strangeone101.bendinggui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.jedk1.jedcore.configuration.JedCoreConfig;
import com.runefist.bendingboard.config.ConfigManager;

public class BendingBoard 
{
	public enum BBPlugin {
		
		JEDCORE, BB, NONE;
		
		public Plugin getPlugin() {
			if (this == BB) return Bukkit.getPluginManager().getPlugin("BendingBoard");
			if (this == JEDCORE) return Bukkit.getPluginManager().getPlugin("JedCore");
			return null;
		}
	};
	
	private static BBPlugin boardPlugin = BBPlugin.NONE;
	
	/**Checks all BendingBoard plugins supported*/
	public static void checkPlugins() {
		if (BBPlugin.JEDCORE.getPlugin() != null) {
			if (JedCoreConfig.board.getConfig().getBoolean("Settings.Enabled")) {
				boardPlugin = BBPlugin.JEDCORE;
			}
		}
		
		if (boardPlugin == BBPlugin.NONE && BBPlugin.BB.getPlugin() != null) {
			boardPlugin = BBPlugin.BB;
		}
	}
	
	/**Returns whether the current board is toggled or not*/
	public static boolean isToggled(Player player) {
		if (boardPlugin == BBPlugin.BB) {
			return ConfigManager.toggledPlayersConfig.get().getStringList("ToggledPlayers").contains(player.getUniqueId().toString());
		} else if (boardPlugin == BBPlugin.JEDCORE) {
			return com.jedk1.jedcore.scoreboard.BendingBoard.disabled.contains(player.getUniqueId());
		}
		
		return false;
	}
	
	/**Toggle the current bending board, if it's in use.*/
	public static void toggle(Player player) {
		if (boardPlugin == BBPlugin.BB) {
			player.performCommand("bending board");
		} else if (boardPlugin == BBPlugin.JEDCORE) {
			com.jedk1.jedcore.scoreboard.BendingBoard.toggle(player);
		}
	}
	
	/**Returns whether there is a bendingboard that can be used on the server*/
	public static boolean isBoardEnabled() {
		return boardPlugin != BBPlugin.NONE && boardPlugin != null;
	}
}
