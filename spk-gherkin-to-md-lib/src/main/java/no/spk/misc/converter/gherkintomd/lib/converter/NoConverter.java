package no.spk.misc.converter.gherkintomd.lib.converter;

import no.spk.misc.converter.gherkintomd.lib.Language;

public class NoConverter implements SingleLineConverter {

    @Override
    public boolean isRelevant(final Language language, final String input) {
        return false;
    }

    @Override
    public String convert(final Language language, final String input) {
        return input;
    }
}
