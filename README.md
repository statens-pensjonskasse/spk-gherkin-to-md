SPK's Gherkin to markdown
=====================================

Gherkin to markdown is a CLI-based tool that is used to convert Gherkin (feature) files to markdown.

How to build
====================================

In the root of the project, run `mvn clean install`.

How to run
========================================

Copy `gherkintomd-cli-*-jar-with-dependencies.jar*` from *gherkintocli/gherkintocli/target*.

You can then run it with:

```sh
java -jar gherkintocli-*-jar-with-dependencies.jar ... <path>
```

Example:

```sh
java -jar gherkintocli-*-jar-with-dependencies.jar \
  ...
  /a/path/blabla
```
