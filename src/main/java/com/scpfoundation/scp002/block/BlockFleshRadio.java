package com.scpfoundation.scp002.block;

import com.scpfoundation.scp002.SCP002;
import com.scpfoundation.scp002.registry.ModSounds;
import com.scpfoundation.scp002.tileentity.TileEntityFleshRadio; // Will need to create this!
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.SoundCategory; // Make sure this is imported for sound control

public class BlockFleshRadio extends BlockContainer {

    public BlockFleshRadio(Material materialIn) {
        super(materialIn); // Using Material.IRON for a metallic feel, or use a custom material
        this.setUnlocalizedName("radio_flesh");
        this.setRegistryName(SCP002.MODID, "radio_flesh");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setHardness(1.0f);
        this.setResistance(5.0f);
        this.setSoundType(ModSounds.FLESH_SQUISHY_WET); // Or a specific mechanical/flesh sound
        this.setLightLevel(0.0f); // No light by default, perhaps a faint glow if powered
    }

    @Override
    @SuppressWarnings("deprecation") // Suppress for createNewTileEntity
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFleshRadio();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL; // Has a distinct model
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false; // Not a full cube if it's a radio model
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false; // Not a full cube
    }
}
