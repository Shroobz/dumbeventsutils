package Klyrithon.dumbeventsutils.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.client.sound.v1.FabricSoundInstance;
import net.minecraft.client.sound.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class CustomSound {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private Identifier soundIdentifier;
    private String targetWorldName;
    private boolean isPlaying = false;
    public static SoundEvent SOUND_EVENT;
    long lastPlayTime = 0;
    private final Set<SoundEvent> noteBlockSounds = new HashSet<>();
    private long songlength;

    public void init(String soundName, String worldName, long songlength) {
        this.soundIdentifier = Identifier.of(soundName);
        this.targetWorldName = worldName;
        this.songlength = songlength*1000;
        SOUND_EVENT = SoundEvent.of(Identifier.of(soundName));

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);

    }
    

    private void onClientTick(MinecraftClient client) {
        if (client.player != null && client.world != null) {
            long CurrentTime = System.currentTimeMillis();
            String currentWorldName = client.world.getRegistryKey().getValue().toString();

            if (currentWorldName.contains(targetWorldName) && (!isPlaying || CurrentTime - lastPlayTime > songlength)) {
                playLoopingSound();
            } else if (!currentWorldName.contains(targetWorldName) && isPlaying) {
                stopLoopingSound();
            }
        }
    }


    private void playLoopingSound() {
        client.getSoundManager().play(PositionedSoundInstance.master(
                SoundEvent.of(soundIdentifier),
                1.0f  // Pitch
        ));
        lastPlayTime = System.currentTimeMillis();
        isPlaying = true;
    }

    private void stopLoopingSound() {
        client.getSoundManager().stopSounds(soundIdentifier, SoundCategory.PLAYERS);
        isPlaying = false;
    }
}

