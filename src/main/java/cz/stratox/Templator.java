package cz.stratox;


import com.hubspot.jinjava.Jinjava;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Templator  {

    public static Path templateDirectory(Map<String, String> context, Path rootDir, List<String> excludes) {


            Deque<Path> deque = new LinkedList<>();

            List<String> ignoredDirs = Stream.concat(
                    List.of(".git", "templates", "META-INF", "public").stream(),
                    excludes.stream()
            ).collect(Collectors.toList());

            Jinjava jinjava = new Jinjava();

            deque.addLast(rootDir);

            while (!deque.isEmpty()) {
                Path currentDir = deque.pop();

                List<Path> files;
                try {
                    files = Files.list(currentDir).collect(Collectors.toList());
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }

                files.forEach(path -> {
                    if (Files.isDirectory(path)) {
                        if (path.toString().contains("{{")) {
                            Path tempPath = renamePath(jinjava, context, path);
                            deque.addLast(tempPath);
                        } else if (!ignoredDirs.contains(path.getFileName().toString())) {
                            deque.addLast(path);
                        }
                    } else if (Files.isRegularFile(path)) {
                        changeFileContent(jinjava, context, path);
                    }
                });
            }

            return rootDir;


    }


    private static void changeFileContent(Jinjava jinjava, Map<String, String> context, Path path) {
        try {
            String template = Files.readString(path);
            String renderedTemplate = jinjava.render(template, context);
            if (!template.equals(renderedTemplate)) {
                Files.writeString(path, renderedTemplate);
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Path renamePath(Jinjava jinjava, Map<String, String> context, Path oldPath) {
        try {
            String renderedPath = jinjava.render(oldPath.toString(), context);

            Path newPath = oldPath;
            if (renderedPath.contains(".")) { //we need to change io.oxus.service to multiple directories, like: io/oxus/service
                String[] parts = renderedPath.split("\\.");
                for (String part : parts) {
                    newPath = newPath.resolve(part); //first part is absolute and in this case newPath is replaced completely
                }
                FileUtils.moveDirectory(oldPath.toFile(), newPath.toFile());
            } else {
                FileUtils.moveDirectory(oldPath.toFile(), new File(renderedPath));
                newPath = Paths.get(renderedPath);
            }

            return newPath;

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
