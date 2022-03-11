package no.spk.misc.converter.gherkintomd.pass;

import java.util.List;

import no.spk.misc.converter.gherkintomd.Language;
import no.spk.misc.converter.gherkintomd.converter.AndConverter;
import no.spk.misc.converter.gherkintomd.converter.BackgroundConverter;
import no.spk.misc.converter.gherkintomd.converter.ButConverter;
import no.spk.misc.converter.gherkintomd.converter.ExamplesConverter;
import no.spk.misc.converter.gherkintomd.converter.FeatureConverter;
import no.spk.misc.converter.gherkintomd.converter.GivenConverter;
import no.spk.misc.converter.gherkintomd.converter.ScenarioConverter;
import no.spk.misc.converter.gherkintomd.converter.SingleLineConverter;
import no.spk.misc.converter.gherkintomd.converter.ThenConverter;
import no.spk.misc.converter.gherkintomd.converter.TrimConverter;
import no.spk.misc.converter.gherkintomd.converter.WhenConverter;

/**
 * Does a pass over the Gherkin content and performs the conversions to Markdown that can be done
 * by only looking at a single line. I.e. the easy conversions from Gherkin to Markdown.
 */
public class SingleLinePass implements Pass {

    private static final List<SingleLineConverter> converters = List.of(
            new FeatureConverter(),
            new ScenarioConverter(),
            new ExamplesConverter(),
            new GivenConverter(),
            new WhenConverter(),
            new ThenConverter(),
            new AndConverter(),
            new ButConverter(),
            new BackgroundConverter()
    );

    private static final SingleLineConverter trimConverter = new TrimConverter();

    private enum ParsingState {
        HEADER,
        BODY
    }

    private ParsingState state = ParsingState.HEADER;

    public String name() {
        return "ordinary pass";
    }

    public String run(final String input) {
        final StringBuilder sb = new StringBuilder();

        Language language = Language.EN;
        boolean wasLanguageFound = false;

        for (final String line : input.split("\n")) {
            if (state == ParsingState.HEADER && line.trim().startsWith("# language:") && !wasLanguageFound) {
                language = Language.language(line);
                wasLanguageFound = true;
            } else if (state == ParsingState.HEADER && line.trim().startsWith("#")) {
            } else {
                state = ParsingState.BODY;

                final Language finalLanguage = language;
                sb
                        .append(
                                converters
                                        .stream()
                                        .filter(converter -> converter.isRelevant(finalLanguage, line))
                                        .findFirst()
                                        .orElse(trimConverter)
                                        .convert(language, line)
                        )
                        .append("\n");
            }
        }

        return sb.toString();
    }
}
