package feralitt.cool_direction_of_view.client;

import feralitt.cool_direction_of_view.client.keybind.KeyBindings;
import net.fabricmc.api.ClientModInitializer;

public class Cool_direction_of_viewClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBindings.register();
    }
}