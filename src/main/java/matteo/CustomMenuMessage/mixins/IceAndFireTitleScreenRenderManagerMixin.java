package matteo.CustomMenuMessage.mixins;

import com.github.alexthe666.iceandfire.client.gui.IceAndFireMainMenu;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import com.iafenvoy.iafpatcher.misc.TitleScreenRenderManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mixin(TitleScreenRenderManager.class)
public abstract class IceAndFireTitleScreenRenderManagerMixin {
    @Shadow
    @Final
    public static ResourceLocation splash;

    @Shadow
    public static SplashRenderer getSplash() {
        return null;
    }

    @Shadow
    private static List<String> splashText;

    @Inject(method = "lambda$getSplash$0", at = @At("TAIL"), cancellable = true)
    private static void CustomMenuMessage$getSplash(String splashText, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false); //sorry
        splashText = null;
    }
}
