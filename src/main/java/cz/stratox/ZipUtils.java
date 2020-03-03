package cz.stratox;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {
    final static String TMP_DIR_PREFIX = "oxus-tmp-";

    public static void extractStream(final InputStream inputStream, final Path destinationDir) {
        try (InputStream zipFileIs = inputStream;
             ZipInputStream zis = new ZipInputStream(Objects.requireNonNull(zipFileIs))
        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                final Path destination = destinationDir.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectory(destination);
                } else {
                    Files.copy(zis, destination);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    public static Path extractStreamToTempDirectory(final InputStream inputStream) {
        Path tempDirectory = null;
        try {
            tempDirectory = Files.createTempDirectory(TMP_DIR_PREFIX);

            extractStream(inputStream, tempDirectory);

            return tempDirectory;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception e) {
            if (tempDirectory != null) {
                FileUtils.deleteQuietly(tempDirectory.toFile());
            }

            throw e;
        }
    }

    public static Path extractFileTempDirectory(final Path input) {
        Path tempDirectory = null;
        try (final InputStream inputStream = Files.newInputStream(input)) {
            tempDirectory = Files.createTempDirectory(TMP_DIR_PREFIX);

            extractStream(inputStream, tempDirectory);

            return tempDirectory;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception e) {
            if (tempDirectory != null) {
                FileUtils.deleteQuietly(tempDirectory.toFile());
            }

            throw e;
        }
    }
}
