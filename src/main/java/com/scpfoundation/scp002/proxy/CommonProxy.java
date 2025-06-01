package com.scpfoundation.scp002.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

// This class will contain code that runs on both client and server,
// or specifically on the server if you don't have a separate ServerProxy.
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        // Common pre-initialization tasks (e.g., registering blocks, items, tile entities, sound events)
        // These tasks need to happen on both client and server worlds.
    }

    public void init(FMLInitializationEvent event) {
        // Common initialization tasks (e.g., registering recipes, world generation)
    }

    public void postInit(FMLPostInitializationEvent event) {
        // Common post-initialization tasks (e.g., inter-mod communication)
    }
}
