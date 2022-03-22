package no.spk.misc.converter.gherkintomd.converter;

import no.spk.misc.converter.gherkintomd.Language;

public interface UsedTagConverter {

    String convert(final Language language, final String input);
}
