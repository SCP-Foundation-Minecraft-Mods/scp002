package com.scpfoundation.scp002.command;

import com.scpfoundation.scp002.SCP002; // Import your main mod class for the logger
import com.scpfoundation.scp002.util.RoomBuilder;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World; // Import World

public class CommandBuildSCP002 extends CommandBase {

    @Override
    public String getName() {
        return "buildscp002";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        // Provide more detailed usage including default values
        return "buildscp002 <x> <y> <z> [size (default: 7)] [height (default: 6)] [door (true/false, default: true)]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // Check for minimum arguments
        if (args.length < 3) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: " + getUsage(sender)));
            return;
        }

        // Parse coordinates. parseBlockPos handles ~ (relative) coordinates automatically.
        BlockPos origin = parseBlockPos(sender, args, 0, false); // 0 is the starting index for coords

        // Parse optional arguments with defaults and validation
        // 'size' is the half-width/depth (radius from center)
        int size = args.length > 3 ? parseInt(args[3], 3, 20) : 7; // Min 3 (7x7 room), Max 20 (41x41 room)

        // 'height' is the interior air space height.
        // We'll enforce a minimum of 3 (for a 3-block tall interior).
        int height = args.length > 4 ? parseInt(args[4], 3, 15) : 6; // Default to 6 for a 6-block tall interior

        // 'includeDoor' is a boolean flag
        boolean includeDoor = args.length > 5 ? parseBoolean(args[5]) : true; // Default true

        // Get the world where the command is executed
        World worldIn = sender.getEntityWorld();
        if (worldIn == null) {
            throw new CommandException("commands.buildscp002.no_world", sender.getName());
        }

        sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Attempting to build SCP-002 chamber at " +
                origin.getX() + ", " + origin.getY() + ", " + origin.getZ() + "..."));

        try {
            // Call RoomBuilder to build the structure
            // Using the refined parameters
            RoomBuilder.buildRoom(worldIn, origin, size, height, includeDoor);
            sender.sendMessage(new TextComponentString(TextFormatting.AQUA + "SCP-002 chamber successfully built at X:" +
                    origin.getX() + " Y:" + origin.getY() + " Z:" + origin.getZ() + "!"));
        } catch (Exception e) {
            // Catch any potential errors during room building
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Failed to build chamber! Error: " + e.getMessage()));
            SCP002.logger.error("Error building SCP-002 chamber at {}: {}", origin, e.getMessage());
            throw new CommandException("commands.buildscp002.failed", e.getMessage());
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2; // Operator level
    }
}
