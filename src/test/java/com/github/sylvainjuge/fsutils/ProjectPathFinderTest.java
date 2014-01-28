package com.github.sylvainjuge.fsutils;


import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectPathFinderTest {

    @Test
    public void findThisProjectPath() throws IOException {
        Path project = ProjectPathFinder.getFolder("fsutils");

        assertThat(project).isNotNull();
        assertThat(Files.isDirectory(project)).isTrue();

        List<Path> paths = new ArrayList<>();
        for (Path p : Files.newDirectoryStream(project)) {
            paths.add(p);
        }

        Path src = project.resolve("src");
        Path target = project.resolve("target");
        Path pomXml = project.resolve("pom.xml");

        assertThat(paths).contains(src, target, pomXml);
    }
}
