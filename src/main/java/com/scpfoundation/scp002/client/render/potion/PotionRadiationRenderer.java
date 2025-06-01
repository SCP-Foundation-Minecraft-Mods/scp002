package com.scpfoundation.scp002.client.render.potion;

import com.scpfoundation.scp002.SCP002;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
// THIS IS THE CRUCIAL IMPORT:

public class PotionRadiationRenderer extends PotionEffect { // This line is now correct

    private static final ResourceLocation CUSTOM_POTION_ICON = new ResourceLocation(SCP002.MODID, "textures/gui/potion/radiation_icon.png");

    public PotionRadiationRenderer(Potion p_i46811_1_) {
        super(p_i46811_1_);
    }


    public boolean renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        mc.getTextureManager().bindTexture(CUSTOM_POTION_ICON);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        mc.ingameGUI.drawTexturedModalRect(x + 6, y + 7, 0, 0, 18, 18);
        GlStateManager.disableBlend();
        return true;
    }
}
