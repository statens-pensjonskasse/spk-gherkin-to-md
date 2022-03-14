package no.spk.misc.converter.gherkintomd.pass;

public interface Pass {

    String name();

    String run(final String input);
}
