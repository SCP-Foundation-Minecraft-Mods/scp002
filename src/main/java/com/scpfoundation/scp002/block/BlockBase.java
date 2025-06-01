package com.scpfoundation.scp002.block;// Essential for block states and properties

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.IProperty; // IProperty is the base for PropertyBool
import net.minecraft.block.state.BlockStateContainer; // For createBlockState()
import net.minecraft.block.state.IBlockState; // For the .with() method and block states

// Other imports you should have:
import com.scpfoundation.scp002.SCP002;
import com.scpfoundation.scp002.registry.ModPotions; // For PotionRadiation
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockBase extends Block {

    // Define a static PropertyBool for the 'powered' state.
    // All BlockBase instances will have this property in their BlockStateContainer,
    // but it will only be actively used for 'flesh' blocks.
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    // A flag to determine if this specific instance of BlockBase is an SCP-002 "flesh" block.
    // This controls whether the power/radiation logic applies to it.
    private final boolean isFlesh;

    /**
     * Base constructor for SCP-002 themed blocks with common properties.
     * This version includes the 'isFlesh' flag to enable or disable the radiation/power logic.
     *
     * @param name The unlocalized name and registry name of the block (e.g., "floor_basic").
     * @param material The material of the block (e.g., Material.CLAY, Material.ROCK).
     * @param tab The creative tab this block will appear in (e.g., CreativeTabs.DECORATIONS).
     * @param soundType The SoundType for this block (e.g., ModSounds.FLESH_SQUISHY).
     * @param hardness The block's hardness (how long it takes to mine).
     * @param resistance The block's blast resistance (how resistant it is to explosions).
     * @param isFlesh If {@code true}, this block will behave like an SCP-002 flesh block,
     * participating in the power propagation and emitting radiation when unpowered.
     */
    public BlockBase(String name, Material material, CreativeTabs tab, SoundType soundType, float hardness, float resistance, boolean isFlesh) {
        super(material);
        this.setUnlocalizedName(name);
        this.setRegistryName(SCP002.MODID, name);
        this.setCreativeTab(tab);
        this.setSoundType(soundType);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.isFlesh = isFlesh; // Store the flesh flag

        // Only apply flesh-specific properties if it's designated as a flesh block
        if (this.isFlesh) {
            // Set the default state of flesh blocks to be unpowered
            this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false));
            // Allow random ticks for flesh blocks, needed for radiation emission and power checks
            this.setTickRandomly(true);
        } else {
            // For non-flesh blocks, the default state is just the base block, no 'powered' property active.
            this.setDefaultState(this.blockState.getBaseState());
        }
    }

    /**
     * Constructor overload for blocks that also emit light.
     * Includes the 'isFlesh' flag.
     *
     * @param name The unlocalized name and registry name of the block.
     * @param material The material of the block.
     * @param tab The creative tab this block will appear in.
     * @param soundType The SoundType for this block.
     * @param hardness The block's hardness.
     * @param resistance The block's blast resistance.
     * @param lightLevel The light level this block emits (0.0F to 1.0F, where 1.0F is full light).
     * @param isFlesh If {@code true}, this block will behave like an SCP-002 flesh block.
     */
    public BlockBase(String name, Material material, CreativeTabs tab, SoundType soundType, float hardness, float resistance, float lightLevel, boolean isFlesh) {
        this(name, material, tab, soundType, hardness, resistance, isFlesh); // Call the primary constructor
        this.setLightLevel(lightLevel); // Set the light emission level
    }

    /**
     * Default constructor for non-flesh blocks (convenience overload).
     * Calls the primary constructor with {@code isFlesh = false}.
     */
    public BlockBase(String name, Material material, CreativeTabs tab, SoundType soundType, float hardness, float resistance) {
        this(name, material, tab, soundType, hardness, resistance, false); // Default to non-flesh
    }

    /**
     * Default constructor for non-flesh blocks with light emission (convenience overload).
     * Calls the primary constructor with {@code isFlesh = false}.
     */
    public BlockBase(String name, Material material, CreativeTabs tab, SoundType soundType, float hardness, float resistance, float lightLevel) {
        this(name, material, tab, soundType, hardness, resistance, lightLevel, false); // Default to non-flesh
    }

    /**
     * Public getter to check if this block instance is a flesh block.
     * Used by other blocks to determine if a neighbor is a flesh block.
     * @return {@code true} if this block is a flesh block, {@code false} otherwise.
     */
    public boolean isFlesh() {
        return this.isFlesh;
    }

    // --- Block State Handling ---
    // These methods are essential for Minecraft to properly save and retrieve the block's state
    // (including the 'powered' property for flesh blocks).

    @Override
    protected BlockStateContainer createBlockState() {
        // If it's a flesh block, include the POWERED property in its block state container.
        if (this.isFlesh) {
            return new BlockStateContainer(this, new IProperty[] {POWERED});
        }
        // Otherwise, it's a standard block with no special properties in its state.
        return new BlockStateContainer(this);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        // Only attempt to retrieve the POWERED state if this block is a flesh block.
        if (this.isFlesh) {
            return this.getDefaultState().withProperty(POWERED, meta == 1); // Meta 1 for powered, 0 for unpowered
        }
        return this.getDefaultState(); // For non-flesh, meta doesn't represent power, just return default.
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        // Only store the POWERED state in metadata if this block is a flesh block.
        if (this.isFlesh) {
            return state.getValue(POWERED) ? 1 : 0; // Return 1 if powered, 0 if unpowered
        }
        return 0; // For non-flesh, metadata is always 0.
    }

    // --- Power Detection and Propagation Logic (Active only for flesh blocks) ---

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote && this.isFlesh) {
            this.updatePowerState(worldIn, pos, state);
        }
        super.onBlockAdded(worldIn, pos, state); // Call super to maintain vanilla behavior
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        // Only run power logic if on the server side and it's a flesh block
        if (!worldIn.isRemote && this.isFlesh) {
            this.updatePowerState(worldIn, pos, state);
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos); // Call super to maintain vanilla behavior
    }

    /**
     * Determines and updates the block's powered state.
     * This method is internal and should only be called if `isFlesh` is true.
     */
    private void updatePowerState(World worldIn, BlockPos pos, IBlockState state) {
        // This method should only be called for flesh blocks, but an extra check doesn't hurt.
        if (!this.isFlesh) return;

        boolean wasPowered = state.getValue(POWERED);
        boolean isNowPowered = this.isReceivingDirectOrIndirectPower(worldIn, pos);

        if (isNowPowered && !wasPowered) {
            // If it should be powered but isn't, set to powered and schedule a tick
            // Flag 3 updates neighbors and notifies client for rendering changes.
            worldIn.setBlockState(pos, state.withProperty(POWERED, true), 3);
            worldIn.scheduleUpdate(pos, this, 1); // Schedule a tick to potentially spread power
        } else if (!isNowPowered && wasPowered) {
            // If it should not be powered but currently is, set to unpowered.
            worldIn.setBlockState(pos, state.withProperty(POWERED, false), 3);
        }
    }

    /**
     * Checks if this block is receiving power either directly (redstone)
     * or indirectly (from an adjacent powered flesh block).
     * This method is internal and should only be called if `isFlesh` is true.
     * @return {@code true} if the block is powered, {@code false} otherwise.
     */
    private boolean isReceivingDirectOrIndirectPower(World worldIn, BlockPos pos) {
        // This method should only be called for flesh blocks.
        if (!this.isFlesh) return false;

        // 1. Check for direct redstone power (simulates being "plugged in")
        // Checks power from all sides (world.isBlockPowered) and specifically from below (e.g., redstone torch).
        if (worldIn.isBlockPowered(pos) || worldIn.isSidePowered(pos, EnumFacing.DOWN)) {
            return true;
        }

        // 2. Check adjacent blocks for other powered flesh blocks
        for (EnumFacing facing : EnumFacing.values()) { // Iterate through all 6 directions
            BlockPos neighborPos = pos.offset(facing);
            IBlockState neighborState = worldIn.getBlockState(neighborPos);
            Block neighborBlock = neighborState.getBlock();

            // If the neighbor is an instance of BlockBase AND is a flesh block (using isFlesh())
            // AND that neighbor block is currently powered, then this block also becomes powered.
            if (neighborBlock instanceof BlockBase && ((BlockBase)neighborBlock).isFlesh() && neighborState.getValue(POWERED)) {
                return true;
            }
        }
        return false; // Not receiving power from any source
    }

    // --- Radiation Emission Logic (Active only for flesh blocks) ---

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        // Only apply radiation logic if on the server side and it's a flesh block
        if (!worldIn.isRemote && this.isFlesh) {
            this.updatePowerState(worldIn, pos, state); // Re-check power state on tick for robustness

            // If the block is NOT powered, emit radiation
            if (!state.getValue(POWERED)) {
                // Find all living entities (including players) within a 2-block radius around the block's center
                for (EntityLivingBase entity : worldIn.getEntitiesWithinAABB(EntityLivingBase.class,
                        this.getCollisionBoundingBox(state, worldIn, pos).grow(2.0D))) {
                    if (entity instanceof EntityPlayer) { // Only affect players
                        EntityPlayer player = (EntityPlayer) entity;
                        // Avoid affecting players in creative or spectator mode
                        if (!player.isCreative() && !player.isSpectator()) {
                            // Apply the custom RADIATION potion effect: 3 seconds duration, level 1, no bubbles
                            player.addPotionEffect(new PotionEffect(ModPotions.RADIATION_POTION, 60, 0, false, true));
                            // Also apply Nausea for a strong disorienting effect: 2 seconds duration, level 1, no bubbles
                            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 40, 0, false, true));
                            // Optional: Add other debuffs like Slowness or Weakness if desired.
                            // player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20, 0, false, true));
                        }
                    }
                }
            }
        }
        super.updateTick(worldIn, pos, state, rand); // Call super to maintain vanilla behavior
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        // Apply a brief, immediate radiation effect when a player walks on an unpowered flesh block
        if (!worldIn.isRemote && this.isFlesh && entityIn instanceof EntityPlayer) {
            IBlockState state = worldIn.getBlockState(pos);
            if (!state.getValue(POWERED)) {
                EntityPlayer player = (EntityPlayer) entityIn;
                if (!player.isCreative() && !player.isSpectator()) {
                    // Apply a shorter, immediate dose of radiation: 1.5 seconds duration
                    player.addPotionEffect(new PotionEffect(ModPotions.RADIATION_POTION, 30, 0, false, true));
                }
            }
        }
        super.onEntityWalk(worldIn, pos, entityIn); // Call super to retain any original walk effects
    }

    // --- Client-Side Rendering (Optional) ---
    // If you want your block to visually change based on its 'powered' state,
    // you will need to define different models/textures in your blockstate JSON for the 'powered' property.
    // These methods ensure that rendering updates correctly based on the block state.
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
}
