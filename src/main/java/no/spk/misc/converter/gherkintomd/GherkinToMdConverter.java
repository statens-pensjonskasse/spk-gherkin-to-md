package no.spk.misc.converter.gherkintomd;

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
        final StringBuilder sb = new StringBuilder();

        int lineNumber = 1;
        Language language = Language.EN;

        for (final String line : gherkin.split("\n")) {
            if (lineNumber == 1 && line.trim().startsWith("# language:")) {
                language = Language.language(line);
                lineNumber++;
                continue;
            }

            final Language finalLanguage = language;
            sb.append(
                    converters
                            .stream()
                            .filter(c -> c.isRelevant(finalLanguage, line))
                            .findFirst()
                            .orElse(noConverter)
                            .convert(language, line)
            );

            lineNumber++;
        }

        return sb.toString();
    }
}
