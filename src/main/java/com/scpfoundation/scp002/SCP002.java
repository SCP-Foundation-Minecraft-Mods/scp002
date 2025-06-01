package com.scpfoundation.scp002;

import com.scpfoundation.scp002.command.CommandBuildSCP002;
import com.scpfoundation.scp002.world.WorldGenerator;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = SCP002.MODID, name = "SCP-002 Mod", version = "1.0", acceptedMinecraftVersions = "[1.12.2]")
public class SCP002 {
    public static final String MODID = "scp002";

    // --- Define your custom DamageSource here ---
    public static final DamageSource DAMAGE_SOURCE_ASSIMILATION = new DamageSource("assimilation")
            .setDamageBypassesArmor() // Damage ignores armor
            .setDamageIsAbsolute();   // Damage is not reduced by enchantments/resistance

    @Mod.Instance(MODID)
    public static SCP002 instance;

    // --- NEW: Define your mod's logger ---
    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // --- NEW: Initialize the logger in preInit ---
        logger = LogManager.getLogger(MODID); // Get a logger instance, named after your MODID

        // Example usage:
        logger.info("SCP-002 Mod: Pre-initialization started.");
        logger.debug("Debug logging is active.");

        // Register your world generator
        GameRegistry.registerWorldGenerator(new WorldGenerator(), 0); // The 0 is generation priority (0 is default)
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        logger.info("SCP-002 Mod: Registering server commands.");
        event.registerServerCommand(new CommandBuildSCP002());
    }
}
