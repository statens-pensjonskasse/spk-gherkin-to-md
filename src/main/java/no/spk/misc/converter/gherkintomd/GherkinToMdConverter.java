package no.spk.misc.converter.gherkintomd;

public class GherkinToMdConverter {

    public String convert(final String gherkin) {
        final StringBuilder sb = new StringBuilder();

        for (final String line : gherkin.split("\n")) {
            if (line.startsWith("Feature:")) {
                sb.append(line.replace("Feature:", "#"));
            }
        }

        return sb.toString();
    }
}
