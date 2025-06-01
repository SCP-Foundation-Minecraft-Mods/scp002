package com.scpfoundation.scp002.block;

import com.scpfoundation.scp002.SCP002;
import com.scpfoundation.scp002.registry.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFleshWindow extends Block {

    public BlockFleshWindow() {
        super(Material.GLASS); // Use glass material for transparency properties
        this.setUnlocalizedName("window_invisible_flesh");
        this.setRegistryName(SCP002.MODID, "window_invisible_flesh");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setHardness(0.3f); // Easy to break like glass
        this.setResistance(1.5f);
        this.setSoundType(ModSounds.FLESH_SQUISHY_WET); // Fleshy sound
        this.setLightLevel(0.0f); // Doesn't emit light

        // Crucial for rendering:
        this.useNeighborBrightness = true; // Allows light to pass through like glass
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false; // Very important: makes the block transparent (allows seeing through)
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false; // Very important: ensures it doesn't render as a full solid block
    }

    // Required for transparent blocks to render correctly
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        // Use TRANSLUCENT for semi-transparent or ALPHATEST for cutout textures.
        // For a window effect, TRANSLUCENT is often appropriate.
        return BlockRenderLayer.TRANSLUCENT;
    }

    // You will need to create a custom blockstate and model for this block.
    // The model will define how each face is textured, allowing for the "invisible from outside" effect.
    // This cannot be done purely in Java.
}
