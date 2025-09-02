package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.tensura.command.argument.RaceArgument;
import com.github.manasmods.tensura.command.argument.SkillArgument;
import io.github.dracosomething.trawakened.commands.argument.AlternateArgument;
import io.github.dracosomething.trawakened.commands.argument.fearArgument;
import io.github.dracosomething.trawakened.commands.argument.nameArgument;
import io.github.dracosomething.trawakened.commands.argument.rankArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class argumentRegistry {
    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> registry;
    private static final RegistryObject<SingletonArgumentInfo<rankArgument>> RANK_ARGUMENT;
    private static final RegistryObject<SingletonArgumentInfo<nameArgument>> NAME_ARGUMENT;
    private static final RegistryObject<SingletonArgumentInfo<fearArgument>> FEAR_ARGUMENT;
    private static final RegistryObject<SingletonArgumentInfo<AlternateArgument>> ALTERNATE_TYPE_ARGUMENT;

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, "tensura");
        RANK_ARGUMENT = registry.register("rank", () -> {
            return ArgumentTypeInfos.registerByClass(rankArgument.class, SingletonArgumentInfo.contextFree(rankArgument::rank));
        });
        NAME_ARGUMENT = registry.register("name", () -> {
            return ArgumentTypeInfos.registerByClass(nameArgument.class, SingletonArgumentInfo.contextFree(nameArgument::word));
        });
        FEAR_ARGUMENT = registry.register("fear", () -> {
            return ArgumentTypeInfos.registerByClass(fearArgument.class, SingletonArgumentInfo.contextFree(fearArgument::fear));
        });
        ALTERNATE_TYPE_ARGUMENT = registry.register("alternate_type", () -> {
            return ArgumentTypeInfos.registerByClass(AlternateArgument.class, SingletonArgumentInfo.contextFree(AlternateArgument::alternate));
        });
    }
}
