package no.spk.misc.converter.gherkintomd.lib.converter;

import no.spk.misc.converter.gherkintomd.lib.Language;

public interface UsedTagConverter {

    String convert(final Language language, final String input);
}
