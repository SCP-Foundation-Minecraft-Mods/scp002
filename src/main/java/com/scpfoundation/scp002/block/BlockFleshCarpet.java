package com.scpfoundation.scp002.block;

import com.scpfoundation.scp002.SCP002;
import com.scpfoundation.scp002.registry.ModSounds;
import net.minecraft.block.BlockCarpet;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFleshCarpet extends BlockCarpet {

    public BlockFleshCarpet() {
        // BlockCarpet constructor takes a MapColor
        super(); // Use a fleshy red color for the map
        this.setUnlocalizedName("carpet_flesh");
        this.setRegistryName(SCP002.MODID, "carpet_flesh");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setHardness(0.1f); // Carpets are very soft
        this.setSoundType(ModSounds.FLESH_SQUISHY); // Soft fleshy sound
        this.setResistance(0.5f);
    }

    // Optional: Add a subtle effect when walking on the carpet
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof EntityPlayer) {
            // Apply a very short, mild nausea or slowness effect for atmosphere
            ((EntityPlayer) entityIn).addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 5, 0, false, false));
            ((EntityPlayer) entityIn).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 5, 0, false, false));
        }
        super.onEntityWalk(worldIn, pos, entityIn); // Call super to ensure default carpet behavior
    }
}
