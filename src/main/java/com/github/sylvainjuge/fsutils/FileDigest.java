package com.github.sylvainjuge.fsutils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class FileDigest {
    private final String algorithm;
    private final int bufferSize;

    public FileDigest(String algorithm, int bufferSize) {
        this.algorithm = algorithm;
        this.bufferSize = bufferSize;
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("buffer size must be > 0");
        }
        newMessageDigest(algorithm);
    }

    public String digest(Path file) throws IOException {
        FileChannel channel = FileChannel.open(file, StandardOpenOption.READ);
        ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);

        MessageDigest digest = newMessageDigest(algorithm);
        while (channel.read(readBuffer) > 0) {
            readBuffer.flip();
            digest.update(readBuffer);
            if (readBuffer.position() == readBuffer.capacity()) {
                readBuffer.clear();
            }
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : digest.digest()) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static MessageDigest newMessageDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
