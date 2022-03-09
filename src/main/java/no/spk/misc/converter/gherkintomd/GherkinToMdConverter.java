package no.spk.misc.converter.gherkintomd;

import no.spk.misc.converter.gherkintomd.converter.FeatureConverter;
import no.spk.misc.converter.gherkintomd.converter.ScenarioConverter;

public class GherkinToMdConverter {

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

            if (FeatureConverter.isFeature(language, line)) {
                sb.append(FeatureConverter.convert(language, line));
            } else if (ScenarioConverter.isScenario(language, line)) {
                sb.append(ScenarioConverter.convert(language, line));
            } else {
                sb.append(line);
            }

            lineNumber++;
        }

        return sb.toString();
    }
}
