package io.github.dracosomething.trawakened.commands.argument;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.dracosomething.trawakened.ability.skill.ultimate.ShadowMonarch;
import io.github.dracosomething.trawakened.library.shadowRank;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class nameArgument implements ArgumentType<String> {
    private final nameArgument.StringType type;

    private nameArgument(nameArgument.StringType type) {
        this.type = type;
    }

    public static nameArgument word() {
        return new nameArgument(nameArgument.StringType.SINGLE_WORD);
    }

    public static nameArgument string() {
        return new nameArgument(nameArgument.StringType.QUOTABLE_PHRASE);
    }

    public static nameArgument greedyString() {
        return new nameArgument(nameArgument.StringType.GREEDY_PHRASE);
    }

    public static String getString(CommandContext<?> context, String name) {
        return (String)context.getArgument(name, String.class);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSourceStack stack) {
            Player source = stack.getPlayer();
            if (SkillAPI.getSkillsFrom(source).getSkill(skillRegistry.SHADOW_MONARCH.get()).isPresent()) {
                ManasSkillInstance instance = SkillAPI.getSkillsFrom(source).getSkill(skillRegistry.SHADOW_MONARCH.get()).get();
                if (instance.getSkill() instanceof ShadowMonarch shadowMonarch) {
                    return context.getSource() instanceof SharedSuggestionProvider ? SharedSuggestionProvider.suggestResource(
                            (shadowMonarch.getShadowStorage().getAllKeys().stream().map((shadow) -> ResourceLocation.tryParse(shadowMonarch.getShadowStorage().getCompound(shadow).getString("name")))), builder) : Suggestions.empty();
                }
            }
        }
        return Suggestions.empty();
    }

    public nameArgument.StringType getType() {
        return this.type;
    }

    public String parse(StringReader reader) throws CommandSyntaxException {
        if (this.type == nameArgument.StringType.GREEDY_PHRASE) {
            String text = reader.getRemaining();
            reader.setCursor(reader.getTotalLength());
            return text;
        } else {
            return this.type == nameArgument.StringType.SINGLE_WORD ? reader.readUnquotedString() : reader.readString();
        }
    }

    public String toString() {
        return "string()";
    }

    public Collection<String> getExamples() {
        return this.type.getExamples();
    }

    public static String escapeIfRequired(String input) {
        char[] var1 = input.toCharArray();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            char c = var1[var3];
            if (!StringReader.isAllowedInUnquotedString(c)) {
                return escape(input);
            }
        }

        return input;
    }

    private static String escape(String input) {
        StringBuilder result = new StringBuilder("\"");

        for(int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            if (c == '\\' || c == '"') {
                result.append('\\');
            }

            result.append(c);
        }

        result.append("\"");
        return result.toString();
    }

    public static enum StringType {
        SINGLE_WORD(new String[]{"word", "words_with_underscores"}),
        QUOTABLE_PHRASE(new String[]{"\"quoted phrase\"", "word", "\"\""}),
        GREEDY_PHRASE(new String[]{"word", "words with spaces", "\"and symbols\""});

        private final Collection<String> examples;

        private StringType(String... examples) {
            this.examples = Arrays.asList(examples);
        }

        public Collection<String> getExamples() {
            return this.examples;
        }
    }
}
