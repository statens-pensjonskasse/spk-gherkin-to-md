package no.spk.misc.converter.gherkintomd;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import no.spk.misc.converter.gherkintomd.converter.AndConverter;
import no.spk.misc.converter.gherkintomd.converter.BackgroundConverter;
import no.spk.misc.converter.gherkintomd.converter.Converter;
import no.spk.misc.converter.gherkintomd.converter.ExamplesConverter;
import no.spk.misc.converter.gherkintomd.converter.FeatureConverter;
import no.spk.misc.converter.gherkintomd.converter.GivenConverter;
import no.spk.misc.converter.gherkintomd.converter.NoConverter;
import no.spk.misc.converter.gherkintomd.converter.ScenarioConverter;
import no.spk.misc.converter.gherkintomd.converter.ThenConverter;
import no.spk.misc.converter.gherkintomd.converter.WhenConverter;

public class GherkinToMdConverter {

    private enum ParsingState {
        HEADER,
        BODY
    }

    private ParsingState state = ParsingState.HEADER;

    private static final List<Converter> converters = List.of(
            new FeatureConverter(),
            new ScenarioConverter(),
            new ExamplesConverter(),
            new GivenConverter(),
            new WhenConverter(),
            new ThenConverter(),
            new AndConverter(),
            new BackgroundConverter()
    );

    private static final Converter noConverter = new NoConverter();

    public String convert(final String gherkin) {
        requireNonNull(gherkin, "The gherkin string was null, but is required");

        final StringBuilder sb = new StringBuilder();

        Language language = Language.EN;
        boolean wasLanguageFound = false;

        for (final String line : gherkin.split("\n")) {
            if (state == ParsingState.HEADER && line.trim().startsWith("# language:") && !wasLanguageFound) {
                language = Language.language(line);
                wasLanguageFound = true;
            } else if (state == ParsingState.HEADER && line.trim().startsWith("#")) {
            } else {
                state = ParsingState.BODY;

                final Language finalLanguage = language;
                sb.append(converters
                                .stream()
                                .filter(c -> c.isRelevant(finalLanguage, line))
                                .findFirst()
                                .orElse(noConverter)
                                .convert(language, line))
                        .append("\n");
            }
        }

        return sb.toString();
    }

    public void convert(final Path path) throws IOException {
        requireNonNull(path, "The path was null, but is required");

        if (path.toFile().isFile()) {
            convertSingleFile(path);
        } else {
            for (final File subFile : requireNonNull(path.toFile().listFiles())) {
                convert(subFile.toPath());
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
