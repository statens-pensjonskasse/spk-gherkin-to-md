package no.spk.misc.converter.gherkintomd.lib;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;

import no.spk.misc.converter.gherkintomd.lib.pass.Pass;

public class Plugins {

    public static List<Supplier<Pass>> getPlugins() throws MalformedURLException {
        return ServiceLoader
                .load(Pass.class, makePluginsClassloader())
                .stream()
                .map(p -> (Supplier<Pass>) p)
                .collect(toList());
    }

    private static URLClassLoader makePluginsClassloader() throws MalformedURLException {
        final File pluginsDirectory = new File("plugins");

        final File[] jarFiles = pluginsDirectory
                .listFiles(file -> file.getPath().toLowerCase().endsWith(".jar"));

        final URL[] urls = jarFiles == null ? new URL[0] : new URL[jarFiles.length];

        if (jarFiles != null) {
            for (int i = 0; i < jarFiles.length; i++) {
                urls[i] = jarFiles[i].toURI().toURL();
            }
        }

        return new URLClassLoader(urls);
    }
}
