package no.spk.misc.converter.gherkintomd.pass;

import no.spk.misc.converter.gherkintomd.converter.DocstringConverter;

public class DocstringPass implements Pass {

    @Override
    public String name() {
        return "docstring pass";
    }

    @Override
    public String run(final String input) {
        return new DocstringConverter().convert(input);
    }
}
