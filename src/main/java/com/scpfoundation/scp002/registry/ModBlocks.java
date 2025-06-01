package com.scpfoundation.scp002.registry;

import com.scpfoundation.scp002.SCP002;
import com.scpfoundation.scp002.block.*; // Import all your custom block classes
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs; // Import CreativeTabs
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer; // Added for BlockEye and other custom blocks that might use it
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;
import java.util.Random; // Added for BlockEye

@Mod.EventBusSubscriber(modid = SCP002.MODID)
public class ModBlocks {

    // --- Existing Block Declarations ---
    public static Block FLOOR_BASIC;
    public static Block FLOOR_NERVE;
    public static Block WALL_MEAT;
    public static Block WALL_EYE;
    public static Block DOOR_FLESH; // This is BlockFleshDoor
    public static Block LADDER_FLESH; // This is FleshLadderBlock
    public static Block TENTACLE_FLESH;
    public static Block SUPPORT_BONE;
    public static Block SOFA_FLESH;
    public static Block TV_STAND;
    public static Block TV_STATIC; // This is BlockTVStatic
    public static Block LAMP_SHADE;

    // --- NEW Block Declarations ---
    public static Block CARPET_FLESH; // This is BlockFleshCarpet
    public static Block RADIO_FLESH; // This is BlockFleshRadio
    public static Block BOOK_UNKNOWN_1;
    public static Block BOOK_UNKNOWN_2;
    public static Block BOOK_UNKNOWN_3;
    public static Block CHILDREN_BOOK_FLESH_1;
    public static Block CHILDREN_BOOK_FLESH_2;
    public static Block CHILDREN_BOOK_FLESH_3;
    public static Block CHILDREN_BOOK_FLESH_4;
    public static Block POTTED_PLANT_FLESH;
    public static Block WINDOW_INVISIBLE_FLESH; // This is BlockFleshWindow


    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();

        // --- Existing Block Registrations ---
        // BlockBase constructors now need the 'isFlesh' boolean parameter
        // public BlockBase(String name, Material material, CreativeTabs tab, SoundType soundType, float hardness, float resistance, boolean isFlesh)
        // public BlockBase(String name, Material material, CreativeTabs tab, SoundType soundType, float hardness, float resistance, float lightLevel, boolean isFlesh)

        FLOOR_BASIC = new BlockBase("floor_basic", Material.CLAY, CreativeTabs.DECORATIONS,
                ModSounds.FLESH_SQUISHY, 0.8f, 4.0f, false); // NOT flesh

        FLOOR_NERVE = new BlockBase("floor_nerve", Material.CLAY, CreativeTabs.DECORATIONS,
                ModSounds.FLESH_SQUISHY_WET, 0.7f, 3.5f, 0.1f, true) { // IS flesh, with light level
            @Override
            public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                            EnumFacing facing, float hitX, float hitY, float hitZ) {
                if (!world.isRemote) {
                    world.playSound(null, pos, ModSounds.FLESH_PULSE, SoundCategory.BLOCKS, 1.0F, 1.0F + (world.rand.nextFloat() - 0.5F) * 0.2F);
                }
                return true;
            }

            @Override
            public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
                if (!worldIn.isRemote && entityIn instanceof EntityPlayer) {
                    ((EntityPlayer) entityIn).addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 20, 0, false, false));
                    ((EntityPlayer) entityIn).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10, 0, false, false));
                }
            }
        };

        WALL_MEAT = new BlockBase("wall_meat", Material.CLAY, CreativeTabs.DECORATIONS,
                ModSounds.FLESH_SQUISHY, 1.0f, 5.0f, true); // IS flesh

        // For specialized blocks (BlockEye, BlockFleshDoor, etc.), their constructors
        // should handle the 'isFlesh' property internally by calling super(material)
        // and then setting their own internal 'isFlesh' state or passing it to BlockBase.
        // Assuming their constructors are set up to handle this correctly.
        WALL_EYE = new BlockEye(Material.CLAY);

        // Make sure BlockFleshDoor's constructor doesn't rely on SCP002.instance.creativeTab
        // You would pass CreativeTabs.DECORATIONS or have your own static CreativeTab instance
        DOOR_FLESH = new BlockFleshDoor();

        LADDER_FLESH = new FleshLadderBlock();

        TENTACLE_FLESH = new BlockBase("tentacle_flesh", Material.CLAY, CreativeTabs.DECORATIONS,
                ModSounds.FLESH_SQUISHY, 0.7f, 3.0f, true); // IS flesh

        SUPPORT_BONE = new BlockBase("support_bone", Material.ROCK, CreativeTabs.BUILDING_BLOCKS,
                ModSounds.BONE_SOUND, 3.0f, 15.0f, false); // NOT flesh

        SOFA_FLESH = new BlockBase("sofa_flesh", Material.CARPET, CreativeTabs.DECORATIONS,
                ModSounds.FLESH_SQUISHY, 0.3f, 1.0f, true); // IS flesh

        TV_STAND = new BlockBase("tv_stand", Material.WOOD, CreativeTabs.DECORATIONS,
                ModSounds.WOOD_CREAKY_SOUND, 1.5f, 7.5f, false); // NOT flesh (as an object in a flesh room)

        TV_STATIC = new BlockTVStatic(Material.CIRCUITS);

        LAMP_SHADE = new BlockBase("lamp_shade", Material.REDSTONE_LIGHT, CreativeTabs.DECORATIONS,
                ModSounds.FLESH_SQUISHY, 0.5f, 2.5f, 0.8f, true); // IS flesh, with light level

        // --- NEW Block Registrations ---

        CARPET_FLESH = new BlockFleshCarpet(); // This constructor should set 'isFlesh' to true internally

        RADIO_FLESH = new BlockFleshRadio(Material.IRON); // This constructor should set 'isFlesh' to true internally

        // BOOKS (These will need custom models with defined block bounds in JSON)
        // Set their properties directly in BlockBase, but their shape is external.
        BOOK_UNKNOWN_1 = new BlockBase("book_unknown_1", Material.CLOTH, CreativeTabs.DECORATIONS,
                ModSounds.FLESH_SQUISHY, 0.1f, 0.5f, false); // NOT flesh
        BOOK_UNKNOWN_2 = new BlockBase("book_unknown_2", Material.CLOTH, CreativeTabs.DECORATIONS,
                ModSounds.FLESH_SQUISHY, 0.1f, 0.5f, false); // NOT flesh
        BOOK_UNKNOWN_3 = new BlockBase("book_unknown_3", Material.CLOTH, CreativeTabs.DECORATIONS,
                ModSounds.FLESH_SQUISHY, 0.1f, 0.5f, false); // NOT flesh

        CHILDREN_BOOK_FLESH_1 = new BlockBase("children_book_flesh_1", Material.CLOTH, CreativeTabs.DECORATIONS,
                ModSounds.FLESH_SQUISHY, 0.1f, 0.5f, true); // IS flesh
        CHILDREN_BOOK_FLESH_2 = new BlockBase("children_book_flesh_2", Material.CLOTH, CreativeTabs.DECORATIONS,
                ModSounds.FLESH_SQUISHY, 0.1f, 0.5f, true); // IS flesh
        CHILDREN_BOOK_FLESH_3 = new BlockBase("children_book_flesh_3", Material.CLOTH, CreativeTabs.DECORATIONS,
                ModSounds.FLESH_SQUISHY, 0.1f, 0.5f, true); // IS flesh
        CHILDREN_BOOK_FLESH_4 = new BlockBase("children_book_flesh_4", Material.CLOTH, CreativeTabs.DECORATIONS,
                ModSounds.FLESH_SQUISHY, 0.1f, 0.5f, true); // IS flesh

        // POTTED_PLANT_FLESH (Will need a custom model with defined block bounds in JSON)
        POTTED_PLANT_FLESH = new BlockBase("potted_plant_flesh", Material.PLANTS, CreativeTabs.DECORATIONS,
                ModSounds.FLESH_SQUISHY, 0.2f, 1.0f, true); // IS flesh
        POTTED_PLANT_FLESH.setLightLevel(0.2f); // Set light level after creation if not using constructor with it

        WINDOW_INVISIBLE_FLESH = new BlockFleshWindow(); // This constructor should set 'isFlesh' to true internally

        // Register all blocks (old and new)
        registry.registerAll(
                FLOOR_BASIC, FLOOR_NERVE, WALL_MEAT, WALL_EYE, DOOR_FLESH, LADDER_FLESH,
                TENTACLE_FLESH, SUPPORT_BONE, SOFA_FLESH, TV_STAND, TV_STATIC, LAMP_SHADE,
                // --- NEW Blocks to register ---
                CARPET_FLESH, RADIO_FLESH,
                BOOK_UNKNOWN_1, BOOK_UNKNOWN_2, BOOK_UNKNOWN_3,
                CHILDREN_BOOK_FLESH_1, CHILDREN_BOOK_FLESH_2, CHILDREN_BOOK_FLESH_3, CHILDREN_BOOK_FLESH_4,
                POTTED_PLANT_FLESH, WINDOW_INVISIBLE_FLESH
        );
    }

    @SubscribeEvent
    public static void onRegisterItemBlocks(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        registry.registerAll(
                new ItemBlock(FLOOR_BASIC).setRegistryName(Objects.requireNonNull(FLOOR_BASIC.getRegistryName())),
                new ItemBlock(FLOOR_NERVE).setRegistryName(Objects.requireNonNull(FLOOR_NERVE.getRegistryName())),
                new ItemBlock(WALL_MEAT).setRegistryName(Objects.requireNonNull(WALL_MEAT.getRegistryName())),
                new ItemBlock(WALL_EYE).setRegistryName(Objects.requireNonNull(WALL_EYE.getRegistryName())),
                new ItemBlock(LADDER_FLESH).setRegistryName(Objects.requireNonNull(LADDER_FLESH.getRegistryName())),
                new ItemBlock(TENTACLE_FLESH).setRegistryName(Objects.requireNonNull(TENTACLE_FLESH.getRegistryName())),
                new ItemBlock(SUPPORT_BONE).setRegistryName(Objects.requireNonNull(SUPPORT_BONE.getRegistryName())),
                new ItemBlock(SOFA_FLESH).setRegistryName(Objects.requireNonNull(SOFA_FLESH.getRegistryName())),
                new ItemBlock(TV_STAND).setRegistryName(Objects.requireNonNull(TV_STAND.getRegistryName())),
                new ItemBlock(TV_STATIC).setRegistryName(Objects.requireNonNull(TV_STATIC.getRegistryName())),
                new ItemBlock(LAMP_SHADE).setRegistryName(Objects.requireNonNull(LAMP_SHADE.getRegistryName())),
                // --- NEW ItemBlocks to register ---
                new ItemBlock(CARPET_FLESH).setRegistryName(Objects.requireNonNull(CARPET_FLESH.getRegistryName())),
                new ItemBlock(RADIO_FLESH).setRegistryName(Objects.requireNonNull(RADIO_FLESH.getRegistryName())),
                new ItemBlock(BOOK_UNKNOWN_1).setRegistryName(Objects.requireNonNull(BOOK_UNKNOWN_1.getRegistryName())),
                new ItemBlock(BOOK_UNKNOWN_2).setRegistryName(Objects.requireNonNull(BOOK_UNKNOWN_2.getRegistryName())),
                new ItemBlock(BOOK_UNKNOWN_3).setRegistryName(Objects.requireNonNull(BOOK_UNKNOWN_3.getRegistryName())),
                new ItemBlock(CHILDREN_BOOK_FLESH_1).setRegistryName(Objects.requireNonNull(CHILDREN_BOOK_FLESH_1.getRegistryName())),
                new ItemBlock(CHILDREN_BOOK_FLESH_2).setRegistryName(Objects.requireNonNull(CHILDREN_BOOK_FLESH_2.getRegistryName())),
                new ItemBlock(CHILDREN_BOOK_FLESH_3).setRegistryName(Objects.requireNonNull(CHILDREN_BOOK_FLESH_3.getRegistryName())),
                new ItemBlock(CHILDREN_BOOK_FLESH_4).setRegistryName(Objects.requireNonNull(CHILDREN_BOOK_FLESH_4.getRegistryName())),
                new ItemBlock(POTTED_PLANT_FLESH).setRegistryName(Objects.requireNonNull(POTTED_PLANT_FLESH.getRegistryName())),
                new ItemBlock(WINDOW_INVISIBLE_FLESH).setRegistryName(Objects.requireNonNull(WINDOW_INVISIBLE_FLESH.getRegistryName()))
                // Remember: DOOR_FLESH is handled by ITEM_DOOR_FLESH in ModItems.java
        );
    }
}
