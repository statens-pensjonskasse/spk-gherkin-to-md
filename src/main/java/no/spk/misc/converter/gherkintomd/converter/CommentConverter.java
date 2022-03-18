package no.spk.misc.converter.gherkintomd.converter;

import java.util.List;
import java.util.Map;

import no.spk.misc.converter.gherkintomd.Language;

public class CommentConverter implements SingleLineConverter {

    private static final Map<Language, List<String>> possibleValues = Map.of(
            Language.EN, List.of("And "),
            Language.NO, List.of("Og ")
    );

    public boolean isRelevant(final Language language, final String input) {
        for (final String possibleValue : possibleValues.get(language)) {
            if (input.trim().startsWith(possibleValue)) {
                return true;
            }
        }

        return false;
    }

    public String convert(final Language language, final String input) {
        String output = input.trim();

        for (final String possibleValue : possibleValues.get(language)) {
            output = output.replace(possibleValue, String.format("**%s** ", possibleValue.trim()));
        }

        return output;
    }
}
