package no.spk.misc.converter.gherkintomd.pass;

import java.util.List;

import no.spk.misc.converter.gherkintomd.Language;
import no.spk.misc.converter.gherkintomd.converter.AndConverter;
import no.spk.misc.converter.gherkintomd.converter.BackgroundConverter;
import no.spk.misc.converter.gherkintomd.converter.ButConverter;
import no.spk.misc.converter.gherkintomd.converter.DocstringConverter;
import no.spk.misc.converter.gherkintomd.converter.ExamplesConverter;
import no.spk.misc.converter.gherkintomd.converter.FeatureConverter;
import no.spk.misc.converter.gherkintomd.converter.GivenConverter;
import no.spk.misc.converter.gherkintomd.converter.ScenarioConverter;
import no.spk.misc.converter.gherkintomd.converter.SingleLineConverter;
import no.spk.misc.converter.gherkintomd.converter.ThenConverter;
import no.spk.misc.converter.gherkintomd.converter.TrimConverter;
import no.spk.misc.converter.gherkintomd.converter.WhenConverter;
import no.spk.misc.converter.gherkintomd.util.StringUtil;

/**
 * Does a pass over the Gherkin content and performs the conversions to Markdown that can be done
 * by only looking at a single line. I.e. the easy conversions from Gherkin to Markdown.
 * <br>
 * Docstrings and code blocks should not have their content converted to Markdown.
 */
public class SingleLinePass implements Pass {

    private static final String DOCSTRING_DELIMITER = "\"\"\"";
    private static final String BACKTICS = "```";

    private enum DocstringParsingState {
        IN_DOCSTRING,
        OUTSIDE_DOCSTRING
    }

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

    private DocstringParsingState docstringParsingState = DocstringParsingState.OUTSIDE_DOCSTRING;

    public String name() {
        return "ordinary pass";
    }

    public String run(final String input) {
        final StringBuilder sb = new StringBuilder();

        Language language = Language.EN;
        boolean wasLanguageFound = false;
        int indentationOfDocstring = 0;

        for (final String line : input.split("\n")) {
            final boolean isEncounteringCodeblockDelimiter = line.trim().startsWith(DOCSTRING_DELIMITER) || line.trim().startsWith(BACKTICS);

            switch (docstringParsingState) {
                case IN_DOCSTRING:
                    if (isEncounteringCodeblockDelimiter) {
                        docstringParsingState = DocstringParsingState.OUTSIDE_DOCSTRING;
                        sb
                                .append(trimConverter.convert(language, line))
                                .append("\n");
                    } else {
                        final int indentationOfLine = StringUtil.findIndentation(line);
                        final int indentationToBeUsed = indentationOfLine - indentationOfDocstring;
                        sb
                                .append(StringUtil.createIndentation(indentationToBeUsed))
                                .append(trimConverter.convert(language, line))
                                .append("\n");
                    }
                    break;
                case OUTSIDE_DOCSTRING:
                    if (state == ParsingState.HEADER && line.trim().startsWith("# language:") && !wasLanguageFound) {
                        language = Language.language(line);
                        wasLanguageFound = true;
                    } else if (state == ParsingState.HEADER && line.trim().startsWith("#")) {
                        // Skipping other preprocessing directives, such as encoding.
                    } else {
                        state = ParsingState.BODY;

                        if (isEncounteringCodeblockDelimiter) {
                            docstringParsingState = DocstringParsingState.IN_DOCSTRING;
                            indentationOfDocstring = StringUtil.findIndentation(line);

                            sb
                                    .append(trimConverter.convert(language, line))
                                    .append("\n");
                        } else {
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
                    break;
                default:
                    throw new IllegalStateException("Unhandled docstring parsing state: " + docstringParsingState);
            }
        }

        return sb.toString();
    }
}
