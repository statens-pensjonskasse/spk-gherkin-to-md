package no.spk.misc.converter.gherkintomd.lib.pass;

import no.spk.misc.converter.gherkintomd.lib.converter.DocstringConverter;

/**
 * Does a pass over the Gherkin content and converts Gherkin docstrings to Markdown code blocks.
 */
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
