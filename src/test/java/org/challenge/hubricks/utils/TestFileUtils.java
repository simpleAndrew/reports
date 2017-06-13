package org.challenge.hubricks.utils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class TestFileUtils {

    public static String getStringPathToResource(String resourceShortcut) {
        URL resource = TestFileUtils.class.getClassLoader().getResource(resourceShortcut);
        Objects.requireNonNull(resource);
        return resource.getFile();
    }

    public static Path getPathToResource(String resourceShortcut) {
        URL resource = TestFileUtils.class.getClassLoader().getResource(resourceShortcut);
        Objects.requireNonNull(resource);
        return Paths.get(resource.getFile());
    }

    public static Stream<String[]> readTestCsvResource(String resourceShortcut) throws IOException {
        return Files.lines(getPathToResource(resourceShortcut)).map(line -> line.split(","));
    }
}
