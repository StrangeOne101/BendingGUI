package com.strangeone101.bendinggui;

import com.projectkorra.projectkorra.board.BendingBoardManager;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.jedk1.jedcore.configuration.JedCoreConfig;

public class BendingBoard 
{
	public enum BBPlugin {
		
		JEDCORE,
		STOCK,
		NONE;
		
		public Plugin getPlugin() {
			if (this == JEDCORE) return Bukkit.getPluginManager().getPlugin("JedCore");
			if (this == STOCK) return Bukkit.getPluginManager().getPlugin("ProjectKorra");
			return null;
		}
	};
	
	private static BBPlugin boardPlugin = BBPlugin.NONE;
	
	/**Checks all BendingBoard plugins supported*/
	public static void checkPlugins() {
		if (BBPlugin.JEDCORE.getPlugin() != null) {
			if (JedCoreConfig.board.getConfig().getBoolean("Settings.Enabled")) {
				boardPlugin = BBPlugin.JEDCORE;
				return;
			}
		}

		if (ConfigManager.defaultConfig.get().getBoolean("Properties.BendingBoard")) {
			boardPlugin = BBPlugin.STOCK;
		}
	}
	
	/**Returns whether the current board is toggled or not*/
	public static boolean isToggled(Player player) {
		if (boardPlugin == BBPlugin.STOCK) {
			return BendingBoardManager.isDisabled(player);
		} else if (boardPlugin == BBPlugin.JEDCORE) {
			return com.jedk1.jedcore.scoreboard.BendingBoard.disabled.contains(player.getUniqueId());
		}
		
		return false;
	}
	
	/**Toggle the current bending board, if it's in use.*/
	public static void toggle(Player player) {
		if (boardPlugin == BBPlugin.STOCK) {
			BendingBoardManager.toggleBoard(player, true);
		} else if (boardPlugin == BBPlugin.JEDCORE) {
			com.jedk1.jedcore.scoreboard.BendingBoard.toggle(player);
		}
	}
	
	/**Returns whether there is a bendingboard that can be used on the server*/
	public static boolean isBoardEnabled() {
		return boardPlugin != BBPlugin.NONE && boardPlugin != null;
	}
}
