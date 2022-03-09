package no.spk.misc.converter.gherkintomd.converter;

import no.spk.misc.converter.gherkintomd.Language;

public interface Converter {

    boolean isRelevant(final Language language, final String input);

    String convert(final Language language, final String input);
}
