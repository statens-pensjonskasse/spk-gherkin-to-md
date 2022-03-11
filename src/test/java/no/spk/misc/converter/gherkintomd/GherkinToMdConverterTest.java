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
    public void shouldConvertAllFilesSimpleFolderCorrectly() throws IOException, URISyntaxException {
        final List<String> featureFiles = allFeaturesInResourceFolder("/simple");

        featureFiles.forEach(
                featureFile ->
                {
                    try {
                        assertThat(
                                new GherkinToMdConverter().convert(
                                        readResourceFile("simple/" + featureFile)
                                )
                        )
                                .as(String.format("Simple folder: assertion failed in pair: %s - %s", featureFile, featureFile + ".md"))
                                .isEqualTo(
                                        readResourceFile("simple/" + featureFile + ".md")
                                );
                    } catch (final IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
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
                                .as(String.format("Intermediate folder: assertion failed in pair: %s - %s", featureFile, featureFile + ".md"))
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
