package no.spk.misc.converter.gherkintomd.lib.pass;

import java.util.List;

import no.spk.misc.converter.gherkintomd.lib.Language;
import no.spk.misc.converter.gherkintomd.lib.converter.IgnoreConverter;
import no.spk.misc.converter.gherkintomd.lib.converter.UsedTagConverter;

/**
 * Does a pass over the Gherkin content and converts tags (@tag) to something else,
 * e.g. a prefix in the scenario or feature it's a tag for.
 *
 * Example:
 *
 * @ignore
 * Scenario: This is a scenario
 *
 *
 * becomes:
 *
 *
 * ## (IGNORE) This is a scenario
 */
public class UsedTagsPass implements Pass {

    private static final List<UsedTagConverter> converters = List.of(
            new IgnoreConverter()
    );

    public String name() {
        return "used tags pass";
    }

    public String run(final String input) {
        String output = input;

        for (final UsedTagConverter converter : converters) {
            output = converter.convert(Language.EN, output);
        }

        return output;
    }
}
