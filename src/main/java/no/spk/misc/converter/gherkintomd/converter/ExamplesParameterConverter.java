package no.spk.misc.converter.gherkintomd.converter;

import java.util.regex.Pattern;

import no.spk.misc.converter.gherkintomd.Language;

public class ExamplesParameterConverter implements SingleLineConverter {

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(<[a-zæøåA-ZÆØÅ0-9]*>)");

    @Override
    public boolean isRelevant(final Language language, final String input) {
        return input.contains("<") && input.contains(">");
    }

    public String convert(final Language language, final String input) {
        return PARAMETER_PATTERN
                .matcher(input)
                .replaceAll(r -> "`" + r.group() + "`");
    }
}
