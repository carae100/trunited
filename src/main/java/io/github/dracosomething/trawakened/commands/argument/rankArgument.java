package io.github.dracosomething.trawakened.commands.argument;

import com.github.manasmods.tensura.command.argument.RaceArgument;
import com.github.manasmods.tensura.command.argument.SkillArgument;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.dracosomething.trawakened.library.shadowRank;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class rankArgument implements ArgumentType<shadowRank> {
    private static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType((o) -> {
        return Component.translatable("trawakened.argument.rank.invalid");
    });

    @Override
    public shadowRank parse(StringReader stringReader) throws CommandSyntaxException {
        String remaining = stringReader.getRemaining();
        String registryName = remaining.contains(" ") ? remaining.split(" ")[0] : remaining;
        stringReader.setCursor(stringReader.getString().indexOf(registryName) + registryName.length());
        return (shadowRank) (Arrays.stream(shadowRank.values()).filter((rank) -> {
            return rank.toLocation().toString().equalsIgnoreCase(registryName);
        }).findFirst().orElseThrow(() -> {
            return ERROR_INVALID_VALUE.create(registryName);
        }));
    }

    public static rankArgument rank() {
        return new rankArgument();
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof SharedSuggestionProvider ? SharedSuggestionProvider.suggestResource(
                (Arrays.stream(shadowRank.values()).toList().stream().map((rank) -> {
                    int index = Arrays.stream(shadowRank.values()).toList().indexOf(rank);
            return Arrays.stream(shadowRank.values()).toList().get(index).toLocation();
        })), builder) : Suggestions.empty();
    }


    public static shadowRank getRank(CommandContext<CommandSourceStack> context, String name) {
        return (shadowRank) context.getArgument(name, shadowRank.class);
    }

    public Collection<String> getExamples() {
        return Stream.of(shadowRank.BASIC.toLocation(), shadowRank.NORMAL.toLocation()).map(ResourceLocation::toString).collect(Collectors.toList());
    }
}
