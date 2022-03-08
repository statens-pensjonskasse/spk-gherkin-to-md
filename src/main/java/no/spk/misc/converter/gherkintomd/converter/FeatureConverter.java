package no.spk.misc.converter.gherkintomd.converter;

import java.util.List;
import java.util.Map;

import no.spk.misc.converter.gherkintomd.Language;

public class FeatureConverter {

    private static final Map<Language, List<String>> possibleValues = Map.of(
            Language.EN, List.of("Feature:"),
            Language.NO, List.of("Egenskap:", "Funksjonalitet:")
    );

    public static boolean isFeature(final Language language, final String input) {
        for (final String possibleValue : possibleValues.get(language)) {
            if (input.startsWith(possibleValue)) {
                return true;
            }
        }

        return false;
    }

    public static String convert(final Language language, final String input) {
        String output = input;

        for (final String possibleValue : possibleValues.get(language)) {
            output = output.replace(possibleValue, "#");
        }

        return output;
    }
}
