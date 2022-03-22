package no.spk.misc.converter.gherkintomd.lib.pass;

public interface Pass {

    String name();

    String run(final String input);
}
