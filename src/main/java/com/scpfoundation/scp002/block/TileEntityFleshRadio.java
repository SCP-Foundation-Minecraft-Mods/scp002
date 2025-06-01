package com.scpfoundation.scp002.block;

import com.scpfoundation.scp002.registry.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFleshRadio extends TileEntity implements ITickable {

    @SideOnly(Side.CLIENT)
    private ISound soundInstance; // Client-side sound instance

    @Override
    public void update() {
        // Play sound only on the client side to avoid server-side sound issues
        if (this.world.isRemote) { // Check if it's the client world
            // Check if the sound instance is null OR if the sound is no longer playing
            if (soundInstance == null || !Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(soundInstance)) {
                // Instantiate PositionedSoundRecord directly using its constructor for 1.12.2
                soundInstance = new PositionedSoundRecord(
                        ModSounds.RADIO_STATIC_SOUND_EVENT, // The SoundEvent to play
                        SoundCategory.BLOCKS,               // The category of the sound (e.g., BLOCKS, AMBIENT, MUSIC)
                        0.8F,                               // Volume (0.0F to 1.0F)
                        1.0F,                               // Pitch (0.5F to 2.0F)
                        (float)this.pos.getX() + 0.5F,      // X coordinate (center of block)
                        (float)this.pos.getY() + 0.5F,      // Y coordinate (center of block)
                        (float)this.pos.getZ() + 0.5F       // Z coordinate (center of block)
                );
                // Get the Minecraft client instance, then its SoundHandler, and play the sound
                Minecraft.getMinecraft().getSoundHandler().playSound(soundInstance);
            }
        }
    }

    @Override
    public void invalidate() { // Called when the TileEntity is removed from the world
        super.invalidate();
        // Stop the looping sound when the block is broken or unloaded
        if (this.world.isRemote && soundInstance != null) {
            Minecraft.getMinecraft().getSoundHandler().stopSound(soundInstance);
            soundInstance = null;
        }
    }

    @Override
    public void onChunkUnload() { // Called when the chunk containing the TileEntity unloads
        super.onChunkUnload();
        if (this.world.isRemote && soundInstance != null) {
            Minecraft.getMinecraft().getSoundHandler().stopSound(soundInstance);
            soundInstance = null;
        }
    }
}