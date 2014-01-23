package com.github.sylvainjuge.fsutils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.fest.assertions.api.Assertions.assertThat;

public class FileDigesterTest {

    private static final String TEST_FILE_ENCODING = "UTF-8";

    @Test
    public void md5() throws IOException {
        testFile(Hashing.md5(), "", "d41d8cd98f00b204e9800998ecf8427e");
        testFile(Hashing.md5(), "test message", "c72b9698fa1927e1dd12d3cf26ed84b2");
    }

    @Test
    public void sha1() throws IOException {
        testFile(Hashing.sha1(), "", "da39a3ee5e6b4b0d3255bfef95601890afd80709");
        testFile(Hashing.sha1(), "test message", "35ee8386410d41d14b3f779fc95f4695f4851682");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void tryNegativeBufferSize() {
        new FileDigester(Hashing.sha1(), -1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void tryZeroBufferSize() {
        new FileDigester(Hashing.sha1(), 0);
    }

    // TODO : hash non existing file should throw exception

    private void testFile(HashFunction hashFunction, String content, String expected) throws IOException {
        FileDigester digest = new FileDigester(hashFunction, 8);
        String result = null;
        Path file = null;
        try {
            file = Files.createTempFile(hashFunction.toString(), "test");
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
