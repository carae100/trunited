package io.github.dracosomething.trawakened.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.dracosomething.trawakened.library.FearTypes;
import io.github.dracosomething.trawakened.library.shadowRank;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class fearArgument implements ArgumentType<FearTypes> {
    private static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType((o) -> {
        return Component.translatable("trawakened.argument.fear.invalid");
    });

    @Override
    public FearTypes parse(StringReader stringReader) throws CommandSyntaxException {
        String remaining = stringReader.getRemaining();
        String registryName = remaining.contains(" ") ? remaining.split(" ")[0] : remaining;
        stringReader.setCursor(stringReader.getString().indexOf(registryName) + registryName.length());
        return (FearTypes) (Arrays.stream(FearTypes.values()).filter((fear) -> {
            return fear.toLocation().toString().equalsIgnoreCase(registryName);
        }).findFirst().orElseThrow(() -> {
            return ERROR_INVALID_VALUE.create(registryName);
        }));
    }

    public static fearArgument fear() {
        return new fearArgument();
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof SharedSuggestionProvider ? SharedSuggestionProvider.suggestResource(
                (Arrays.stream(FearTypes.values()).toList().stream().map(FearTypes::toLocation)), builder) : Suggestions.empty();
    }


    public static FearTypes getFear(CommandContext<CommandSourceStack> context, String name) {
        return (FearTypes) context.getArgument(name, FearTypes.class);
    }

    public Collection<String> getExamples() {
        return Stream.of(FearTypes.RELIGION.toLocation(), FearTypes.DARKNESS.toLocation()).map(ResourceLocation::toString).collect(Collectors.toList());
    }
}
