package com.github.sylvainjuge.fsutils;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.fest.assertions.api.Assertions.assertThat;

public class FileDigestTest {

    private static final String TEST_FILE_ENCODING = "UTF-8";

    @Test
    public void md5() throws IOException {
        testFile("MD5", "", "d41d8cd98f00b204e9800998ecf8427e");
        testFile("MD5", "test message", "c72b9698fa1927e1dd12d3cf26ed84b2");
    }

    @Test
    public void sha1() throws IOException {
        testFile("SHA1", "", "da39a3ee5e6b4b0d3255bfef95601890afd80709");
        testFile("SHA1", "test message", "35ee8386410d41d14b3f779fc95f4695f4851682");
    }

    // TODO : hash non existing file should throw exception
    // TODO : create with <= 0 buffer size should throw exception

    private void testFile(String algorithm, String content, String expected) throws IOException {
        FileDigest digest = new FileDigest(algorithm, 1024);
        String result = null;
        Path file = null;
        try {
            file = Files.createTempFile(algorithm, "test");
            Files.write(file, content.getBytes(Charset.forName(TEST_FILE_ENCODING)));
            result = digest.digest(file);
        } finally {
            if (null != file) {
                Files.deleteIfExists(file);
            }
        }
        assertThat(result).isEqualTo(expected);
    }

}