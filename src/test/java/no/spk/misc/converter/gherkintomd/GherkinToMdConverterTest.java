package no.spk.misc.converter.gherkintomd;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class GherkinToMdConverterTest {

    @Test
    public void shouldConvertFeatureToH1_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/01_EN_feature_only.feature")
                )
        )
                .isEqualTo("# This is a feature\n");
    }

    @Test
    public void shouldConvertFeatureToH1_NO_1() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/01_NO_1_feature_only.feature")
                )
        )
                .isEqualTo("# Dette er en feature\n");
    }

    @Test
    public void shouldConvertScenarioToH2_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/02_EN_scenario_only.feature")
                )
        )
                .isEqualTo("## This is a scenario\n");
    }

    @Test
    public void shouldConvertScenarioToH2_NO() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/02_NO_scenario_only.feature")
                )
        )
                .isEqualTo("## Dette er en scenariomal\n");
    }

    @Test
    public void shouldConvertGivenToBold_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/03_EN_given_only.feature")
                )
        )
                .isEqualTo("**Given** that we have something\n");
    }

    @Test
    public void shouldConvertGivenToBold_NO() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/03_NO_given_only.feature")
                )
        )
                .isEqualTo("**Gitt** at vi har noe\n");
    }

    @Test
    public void shouldConvertWhenToBold_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/04_EN_when_only.feature")
                )
        )
                .isEqualTo("**When** doing something\n");
    }

    @Test
    public void shouldConvertWhenToBold_NO() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/04_NO_when_only.feature")
                )
        )
                .isEqualTo("**Når** man gjør noe\n");
    }

    @Test
    public void shouldConvertThenToBold_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/05_EN_then_only.feature")
                )
        )
                .isEqualTo("**Then** do something\n");
    }

    @Test
    public void shouldConvertThenToBold_NO() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/05_NO_then_only.feature")
                )
        )
                .isEqualTo("**Så** gjør noe\n");
    }

    @Test
    public void shouldConvertAndToBold_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/06_EN_and_only.feature")
                )
        )
                .isEqualTo("**And** also do something else\n");
    }

    @Test
    public void shouldConvertAndToBold_NO() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/06_NO_and_only.feature")
                )
        )
                .isEqualTo("**Og** også gjør det\n");
    }

    @Test
    public void shouldConvertBackgroundToH2_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/07_EN_background_only.feature")
                )
        )
                .isEqualTo("## Background\n");
    }

    @Test
    public void shouldConvertBackgroundToH2_NO() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/07_NO_background_only.feature")
                )
        )
                .isEqualTo("## Bakgrunn\n");
    }

    @Test
    public void shouldConvertExamplesToH3_EN() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/08_EN_examples_only.feature")
                )
        )
                .isEqualTo("### Examples\n");
    }

    @Test
    public void shouldConvertExamplesToH3_NO() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/08_NO_examples_only.feature")
                )
        )
                .isEqualTo("### Eksempler\n");
    }

    @Test
    public void shouldConvertOneTableCorrectly() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/09_one_table.feature")
                )
        )
                .isEqualTo(
                        readResourceFile("simple/09_one_table_expected.md")
                );
    }

    @Test
    public void shouldConvertTwoTablesCorrectly() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readResourceFile("simple/10_two_tables.feature")
                )
        )
                .isEqualTo(
                        readResourceFile("simple/10_two_tables_expected.md")
                );
    }

    @Test
    public void shouldConvertAllFilesIntermediateFolderCorrectly() throws IOException, URISyntaxException {
        final List<String> featureFiles = allFeaturesInResourceFolder("/intermediate");

        featureFiles.forEach(
                featureFile ->
                {
                    try {
                        assertThat(
                                new GherkinToMdConverter().convert(
                                        readResourceFile("intermediate/" + featureFile)
                                )
                        )
                                .isEqualTo(
                                        readResourceFile("intermediate/" + featureFile + ".md")
                                );
                    } catch (final IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
        );
    }

    private String readResourceFile(final String filename) throws IOException {
        return Files.readString(
                Path.of(
                        requireNonNull(getClass().getClassLoader().getResource(filename)).getPath()
                )
        );
    }

    private List<String> allFeaturesInResourceFolder(final String subfolder) throws URISyntaxException, IOException {
        final URL folderUrl = getClass().getResource(subfolder);
        assert folderUrl != null;
        final Path folder = Paths.get(folderUrl.toURI());
        return Files.list(folder)
                .filter(f -> f.toFile().getName().endsWith(".feature"))
                .map(f -> f.getFileName().toString())
                .collect(Collectors.toList());
    }
}
