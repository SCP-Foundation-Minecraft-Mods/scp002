package com.scpfoundation.scp002.tileentity;

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
    private ISound soundInstance;

    @Override
    public void update() {
        if (this.world.isRemote) {
            if (soundInstance == null || !Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(soundInstance)) {
                soundInstance = new PositionedSoundRecord(
                        ModSounds.RADIO_STATIC_SOUND_EVENT,
                        SoundCategory.BLOCKS,
                        0.8F,
                        1.0F,
                        (float)this.pos.getX() + 0.5F,
                        (float)this.pos.getY() + 0.5F,
                        (float)this.pos.getZ() + 0.5F
                );
                Minecraft.getMinecraft().getSoundHandler().playSound(soundInstance);
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        stopSound(); // Call helper method
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        stopSound(); // Call helper method
    }

    @SideOnly(Side.CLIENT)
    private void stopSound() {
        if (soundInstance != null) {
            Minecraft.getMinecraft().getSoundHandler().stopSound(soundInstance);
            soundInstance = null;
        }
    }
}
