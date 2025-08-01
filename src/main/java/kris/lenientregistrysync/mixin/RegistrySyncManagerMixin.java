package kris.lenientregistrysync.mixin;

import net.fabricmc.fabric.impl.registry.sync.RegistrySyncManager;
import net.fabricmc.fabric.impl.registry.sync.RemappableRegistry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

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
    
    /**
     * 直接修改 apply 方法的 mode 参数
     * 这是最可靠的解决方案，因为它不依赖于方法调用位置
     */
    @ModifyVariable(
        method = "apply",
        at = @At("HEAD"),
        ordinal = 0,
        argsOnly = true,
        remap = false
    )
    private static RemappableRegistry.RemapMode modifyApplyMode(RemappableRegistry.RemapMode mode) {
        if (LenientRegistrySync.CONFIG.enabled && mode == RemappableRegistry.RemapMode.REMOTE) {
            LenientRegistrySync.LOGGER.warn("[LenientRegistry] Changing remap mode from REMOTE to AUTHORITATIVE to allow missing registry entries.");
            return RemappableRegistry.RemapMode.AUTHORITATIVE;
        }
        return mode;
    }
}