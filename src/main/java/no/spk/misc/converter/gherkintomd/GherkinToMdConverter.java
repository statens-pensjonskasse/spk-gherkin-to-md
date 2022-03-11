package no.spk.misc.converter.gherkintomd;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

import no.spk.misc.converter.gherkintomd.pass.SingleLinePass;
import no.spk.misc.converter.gherkintomd.pass.Pass;
import no.spk.misc.converter.gherkintomd.pass.TablePass;

public class GherkinToMdConverter {

    private static final List<Supplier<Pass>> passes = List.of(
            SingleLinePass::new,
            TablePass::new
    );

    public String convert(final String gherkin) {
        requireNonNull(gherkin, "The gherkin string is required, but was null");

        final Result result = new Result(gherkin);

        passes.forEach(pass ->
                result.updateResult(
                        pass.get().run(result.current()),
                        pass.get()
                )
        );

        return result.current();
    }

    public void convert(final Path path) throws IOException {
        requireNonNull(path, "The path is required, but was null");

        if (path.toFile().isFile()) {
            convertSingleFile(path);
        } else {
            final List<Path> filesInFolder = Files.list(path)
                    .filter(f -> f.toFile().isDirectory() || f.toFile().getName().endsWith(".feature"))
                    .collect(toList());

            for (final Path subFile : filesInFolder) {
                convert(subFile);
            }
        }
    }

    private void convertSingleFile(final Path path) throws IOException {
        final String gherkin = Files.readString(path);
        final String markdown = convert(gherkin);

        try (final PrintWriter writer = new PrintWriter(
                new FileWriter(path.toFile() + ".md", Charset.defaultCharset())
        )) {
            writer.print(markdown);
        }
    }
}
