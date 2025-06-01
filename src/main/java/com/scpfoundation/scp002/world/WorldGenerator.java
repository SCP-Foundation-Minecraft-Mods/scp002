package com.scpfoundation.scp002.world;

import com.scpfoundation.scp002.SCP002;
import com.scpfoundation.scp002.util.RoomBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks; // Needed for checking bedrock
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenerator implements IWorldGenerator {

    // Define acceptable Y-level range for generation (e.g., below sea level but above true bedrock)
    private static final int MIN_GEN_Y = 10; // Avoids very shallow generation
    private static final int MAX_GEN_Y = 50; // Avoids generating too close to bedrock (Y=0) or in void

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        // Only generate in the Overworld (dimension 0)
        if (world.provider.getDimension() == 0) {
            // Convert chunk coordinates to block coordinates (center of the chunk)
            int x = chunkX * 16 + 8;
            int z = chunkZ * 16 + 8;

            // Frequency check: 1 in X chunks. Adjust this value to control rarity.
            // A common rate for unique structures is 1 in 500 to 1 in 2000 chunks.
            if (random.nextInt(750) == 0) { // Example: ~1 in 750 chunks
                // Choose a random Y-level within the defined range
                int targetY = random.nextInt(MAX_GEN_Y - MIN_GEN_Y) + MIN_GEN_Y;

                // Determine the highest solid block at the x, z column
                // We need to find a suitable underground location.
                // Start searching downwards from a safe height (e.g., 60 or 70)
                // to find a solid block, then place the structure below it.
                BlockPos surfacePos = world.getHeight(new BlockPos(x, 0, z)); // Gets highest non-air block

                // We want to generate *underground*, so let's find a solid spot.
                // Start searching down from a reasonable mid-level.
                BlockPos idealGroundPos = findSuitableGround(world, new BlockPos(x, surfacePos.getY() - 10, z), targetY);

                if (idealGroundPos != null) {
                    // Adjust the origin to be at the chosen idealGroundPos
                    // We generate the room with a radius, so ensure the origin
                    // accounts for the desired center of the room.
                    BlockPos generationOrigin = new BlockPos(idealGroundPos.getX(), idealGroundPos.getY(), idealGroundPos.getZ());

                    // Call your RoomBuilder to build the main chamber
                    // Parameters: world, origin (bottom-center of room), roomSize (radius), roomHeight (blocks high), includeDoor
                    // For a 15x15x6 interior: roomSize = 7 (radius), roomHeight = 6 (0-5, 6 layers).
                    // The RoomBuilder's `buildRoom` should place the door at the specified `origin`
                    // and build outwards/upwards from there.
                    // Assuming RoomBuilder places its floor at the origin Y.
                    RoomBuilder.buildRoom(world, generationOrigin, 7, 6, true); // Changed roomHeight to 6 for clarity (6 blocks tall)

                    SCP002.logger.info("Generated SCP-002 chamber at X:{} Y:{} Z:{}", generationOrigin.getX(), generationOrigin.getY(), generationOrigin.getZ());
                    // Optional: Store the location of this chamber if you need to reference it later
                    // e.g., in a WorldSavedData for persistent tracking or to ensure uniqueness.
                }
            }
        }
    }

    /**
     * Finds a suitable Y-level for generation, ensuring it's solid and not bedrock.
     * Searches downwards from a starting point.
     * @param world The world.
     * @param startPos The position to start searching downwards from.
     * @param preferredY The preferred Y-level for the structure's base.
     * @return A suitable BlockPos for the structure's origin, or null if none found.
     */
    private BlockPos findSuitableGround(World world, BlockPos startPos, int preferredY) {
        // First, check the preferredY. If it's solid and not bedrock, use it.
        BlockPos checkPos = new BlockPos(startPos.getX(), preferredY, startPos.getZ());
        IBlockState stateAtPreferredY = world.getBlockState(checkPos);

        // Check if the preferredY is solid and not bedrock
        if (stateAtPreferredY.getMaterial().isSolid() && stateAtPreferredY.getBlock() != Blocks.BEDROCK) {
            // Also ensure space *above* the preferredY for the room
            boolean hasSpaceAbove = true;
            for (int yOffset = 1; yOffset < 7; yOffset++) { // Check 6 blocks above for room height
                if (!world.isAirBlock(checkPos.up(yOffset))) {
                    hasSpaceAbove = false;
                    break;
                }
            }
            if (hasSpaceAbove) {
                return checkPos; // Found a suitable spot at preferredY
            }
        }

        // If preferredY isn't suitable, search downwards from the startPos
        for (int y = startPos.getY(); y >= MIN_GEN_Y; y--) {
            BlockPos currentPos = new BlockPos(startPos.getX(), y, startPos.getZ());
            IBlockState currentState = world.getBlockState(currentPos);

            // Look for a solid block that isn't bedrock
            if (currentState.getMaterial().isSolid() && currentState.getBlock() != Blocks.BEDROCK) {
                // Ensure space above this block for the entire structure
                boolean hasSpaceAbove = true;
                for (int yOffset = 1; yOffset < 7; yOffset++) { // Check 6 blocks above for room height
                    if (!world.isAirBlock(currentPos.up(yOffset))) {
                        hasSpaceAbove = false;
                        break;
                    }
                }
                if (hasSpaceAbove) {
                    return currentPos; // Return the position of the solid block
                }
            }
        }
        return null; // No suitable spot found
    }
}
