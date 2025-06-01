package com.scpfoundation.scp002.proxy;

// ENSURE ALL THESE IMPORTS ARE PRESENT:
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import com.scpfoundation.scp002.registry.ModPotions; // Assuming your ModPotions class is here
import com.scpfoundation.scp002.client.render.potion.PotionRadiationRenderer; // Your new renderer class

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        // This line MUST be exactly as shown:
        //RenderingRegistry.registerPotionEffectRenderer(ModPotions.RADIATION_POTION, new PotionRadiationRenderer());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
