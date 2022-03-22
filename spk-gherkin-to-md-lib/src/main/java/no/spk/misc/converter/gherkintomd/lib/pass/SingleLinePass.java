package no.spk.misc.converter.gherkintomd.lib.pass;

import static java.util.stream.Collectors.toList;

import java.util.List;

import no.spk.misc.converter.gherkintomd.lib.Language;
import no.spk.misc.converter.gherkintomd.lib.converter.AndConverter;
import no.spk.misc.converter.gherkintomd.lib.converter.BackgroundConverter;
import no.spk.misc.converter.gherkintomd.lib.converter.ExamplesConverter;
import no.spk.misc.converter.gherkintomd.lib.converter.ExamplesParameterConverter;
import no.spk.misc.converter.gherkintomd.lib.converter.FeatureConverter;
import no.spk.misc.converter.gherkintomd.lib.converter.GivenConverter;
import no.spk.misc.converter.gherkintomd.lib.converter.ScenarioConverter;
import no.spk.misc.converter.gherkintomd.lib.converter.SingleLineConverter;
import no.spk.misc.converter.gherkintomd.lib.converter.ThenConverter;
import no.spk.misc.converter.gherkintomd.lib.converter.TrimConverter;
import no.spk.misc.converter.gherkintomd.lib.converter.WhenConverter;
import no.spk.misc.converter.gherkintomd.lib.util.StringUtil;
import no.spk.misc.converter.gherkintomd.lib.converter.ButConverter;

/**
 * Does a pass over the Gherkin content and performs the conversions to Markdown that can be done
 * by only looking at a single line. I.e. the easy conversions from Gherkin to Markdown.
 * <br>
 * Docstrings and code blocks should not have their content converted to Markdown.
 */
public class SingleLinePass implements Pass {

    private static final String DOCSTRING_DELIMITER = "\"\"\"";
    private static final String BACKTICS = "```";
    private static final String PREPROCESSOR_CHARACTER = "#";
    private static final String COMMENT_CHARACTER = "#";

    private enum DocstringParsingState {
        IN_DOCSTRING,
        OUTSIDE_DOCSTRING
    }

    private static final List<SingleLineConverter> converters = List.of(
            new FeatureConverter(),
            new ScenarioConverter(),
            new ExamplesConverter(),
            new ExamplesParameterConverter(),
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
            final boolean isEncounteringComment = line.trim().startsWith(COMMENT_CHARACTER);

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
                    } else if (state == ParsingState.HEADER && line.trim().startsWith(PREPROCESSOR_CHARACTER)) {
                        // Skipping other preprocessing directives, such as encoding.
                        continue;
                    } else {
                        state = ParsingState.BODY;

                        if (isEncounteringCodeblockDelimiter) {
                            docstringParsingState = DocstringParsingState.IN_DOCSTRING;
                            indentationOfDocstring = StringUtil.findIndentation(line);

                            sb
                                    .append(trimConverter.convert(language, line))
                                    .append("\n");
                        } else if (isEncounteringComment) {
                            continue;
                        } else {
                            final Language finalLanguage = language;
                            final List<SingleLineConverter> chosenConverters = converters
                                    .stream()
                                    .filter(converter -> converter.isRelevant(finalLanguage, line))
                                    .collect(toList());

                            if (chosenConverters.isEmpty()) {
                                sb
                                        .append(trimConverter.convert(finalLanguage, line))
                                        .append("\n");
                            } else {
                                String converted = line;
                                for (final SingleLineConverter c : chosenConverters) {
                                    converted = c.convert(language, converted);
                                }
                                sb
                                        .append(converted)
                                        .append("\n");
                            }
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
