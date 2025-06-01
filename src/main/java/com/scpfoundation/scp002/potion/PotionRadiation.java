package com.scpfoundation.scp002.potion;

import com.scpfoundation.scp002.SCP002;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class PotionRadiation extends Potion {

    public static final DamageSource RADIATION_DAMAGE = new DamageSource(SCP002.MODID + ".radiation").setDamageBypassesArmor().setMagicDamage();

    // Define the ResourceLocation for your custom potion icon texture
    // Keep this, as it's needed by the renderer
    private static final ResourceLocation CUSTOM_POTION_ICON = new ResourceLocation(SCP002.MODID, "textures/gui/potion/radiation_icon.png");

    public PotionRadiation() {
        super(true, 0x00FF00); // Green color for radiation effect overlay
        this.setRegistryName(new ResourceLocation(SCP002.MODID, "radiation"));
        this.setPotionName("effect.radiation");
        // No need to setIconIndex here anymore as we'll use a custom renderer.
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBase, int amplifier) {
        float damage = (amplifier + 1);
        if (entityLivingBase.getHealth() > 0.0F) {
            entityLivingBase.attackEntityFrom(RADIATION_DAMAGE, damage);
        }
    }

    // REMOVE THE FOLLOWING METHODS (renderHUDEffect, renderInventoryEffect, getStatusIconIndex)
    // from this class entirely. They will be in the PotionRadiationRenderer instead.
}
