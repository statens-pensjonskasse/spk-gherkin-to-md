package no.spk.misc.converter.gherkintomd.util;

public class StringUtil {

    /**
     * Finds indentation of a line in number of spaces.
     * Does not handle tab or mix of tabs and spaces, at the moment.
     *
     * @param line is the line to find indentation for
     * @return number of spaces the line is indented
     */
    public static int findIndentation(final String line) {
        int indentation = 0;

        for (char ch : line.toCharArray()) {
            if (ch == ' ') {
                indentation++;
            } else {
                break;
            }
        }

        return indentation;
    }

    public static String createIndentation(final int indentation) {
        return " ".repeat(Math.max(0, indentation));
    }
}
