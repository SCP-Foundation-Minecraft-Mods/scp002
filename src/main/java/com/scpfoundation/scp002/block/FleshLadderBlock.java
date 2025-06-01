package com.scpfoundation.scp002.block;

import com.scpfoundation.scp002.SCP002;
import com.scpfoundation.scp002.registry.ModSounds; // For flesh sounds
import net.minecraft.block.BlockLadder; // Extend BlockLadder for default ladder behavior
import net.minecraft.block.material.Material;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks; // For setting super constructor material

public class FleshLadderBlock extends BlockLadder {

    public FleshLadderBlock() {
        // BlockLadder constructor typically takes Material.WOOD
        super(); // Call the default BlockLadder constructor (uses Material.WOOD)
        this.setUnlocalizedName("ladder_flesh");
        this.setRegistryName(SCP002.MODID, "ladder_flesh");
        this.setCreativeTab(CreativeTabs.DECORATIONS); // Or another appropriate tab
        this.setHardness(0.4f); // Ladders are usually not very hard
        this.setResistance(2.0f);
        this.setSoundType(ModSounds.FLESH_SQUISHY); // Use your general flesh sound type
    }

    // No need to override getItem() unless you have a custom item for this ladder specifically.
    // If you plan to make it craftable or obtainable as an item, ensure you register an ItemBlock for it
    // in ModBlocks.java if not using a custom Item class for it.
}
