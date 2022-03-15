SPK's Gherkin to Markdown
=====================================

Gherkin to Markdown is a CLI-based tool that is used to convert Gherkin (feature) files to Markdown.

How to build
====================================

In the root of the project, run `mvn clean install`.

How to run
========================================

Copy `spk-gherkin-to-md-*-jar-with-dependencies.jar*` from *target/* to the directory you want to run it from.

You can then run it with:

```sh
java -jar spk-gherkin-to-md-*-jar-with-dependencies.jar <path>
```

Example:

```sh
java -jar spk-gherkin-to-md-*-jar-with-dependencies.jar /a/path/blabla
```

When the path is a file, the file will be converted to Markdown. When a directory is provided, the program
will recursively traverse the directory structure and convert all the feature files to Markdown.
