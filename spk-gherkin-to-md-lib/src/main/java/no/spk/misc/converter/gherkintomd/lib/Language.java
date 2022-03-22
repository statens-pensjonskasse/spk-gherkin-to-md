package no.spk.misc.converter.gherkintomd.lib;

import java.util.Optional;
import java.util.stream.Stream;

public enum Language {
    EN("en"),
    NO("no");

    private final String languageCode;

    Language(final String languageCode) {
        this.languageCode = languageCode;
    }

    public static Language language(final String text) {
        return Optional.ofNullable(text)
                .map(String::trim)
                .map(String::toLowerCase)
                .map(line -> line.replaceAll("# language: ", ""))
                .flatMap(
                        code -> Stream.of(values())
                                .filter(language -> language.supports(code))
                                .findAny()
                )
                .orElse(
                        Language.EN
                );
    }

    private static Stream<String> supportedLanguages() {
        return Stream
                .of(values())
                .flatMap(Language::languageCodes);
    }

    private Stream<String> languageCodes() {
        return Stream.of(
                name(),
                languageCode
        );
    }

    private boolean supports(final String languageCode) {
        return languageCodes().anyMatch(languageCode::equals);
    }
}
