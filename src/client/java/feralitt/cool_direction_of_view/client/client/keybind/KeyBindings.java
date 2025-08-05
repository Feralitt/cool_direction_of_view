package feralitt.cool_direction_of_view.client.keybind;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private static KeyBinding snapToDirectionKey;
    private static long keyPressTime = 0;
    private static boolean wasPressed = false;

    public static void register() {
        snapToDirectionKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cool_direction_of_view.snap",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UP,
                "category.cool_direction_of_view.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            boolean isPressed = snapToDirectionKey.isPressed();

            if (isPressed && !wasPressed) {
                // First press - snap horizontal direction
                keyPressTime = System.currentTimeMillis();
                snapToCardinalDirection(client);
            } else if (isPressed && wasPressed) {
                // Continued press - check if 2 seconds have passed
                if (System.currentTimeMillis() - keyPressTime >= 2000) {
                    client.player.setPitch(0);
                    keyPressTime = Long.MAX_VALUE; // Prevent repeated execution
                }
            }

            wasPressed = isPressed;
        });
    }

    private static void snapToCardinalDirection(net.minecraft.client.MinecraftClient client) {
        if (client.player == null) return;

        float yaw = client.player.getYaw();
        float pitch = client.player.getPitch();

        // Find nearest cardinal direction (0, 90, 180, 270)
        float snappedYaw = Math.round(yaw / 90) * 90;

        client.player.setYaw(snappedYaw);
        client.player.setPitch(pitch); // Keep original pitch
        client.player.setHeadYaw(snappedYaw);
    }
}