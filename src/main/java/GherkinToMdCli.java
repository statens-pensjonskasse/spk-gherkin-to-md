import java.util.List;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@SuppressWarnings("unused")
@Command(name = "gherkintomd",
        mixinStandardHelpOptions = true,
        description = "Convert gherkin (feature) files to markdown (md)",
        version = "gherkintomd 1.0.0"
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
