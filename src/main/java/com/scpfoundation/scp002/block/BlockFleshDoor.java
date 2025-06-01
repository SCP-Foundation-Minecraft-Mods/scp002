package com.scpfoundation.scp002.block;

import com.scpfoundation.scp002.SCP002;
import com.scpfoundation.scp002.registry.ModSounds;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents; // For vanilla door sound
import net.minecraft.nbt.NBTTagCompound; // To store player data
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockFleshDoor extends BlockDoor {

    // Define the fixed destination for SCP-002 interior.
    // Choose coordinates far away from spawn to avoid conflicts.
    // Y=64 is a common ground level.
    private static final BlockPos SCP002_INTERIOR_POS = new BlockPos(10000, 64, 10000);

    public BlockFleshDoor() {
        // Material.CLAY is a common choice for flesh-like blocks
        super(Material.CLAY);
        this.setUnlocalizedName("flesh_door");
        this.setRegistryName(SCP002.MODID, "flesh_door");
        this.setCreativeTab(CreativeTabs.MATERIALS); // Assuming SCP002 has a static creativeTab
        // Or CreativeTabs.DECORATIONS;
        this.setHardness(3.0F); // Adjust as desired
        this.setSoundType(ModSounds.FLESH_SQUISHY_WET); // Or ModSounds.FLESH_SQUISHY
    }

    // You might need to add a constructor that takes the name if you have a BlockBase-like
    // structure for your specialized blocks. For BlockDoor, it's typically simpler.
    // For example:
    // public BlockFleshDoor(String name) {
    //     super(Material.CLAY);
    //     this.setUnlocalizedName(name);
    //     this.setRegistryName(SCP002.MODID, name);
    //     this.setCreativeTab(CreativeTabs.DECORATIONS);
    //     this.setSoundType(ModSounds.FLESH_SQUISHY_WET);
    //     this.setHardness(3.0F);
    // }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        // Ensure this logic only runs on the server side
        if (!worldIn.isRemote) {
            NBTTagCompound playerData = playerIn.getEntityData();

            // Check if the player is NOT already in SCP-002 (based on your SCP002Events tag)
            if (!playerData.getBoolean("isInSCP002")) {
                SCP002.logger.info(playerIn.getName() + " is entering SCP-002 via Flesh Door.");

                // --- 1. Store Player's Current Location ---
                // Create a new NBTTagCompound to store the position data
                NBTTagCompound lastPos = new NBTTagCompound();
                lastPos.setDouble("posX", playerIn.posX);
                lastPos.setDouble("posY", playerIn.posY);
                lastPos.setDouble("posZ", playerIn.posZ);
                lastPos.setInteger("dimension", playerIn.dimension);
                playerData.setTag("lastOverworldPos", lastPos); // Store under a unique tag

                // --- 2. Set the 'isInSCP002' NBT Tag ---
                playerData.setBoolean("isInSCP002", true);

                // --- 3. Teleport the Player ---
                // For teleporting within the same dimension (Overworld to remote Overworld)
                playerIn.setPositionAndUpdate(
                        SCP002_INTERIOR_POS.getX() + 0.5D, // Add 0.5D to center the player on the block
                        SCP002_INTERIOR_POS.getY(),
                        SCP002_INTERIOR_POS.getZ() + 0.5D
                );

                // --- 4. Add Visual and Audio Feedback for Teleportation ---
                // Play a mysterious sound at the source and destination (or just destination)
                worldIn.playSound(null, pos, ModSounds.FLESH_TELEPORT_SOUND, SoundCategory.BLOCKS, 1.0F, 1.0F);
                // Also play a sound at the destination
                worldIn.playSound(null, SCP002_INTERIOR_POS, ModSounds.FLESH_TELEPORT_SOUND, SoundCategory.BLOCKS, 1.0F, 1.0F);


                // Spawn some particles around the player for visual effect
                if (worldIn instanceof WorldServer) {
                    ((WorldServer) worldIn).spawnParticle(
                            EnumParticleTypes.PORTAL, // Portal effect
                            playerIn.posX, playerIn.posY + 1.0D, playerIn.posZ,
                            20, // Count
                            0.5D, 1.0D, 0.5D, // Offset XYZ
                            0.0D // Speed
                    );
                    ((WorldServer) worldIn).spawnParticle(
                            EnumParticleTypes.CRIT_MAGIC, // Eerie magic sparkle
                            playerIn.posX, playerIn.posY + 1.0D, playerIn.posZ,
                            10, 0.5D, 1.0D, 0.5D, 0.0D
                    );
                }

                // IMPORTANT: Return true to indicate that the activation was handled by your code
                // This prevents the default BlockDoor behavior (opening/closing) from happening.
                return true;
            } else {
                // If player is already in SCP-002 and tries to interact with the door again,
                // perhaps make it clear they can't go back or just let default door behavior happen.
                // For now, we'll let it try to open/close like a normal door if they are already inside.
                // If you want to prevent going back, return false and add a message.
                SCP002.logger.debug(playerIn.getName() + " tried to use Flesh Door while already in SCP-002.");
            }
        }
        // Let the default BlockDoor logic handle opening/closing if our teleportation didn't occur
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    // You might want to override getCollisionBoundingBox or isPassable
    // if you want the door to be initially impassable or have custom hitboxes.
    // By default, BlockDoor handles its own open/closed state for collisions.
}
