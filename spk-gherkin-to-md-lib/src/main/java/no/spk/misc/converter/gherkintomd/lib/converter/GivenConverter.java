package no.spk.misc.converter.gherkintomd.lib.converter;

import java.util.List;
import java.util.Map;

import no.spk.misc.converter.gherkintomd.lib.Language;

public class GivenConverter implements SingleLineConverter {

    private static final Map<Language, List<String>> possibleValues = Map.of(
            Language.EN, List.of("Given "),
            Language.NO, List.of("Gitt ")
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
