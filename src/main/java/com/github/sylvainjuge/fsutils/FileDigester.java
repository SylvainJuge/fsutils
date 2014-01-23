package com.github.sylvainjuge.fsutils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class FileDigester {
    private final int bufferSize;
    private final HashFunction hashFunction;

    public FileDigester(HashFunction hashFunction, int bufferSize) {
        this.hashFunction = hashFunction;
        this.bufferSize = bufferSize;
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("buffer size must be > 0");
        }
    }

    public String digest(Path file) throws IOException {
        FileChannel channel = FileChannel.open(file, StandardOpenOption.READ);
        ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);

        Hasher hasher = hashFunction.newHasher();
        while (channel.read(readBuffer) > 0) {
            readBuffer.flip();
            hasher.putBytes(readBuffer.array(), readBuffer.position(), readBuffer.limit());
            if (readBuffer.position() == readBuffer.capacity()) {
                readBuffer.clear();
            }
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : hasher.hash().asBytes()) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}