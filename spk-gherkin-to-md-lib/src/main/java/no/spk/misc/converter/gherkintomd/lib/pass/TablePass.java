package no.spk.misc.converter.gherkintomd.lib.pass;

import no.spk.misc.converter.gherkintomd.lib.converter.TableConverter;

/**
 * Does a pass over the Gherkin content and converts the Gherkin tables to Markdown tables.
 *
 * Tables inside docstrings should not be converted.
 */
public class TablePass implements Pass {

    public String name() {
        return "table pass";
    }

    public String run(final String input) {
        return new TableConverter().convert(input);
    }
}
