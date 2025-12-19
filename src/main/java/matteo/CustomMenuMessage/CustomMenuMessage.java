package matteo.CustomMenuMessage;

import net.minecraft.client.resources.SplashManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

@Mod("custom_menu_message")
public class CustomMenuMessage {
    public static Logger LOGGER = LogManager.getLogger("Custom Menu Message");

    public CustomMenuMessage() throws IOException {
        LOGGER.info("baking");
        JsonConfig.get();
    }
}

