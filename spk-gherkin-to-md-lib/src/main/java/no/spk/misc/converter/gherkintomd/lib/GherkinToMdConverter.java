package no.spk.misc.converter.gherkintomd.lib;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import no.spk.misc.converter.gherkintomd.lib.pass.AfterPass;
import no.spk.misc.converter.gherkintomd.lib.pass.BeforePass;
import no.spk.misc.converter.gherkintomd.lib.pass.DocstringPass;
import no.spk.misc.converter.gherkintomd.lib.pass.Pass;
import no.spk.misc.converter.gherkintomd.lib.pass.SingleLinePass;
import no.spk.misc.converter.gherkintomd.lib.pass.TablePass;
import no.spk.misc.converter.gherkintomd.lib.pass.UsedTagsPass;

public class GherkinToMdConverter {

    private final List<Supplier<BeforePass>> beforePasses = new ArrayList<>();
    private final List<Supplier<Pass>> builtinPasses = new ArrayList<>();
    private final List<Supplier<AfterPass>> afterPasses = new ArrayList<>();

    public GherkinToMdConverter() throws MalformedURLException {
        beforePasses.addAll(
                Plugins.getBeforePlugins()
        );

        builtinPasses.addAll(
                List.of(
                        DocstringPass::new,
                        SingleLinePass::new,
                        TablePass::new,
                        UsedTagsPass::new
                )
        );

        afterPasses.addAll(
                Plugins.getAfterPlugins()
        );
    }

    public String convert(final String gherkin) {
        requireNonNull(gherkin, "The gherkin string is required, but was null");

        final Result result = new Result(gherkin);

        beforePasses.forEach(pass ->
                result.updateResult(
                        pass.get().run(result.current()),
                        pass.get()
                )
        );

        builtinPasses.forEach(pass ->
                result.updateResult(
                        pass.get().run(result.current()),
                        pass.get()
                )
        );

        afterPasses.forEach(pass ->
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
