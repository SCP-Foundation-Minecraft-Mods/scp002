package com.scpfoundation.scp002.registry;

import com.scpfoundation.scp002.SCP002;
import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = SCP002.MODID) // This annotation tells Forge to look for @SubscribeEvent methods here
public class ModSounds {

    // --- SoundEvent Declarations ---
    // These are the *events* that you will play or use in SoundTypes
    public static final SoundEvent SCP002_WHISPERS = registerSoundEvent("scp002.whispers");
    public static final SoundEvent TV_STATIC_SOUND_EVENT = registerSoundEvent("scp002.tv_static_loop"); // Renamed for clarity as a loop

    // Assimilation/Interaction Sounds
    public static final SoundEvent ASSIMILATION_START = registerSoundEvent("scp002.assimilation_start");
    public static final SoundEvent HATCH_CREAK = registerSoundEvent("scp002.hatch_creak");
    public static final SoundEvent HATCH_THUD = registerSoundEvent("scp002.hatch_thud");

    // Flesh Block Sounds
    public static final SoundEvent FLESH_BREAK = registerSoundEvent("block.flesh.break");
    public static final SoundEvent FLESH_STEP = registerSoundEvent("block.flesh.step");
    public static final SoundEvent FLESH_PLACE = registerSoundEvent("block.flesh.place");
    public static final SoundEvent FLESH_HIT = registerSoundEvent("block.flesh.hit");
    public static final SoundEvent FLESH_FALL = registerSoundEvent("block.flesh.fall");

    // Nerve Floor Specific Sounds
    public static final SoundEvent FLESH_PULSE = registerSoundEvent("block.flesh.pulse"); // For nerve block activation

    // Eye Block Specific Sounds
    public static final SoundEvent EYE_BLINK = registerSoundEvent("block.eye.blink");
    public static final SoundEvent EYE_WATCH = registerSoundEvent("block.eye.watch");

    // Bone Sounds
    public static final SoundEvent BONE_BREAK = registerSoundEvent("block.bone.break");
    public static final SoundEvent BONE_STEP = registerSoundEvent("block.bone.step");
    public static final SoundEvent BONE_PLACE = registerSoundEvent("block.bone.place");
    public static final SoundEvent BONE_HIT = registerSoundEvent("block.bone.hit");
    public static final SoundEvent BONE_FALL = registerSoundEvent("block.bone.fall");

    // Creaky Wood (for TV stand)
    public static final SoundEvent WOOD_CREAKY_BREAK = registerSoundEvent("block.wood_creaky.break");
    public static final SoundEvent WOOD_CREAKY_STEP = registerSoundEvent("block.wood_creaky.step");
    public static final SoundEvent WOOD_CREAKY_PLACE = registerSoundEvent("block.wood_creaky.place");
    public static final SoundEvent WOOD_CREAKY_HIT = registerSoundEvent("block.wood_creaky.hit");
    public static final SoundEvent WOOD_CREAKY_FALL = registerSoundEvent("block.wood_creaky.fall");

    public static final SoundEvent RADIO_STATIC_SOUND_EVENT = registerSoundEvent("radio_static.sound");

    public static SoundEvent FLESH_TELEPORT_SOUND;

    // --- SoundType Definitions ---
    // These define the set of sounds a block uses for common interactions (break, step, place, hit, fall)
    public static final SoundType FLESH_SQUISHY = new SoundType(
            1.0F, // volume
            1.0F, // pitch
            FLESH_BREAK,
            FLESH_STEP,
            FLESH_PLACE,
            FLESH_HIT,
            FLESH_FALL
    );

    public static final SoundType FLESH_SQUISHY_WET = new SoundType(
            1.1F, // slightly louder
            0.9F, // slightly lower pitch
            FLESH_BREAK, // Can reuse generic flesh sounds
            FLESH_STEP,
            FLESH_PLACE,
            FLESH_HIT,
            FLESH_FALL
    );

    public static final SoundType FLESH_EYE = new SoundType(
            0.7F, // Softer volume
            1.0F,
            FLESH_BREAK, // Can reuse generic flesh sounds for breaking, etc.
            FLESH_STEP,
            FLESH_PLACE,
            EYE_BLINK, // Maybe a different sound on hit
            FLESH_FALL
    );

    public static final SoundType BONE_SOUND = new SoundType(
            1.0F,
            0.8F, // Lower pitch for solid bone
            BONE_BREAK,
            BONE_STEP,
            BONE_PLACE,
            BONE_HIT,
            BONE_FALL
    );

    public static final SoundType WOOD_CREAKY_SOUND = new SoundType(
            0.9F,
            1.0F,
            WOOD_CREAKY_BREAK,
            WOOD_CREAKY_STEP,
            WOOD_CREAKY_PLACE,
            WOOD_CREAKY_HIT,
            WOOD_CREAKY_FALL
    );

    // For the TV_STATIC block, you might not want default place/break sounds
    // but rather for the TileEntity to play the loop.
    public static final SoundType TV_STATIC_SOUND_TYPE = new SoundType(
            0.8F, // Volume
            1.0F, // Pitch
            FLESH_BREAK, // Use a placeholder/generic for break/step if TV isn't "flesh"
            FLESH_STEP,
            TV_STATIC_SOUND_EVENT, // This sound plays on block PLACE (usually)
            FLESH_HIT,
            FLESH_FALL
    );


    // --- SoundEvent Registration Method ---
    // This method creates a SoundEvent and sets its registry name
    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(SCP002.MODID, name);
        return new SoundEvent(id).setRegistryName(id);
    }

    // --- Forge Registry Event Subscriber ---
    // This method is called by Forge to register all your SoundEvents
    @SubscribeEvent
    public static void onRegisterSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> registry = event.getRegistry();

        registry.registerAll(
                // Register all your custom SoundEvents here
                SCP002_WHISPERS,
                TV_STATIC_SOUND_EVENT,
                ASSIMILATION_START,
                HATCH_CREAK,
                HATCH_THUD,
                FLESH_BREAK,
                FLESH_STEP,
                FLESH_PLACE,
                FLESH_HIT,
                FLESH_FALL,
                FLESH_PULSE,
                EYE_BLINK,
                EYE_WATCH,
                BONE_BREAK,
                BONE_STEP,
                BONE_PLACE,
                BONE_HIT,
                BONE_FALL,
                WOOD_CREAKY_BREAK,
                WOOD_CREAKY_STEP,
                WOOD_CREAKY_PLACE,
                WOOD_CREAKY_HIT,
                WOOD_CREAKY_FALL,
                RADIO_STATIC_SOUND_EVENT,
                FLESH_TELEPORT_SOUND
        );
    }
}
