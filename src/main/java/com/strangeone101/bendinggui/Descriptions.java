package com.strangeone101.bendinggui;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Descriptions 
{
	private final static String DEFAULT = ChatColor.ITALIC + "No description set";
	
	private static HashMap<String, String> descmap;
	
	protected static void addDefaults()
	{
		descmap.put("AirBlast", "Releases a blast of air that pushing all mobs and items");
		descmap.put("AirBubble", "Allows the user to keep a bubble of air around them while traveling underwater");
		descmap.put("AcrobatStance", "Makes the users faster and stronger but uses more energy in the process");
		descmap.put("AirBurst", "Creates a powerful gust of air that can blow away your enimies");
		descmap.put("AirScooter", "Allows the user to ride a ball of air to scale across terrain fast");
		descmap.put("AirSpout", "Allows the user to walk on a spout of air");
		descmap.put("AirSuction", "Pulls all mobs and items towards the the user");
		descmap.put("AirSwipe", "Releases a wider gust of air, pushing mobs and items");
		descmap.put("Flight", "Allows the user to fly by holding sneak (SHIFT)");	
		descmap.put("Suffocate", "Bends the air right out of another player's lungs");	
		descmap.put("Tornado", "Creates a tornado of air that will misplace other users");	
		
		descmap.put("Bloodbending", "Allows the user to manipulate other players and mobs with their bending");
		descmap.put("HealingWaters", "Allows the user to heal with water");	
		descmap.put("IceBlast", "Blasts a chunk of ice towards your target");	
		descmap.put("IceSpike", "Pulls a spike of ice up from bellow your enemy");	
		descmap.put("OctopusForm", "Creates a octopus of water around the user");	
		descmap.put("PhaseChange", "Allows the user to freeze and melt water");	
		descmap.put("PlantArmor", "Uses leaves and plants to create armor for the user");	
		descmap.put("Surge", "Creates a wave or a shield of water and ice");	
		descmap.put("Torrent", "Creates a ring of water around the user which can be used to freeze targets or push them away");	
		descmap.put("WaterArms", "Allows the user to use water to create arms with lots of different abilities");
		descmap.put("WaterBubble", "Allows the user to travel underwater with a bubble of air around them");
		descmap.put("WaterManipulation", "Allows the user to fire water at a target");
		descmap.put("WaterSpout", "Allows the user to stand on a spout of water");
		
		descmap.put("Blaze", "Releases a powerful ring of fire around you");
		descmap.put("Combustion", "Creates a fire-like projectile with their the user's mind");
		descmap.put("FireBlast", "Blasts a ball of fire towards your enemy");	
		descmap.put("FireBurst", "Sets fire to everything around you");	
		descmap.put("FireJet", "Allows the user to fly for short distances with fire");	
		descmap.put("FireShield", "Allows the user to create a shield of fire in front of them");	
		descmap.put("HeatControl", "Allows the user to put out fire and not burn");	
		descmap.put("Illumination", "Allows the bender to see by holding a flame");	
		descmap.put("Lightning", "Allows the user to release a stike of lightning");	
		
		descmap.put("Catapult", "Allows the user to catapult themselves through the air by launching themselves up from the earth");
		descmap.put("Collapse", "Collapses or pulls down the earth");
		descmap.put("EarthArmor", "Uses earth as armor for short periods of time");
		descmap.put("EarthBlast", "Blasts a chunk of earth wherever the user wants it to go");
		descmap.put("EarthGrab", "Allows the user to trap other mobs and players in earth");
		descmap.put("EarthSmash", "Allows the user to grab a huge chunk of earth and throw it");
		descmap.put("EarthTunnel", "Allows the user to tunnel through the earth");	
		descmap.put("RaiseEarth", "Creates walls or collums of earth in front of the user");
		descmap.put("SandSpout", "Creates a spout of sand for the user to stand on while bliding users bellow");	
		descmap.put("Shockwave", "Releases a powerful shockwave of earth that sends targets flying");
		descmap.put("Tremorsense", "Allows the user to see nearby airpockets (caves)");	
		
		descmap.put("Extraction", "Allows the user to extract metals directly from the ore");
		descmap.put("LavaFlow", "Turns earth into a pool or lava or creates a ring of lava around the user");	
		descmap.put("MetalClips", "Allows the user to fire slices of metal at a target and capture them");	
		
		descmap.put("HighJump", "Makes the user jump high in the air");	
		descmap.put("Paralyze", "Allows the user to paralyze other benders and block their bending");	
		descmap.put("QuickStrike", "Allows the user to attack quickly with a chance to chi block the target");	
		descmap.put("RapidPunch", "Allows the user to attack faster while dealing more damage");	
		descmap.put("Smokescreen", "Releases smoke and blinds all nearby players");	
		descmap.put("SwiftKick", "Damages the target with a high chance of blocking their chi, if the user is in the air");	
		descmap.put("WarriorStance", "Makes the user's attacks more powerful but also makes the user more vulnerable");
		
		descmap.put("AvatarState", "Users in the Avatar State are much more powerful than normal benders. Their bending power is multiplied heavily and they take far less damage from attacks.");
		
		
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
		descmap.put("Drain", "Fill up all bottles in your inventory from the water in plants around you!");
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
		
		descmap.put("LoonyBlast", "Use Loonergy to blast away enemies with extreme power!");
		descmap.put("ToonShield", "Shield yourself from any attack with the power of Loonergy!");
		
		descmap.put("CactusBlast", "Fight in the desert by throwing cactus!");
		descmap.put("Sandstorm", "Allows the user to whip up a sandstorm!");
		descmap.put("EarthBurrow", "Allows the user to burrow themselves in the ground");
		
		descmap.put("Combo-AirSlam", "Kick your enemy up in the air then kick them away!");
		descmap.put("Combo-AirStream", "Creates a current of air that can be controlled by the player for a while");
		descmap.put("Combo-AirSweep", "Create a sweeping current of air that can sweep enemies off their feet!");
		descmap.put("Combo-Twister", "Create a twister to suck up and blow away your opponents!");
		descmap.put("Combo-SwiftStream", "Pull all enemies along with you as you fly!");
		
		descmap.put("Combo-Crevice", "Creates a crevice in the ground that can swallow players!");
		descmap.put("Combo-MagmaBlast", "Fire balls of magma at your enemies!");
		
		descmap.put("Combo-FireKick", "Create a small arc of fire from your feet!");
		descmap.put("Combo-FireSpin", "Create a huge ring of fire around you that does damage and huge knockback!");
		descmap.put("Combo-FireWheel", "Hurl a spinning wheel of fire towards your enemies!");
		descmap.put("Combo-JetBlaze", "Blaze it up as you launch from your powerful FireJet!");
		descmap.put("Combo-JetBlast", "Launch with a boom as your launch with a powerful FireJet!");
		
		descmap.put("Combo-WaterFlow", "Create a huge torrent of water that can sweep away your enemies!");
		descmap.put("Combo-Maelstrom", "Suck everyone and everything up in a deep whirlpool!");
		descmap.put("Combo-WaterGimbal", "Control two torrents of water at once!");
		descmap.put("Combo-IceBullet", "Make a dome of ice and that can shoot shards of ice!");
		descmap.put("Combo-IceWave", "Freeze your WaterWave to damage enemies that you hit!");
		
		descmap.put("Combo-Immobilize", "Freeze your enemies for a few seconds!");
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
