package no.spk.misc.converter.gherkintomd.converter;

public class DocstringConverter {

    private static final String DOCSTRING_DELIMITER = "\"\"\"";
    private static final String BACKTICS = "```";

    private enum DocstringParsingState {
        IN_DOCSTRING,
        OUTSIDE_DOCSTRING
    }

    private DocstringParsingState state = DocstringParsingState.OUTSIDE_DOCSTRING;

    public String convert(final String input) {
        final StringBuilder sb = new StringBuilder();

        boolean previousLineWasEmpty = true;

        for (final String line : input.split("\n")) {
            switch (state) {
                case IN_DOCSTRING:
                    if (line.trim().startsWith(DOCSTRING_DELIMITER)) {
                        state = DocstringParsingState.OUTSIDE_DOCSTRING;

                        sb
                                .append(line.replaceAll(DOCSTRING_DELIMITER, BACKTICS))
                                .append("\n");
                    } else {
                        sb
                                .append(line)
                                .append("\n");
                    }
                    break;
                case OUTSIDE_DOCSTRING:
                    if (line.trim().startsWith(DOCSTRING_DELIMITER)) {
                        state = DocstringParsingState.IN_DOCSTRING;

                        sb
                                .append(previousLineWasEmpty ? "" : "\n")
                                .append(line.replaceAll(DOCSTRING_DELIMITER, BACKTICS))
                                .append("\n");
                    } else {
                        sb
                                .append(line)
                                .append("\n");
                    }
                    break;
                default:
                    break;
            }

            previousLineWasEmpty = line.isEmpty();
        }

        return sb.toString();
    }
}
