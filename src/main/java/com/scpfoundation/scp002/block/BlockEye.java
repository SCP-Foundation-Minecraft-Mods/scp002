package com.scpfoundation.scp002.block;

import com.scpfoundation.scp002.SCP002;
import com.scpfoundation.scp002.registry.ModSounds; // Needed for specific eye sounds
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEye extends Block {

    public BlockEye(Material materialIn) {
        super(materialIn);
        // Set all common properties directly in the constructor
        this.setUnlocalizedName("wall_eye");
        this.setRegistryName(SCP002.MODID, "wall_eye");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setSoundType(ModSounds.FLESH_EYE); // Using the custom eye sound type
        this.setHardness(0.5f);
        this.setResistance(3.0f);
        this.setLightLevel(0.2f); // Emits dim, perhaps pulsating light
        this.setTickRandomly(true); // Essential for random ticks for blinking/watching sounds
    }

    // This method is called when a player right-clicks the block
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, net.minecraft.util.EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            // Play a distinct sound on activation
            worldIn.playSound(null, pos, ModSounds.EYE_BLINK, SoundCategory.BLOCKS, 0.8f, 1.0f + (worldIn.rand.nextFloat() - 0.5f) * 0.2f);
        }
        return true; // Indicate that the activation was handled
    }

    // This method is called randomly on the client side for visual/sound effects
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, java.util.Random rand) {
        // Play a subtle "watching" sound randomly
        if (rand.nextInt(200) == 0) { // 1 in 200 chance per tick
            worldIn.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ModSounds.EYE_WATCH, SoundCategory.AMBIENT, 0.5f, 1.0f + (rand.nextFloat() - 0.5f) * 0.2f, false);
        }
    }

    // You could also add other interactions, like an entity looking at it.
}
