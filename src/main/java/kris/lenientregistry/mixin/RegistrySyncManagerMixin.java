package kris.lenientregistry.mixin;

import kris.lenientregistry.LenientRegistry;
import net.fabricmc.fabric.impl.registry.sync.RemapException;
import net.fabricmc.fabric.impl.registry.sync.RegistrySyncManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RegistrySyncManager.class)
public class RegistrySyncManagerMixin {
    @Redirect(
        method = "checkRemoteRemap",
        at = @At(
            value = "NEW",
            target = "Lnet/fabricmc/fabric/impl/registry/sync/RemapException;"
        ),
        remap = false
    )
    private static RemapException onRemapException(Text message) {
        if (LenientRegistry.CONFIG.enabled) {
            LenientRegistry.LOGGER.warn("[LenientRegistry] Ignoring missing registry entries");
            return null; // 返回 null 以取消抛出异常
        }
        return new RemapException(message);
    }
}
