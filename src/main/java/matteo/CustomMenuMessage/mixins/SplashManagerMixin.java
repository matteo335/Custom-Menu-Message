package matteo.CustomMenuMessage.mixins;

import matteo.CustomMenuMessage.CustomMenuMessage;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.resources.SplashManager;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.util.RandomSource;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@OnlyIn(Dist.CLIENT)
@Mixin(SplashManager.class)
public abstract class SplashManagerMixin {
    @Inject(method = "getSplash", at = @At("TAIL"), cancellable = true)
    public void CustomMenuMessage$splash(CallbackInfoReturnable<SplashRenderer> cir) throws FileNotFoundException {
        File file = Path.of("config/custom_menu_message.json").toFile();
        Scanner scanner = new Scanner(file);

        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine()).append("\n");
        }
        String fileContents = builder.toString();

        //I agree this whole system can certainly be improved, despite the performance impacts being negligible
        //group 1
        String string = "([[0-9] /[a-zA-Z\\\\-_ ’'.,:()+=![-]>/ÆÐƎƏƐƔĲŊŒẞÞǷȜæðǝəɛɣĳŋœĸſßþƿȝĄƁÇĐƊĘĦĮƘŁØƠŞȘŢȚŦŲƯY̨Ƴąɓçđɗęħįƙłøơşșţțŧųưy̨ƴÁÀÂÄǍĂĀÃÅǺĄÆǼǢƁĆĊĈČÇĎḌĐƊÐÉÈĖÊËĚĔĒĘẸƎƏƐĠĜǦĞĢƔáàâäǎăāãåǻąæǽǣɓćċĉčçďḍđɗðéèėêëěĕēęẹǝəɛġĝǧğģɣĤḤĦIÍÌİÎÏǏĬĪĨĮỊĲĴĶƘĹĻŁĽĿʼNŃN̈ŇÑŅŊÓÒÔÖǑŎŌÕŐỌØǾƠŒĥḥħıíìiîïǐĭīĩįịĳĵķƙĸĺļłľŀŉńn̈ňñņŋóòôöǒŏōõőọøǿơœŔŘŖŚŜŠŞȘṢẞŤŢṬŦÞÚÙÛÜǓŬŪŨŰŮŲỤƯẂẀŴẄǷÝỲŶŸȲỸƳŹŻŽẒŕřŗſśŝšşșṣßťţṭŧþúùûüǔŭūũűůųụưẃẁŵẅƿýỳŷÿȳỹƴźżžẓ]$/]*)";
        String integerString = "([[0-9] /]*)";

        //group 2
        String stringRegex = "(.*)\": \"";
        String integerRegex = "(.*)\": ";

        Pattern logConfig = Pattern.compile("\"EnableLogs\": \"" + "([a-z]*)");
        Matcher logMatcher = logConfig.matcher(fileContents);
        boolean log = true;

        while (logMatcher.find()) {
            if (Objects.equals(logMatcher.group(1), "false")) {
                CustomMenuMessage.LOGGER.debug("annoying logs are disabled");
                log = false;
            }
        }

        ///Check if the matcher is working properly
            /*String matcher$fdk$1;
            String matcherA1v$fn;
            String matcher3éof20à5A;
            String matcherAfd8pùé;
            String matcher10;*/

        //group 2
        Matcher add = Pattern.compile("\"add" + stringRegex + string).matcher(fileContents);
        Matcher chance = Pattern.compile("\"chance" + integerRegex + integerString).matcher(fileContents);

        Matcher iceAndFireDisabled = Pattern.compile("\"DisableIceAndFireMessages\": \"" + "([a-z]*)").matcher(fileContents);
        File iceAndFireConfig = Path.of("config/iceandfire-client.toml").toFile();

        boolean enableIceAndFireMessages = false;
        boolean iceAndFireMenuEnabled = false;

        int storeChance = 0;
        int storeRandom;
        String storeString = "nothing :P";

        if (FMLLoader.getLoadingModList().getMods().stream().anyMatch(mod -> mod.getModId().equals("iceandfire")) && iceAndFireConfig.exists() && !enableIceAndFireMessages) {
            Scanner iceAndFireScanner = new Scanner(iceAndFireConfig);
            while (iceAndFireScanner.hasNextLine()) {
                builder.append(iceAndFireScanner.nextLine()).append("\n");
            }
            String iceAndFireContents = builder.toString();
            Matcher iceAndFireMatcher = Pattern.compile("\"Custom main menu\" = " + "([a-z]*)").matcher(iceAndFireContents);

            if (iceAndFireMatcher.find()) {
                if (iceAndFireMatcher.group(1).equals("true")) {
                    if (log) { CustomMenuMessage.LOGGER.info("Ice and Fire menu is enabled"); }
                    iceAndFireMenuEnabled = true;

                    if (iceAndFireDisabled.find()) {
                        if (Objects.equals(iceAndFireDisabled.group(1), "true")) {
                            enableIceAndFireMessages = true;
                        }
                    }
                } else if (log) {
                    CustomMenuMessage.LOGGER.info("Ice and Fire menu is disabled");
                }
            } else {
                CustomMenuMessage.LOGGER.warn("Matcher did not find the Custom main menu config of Ice And Fire?");
            }
        }
        if (!enableIceAndFireMessages && !iceAndFireMenuEnabled) {

            while (add.find()) {
                if (log) {
                    CustomMenuMessage.LOGGER.info("Add value found -> " + add.group() + " -> (" + add.group(2) + ")");
                }

                if (chance.find()) {
                    if (log) {
                        CustomMenuMessage.LOGGER.info("Chance value found -> " + chance.group() + " -> (" + chance.group(2) + ")");
                    }
                    storeRandom = RandomSource.create().nextInt(1, 1001);

                    if (log) {
                        CustomMenuMessage.LOGGER.debug(storeString + " " + storeChance + " VS " + add.group(2) + " " + (Integer.parseInt(chance.group(2)) - storeRandom));
                    }

                    if (storeRandom <= Integer.parseInt(chance.group(2))) {
                        if (storeChance < Integer.parseInt(chance.group(2)) - storeRandom) {
                            storeChance = Integer.parseInt(chance.group(2)) - storeRandom;
                            storeString = add.group(2);
                            cir.setReturnValue(new SplashRenderer(storeString));
                        }
                    }

                    if (log) {
                        CustomMenuMessage.LOGGER.debug("winner is: " + storeString + " " + storeChance);
                    }
                }
            }
            Pattern removePattern = Pattern.compile("\"remove" + stringRegex + string);
            Matcher remove = removePattern.matcher(fileContents);

            while (remove.find()) {
                if (log) {
                    CustomMenuMessage.LOGGER.info("Remove value found: " + remove.group() + " -> (" + remove.group(2) + ")");
                }

                try {
                    if (String.valueOf(cir.getReturnValue()).equals(remove.group(2)) || storeString.equals(remove.group(2))) {
                        if (log) {
                            CustomMenuMessage.LOGGER.info("\"" + remove.group(2) + "\" is removed. Trying again...");
                        }
                        CustomMenuMessage$splash(cir);
                    }
                } catch (StackOverflowError exception) {
                    CustomMenuMessage.LOGGER.info("Excuse me player. Did you managed to StackOverflow yourself with the configs? Well. It's not going to crash, sorry.");
                }
            }

            Pattern replaceTargetPattern = Pattern.compile("\"replaceTarget" + stringRegex + string);
            Matcher replaceTarget = replaceTargetPattern.matcher(fileContents);
            Pattern replaceToPattern = Pattern.compile("\"replaceTo" + stringRegex + string);
            Matcher replaceTo = replaceToPattern.matcher(fileContents);

            while (replaceTarget.find()) {
                while (replaceTo.find()) {
                    if (log) {
                        CustomMenuMessage.LOGGER.info("ReplaceTarget value found: " + replaceTarget.group() + " -> (" + replaceTarget.group(2) + ")");
                        CustomMenuMessage.LOGGER.info("ReplaceTo value found: " + replaceTo.group() + " -> (" + replaceTo.group(2) + ")");
                    }
                    if (String.valueOf(cir.getReturnValue()).equals(replaceTarget.group(2))) {
                        cir.setReturnValue(new SplashRenderer(replaceTo.group(2)));
                    }
                }
            }
        }
    }
}