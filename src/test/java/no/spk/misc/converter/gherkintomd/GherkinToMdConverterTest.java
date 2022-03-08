package no.spk.misc.converter.gherkintomd;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;


public class GherkinToMdConverterTest {

    @Test
    public void shouldConvertFeatureToH1() throws IOException {
        assertThat(
                new GherkinToMdConverter().convert(
                        readTestFeatureFile("01_feature_only.feature")
                )
        )
                .isEqualTo("# This is a feature");
    }

    private String readTestFeatureFile(final String filename) throws IOException {
        return Files.readString(
                Path.of(
                        requireNonNull(getClass().getClassLoader().getResource(filename)).getPath()
                )
        );
    }
}
