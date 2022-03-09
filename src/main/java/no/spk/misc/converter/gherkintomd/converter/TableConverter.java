package no.spk.misc.converter.gherkintomd.converter;

import java.util.ArrayList;
import java.util.List;

public class TableConverter {

    private enum TableParsingState {
        IN_TABLE_HEADER,
        IN_TABLE,
        OUTSIDE_TABLE
    }

    private TableParsingState state = TableParsingState.OUTSIDE_TABLE;

    public String convert(final String input) {
        final StringBuilder sb = new StringBuilder();

        for (final String line : input.split("\n")) {
            if (line.trim().startsWith("|")) {
                if (state == TableParsingState.OUTSIDE_TABLE) {
                    state = TableParsingState.IN_TABLE_HEADER;

                    sb
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
            } else {
                state = TableParsingState.OUTSIDE_TABLE;

                sb
                        .append(line)
                        .append("\n");
            }
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
