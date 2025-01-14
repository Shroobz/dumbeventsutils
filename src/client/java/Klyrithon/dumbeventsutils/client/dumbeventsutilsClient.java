package Klyrithon.dumbeventsutils.client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.collection.ArrayListDeque;
import org.lwjgl.glfw.GLFW;

public class dumbeventsutilsClient implements ClientModInitializer {
    private static final Option CONFIG = Option.load();
    private int lastMessageCount = 0;
    @Override
    public void onInitializeClient() {
        KeyBinding toggleKey;

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Toggle Auto GG",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_EQUAL, // Default key: =
                "key.categories.misc"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleKey.wasPressed()) {
                // Toggle the option
                CONFIG.toggleOption = !CONFIG.toggleOption;
                CONFIG.save();

                // Print the new state to the chat
                if (client.player != null) {
                    if (CONFIG.toggleOption == true) {
                        client.player.sendMessage(net.minecraft.text.Text.of("§bToggle AutoGG: §aON"), false);
                    } else {
                        client.player.sendMessage(net.minecraft.text.Text.of("§bToggle AutoGG: §cOFF"), false);
                    }
                }
            }
        });

        CustomSound rushE = new CustomSound();
        rushE.init("dumbeventsutils:rushe", "tnttag", 159);
        CustomSound Anvil = new CustomSound();
        Anvil.init("dumbeventsutils:numba1", "anvilfalling", 165);
        CustomSound Lava = new CustomSound();
        Lava.init("dumbeventsutils:mountainman", "lavarising", 182);
        CustomSound Spleef = new CustomSound();
        Spleef.init("dumbeventsutils:tigerman", "spleef", 245);
        CustomSound DeathRun = new CustomSound();
        DeathRun.init("dumbeventsutils:isthatasansundertalereferenceinthebig2025", "deathrun", 315);
        CustomSound Avalanche = new CustomSound();
        Avalanche.init("dumbeventsutils:yetuman", "avalanche", 155);
        CustomSound IceBoat = new CustomSound();
        IceBoat.init("dumbeventsutils:stormsand", "iceboatracing", 232);


        System.out.println("sigma???");
        ClientReceiveMessageEvents.GAME.register((player, world) -> {
            if (!CONFIG.toggleOption) return;
            System.out.println("new chat messaeg omh: " + player.getString());
            if (matchesWinnerMessage(player.getString())) {
                MinecraftClient.getInstance().player.networkHandler.sendChatMessage("gg");
            }
        });
    }

    private boolean matchesWinnerMessage(String message) {
        // Simplified matching logic for the winner message
        if (message.contains(":")) {
            return false;
        }
        return (message.contains("is the winner!") || message.contains("Trappers are the winners!") || message.contains("Runners are the winners!"));
    }
}
