package no.spk.misc.converter.gherkintomd.converter;

import java.util.List;
import java.util.Map;

import no.spk.misc.converter.gherkintomd.Language;

public class BackgroundConverter {

    private static final Map<Language, List<String>> possibleValues = Map.of(
            Language.EN, List.of("Background:"),
            Language.NO, List.of("Bakgrunn:")
    );

    public static boolean isBackground(final Language language, final String input) {
        for (final String possibleValue : possibleValues.get(language)) {
            if (input.trim().startsWith(possibleValue)) {
                return true;
            }
        }

        return false;
    }

    public static String convert(final Language language, final String input) {
        String output = input.trim();

        for (final String possibleValue : possibleValues.get(language)) {
            output = output.replace(
                    possibleValue,
                    String.format(
                            "## %s",
                            possibleValue.replace(":", "")
                    )
            );
        }

        return output;
    }
}
