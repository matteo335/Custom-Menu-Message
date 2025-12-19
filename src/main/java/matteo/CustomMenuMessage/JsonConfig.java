package matteo.CustomMenuMessage;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class JsonConfig {
    private static final Path path = FMLPaths.CONFIGDIR.get().resolve("custom_menu_message.json");

    public static void get() throws IOException {
        boolean fileCheck = Path.of("config/custom_menu_message.json").toFile().exists();
        if (!fileCheck) {
            CustomMenuMessage.LOGGER.info("Config not found, creating a new one");
            description();
        }
    }

    public static void description() throws IOException {
        String content =
                """
                        {
                        "Add": "-add- a few message",
                        "Remove": "-remove- reroll until a different message is find",
                        "Replace": "-replace- a specific messages and display your own instead",
                        "Chance": "the -chance- of your message to appears. 1000 = 100%",
                        
                        "Get the list of messages there": "https://minecraft.wiki/w/Splash",
                        "Note": "None of the vanilla splashes will ever show if the custom main menu of Ice and Fire is enabled, with or without my mod",
                        
                        "EnableLogs": "true",
                        "DisableIceAndFireMessages": "true",
                        
                        "add": "Change it in the MenuMessage config, in the -> modpack folder",
                        "chance": 1000,
                    
                        "addHelloWorld": "Hello World",
                        "chance": 0,
                        
                        "remove": "This text is hard to read if you play the game at the default resolution, but at 1080p it's fine!",
                        
                        "replaceTarget": "This text is hard to read if you play the game at the default resolution, but at 1080p it's fine!",
                        "replaceTo": "Hello World"
                        }
                        """;
        try (FileWriter writer = new FileWriter(path.toFile())) {
            writer.write(content);
        }
    }
}