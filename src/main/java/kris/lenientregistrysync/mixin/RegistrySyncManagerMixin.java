package kris.lenientregistrysync.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.impl.registry.sync.RegistrySyncManager;
import net.fabricmc.fabric.impl.registry.sync.RemapException;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import kris.lenientregistrysync.LenientRegistrySync;

@Mixin(RegistrySyncManager.class)
public class RegistrySyncManagerMixin {
    /**
     * 拦截 checkRemoteRemap，跳过对未知注册表项的检查
     */
    @Inject(
        method = "checkRemoteRemap",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    private static void onCheckRemoteRemap(
        Map<Identifier, Object2IntMap<Identifier>> map,
        CallbackInfo ci
    ) {
        if (LenientRegistrySync.CONFIG.enabled) {
            LenientRegistrySync.LOGGER.warn("[LenientRegistry] Ignoring missing registry entries (unknown remote entries). Running in lenient mode.");
            ci.cancel(); // 跳过整个检查逻辑
        }
    }
}