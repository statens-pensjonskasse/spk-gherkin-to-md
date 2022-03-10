package no.spk.misc.converter.gherkintomd.pass;

import no.spk.misc.converter.gherkintomd.converter.TableConverter;

public class TablePass implements Pass {

    public String name() {
        return "table pass";
    }

    public String run(final String input) {
        return new TableConverter().convert(input);
    }
}
