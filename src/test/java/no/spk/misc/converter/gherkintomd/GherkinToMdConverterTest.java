package no.spk.misc.converter.gherkintomd;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

public class GherkinToMdConverterTest {

    @Test
    public void shouldConvertFeatureToH1_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("01_EN_feature_only.feature")
                )
        )
                .isEqualTo("# This is a feature\n");
    }

    @Test
    public void shouldConvertFeatureToH1_NO_1() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("01_NO_1_feature_only.feature")
                )
        )
                .isEqualTo("# Dette er en feature\n");
    }

    @Test
    public void shouldConvertScenarioToH2_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("02_EN_scenario_only.feature")
                )
        )
                .isEqualTo("## This is a scenario\n");
    }

    @Test
    public void shouldConvertScenarioToH2_NO() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("02_NO_scenario_only.feature")
                )
        )
                .isEqualTo("## Dette er en scenariomal\n");
    }

    @Test
    public void shouldConvertGivenToBold_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("03_EN_given_only.feature")
                )
        )
                .isEqualTo("**Given** that we have something\n");
    }

    @Test
    public void shouldConvertGivenToBold_NO() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("03_NO_given_only.feature")
                )
        )
                .isEqualTo("**Gitt** at vi har noe\n");
    }

    @Test
    public void shouldConvertWhenToBold_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("04_EN_when_only.feature")
                )
        )
                .isEqualTo("**When** doing something\n");
    }

    @Test
    public void shouldConvertWhenToBold_NO() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("04_NO_when_only.feature")
                )
        )
                .isEqualTo("**Når** man gjør noe\n");
    }

    @Test
    public void shouldConvertThenToBold_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("05_EN_then_only.feature")
                )
        )
                .isEqualTo("**Then** do something\n");
    }

    @Test
    public void shouldConvertThenToBold_NO() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("05_NO_then_only.feature")
                )
        )
                .isEqualTo("**Så** gjør noe\n");
    }

    @Test
    public void shouldConvertAndToBold_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("06_EN_and_only.feature")
                )
        )
                .isEqualTo("**And** also do something else\n");
    }

    @Test
    public void shouldConvertAndToBold_NO() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("06_NO_and_only.feature")
                )
        )
                .isEqualTo("**Og** også gjør det\n");
    }

    @Test
    public void shouldConvertBackgroundToH2_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("07_EN_background_only.feature")
                )
        )
                .isEqualTo("## Background\n");
    }

    @Test
    public void shouldConvertBackgroundToH2_NO() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("07_NO_background_only.feature")
                )
        )
                .isEqualTo("## Bakgrunn\n");
    }

    @Test
    public void shouldConvertExamplesToH2_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("08_EN_examples_only.feature")
                )
        )
                .isEqualTo("## Examples\n");
    }

    @Test
    public void shouldConvertExamplesToH2_NO() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("08_NO_examples_only.feature")
                )
        )
                .isEqualTo("## Eksempler\n");
    }

    private String readTestFeatureFile(final String filename) throws IOException {
        return Files.readString(
                Path.of(
                        requireNonNull(getClass().getClassLoader().getResource(filename)).getPath()
                )
        );
    }
}
