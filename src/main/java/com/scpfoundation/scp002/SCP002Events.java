package com.scpfoundation.scp002;

import com.scpfoundation.scp002.registry.ModBlocks;
import com.scpfoundation.scp002.registry.ModSounds;
import com.scpfoundation.scp002.util.RoomBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = SCP002.MODID)
public class SCP002Events {

    // Timer to track how long a player has been inside SCP-002
    private static final Map<UUID, Integer> insideTimer = new HashMap<>();
    private static final Random RAND = new Random(); // For random effects/furniture

    // Define assimilation parameters
    private static final int ASSIMILATION_TIME_TICKS = 20 * 25; // 25 seconds (20 ticks per second)
    private static final int WHISPER_INTERVAL_TICKS = 20 * 5; // Every 5 seconds

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return; // Only process at the end of the tick

        EntityPlayer player = event.player;
        World world = player.world;

        // Ensure this logic only runs on the server side
        if (!world.isRemote) {
            UUID playerId = player.getUniqueID();

            // --- SCP-002 Interior Detection ---
            // Assuming `isInSCP002` NBT tag is set when the player is teleported by BlockFleshDoor
            boolean isInSCP002Interior = player.getEntityData().getBoolean("isInSCP002");

            if (isInSCP002Interior) {
                // Increment timer
                int timer = insideTimer.getOrDefault(playerId, 0) + 1;
                insideTimer.put(playerId, timer);

                // Play whispers periodically
                if (timer % WHISPER_INTERVAL_TICKS == 0) {
                    world.playSound(
                            null, // null for no specific entity source (plays at location)
                            player.posX, player.posY, player.posZ,
                            ModSounds.SCP002_WHISPERS, // Use your custom whisper sound event
                            SoundCategory.AMBIENT,
                            0.7F + RAND.nextFloat() * 0.3F, // Slight volume variation
                            0.8F + RAND.nextFloat() * 0.4F // Pitch variation
                    );
                }

                // Spawn unsettling particles around the player
                if (world instanceof WorldServer) { // Ensure world is a WorldServer for spawnParticle
                    ((WorldServer) world).spawnParticle(
                            EnumParticleTypes.DRIP_LAVA, // Or custom particle, can be colored
                            player.posX + RAND.nextDouble() * 0.8 - 0.4,
                            player.posY + RAND.nextDouble() * 1.8,
                            player.posZ + RAND.nextDouble() * 0.8 - 0.4,
                            1, // Count
                            0.0, 0.0, 0.0, // Offset X, Y, Z
                            0.005 // Speed (small for slow drips)
                    );
                    ((WorldServer) world).spawnParticle(
                            EnumParticleTypes.SMOKE_NORMAL,
                            player.posX + RAND.nextDouble() * 0.8 - 0.4,
                            player.posY + RAND.nextDouble() * 1.8,
                            player.posZ + RAND.nextDouble() * 0.8 - 0.4,
                            2, 0.0, 0.0, 0.0, 0.01
                    );
                }

                // --- Assimilation Trigger ---
                if (timer >= ASSIMILATION_TIME_TICKS) {
                    player.sendMessage(new TextComponentString(TextFormatting.RED + "You feel yourself becoming part of the room..."));
                    world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.ASSIMILATION_START, SoundCategory.HOSTILE, 1.5F, 1.0F); // Play assimilation sound

                    performAssimilation(world, player); // Call the assimilation logic

                    insideTimer.remove(playerId); // Reset timer for this player
                    player.getEntityData().setBoolean("isInSCP002", false); // Remove the NBT tag
                }

            } else {
                // Player is no longer detected inside SCP-002, reset timer
                if (insideTimer.containsKey(playerId)) {
                    insideTimer.remove(playerId);
                    player.sendMessage(new TextComponentString(TextFormatting.GREEN + "You've escaped the confines of SCP-002... for now."));
                }
            }
        }
    }

    /**
     * Handles the assimilation process: player death, item conversion, and new furniture generation.
     * This method assumes player has been teleported to assimilation location previously.
     *
     * @param world  The world object.
     * @param player The player being assimilated.
     */
    private static void performAssimilation(World world, EntityPlayer player) {
        // --- Player Assimilation (Death) ---
        // Kill the player. This will trigger their death screen and respawn.
        player.attackEntityFrom(SCP002.DAMAGE_SOURCE_ASSIMILATION, Float.MAX_VALUE);

        // --- Item Conversion/Spawning New Furniture ---
        // Find a suitable nearby spot within the SCP-002 interior where the player was.
        BlockPos assimilationSpot = player.getPosition(); // Use player's current position

        // Randomly choose what furniture block the player becomes
        // Now RAND.nextInt(10) will generate numbers 0 through 9, covering all 10 possible outcomes below.
        IBlockState assimilatedFurnitureState = null;
        switch (RAND.nextInt(10)) { // Changed range to cover all lore-accurate items
            case 0:
                assimilatedFurnitureState = ModBlocks.SOFA_FLESH.getDefaultState(); // Sofas
                break;
            case 1:
                assimilatedFurnitureState = ModBlocks.CARPET_FLESH.getDefaultState(); // Rugs/Carpets
                break;
            case 2:
                assimilatedFurnitureState = ModBlocks.TV_STAND.getDefaultState(); // Televisions (represented by TV stand)
                break;
            case 3:
                assimilatedFurnitureState = ModBlocks.LAMP_SHADE.getDefaultState(); // Lamps
                break;
            case 4:
                assimilatedFurnitureState = ModBlocks.RADIO_FLESH.getDefaultState(); // Radios - NEW!
                break;
            case 5:
                assimilatedFurnitureState = ModBlocks.BOOK_UNKNOWN_1.getDefaultState(); // Books - NEW!
                break;
            case 6:
                assimilatedFurnitureState = ModBlocks.BOOK_UNKNOWN_2.getDefaultState(); // Books - NEW!
                break;
            case 7:
                assimilatedFurnitureState = ModBlocks.BOOK_UNKNOWN_3.getDefaultState(); // Books - NEW!
                break;
            case 8:
                // Option for children's books if desired, or combine into fewer book types
                assimilatedFurnitureState = ModBlocks.CHILDREN_BOOK_FLESH_1.getDefaultState(); // Children's Books - NEW!
                break;
            case 9:
                // If you want to include Tentacle Flesh or other unique fleshy growths,
                // you could add more cases or replace one of the book cases.
                assimilatedFurnitureState = ModBlocks.TENTACLE_FLESH.getDefaultState(); // Or another Children's Book or Potted Plant
                break;
        }

        // Add a null check just in case RAND.nextInt() or block registration goes wrong (good practice)
        if (assimilatedFurnitureState == null) {
            SCP002.logger.warn("Assimilated furniture state was null! Defaulting to WALL_MEAT.");
            assimilatedFurnitureState = ModBlocks.WALL_MEAT.getDefaultState(); // Fallback
        }

        // Build a small, contained chamber around the player's assimilation spot,
        // potentially placing the new furniture block in the center.
        // Parameters: world, origin (center of assimilation), minSize, maxSize, furnitureBlock
        RoomBuilder.buildSmallChamber(world, assimilationSpot, 1, 1, assimilatedFurnitureState); // 1x1 size for just a furniture block
    }
}
