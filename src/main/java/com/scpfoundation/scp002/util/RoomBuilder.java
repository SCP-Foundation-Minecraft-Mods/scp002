package com.scpfoundation.scp002.util;

import com.scpfoundation.scp002.registry.ModBlocks;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class RoomBuilder {

    private static final Random rand = new Random();

    // Store common block states for efficiency and readability
    private static final IBlockState WALL_MEAT_STATE = ModBlocks.WALL_MEAT.getDefaultState();
    private static final IBlockState WALL_EYE_STATE = ModBlocks.WALL_EYE.getDefaultState();
    private static final IBlockState FLOOR_BASIC_STATE = ModBlocks.FLOOR_BASIC.getDefaultState();
    private static final IBlockState FLOOR_NERVE_STATE = ModBlocks.FLOOR_NERVE.getDefaultState();
    private static final IBlockState DOOR_FLESH_STATE = ModBlocks.DOOR_FLESH.getDefaultState();
    private static final IBlockState LADDER_FLESH_STATE = ModBlocks.LADDER_FLESH.getDefaultState();
    private static final IBlockState TENTACLE_FLESH_STATE = ModBlocks.TENTACLE_FLESH.getDefaultState();
    private static final IBlockState SUPPORT_BONE_STATE = ModBlocks.SUPPORT_BONE.getDefaultState();
    private static final IBlockState SOFA_FLESH_STATE = ModBlocks.SOFA_FLESH.getDefaultState();
    private static final IBlockState TV_STAND_STATE = ModBlocks.TV_STAND.getDefaultState();
    private static final IBlockState TV_STATIC_STATE = ModBlocks.TV_STATIC.getDefaultState();
    private static final IBlockState LAMP_SHADE_STATE = ModBlocks.LAMP_SHADE.getDefaultState();
    private static final IBlockState CARPET_FLESH_STATE = ModBlocks.CARPET_FLESH.getDefaultState(); // New
    private static final IBlockState RADIO_FLESH_STATE = ModBlocks.RADIO_FLESH.getDefaultState(); // New
    // Assuming books don't need orientation properties as much, or handle it internally
    private static final IBlockState BOOK_UNKNOWN_1_STATE = ModBlocks.BOOK_UNKNOWN_1.getDefaultState(); // New
    private static final IBlockState BOOK_UNKNOWN_2_STATE = ModBlocks.BOOK_UNKNOWN_2.getDefaultState(); // New
    private static final IBlockState BOOK_UNKNOWN_3_STATE = ModBlocks.BOOK_UNKNOWN_3.getDefaultState(); // New
    private static final IBlockState CHILDREN_BOOK_FLESH_1_STATE = ModBlocks.CHILDREN_BOOK_FLESH_1.getDefaultState(); // New
    private static final IBlockState CHILDREN_BOOK_FLESH_2_STATE = ModBlocks.CHILDREN_BOOK_FLESH_2.getDefaultState(); // New
    private static final IBlockState CHILDREN_BOOK_FLESH_3_STATE = ModBlocks.CHILDREN_BOOK_FLESH_3.getDefaultState(); // New
    private static final IBlockState CHILDREN_BOOK_FLESH_4_STATE = ModBlocks.CHILDREN_BOOK_FLESH_4.getDefaultState(); // New
    private static final IBlockState POTTED_PLANT_FLESH_STATE = ModBlocks.POTTED_PLANT_FLESH.getDefaultState(); // New

    private static final IBlockState AIR_STATE = Blocks.AIR.getDefaultState();


    /**
     * Builds a basic SCP-002 room structure. This method is ideal for initial room generation.
     *
     * @param world The world to build the room in.
     * @param origin The central lower corner (e.g., floor level) of the room.
     * @param roomSize The half-width/half-depth of the square room (e.g., 7 for a 15x15 room).
     * @param roomHeight The height of the interior air space (e.g., 6 for 6 blocks tall).
     * @param includeDoor If true, a flesh door will be placed.
     */
    public static void buildRoom(World world, BlockPos origin, int roomSize, int roomHeight, boolean includeDoor) {
        // Ensure minimum dimensions for a functional room
        if (roomSize < 3) roomSize = 3; // Minimum 7x7 outer walls
        if (roomHeight < 3) roomHeight = 3; // Minimum 3 blocks interior height

        // Calculate actual bounds for iterating
        int minX = -roomSize;
        int maxX = roomSize;
        int minZ = -roomSize;
        int maxZ = roomSize;
        int minY = 0; // Relative to origin (floor level)
        int interiorMaxY = roomHeight - 1; // Highest Y-level for interior air blocks
        int ceilingY = roomHeight; // Y-level for the ceiling blocks (1 block thick)


        // 1. Core Room Structure (Walls, Floor, Ceiling)
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= ceilingY; y++) { // Iterate up to and including the ceiling Y
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos currentPos = origin.add(x, y, z);

                    boolean isFloor = (y == minY);
                    boolean isCeiling = (y == ceilingY);
                    boolean isOuterWall = (x == minX || x == maxX || z == minZ || z == maxZ);

                    if (isFloor) {
                        world.setBlockState(currentPos, rand.nextBoolean() ? FLOOR_BASIC_STATE : FLOOR_NERVE_STATE);
                    } else if (isCeiling) {
                        world.setBlockState(currentPos, WALL_MEAT_STATE);
                    } else if (isOuterWall) {
                        // Place wall blocks
                        world.setBlockState(currentPos, rand.nextInt(4) == 0 ? WALL_EYE_STATE : WALL_MEAT_STATE);
                    } else {
                        // Clear the interior space
                        world.setBlockToAir(currentPos);
                    }
                }
            }
        }

        // 2. Add structural bone supports underneath the floor
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                BlockPos supportPos = origin.add(x, minY - 1, z);
                world.setBlockState(supportPos, SUPPORT_BONE_STATE);
            }
        }

        // Variable to store the actual facing of the door, if placed
        EnumFacing actualDoorFacing = null;

        // 3. Place Door (if required)
        if (includeDoor) {
            // Pick a random wall face for door placement
            EnumFacing doorFacing = EnumFacing.HORIZONTALS[rand.nextInt(4)];
            BlockPos doorBottomPos;
            IBlockState orientedDoorState = DOOR_FLESH_STATE;

            PropertyDirection FACING = BlockHorizontal.FACING;

            switch (doorFacing) {
                case NORTH:
                    doorBottomPos = origin.add(0, minY + 1, minZ); // Offset to be within wall
                    orientedDoorState = orientedDoorState.withProperty(FACING, EnumFacing.NORTH);
                    break;
                case SOUTH:
                    doorBottomPos = origin.add(0, minY + 1, maxZ); // Offset to be within wall
                    orientedDoorState = orientedDoorState.withProperty(FACING, EnumFacing.SOUTH);
                    break;
                case WEST:
                    doorBottomPos = origin.add(minX, minY + 1, 0); // Offset to be within wall
                    orientedDoorState = orientedDoorState.withProperty(FACING, EnumFacing.WEST);
                    break;
                case EAST:
                default:
                    doorBottomPos = origin.add(maxX, minY + 1, 0); // Offset to be within wall
                    orientedDoorState = orientedDoorState.withProperty(FACING, EnumFacing.EAST);
                    break;
            }

            // Ensure the door placement spot is clear before placing
            if (world.isAirBlock(doorBottomPos) && world.isAirBlock(doorBottomPos.up())) {
                world.setBlockState(doorBottomPos, orientedDoorState);
                world.setBlockState(doorBottomPos.up(), orientedDoorState.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER));
                actualDoorFacing = doorFacing; // Store the facing of the placed door
            } else {
                // Fallback: place door at a fixed spot (e.g., North wall) if random spot is blocked
                doorBottomPos = origin.add(0, minY + 1, minZ);
                orientedDoorState = DOOR_FLESH_STATE.withProperty(FACING, EnumFacing.NORTH);
                // Ensure fallback spot is clear, if not, no door will be placed.
                if (world.isAirBlock(doorBottomPos) && world.isAirBlock(doorBottomPos.up())) {
                    world.setBlockState(doorBottomPos, orientedDoorState);
                    world.setBlockState(doorBottomPos.up(), orientedDoorState.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER));
                    actualDoorFacing = EnumFacing.NORTH; // Store fallback facing
                }
            }
        }

        // 4. Place Fixed and Random Furniture/Details
        int interiorMinX = minX + 1;
        int interiorMaxX = maxX - 1;
        int interiorMinZ = minZ + 1;
        int interiorMaxZ = maxZ - 1;
        int floorY = minY + 1; // Y-level of the interior floor for furniture placement

        // Sofa against a random wall (single block sofa assumed)
        // If your sofa is truly 2 blocks wide, you'd need a more complex placement logic here.
        if (roomSize >= 3) {
            EnumFacing sofaWall = EnumFacing.HORIZONTALS[rand.nextInt(4)];
            BlockPos sofaPos;
            IBlockState sofaOrientation = SOFA_FLESH_STATE;

            // Choose a spot slightly offset from the exact corner to allow space
            if (sofaWall == EnumFacing.NORTH) {
                sofaPos = origin.add(rand.nextInt(interiorMaxX - interiorMinX - 1) + interiorMinX + 1, floorY, interiorMinZ);
                sofaOrientation = sofaOrientation.withProperty(BlockHorizontal.FACING, EnumFacing.NORTH);
            } else if (sofaWall == EnumFacing.SOUTH) {
                sofaPos = origin.add(rand.nextInt(interiorMaxX - interiorMinX - 1) + interiorMinX + 1, floorY, interiorMaxZ);
                sofaOrientation = sofaOrientation.withProperty(BlockHorizontal.FACING, EnumFacing.SOUTH);
            } else if (sofaWall == EnumFacing.WEST) {
                sofaPos = origin.add(interiorMinX, floorY, rand.nextInt(interiorMaxZ - interiorMinZ - 1) + interiorMinZ + 1);
                sofaOrientation = sofaOrientation.withProperty(BlockHorizontal.FACING, EnumFacing.WEST);
            } else { // EAST
                sofaPos = origin.add(interiorMaxX, floorY, rand.nextInt(interiorMaxZ - interiorMinZ - 1) + interiorMinZ + 1);
                sofaOrientation = sofaOrientation.withProperty(BlockHorizontal.FACING, EnumFacing.EAST);
            }

            if (world.isAirBlock(sofaPos)) {
                world.setBlockState(sofaPos, sofaOrientation);
            }
        }

        // TV and stand in a random spot, likely on the floor
        if (roomSize >= 4 && rand.nextBoolean()) { // Add some chance to spawn
            int tvX = rand.nextInt(interiorMaxX - interiorMinX - 1) + interiorMinX + 1;
            int tvZ = rand.nextInt(interiorMaxZ - interiorMinZ - 1) + interiorMinZ + 1;
            BlockPos tvStandPos = origin.add(tvX, floorY, tvZ);
            if (world.isAirBlock(tvStandPos) && world.isAirBlock(tvStandPos.up())) {
                world.setBlockState(tvStandPos, TV_STAND_STATE.withProperty(BlockHorizontal.FACING, EnumFacing.HORIZONTALS[rand.nextInt(4)]));
                world.setBlockState(tvStandPos.up(), TV_STATIC_STATE);
            }
        }

        // Lamp in a corner (randomly chosen)
        if (roomSize >= 3 && rand.nextFloat() < 0.8f) { // High chance to spawn
            BlockPos lampPos;
            int corner = rand.nextInt(4);
            if (corner == 0) lampPos = origin.add(interiorMinX, floorY, interiorMinZ);
            else if (corner == 1) lampPos = origin.add(interiorMaxX, floorY, interiorMinZ);
            else if (corner == 2) lampPos = origin.add(interiorMinX, floorY, interiorMaxZ);
            else lampPos = origin.add(interiorMaxX, floorY, interiorMaxZ);

            if (world.isAirBlock(lampPos)) {
                world.setBlockState(lampPos, LAMP_SHADE_STATE);
            }
        }

        // --- Add random scattering of new furniture types ---
        int randomFurnitureCount = rand.nextInt(roomSize * 2) + 1; // More random items
        for (int i = 0; i < randomFurnitureCount; i++) {
            int furnitureX = rand.nextInt(interiorMaxX - interiorMinX + 1) + interiorMinX;
            int furnitureZ = rand.nextInt(interiorMaxZ - interiorMinZ + 1) + interiorMinZ;
            BlockPos furnitureBasePos = origin.add(furnitureX, floorY, furnitureZ);

            if (world.isAirBlock(furnitureBasePos)) {
                IBlockState randomFurnitureState = null;
                int furnitureType = rand.nextInt(6); // 0-5 for 6 different types now

                switch (furnitureType) {
                    case 0: randomFurnitureState = CARPET_FLESH_STATE; break; // Carpets
                    case 1: randomFurnitureState = RADIO_FLESH_STATE; break; // Radios
                    case 2: randomFurnitureState = BOOK_UNKNOWN_1_STATE; break; // Generic Books
                    case 3: randomFurnitureState = CHILDREN_BOOK_FLESH_1_STATE; break; // Children's Books
                    case 4: randomFurnitureState = TENTACLE_FLESH_STATE; break; // Tentacles (already present, but good for random)
                    case 5: randomFurnitureState = POTTED_PLANT_FLESH_STATE; break; // Potted Plant
                }

                if (randomFurnitureState != null) {
                    // Handle orientation for blocks that have FACING property
                    if (randomFurnitureState.getPropertyKeys().contains(BlockHorizontal.FACING)) {
                        randomFurnitureState = randomFurnitureState.withProperty(BlockHorizontal.FACING, EnumFacing.HORIZONTALS[rand.nextInt(4)]);
                    }
                    world.setBlockState(furnitureBasePos, randomFurnitureState);

                    // For carpets, ensure they're placed on the floor
                    if (randomFurnitureState == CARPET_FLESH_STATE) {
                        // Carpets are often placed on top of blocks, but if your CARPET_FLESH is like a normal block,
                        // the default placement at floorY is fine. If it's like a vanilla carpet (layer),
                        // it should be fine.
                    }
                }
            }
        }


        // 5. Add random fleshy environmental details/supports inside (separate from furniture)
        int detailCount = rand.nextInt(roomSize * 2) + 2;
        for (int i = 0; i < detailCount; i++) {
            int detailX = rand.nextInt(interiorMaxX - interiorMinX + 1) + interiorMinX;
            int detailY = rand.nextInt(interiorMaxY - floorY + 1) + floorY; // Place within interior air space
            int detailZ = rand.nextInt(interiorMaxZ - interiorMinZ + 1) + interiorMinZ;
            BlockPos detailPos = origin.add(detailX, detailY, detailZ);

            if (world.isAirBlock(detailPos)) {
                // More chance for tentacles, less for bones in random decor
                if (rand.nextFloat() < 0.7f) {
                    world.setBlockState(detailPos, TENTACLE_FLESH_STATE);
                } else {
                    world.setBlockState(detailPos, SUPPORT_BONE_STATE);
                }
            }
        }

        // 6. Ladder for exit/verticality
        EnumFacing ladderWall = EnumFacing.HORIZONTALS[rand.nextInt(4)];
        if (includeDoor && actualDoorFacing != null) { // Only avoid if a door was actually placed
            // Ensure ladder is not on the same wall as the door
            while (ladderWall == actualDoorFacing) {
                ladderWall = EnumFacing.HORIZONTALS[rand.nextInt(4)];
            }
        }

        BlockPos ladderStartPos;
        PropertyDirection LADDER_FACING = BlockLadder.FACING;
        IBlockState orientedLadderState = LADDER_FLESH_STATE;

        // Position the ladder against the selected wall
        switch (ladderWall) {
            case NORTH:
                ladderStartPos = origin.add(0, floorY, minZ); // Place at interior wall edge
                orientedLadderState = orientedLadderState.withProperty(LADDER_FACING, EnumFacing.NORTH);
                break;
            case SOUTH:
                ladderStartPos = origin.add(0, floorY, maxZ); // Place at interior wall edge
                orientedLadderState = orientedLadderState.withProperty(LADDER_FACING, EnumFacing.SOUTH);
                break;
            case WEST:
                ladderStartPos = origin.add(minX, floorY, 0); // Place at interior wall edge
                orientedLadderState = orientedLadderState.withProperty(LADDER_FACING, EnumFacing.WEST);
                break;
            case EAST:
            default:
                ladderStartPos = origin.add(maxX, floorY, 0); // Place at interior wall edge
                orientedLadderState = orientedLadderState.withProperty(LADDER_FACING, EnumFacing.EAST);
                break;
        }

        // Place ladder from floor up to ceiling.
        // It will need to extend outside the room (upwards) to be functional,
        // typically breaking through existing blocks until it reaches a surface or void.
        for (int y = floorY; y <= world.getActualHeight(); y++) { // Extend well above the room
            BlockPos currentLadderPos = ladderStartPos.add(0, y - floorY, 0);
            if (world.isAirBlock(currentLadderPos) || world.getBlockState(currentLadderPos).getMaterial().isReplaceable()) {
                world.setBlockState(currentLadderPos, orientedLadderState);
            } else if (world.getBlockState(currentLadderPos).getBlock() != Blocks.BEDROCK) { // Don't break bedrock
                world.setBlockToAir(currentLadderPos); // Clear blocks above if not air/replaceable
                world.setBlockState(currentLadderPos, orientedLadderState);
            } else {
                break; // Stop at bedrock or unbreakble blocks
            }
        }
    }

    /**
     * Builds a smaller, more contained room, typically for an "assimilation chamber" or a very small localized growth.
     * This version can optionally be called when a player assimilates to generate a new piece of furniture.
     *
     * @param world The world to build in.
     * @param origin The central point for the small room (this will be the floor level for the placed furniture).
     * @param minSize The minimum size (half-width/depth) of the small room (e.g., 1 for 3x3 outer walls).
     * @param maxSize The maximum size (half-width/depth) of the small room (e.g., 2 for 5x5 outer walls).
     * @param furnitureBlock The specific furniture block to place in the center if desired (can be null for just a small room).
     */
    public static void buildSmallChamber(World world, BlockPos origin, int minSize, int maxSize, IBlockState furnitureBlock) {
        int actualSize = rand.nextInt(maxSize - minSize + 1) + minSize;
        int interiorHeight = rand.nextInt(2) + 2; // Interior height, e.g., 2 or 3 blocks tall

        int minX = -actualSize;
        int maxX = actualSize;
        int minZ = -actualSize;
        int maxZ = actualSize;
        int floorY = 0; // Relative to origin (origin is the floor level where furniture is placed)
        int ceilingY = interiorHeight + 1; // 1 block thick ceiling above air space

        for (int x = minX; x <= maxX; x++) {
            for (int y = floorY - 1; y <= ceilingY; y++) { // Iterate from 1 block below floor to ceiling
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos currentPos = origin.add(x, y, z);
                    boolean isFloor = (y == floorY - 1); // This is the block *below* the furniture
                    boolean isCeiling = (y == ceilingY);
                    boolean isWall = (x == minX || x == maxX || z == minZ || z == maxZ);

                    if (isFloor) {
                        world.setBlockState(currentPos, rand.nextBoolean() ? FLOOR_BASIC_STATE : FLOOR_NERVE_STATE);
                    } else if (isCeiling) {
                        world.setBlockState(currentPos, WALL_MEAT_STATE);
                    } else if (isWall) {
                        world.setBlockState(currentPos, rand.nextInt(5) == 0 ? WALL_EYE_STATE : WALL_MEAT_STATE);
                    } else {
                        // Clear the interior space where the player was/furniture will be
                        world.setBlockToAir(currentPos);
                    }
                }
            }
        }

        // Place the specific furniture block at the origin (floor level)
        if (furnitureBlock != null) {
            BlockPos furniturePos = origin; // Origin is where the player assimilated, so place it there
            if (world.isAirBlock(furniturePos)) {
                // Orient the furniture if it has a FACING property
                if (furnitureBlock.getPropertyKeys().contains(BlockHorizontal.FACING)) {
                    furnitureBlock = furnitureBlock.withProperty(BlockHorizontal.FACING, EnumFacing.HORIZONTALS[rand.nextInt(4)]);
                }
                world.setBlockState(furniturePos, furnitureBlock);

                // If it's a carpet, ensure the block below it is solid
                if (furnitureBlock == CARPET_FLESH_STATE && !world.getBlockState(furniturePos.down()).getMaterial().isSolid()) {
                    world.setBlockState(furniturePos.down(), FLOOR_BASIC_STATE); // Place a floor block if needed
                }
            }
        }

        // Add structural bone supports underneath the floor of the small chamber
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                BlockPos supportPos = origin.add(x, floorY - 2, z); // One block below the chamber's floor
                world.setBlockState(supportPos, SUPPORT_BONE_STATE);
            }
        }
    }
}
