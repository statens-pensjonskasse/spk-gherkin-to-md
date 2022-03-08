package no.spk.misc.converter.gherkintomd;

import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@SuppressWarnings("unused")
@Command(name = "spk-gherkin-to-md",
        mixinStandardHelpOptions = true,
        description = "Convert gherkin (feature) files to markdown (md)",
        version = "spk-gherkin-to-md 0.0.1"
)
public class GherkinToMdCli implements Callable<Integer> {

    @Parameters(description = "Path to find Gherkin files in.")
    private String path;

    @Override
    public Integer call() {
        return 0;
    }

    public static void main(final String... args) {
        final CommandLine commandLine = new CommandLine(new GherkinToMdCli());

        final int exitCode = commandLine.execute(args);

        System.exit(exitCode);
    }
}
