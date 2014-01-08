package com.github.sylvainjuge.fsutils;

import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class ListPathVisitorTest extends TestCase {

    @Test
    public void defaultListIsEmpty() {
        ListPathVisitor visitor = new ListPathVisitor();
        assertThat(visitor.getList()).isEmpty();
    }

    @Test
    public void listSampleFiles() throws IOException {
        Path sampleFolder = ProjectPathFinder.getFolder("fsutils").resolve(Paths.get("src", "test", "resources", "testSample"));
        assertThat(Files.exists(sampleFolder)).isTrue();

        ListPathVisitor visitor = new ListPathVisitor();
        Files.walkFileTree(sampleFolder, visitor);

        List<Path> actual = new ArrayList<>();
        for (Path p : visitor.getList()) {
            actual.add(sampleFolder.relativize(p));
        }

        List<Path> expected = new ArrayList<>();
        expected.add(Paths.get(""));
        expected.add(Paths.get("file1"));
        expected.add(Paths.get("file2"));
        expected.add(Paths.get("folder", "file3"));
        expected.add(Paths.get("folder", "subfolder"));
        expected.add(Paths.get("folder", "subfolder", "file4"));

        assertThat(actual).containsAll(expected);
    }
}
