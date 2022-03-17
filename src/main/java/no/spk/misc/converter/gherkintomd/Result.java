package no.spk.misc.converter.gherkintomd;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import no.spk.misc.converter.gherkintomd.pass.Pass;

public class Result {

    String result;
    final List<String> passes;

    public Result(final String result) {
        this.result = requireNonNull(result, "result is required, but was null");
        this.passes = new ArrayList<>();
    }

    public void updateResult(final String newResult, final Pass passUsed) {
        result = newResult;
        passes.add(passUsed.name());
    }

    public String current() {
        return result;
    }
}
