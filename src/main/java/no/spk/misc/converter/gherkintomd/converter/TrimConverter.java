package no.spk.misc.converter.gherkintomd.converter;

import no.spk.misc.converter.gherkintomd.Language;

public class TrimConverter implements SingleLineConverter {

    @Override
    public boolean isRelevant(final Language language, final String input) {
        return false;
    }

    @Override
    public String convert(final Language language, final String input) {
        return input.trim();
    }
}
