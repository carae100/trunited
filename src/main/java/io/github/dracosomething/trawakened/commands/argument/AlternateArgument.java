package io.github.dracosomething.trawakened.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.dracosomething.trawakened.library.AlternateType;
import io.github.dracosomething.trawakened.library.FearTypes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AlternateArgument implements ArgumentType<AlternateType.Assimilation> {
    private static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType((o) -> {
        return Component.translatable("trawakened.argument.alternate.invalid");
    });

    @Override
    public AlternateType.Assimilation parse(StringReader stringReader) throws CommandSyntaxException {
        String remaining = stringReader.getRemaining();
        String registryName = remaining.contains(" ") ? remaining.split(" ")[0] : remaining;
        stringReader.setCursor(stringReader.getString().indexOf(registryName) + registryName.length());
        return (Arrays.stream(AlternateType.Assimilation.values()).filter((fear) -> {
            return fear.getName().equalsIgnoreCase(registryName);
        }).findFirst().orElseThrow(() -> {
            return ERROR_INVALID_VALUE.create(registryName);
        }));
    }

    public static AlternateArgument alternate() {
        return new AlternateArgument();
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof SharedSuggestionProvider ? SharedSuggestionProvider.suggest(
                (Arrays.stream(AlternateType.Assimilation.values()).toList().stream().map(AlternateType.Assimilation::getName)), builder) : Suggestions.empty();
    }


    public static AlternateType.Assimilation getAlternateType(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, AlternateType.Assimilation.class);
    }

    public Collection<String> getExamples() {
        return Stream.of(AlternateType.Assimilation.FLAWED, AlternateType.Assimilation.OVERDRIVEN, AlternateType.Assimilation.COMPLETE).map(AlternateType.Assimilation::getName).collect(Collectors.toList());
    }
}
