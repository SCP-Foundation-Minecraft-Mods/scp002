package com.scpfoundation.scp002.registry;

import com.scpfoundation.scp002.SCP002; // Import your main mod class for MODID
import com.scpfoundation.scp002.potion.PotionRadiation; // Import your PotionRadiation class
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent; // Import for the registry event
import net.minecraftforge.fml.common.Mod; // Import for @Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent; // Import for @SubscribeEvent
import net.minecraftforge.fml.common.registry.ForgeRegistries; // Not strictly needed if using RegistryEvent, but useful to know

// This annotation tells Forge to subscribe this class to the mod's event bus
// for events like RegistryEvent.
@Mod.EventBusSubscriber(modid = SCP002.MODID)
public class ModPotions {

    // Declare your PotionRadiation instance as public static final.
    // This makes it accessible from other classes like ClientProxy.
    // The PotionRadiation constructor will be called here to create the instance.
    public static final Potion RADIATION_POTION = new PotionRadiation();

    /**
     * This method listens for the RegistryEvent.Register<Potion> event.
     * This is the standard Forge way to register custom Potion instances.
     *
     * @param event The registry event.
     */
    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        // Register your potion instance with the event's registry.
        event.getRegistry().register(RADIATION_POTION);

        SCP002.logger.info("Registered Potion: {}", RADIATION_POTION.getRegistryName());
    }
}
