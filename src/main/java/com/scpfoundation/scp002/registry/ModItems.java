package com.scpfoundation.scp002.registry;

import com.scpfoundation.scp002.SCP002;
import com.scpfoundation.scp002.item.ItemStructureBuilder;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock; // Import ItemBlock
import net.minecraft.item.ItemDoor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects; // Import Objects for Objects.requireNonNull

@Mod.EventBusSubscriber(modid = SCP002.MODID)
public class ModItems {

    // --- Item Declarations ---
    public static Item FLESH_SCRAP;
    public static Item EYE_ORB;
    public static Item ASSIMILATED_KEYCARD;
    public static Item ITEM_DOOR_FLESH;
    public static Item STRUCTURE_BUILDER_WAND;

    // We don't declare ItemBlocks here because they are directly created
    // from the Block instances in the registration method.

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        // --- Basic Item Instantiation and Registration ---
        FLESH_SCRAP = new Item()
                .setUnlocalizedName("flesh_scrap")
                .setRegistryName(SCP002.MODID, "flesh_scrap")
                .setCreativeTab(CreativeTabs.MATERIALS);

        EYE_ORB = new Item()
                .setUnlocalizedName("eye_orb")
                .setRegistryName(SCP002.MODID, "eye_orb")
                .setCreativeTab(CreativeTabs.MISC);

        ASSIMILATED_KEYCARD = new Item()
                .setUnlocalizedName("assimilated_keycard")
                .setRegistryName(SCP002.MODID, "assimilated_keycard")
                .setCreativeTab(CreativeTabs.TOOLS);

        // ITEM_DOOR_FLESH is a special ItemDoor, handled here
        ITEM_DOOR_FLESH = new ItemDoor(ModBlocks.DOOR_FLESH)
                .setUnlocalizedName("door_flesh")
                .setRegistryName(SCP002.MODID, "door_flesh")
                .setCreativeTab(CreativeTabs.REDSTONE);

        STRUCTURE_BUILDER_WAND = new ItemStructureBuilder();

        // Register all standard items here
        registry.registerAll(
                FLESH_SCRAP,
                EYE_ORB,
                ASSIMILATED_KEYCARD,
                ITEM_DOOR_FLESH,
                STRUCTURE_BUILDER_WAND
        );
    }

    @SubscribeEvent
    public static void onRegisterItemBlocks(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        // --- Register ItemBlocks for all custom blocks ---
        // An ItemBlock allows your custom block to exist as an item in the inventory
        // and be placed in the world.
        registry.registerAll(
                new ItemBlock(ModBlocks.FLOOR_BASIC).setRegistryName(Objects.requireNonNull(ModBlocks.FLOOR_BASIC.getRegistryName())),
                new ItemBlock(ModBlocks.FLOOR_NERVE).setRegistryName(Objects.requireNonNull(ModBlocks.FLOOR_NERVE.getRegistryName())),
                new ItemBlock(ModBlocks.WALL_MEAT).setRegistryName(Objects.requireNonNull(ModBlocks.WALL_MEAT.getRegistryName())),
                new ItemBlock(ModBlocks.WALL_EYE).setRegistryName(Objects.requireNonNull(ModBlocks.WALL_EYE.getRegistryName())),
                // ITEM_DOOR_FLESH is already handled as a special ItemDoor in onRegisterItems,
                // so we do NOT register an ItemBlock for DOOR_FLESH here.
                new ItemBlock(ModBlocks.LADDER_FLESH).setRegistryName(Objects.requireNonNull(ModBlocks.LADDER_FLESH.getRegistryName())),
                new ItemBlock(ModBlocks.TENTACLE_FLESH).setRegistryName(Objects.requireNonNull(ModBlocks.TENTACLE_FLESH.getRegistryName())),
                new ItemBlock(ModBlocks.SUPPORT_BONE).setRegistryName(Objects.requireNonNull(ModBlocks.SUPPORT_BONE.getRegistryName())),
                new ItemBlock(ModBlocks.SOFA_FLESH).setRegistryName(Objects.requireNonNull(ModBlocks.SOFA_FLESH.getRegistryName())),
                new ItemBlock(ModBlocks.TV_STAND).setRegistryName(Objects.requireNonNull(ModBlocks.TV_STAND.getRegistryName())),
                new ItemBlock(ModBlocks.TV_STATIC).setRegistryName(Objects.requireNonNull(ModBlocks.TV_STATIC.getRegistryName())),
                new ItemBlock(ModBlocks.LAMP_SHADE).setRegistryName(Objects.requireNonNull(ModBlocks.LAMP_SHADE.getRegistryName())),

                // --- NEW ItemBlocks from the previous additions ---
                new ItemBlock(ModBlocks.CARPET_FLESH).setRegistryName(Objects.requireNonNull(ModBlocks.CARPET_FLESH.getRegistryName())),
                new ItemBlock(ModBlocks.RADIO_FLESH).setRegistryName(Objects.requireNonNull(ModBlocks.RADIO_FLESH.getRegistryName())),
                new ItemBlock(ModBlocks.BOOK_UNKNOWN_1).setRegistryName(Objects.requireNonNull(ModBlocks.BOOK_UNKNOWN_1.getRegistryName())),
                new ItemBlock(ModBlocks.BOOK_UNKNOWN_2).setRegistryName(Objects.requireNonNull(ModBlocks.BOOK_UNKNOWN_2.getRegistryName())),
                new ItemBlock(ModBlocks.BOOK_UNKNOWN_3).setRegistryName(Objects.requireNonNull(ModBlocks.BOOK_UNKNOWN_3.getRegistryName())),
                new ItemBlock(ModBlocks.CHILDREN_BOOK_FLESH_1).setRegistryName(Objects.requireNonNull(ModBlocks.CHILDREN_BOOK_FLESH_1.getRegistryName())),
                new ItemBlock(ModBlocks.CHILDREN_BOOK_FLESH_2).setRegistryName(Objects.requireNonNull(ModBlocks.CHILDREN_BOOK_FLESH_2.getRegistryName())),
                new ItemBlock(ModBlocks.CHILDREN_BOOK_FLESH_3).setRegistryName(Objects.requireNonNull(ModBlocks.CHILDREN_BOOK_FLESH_3.getRegistryName())),
                new ItemBlock(ModBlocks.CHILDREN_BOOK_FLESH_4).setRegistryName(Objects.requireNonNull(ModBlocks.CHILDREN_BOOK_FLESH_4.getRegistryName())),
                new ItemBlock(ModBlocks.POTTED_PLANT_FLESH).setRegistryName(Objects.requireNonNull(ModBlocks.POTTED_PLANT_FLESH.getRegistryName())),
                new ItemBlock(ModBlocks.WINDOW_INVISIBLE_FLESH).setRegistryName(Objects.requireNonNull(ModBlocks.WINDOW_INVISIBLE_FLESH.getRegistryName()))
        );
    }
}
