package com.scpfoundation.scp002.block;

import com.scpfoundation.scp002.SCP002;
import com.scpfoundation.scp002.tileentity.TileEntityTVStatic;
import com.scpfoundation.scp002.registry.ModSounds;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.EnumBlockRenderType;

public class BlockTVStatic extends BlockContainer {

    public BlockTVStatic(Material materialIn) {
        super(materialIn);
        this.setUnlocalizedName("tv_static");
        this.setRegistryName(SCP002.MODID, "tv_static");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setHardness(0.5f);
        this.setResistance(2.5f);
        this.setLightLevel(0.9f);
        this.setSoundType(ModSounds.TV_STATIC_SOUND_TYPE);
    }

    // --- TileEntity Methods ---

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityTVStatic();
    }

    // --- Rendering Methods ---
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
    }
}
