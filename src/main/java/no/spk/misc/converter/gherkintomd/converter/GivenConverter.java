package no.spk.misc.converter.gherkintomd.converter;

import java.util.List;
import java.util.Map;

import no.spk.misc.converter.gherkintomd.Language;

public class GivenConverter implements Converter {

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
        String output = input;

        for (final String possibleValue : possibleValues.get(language)) {
            output = output.replace(possibleValue, String.format("**%s** ", possibleValue.trim()));
        }

        return output;
    }
}
