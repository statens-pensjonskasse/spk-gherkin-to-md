package no.spk.misc.converter.gherkintomd;

import java.nio.file.Path;
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
    private Path path;

    @Override
    public Integer call() {
        try {
            new GherkinToMdConverter().convert(path);

            return 0;
        } catch (final Exception ex) {
            System.out.format("The program threw an exception with message: %s\n", ex.getMessage());
            return 1;
        }
    }

    public static void main(final String... args) {
        final CommandLine commandLine = new CommandLine(new GherkinToMdCli());

        final int exitCode = commandLine.execute(args);

        System.exit(exitCode);
    }
}
