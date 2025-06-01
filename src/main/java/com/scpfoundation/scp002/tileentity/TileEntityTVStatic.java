package com.scpfoundation.scp002.tileentity;

import com.scpfoundation.scp002.registry.ModSounds;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileEntityTVStatic extends TileEntity implements ITickable {

    private int tickCounter = 0;

    public TileEntityTVStatic() {
        // No special initialization needed here.
    }

    @Override
    public void update() {
        // Only run on the server side to prevent desync and duplicated sound events
        if (!this.world.isRemote) {
            tickCounter++;

            // Play the static sound every 15 ticks (approx. every 0.75 seconds)
            if (tickCounter >= 15) { // This creates a "burst" of static every 15 ticks
                this.world.playSound(
                        null,
                        this.pos.getX() + 0.5,
                        this.pos.getY() + 0.5,
                        this.pos.getZ() + 0.5,
                        ModSounds.TV_STATIC_SOUND_EVENT,
                        SoundCategory.BLOCKS,
                        0.6F,
                        1.0F + (this.world.rand.nextFloat() - 0.5F) * 0.2F
                );
                tickCounter = 0;
            }
        }
    }
}
