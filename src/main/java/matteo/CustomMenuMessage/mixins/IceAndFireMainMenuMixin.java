package matteo.CustomMenuMessage.mixins;

import com.github.alexthe666.iceandfire.client.gui.IceAndFireMainMenu;

import matteo.CustomMenuMessage.CustomMenuMessage;

import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Objects;

import static com.github.alexthe666.iceandfire.IafConfig.customMainMenu;

@OnlyIn(Dist.CLIENT)
@Mixin(value = IceAndFireMainMenu.class, priority = 9999)
public abstract class IceAndFireMainMenuMixin {
    @Shadow private String splashText;

    @Unique
    private String customMenuMessage$storeString = "Nothing :P";
    @Unique
    private boolean customMenuMessage$checkIceAndFire = false;

    @Inject(method = "tick", at = @At("TAIL"))
    private void CustomMenuMessage$IceAndFireMainMenu(CallbackInfo ci) throws FileNotFoundException {
        File file = Path.of("config/custom_menu_message.json").toFile();
        Scanner scanner = new Scanner(file);

        StringBuilder builder = new StringBuilder();
        if (!this.customMenuMessage$storeString.equals(this.splashText)) {
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine()).append("\n");
            }
        }
        String fileContents = builder.toString();

        //I agree this whole system can certainly be improved, despite the performance impacts being negligible
        //group 1
        String string = "([[0-9] /[a-zA-Z\\\\-_ ’'.,:()+=![-]>/‘ÆÐƎƏƐƔĲŊŒẞÞǷȜæðǝəɛɣĳŋœĸſßþƿȝĄƁÇĐƊĘĦĮƘŁØƠŞȘŢȚŦŲƯY̨Ƴąɓçđɗęħįƙłøơşșţțŧųưy̨ƴÁÀÂÄǍĂĀÃÅǺĄÆǼǢƁĆĊĈČÇĎḌĐƊÐÉÈĖÊËĚĔĒĘẸƎƏƐĠĜǦĞĢƔáàâäǎăāãåǻąæǽǣɓćċĉčçďḍđɗðéèėêëěĕēęẹǝəɛġĝǧğģɣĤḤĦIÍÌİÎÏǏĬĪĨĮỊĲĴĶƘĹĻŁĽĿʼNŃN̈ŇÑŅŊÓÒÔÖǑŎŌÕŐỌØǾƠŒĥḥħıíìiîïǐĭīĩįịĳĵķƙĸĺļłľŀŉńn̈ňñņŋóòôöǒŏōõőọøǿơœŔŘŖŚŜŠŞȘṢẞŤŢṬŦÞÚÙÛÜǓŬŪŨŰŮŲỤƯẂẀŴẄǷÝỲŶŸȲỸƳŹŻŽẒŕřŗſśŝšşșṣßťţṭŧþúùûüǔŭūũűůųụưẃẁŵẅƿýỳŷÿȳỹƴźżžẓ]$/]*)";
        String integerString = "([[0-9] /]*)";

        //group 2
        String stringRegex = "(.*)\": \"";
        String integerRegex = "(.*)\": ";

        Pattern iceAndFire = Pattern.compile("\"DisableIceAndFireMessages\": \"" + "([a-z]*)");
        Matcher disabled = iceAndFire.matcher(fileContents);

        Pattern logConfig = Pattern.compile("\"EnableLogs\": \"" + "([a-z]*)");
        Matcher logMatcher = logConfig.matcher(fileContents);

        boolean log = true;
        boolean disableIceAndFireMessages = false;

        if (disabled.find()) {
            if (!customMenuMessage$checkIceAndFire && customMainMenu) {
                if (Objects.equals(disabled.group(1), "true")) {
                    customMenuMessage$checkIceAndFire = true;
                    disableIceAndFireMessages = true;
                    CustomMenuMessage.LOGGER.info("Ice and Fire menu messages disabled");
                } else {
                    customMenuMessage$checkIceAndFire = true;
                    CustomMenuMessage.LOGGER.debug("Ice and Fire menu messages enabled");
                }
                customMenuMessage$checkIceAndFire = true;
            }
        }
        if (disableIceAndFireMessages) {

            while (logMatcher.find()) {
                if (Objects.equals(logMatcher.group(1), "false")) {
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
            Pattern addPattern = Pattern.compile("\"add" + stringRegex + string);
            Pattern chancePattern = Pattern.compile("\"chance" + integerRegex + integerString);
            Matcher add = addPattern.matcher(fileContents);
            Matcher chance = chancePattern.matcher(fileContents);
            int storeChance = 0;
            int storeRandom;

            while (add.find()) {
                if (log) {
                    CustomMenuMessage.LOGGER.info("Add value found -> " + add.group() + " -> (" + add.group(2) + ")");
                }

                if (chance.find()) {
                    if (log) {
                        CustomMenuMessage.LOGGER.info("Chance value found -> " + chance.group() + " -> (" + chance.group(2) + ")");
                    }
                    storeRandom = RandomSource.create().nextInt(0, 1001);

                    if (log) {
                        CustomMenuMessage.LOGGER.debug(customMenuMessage$storeString + " " + storeChance + " VS " + add.group(2) + " " + (Integer.parseInt(chance.group(2)) - storeRandom));
                    }

                    if (storeRandom <= Integer.parseInt(chance.group(2))) {
                        if (storeChance < Integer.parseInt(chance.group(2)) - storeRandom) {
                            storeChance = Integer.parseInt(chance.group(2)) - storeRandom;
                            customMenuMessage$storeString = add.group(2);
                            this.splashText = customMenuMessage$storeString;
                        }
                    }

                    if (log) {
                        CustomMenuMessage.LOGGER.debug("winner is: " + customMenuMessage$storeString + " " + storeChance);
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
                    if (String.valueOf(this.splashText).equals(remove.group(2)) || customMenuMessage$storeString.equals(remove.group(2))) {
                        if (log) {
                            CustomMenuMessage.LOGGER.info("\"" + remove.group(2) + "\" is removed. Trying again...");
                        }
                        CustomMenuMessage$IceAndFireMainMenu(ci);
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
                    if (String.valueOf(this.splashText).equals(replaceTarget.group(2))) {
                        this.splashText = replaceTo.group(2);
                    }
                }
            }
            this.customMenuMessage$storeString = this.splashText;
        }
    }
}