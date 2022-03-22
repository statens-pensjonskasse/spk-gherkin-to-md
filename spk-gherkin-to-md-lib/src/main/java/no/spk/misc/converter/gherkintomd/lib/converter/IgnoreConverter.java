package no.spk.misc.converter.gherkintomd.lib.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.spk.misc.converter.gherkintomd.lib.Language;

public class IgnoreConverter implements UsedTagConverter {

    private static final String TAG = "@ignore";
    private static final String PREFIX = "(ignored)";
    private static final Pattern NEXT_LINE_PATTERN = Pattern.compile("(#*) .*");

    public String convert(final Language language, final String input) {
        final StringBuilder sb = new StringBuilder();

        boolean shouldNextLineHaveIgnorePrefix = false;

        for (final String line : input.split("\n")) {
            if (line.trim().startsWith(TAG)) {
                shouldNextLineHaveIgnorePrefix = true;
            } else if (shouldNextLineHaveIgnorePrefix) {
                final Matcher matcher = NEXT_LINE_PATTERN.matcher(line);

                if (matcher.matches()) {
                    final String headerPrefix = matcher.group(1);
                    sb
                            .append(headerPrefix)
                            .append(" ")
                            .append(PREFIX)
                            .append(line.replace(headerPrefix, ""))
                            .append("\n");
                } else {
                    sb
                            .append(PREFIX)
                            .append(" ")
                            .append(line)
                            .append("\n");
                }

                shouldNextLineHaveIgnorePrefix = false;
            } else {
                sb
                        .append(line)
                        .append("\n");
            }
        }

        return sb.toString();
    }
}
