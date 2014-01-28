package com.github.sylvainjuge.fsutils;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;


public class CopyVisitorTest {

    private final static Path SAMPLE_FILES =
            ProjectPathFinder.getFolder("fsutils")
                    .resolve(Paths.get("src", "test", "resources", "testSample"));

    @Test
    public void copyFolderOnPrevisitDirectory() throws IOException {
        Path src = SAMPLE_FILES;

        Path target = Files.createTempDirectory("test").resolve("target");
        try {
            CopyVisitor visitor = new CopyVisitor(src, target);

            assertThat(Files.exists(target)).isFalse();
            visitor.preVisitDirectory(src, null);
            assertThat(Files.isDirectory(target)).isTrue();
        } finally {
            Files.deleteIfExists(target);
            Files.deleteIfExists(target.getParent());
        }
    }

    @Test
    public void copyFileOnVisitFile() throws IOException {
        Path src = SAMPLE_FILES;
        Path targetFolder = Files.createTempDirectory("test");

        Path targetFile = targetFolder.resolve("file1");
        Path srcFile = src.resolve("file1");

        try {

            assertThat(Files.exists(targetFile)).isFalse();

            CopyVisitor visitor = new CopyVisitor(src, targetFolder);
            visitor.visitFile(srcFile, null);

            assertThat(Files.isRegularFile(targetFile)).isTrue();
        } finally {
            Files.deleteIfExists(targetFile);
            Files.deleteIfExists(targetFolder);
        }
    }

//    @Test
//    public void copySampleFilesWithChecksum() {
//
//    }

}
