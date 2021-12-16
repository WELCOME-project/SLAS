package edu.upf.taln.welcome.slas.commons.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AutoDeletingTempFile implements AutoCloseable {

    private final Path file;

    public AutoDeletingTempFile() throws IOException {
        file = Files.createTempFile(null, null);
    }

    public Path getFile() {
        return file;
    }

    @Override
    public void close() throws IOException {
        Files.deleteIfExists(file);
    }
}
