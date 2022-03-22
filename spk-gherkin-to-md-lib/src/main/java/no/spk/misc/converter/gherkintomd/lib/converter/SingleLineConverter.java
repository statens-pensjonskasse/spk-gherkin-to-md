package no.spk.misc.converter.gherkintomd.lib.converter;

import no.spk.misc.converter.gherkintomd.lib.Language;

public interface SingleLineConverter {

    boolean isRelevant(final Language language, final String input);

    String convert(final Language language, final String input);
}
