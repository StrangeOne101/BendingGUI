package com.strangeone101.bendinggui;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.projectkorra.projectkorra.ability.StockAbility;

public class Descriptions 
{
	private final static String DEFAULT = "***Description Coming Soon!***";
	
	private static HashMap<String, String> descmap;
	
	protected static void addDefaults()
	{
		descmap.put(StockAbility.AirBlast.toString(), "Releases a blast of air that pushing all mobs and items");
		descmap.put(StockAbility.AirBubble.toString(), "Allows the user to keep a bubble of air around them while traveling underwater");
		descmap.put(StockAbility.AcrobatStance.toString(), "Makes the users faster and stronger but uses more energy in the process");
		descmap.put(StockAbility.AirBurst.toString(), "Creates a powerful gust of air that can blow away your enimies");
		descmap.put(StockAbility.AirScooter.toString(), "Allows the user to ride a ball of air to scale across terrain fast");
		descmap.put(StockAbility.AirShield.toString(), "Allows the user to make a shield of air around them");
		descmap.put(StockAbility.AirSpout.toString(), "Allows the user to walk on a spout of air");
		descmap.put(StockAbility.AirSuction.toString(), "Pulls all mobs and items towards the the user");
		descmap.put(StockAbility.AirSwipe.toString(), "Releases a wider gust of air, pushing mobs and items");
		descmap.put(StockAbility.Flight.toString(), "Allows the user to fly by holding sneak (SHIFT)");	
		descmap.put(StockAbility.Suffocate.toString(), "Bends the air right out of another player's lungs");	
		descmap.put(StockAbility.Tornado.toString(), "Creates a tornado of air that will misplace other users");	
		
		descmap.put(StockAbility.Bloodbending.toString(), "Allows the user to manipulate other players and mobs with their bending");
		descmap.put(StockAbility.HealingWaters.toString(), "Allows the user to heal with water");	
		descmap.put(StockAbility.IceBlast.toString(), "Blasts a chunk of ice towards your target");	
		descmap.put(StockAbility.IceSpike.toString(), "Pulls a spike of ice up from bellow your enemy");	
		descmap.put(StockAbility.OctopusForm.toString(), "Creates a octopus of water around the user");	
		descmap.put(StockAbility.PhaseChange.toString(), "Allows the user to freeze and melt water");	
		descmap.put(StockAbility.PlantArmor.toString(), "Uses leaves and plants to create armor for the user");	
		descmap.put(StockAbility.Surge.toString(), "Creates a wave or a shield of water and ice");	
		descmap.put(StockAbility.Torrent.toString(), "Creates a ring of water around the user which can be used to freeze targets or push them away");	
		descmap.put(StockAbility.WaterArms.toString(), "Allows the user to use water to create arms with lots of different abilities");
		descmap.put(StockAbility.WaterBubble.toString(), "Allows the user to travel underwater with a bubble of air around them");
		descmap.put(StockAbility.WaterManipulation.toString(), "Allows the user to fire water at a target");
		descmap.put(StockAbility.WaterSpout.toString(), "Allows the user to stand on a spout of water");
		
		descmap.put(StockAbility.Blaze.toString(), "Releases a powerful ring of fire around you");
		descmap.put(StockAbility.Combustion.toString(), "Creates a fire-like projectile with their the user's mind");
		descmap.put(StockAbility.FireBlast.toString(), "Blasts a ball of fire towards your enemy");	
		descmap.put(StockAbility.FireBurst.toString(), "Sets fire to everything around you");	
		descmap.put(StockAbility.FireJet.toString(), "Allows the user to fly for short distances with fire");	
		descmap.put(StockAbility.FireShield.toString(), "Allows the user to create a shield of fire in front of them");	
		descmap.put(StockAbility.HeatControl.toString(), "Allows the user to put out fire and not burn");	
		descmap.put(StockAbility.Illumination.toString(), "Allows the bender to see by holding a flame");	
		descmap.put(StockAbility.Lightning.toString(), "Allows the user to release a stike of lightning");	
		
		descmap.put(StockAbility.Catapult.toString(), "Allows the user to catapult themselves through the air by launching themselves up from the earth");
		descmap.put(StockAbility.Collapse.toString(), "Collapses or pulls down the earth");
		descmap.put(StockAbility.EarthArmor.toString(), "Uses earth as armor for short periods of time");
		descmap.put(StockAbility.EarthBlast.toString(), "Blasts a chunk of earth wherever the user wants it to go");
		descmap.put(StockAbility.EarthGrab.toString(), "Allows the user to trap other mobs and players in earth");
		descmap.put(StockAbility.EarthSmash.toString(), "Allows the user to grab a huge chunk of earth and throw it");
		descmap.put(StockAbility.EarthTunnel.toString(), "Allows the user to tunnel through the earth");	
		descmap.put(StockAbility.RaiseEarth.toString(), "Creates walls or collums of earth in front of the user");
		descmap.put(StockAbility.SandSpout.toString(), "Creates a spout of sand for the user to stand on while bliding users bellow");	
		descmap.put(StockAbility.Shockwave.toString(), "Releases a powerful shockwave of earth that sends targets flying");
		descmap.put(StockAbility.Tremorsense.toString(), "Allows the user to see nearby airpockets (caves)");	
		
		descmap.put(StockAbility.Extraction.toString(), "Allows the user to extract metals directly from the ore");
		descmap.put(StockAbility.LavaFlow.toString(), "Turns earth into a pool or lava or creates a ring of lava around the user");	
		descmap.put(StockAbility.MetalClips.toString(), "Allows the user to fire slices of metal at a target and capture them");	
		
		descmap.put(StockAbility.HighJump.toString(), "Makes the user jump high in the air");	
		descmap.put(StockAbility.Paralyze.toString(), "Allows the user to paralyze other benders and block their bending");	
		descmap.put(StockAbility.QuickStrike.toString(), "Allows the user to attack quickly with a chance to chi block the target");	
		descmap.put(StockAbility.RapidPunch.toString(), "Allows the user to attack faster while dealing more damage");	
		descmap.put(StockAbility.Smokescreen.toString(), "Releases smoke and blinds all nearby players");	
		descmap.put(StockAbility.SwiftKick.toString(), "Damages the target with a high chance of blocking their chi, if the user is in the air");	
		descmap.put(StockAbility.WarriorStance.toString(), "Makes the user's attacks more powerful but also makes the user more vulnerable");
		
		descmap.put(StockAbility.AvatarState.toString(), "Users in the Avatar State are much more powerful than normal benders. Their bending power is multiplied heavily and they take far less damage from attacks.");
		
		
		descmap.put("AirBlade", "Creates a powerful blade of air that damages mobs and players");
		descmap.put("AirBreath", "Releases a powerful breath of air that knocks back your target");
		descmap.put("AirGlide", "Allows the user to glide down safely instead of falling");
		descmap.put("AirPunch", "Allows the user to punch in rapid succession with blasts of air");
		descmap.put("Meditate", "Allows the user to become stronger for short periods of time after meditating");
		descmap.put("SonicBlast", "Creates an ear-piecing sonicblast that does high damage to other mobs and players");
		descmap.put("FartBlast", "Allows the user to stink other players out by farting on them");
		
		descmap.put("EarthKick", "Allows the user to kick the earth and hurl chunks at their opponent");
		descmap.put("EarthLine", "Creates a line of risen earth that inflicts damage on all mobs and players that it hits");
		descmap.put("EarthShard", "Allows the user to pickup multiple chunks of earth and hurl them towards their target");
		descmap.put("EarthSurf", "Allows the user to ride on earth to scale the terrain fast");
		descmap.put("EarthPillar", "Pulls a pillar of earth out in whatever direction you look");
		descmap.put("MudSurge", "Hurls mud towards a target");
		descmap.put("SandBlast", "Blasts sand towards an enemy, temporarily blinding them");
		descmap.put("SandShift", "Allows the user to change sandstone without the need for crafting it");
		descmap.put("StatePhase", "Allows the bender to create sand from earth and vise versa");
		descmap.put("MetalFragments", "Allows the bender to shoot fragments of metal towards a target");
		descmap.put("MetalShred", "Allows the user to shred a metal wall to gain access through it");
		descmap.put("MetalHook", "Allows the bender to use metalbending to hook or graple onto a surface and pull themselves in");
		descmap.put("MagnetShield", "Repels all metal objects hurling towards the user");
		descmap.put("LavaFlux", "Creates a wave of lava that can be hurled towards a target");
		descmap.put("LavaDisc", "Melts a block of earth into a disk of lava that can be thrown");
		descmap.put("Fissure", "Creates a line of lava that can swallow up mobs");
		descmap.put("LavaThrow", "Allows the user to hurl waves of lava at a target");
		
		descmap.put("FireBall", "Creates a fireball that can be cast towards a user");
		descmap.put("FireBreath", "Allows the user to breathe a powerful breath of fire");
		descmap.put("FirePunch", "Allows the user to damage and set fire to their enemy with just one punch");
		descmap.put("FireShots", "Allows the user to cast multiple and short fireballs towards a target");
		descmap.put("WallOfFire", "Creates wall of fire that blocks all incoming projectiles and mobs");
		descmap.put("Discharge", "Creates a powerful bolt of eletricity to fry your targets");
		descmap.put("LightningBurst", "Allows the user to charge lightning and release it in all directions at once");
		
		descmap.put("Maelstrom", "Creates a whirlpool that sucks all mobs and players into it");
		descmap.put("WakeFishing", "Allows the user to use waterbending to fish");
		descmap.put("WaterGimbal", "Creates wall of fire that blocks all incoming projectiles and mobs");
		descmap.put("PlantDrain", "Drains all nearby plants of water to create water for waterbending");
		descmap.put("PlantWhip", "Creates a vine of leaves that can quickly whip a target");
		descmap.put("PlantArmor", "Allows the user to create basic armor for themselves with leaves");
		descmap.put("PlantBlast", "Allows the user to blast quick, plant projectiles at their target");
		descmap.put("IceClaws", "Creates ice on your hand that can damage and slow your target");
		descmap.put("IceWall", "Creates a wall of ice in front of the user");
		descmap.put("IceStream", "Creates an stream-like projectile that freezes and slows targets on impact");
		descmap.put("FrostBreath", "Allows the user to freeze their breath, damaging and slowing their targets");
		descmap.put("BloodPuppet", "Allows the manipulation of mobs and players to make them damage each other");
		
		descmap.put("DaggerThrow", "Allows the user to rapidfire arrows towards a target");
		
		descmap.put("SpiritBeam", "Releases a powerful blast as the spirit of the avatar. The user must be in the AvatarState to use this move.");
		descmap.put("ElementSphere", "Creates a sphere of all the elements around the user, allowing them to use all four at once");
			
		
	}
	
	public static void load()
	{
		FileConfiguration config = new YamlConfiguration();
		try 
		{
			File file = new File(BendingGUI.INSTANCE.getDataFolder(), "lang.yml");
			if (!file.exists()) 
			{
				file.createNewFile();
				
			}
			descmap = new HashMap<String, String>();
			addDefaults();
			config.load(file);
			Map<String, Object> map = config.getValues(false);
			for (String s : map.keySet())
			{
				descmap.put(s, map.get(s).toString());
			}
			
			
		}
		catch (IOException e) {e.printStackTrace();} 
		catch (InvalidConfigurationException e) {e.printStackTrace();}
	}
	
	public static void save()
	{
		FileConfiguration config = new YamlConfiguration();
		try 
		{
			File file = new File(BendingGUI.INSTANCE.getDataFolder(), "lang.yml");
			if (!file.exists()) 
			{
				file.createNewFile();
				
			}
			config.load(file);
			for (String s : descmap.keySet())
			{
				config.set(s, descmap.get(s));
			}
			config.save(file);
		}
		catch (IOException e) {e.printStackTrace();} 
		catch (InvalidConfigurationException e) {e.printStackTrace();}
			
	}
	
	public static String getDescription(String move)
	{
		if (descmap.containsKey(move))
		{
			return descmap.get(move);
		}
		return DEFAULT;
	}
}
