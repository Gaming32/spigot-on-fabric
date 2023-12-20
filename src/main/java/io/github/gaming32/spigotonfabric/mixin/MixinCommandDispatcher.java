package io.github.gaming32.spigotonfabric.mixin;

import net.minecraft.commands.CommandDispatcher;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CommandDispatcher.class)
public class MixinCommandDispatcher {
    //region sendCommands
//    @ModifyVariable(
//        method = "sendCommands",
//        at = @At(
//            value = "INVOKE",
//            target = "Lcom/google/common/collect/Maps;newHashMap()Ljava/util/HashMap;",
//            shift = At.Shift.BY,
//            by = 2 // INVOKESTATIC ASTORE_1
//        )
//    )
//    private Map<CommandNode<CommandListenerWrapper>, CommandNode<ICompletionProvider>> useIdentityHashMap(Map<CommandNode<CommandListenerWrapper>, CommandNode<ICompletionProvider>> original) {
//        return new IdentityHashMap<>(original);
//    }

//    @Inject(
//        method = "sendCommands",
//        at = @At(
//            value = "NEW",
//            target = "()Lcom/mojang/brigadier/tree/RootCommandNode;"
//        )
//    )
//    private void generate
    //endregion
}
