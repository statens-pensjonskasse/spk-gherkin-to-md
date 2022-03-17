package no.spk.misc.converter.gherkintomd.converter;

import java.util.ArrayList;
import java.util.List;

import no.spk.misc.converter.gherkintomd.Language;
import no.spk.misc.converter.gherkintomd.util.StringUtil;

public class TableConverter {

    private static final String DOCSTRING_DELIMITER = "\"\"\"";
    private static final String BACKTICS = "```";

    private enum DocstringParsingState {
        IN_DOCSTRING,
        OUTSIDE_DOCSTRING
    }

    private enum TableParsingState {
        IN_TABLE_HEADER,
        IN_TABLE,
        OUTSIDE_TABLE
    }

    private static final SingleLineConverter trimConverter = new TrimConverter();

    private TableParsingState state = TableParsingState.OUTSIDE_TABLE;

    private DocstringParsingState docstringParsingState = DocstringParsingState.OUTSIDE_DOCSTRING;

    public String convert(final String input) {
        final StringBuilder sb = new StringBuilder();

        boolean previousLineWasEmpty = true;
        int indentationOfDocstring = 0;

        for (final String line : input.split("\n")) {
            final boolean isEncounteringCodeblockDelimiter = line.trim().startsWith(DOCSTRING_DELIMITER) || line.trim().startsWith(BACKTICS);

            switch (docstringParsingState) {
                case IN_DOCSTRING:
                    if (isEncounteringCodeblockDelimiter) {
                        docstringParsingState = DocstringParsingState.OUTSIDE_DOCSTRING;
                        sb
                                .append(trimConverter.convert(Language.EN, line))
                                .append("\n");
                    } else {
                        final int indentationOfLine = StringUtil.findIndentation(line);
                        final int indentationToBeUsed = indentationOfLine - indentationOfDocstring;
                        sb
                                .append(StringUtil.createIndentation(indentationToBeUsed))
                                .append(trimConverter.convert(Language.EN, line))
                                .append("\n");
                    }
                    break;
                case OUTSIDE_DOCSTRING:
                    if (line.trim().startsWith("|")) {
                        if (state == TableParsingState.OUTSIDE_TABLE) {
                            state = TableParsingState.IN_TABLE_HEADER;

                            sb
                                    .append(previousLineWasEmpty ? "" : "\n")
                                    .append(line)
                                    .append("\n");
                        } else if (state == TableParsingState.IN_TABLE_HEADER) {
                            state = TableParsingState.IN_TABLE;

                            sb
                                    .append(
                                            createHeader(
                                                    numberOfColumns(line)
                                            )
                                    )
                                    .append("\n")
                                    .append(line)
                                    .append("\n");
                        } else {
                            sb
                                    .append(line)
                                    .append("\n");
                        }
                    } else if (isEncounteringCodeblockDelimiter) {
                        docstringParsingState = DocstringParsingState.IN_DOCSTRING;
                        indentationOfDocstring = StringUtil.findIndentation(line);

                        sb
                                .append(line)
                                .append("\n");
                    } else {
                        state = TableParsingState.OUTSIDE_TABLE;

                        sb
                                .append(line)
                                .append("\n");
                    }
                    break;
                default:
                    throw new IllegalStateException("Unhandled docstring parsing state: " + docstringParsingState);
            }

            previousLineWasEmpty = line.isEmpty();
        }

        return sb.toString();
    }

    private int numberOfColumns(final String line) {
        return line.split("\\|").length;
    }

    private String createHeader(final int numberOfColumns) {
        final List<String> bars = new ArrayList<>();
        for (int col = 0; col < numberOfColumns; col++) {
            bars.add("|");
        }

        return String.join("-", bars);
    }
}
