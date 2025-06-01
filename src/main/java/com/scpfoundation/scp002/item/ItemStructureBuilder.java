package com.scpfoundation.scp002.item;

import com.scpfoundation.scp002.SCP002;
import com.scpfoundation.scp002.util.RoomBuilder; // Import your RoomBuilder
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemStructureBuilder extends Item {

    public ItemStructureBuilder() {
        this.setUnlocalizedName("structure_builder_wand");
        this.setRegistryName(SCP002.MODID, "structure_builder_wand");
        // Use a custom mod-specific creative tab if you have one, otherwise CreativeTabs.TOOLS is fine.
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setMaxStackSize(1);
    }

    /**
     * Called when the item is right-clicked on a block.
     * This triggers the construction of an SCP-002 chamber.
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        // Ensure this logic only runs on the server side to prevent desyncs
        if (!worldIn.isRemote) {
            // Calculate the origin for the room.
            // 'pos' is the clicked block. We want the room to build *above* this block.
            // The RoomBuilder assumes 'origin' is the lowest Y-level of the room (its floor).
            BlockPos structureOrigin = pos.up();

            // Define room parameters. These can be customized or made configurable.
            int roomSize = 7;     // Half-width/depth, for a 15x15 outer wall room
            int roomHeight = 6;   // Interior air space height (e.g., 6 blocks tall)
            boolean includeDoor = true; // Whether to include a flesh door

            // Provide immediate feedback to the player
            player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Attempting to generate SCP-002 chamber at " +
                    structureOrigin.getX() + ", " + structureOrigin.getY() + ", " + structureOrigin.getZ() + "..."));

            try {
                // Call the RoomBuilder to construct the chamber
                RoomBuilder.buildRoom(worldIn, structureOrigin, roomSize, roomHeight, includeDoor);
                player.sendMessage(new TextComponentString(TextFormatting.AQUA + "SCP-002 Chamber successfully generated!"));
            } catch (Exception e) {
                // Catch any potential errors during room building (e.g., if RoomBuilder were to throw exceptions)
                player.sendMessage(new TextComponentString(TextFormatting.RED + "Failed to generate chamber! Error: " + e.getMessage()));
                SCP002.logger.error("Error generating SCP-002 chamber at {}: {}", structureOrigin, e.getMessage());
                return EnumActionResult.FAIL; // Indicate failure
            }

            // Optionally, consume the item or damage it if it's not meant to be infinite
            // player.getHeldItem(hand).shrink(1); // Consume the item
            // player.getHeldItem(hand).damageItem(1, player); // Damage the item (e.g., for durability)

            return EnumActionResult.SUCCESS; // Indicate that the action was successful
        }
        // Do nothing on client side or if not successful
        return EnumActionResult.PASS;
    }
}
