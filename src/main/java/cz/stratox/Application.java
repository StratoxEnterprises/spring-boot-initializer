package cz.stratox;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class Application {
    public static void main(String[] args) throws IOException {
        
        final var groupId = "cz.tripsis";
        final var artifactId = "spring-boot-demo";

        final var domainName = "dev.nonprod.oxus.oxuscloud.com";
        final var gitlabRepositoryHost = "gitlab.control.oxus.oxuscloud.com";

        final var context = new HashMap<String, String>();
        context.put("gitlabCustomerGroup", "test-oxus");
        context.put("dockerImageRepository", String.format("docker.%s", domainName));
        context.put("oxuscloudRepoReleasesUrl", String.format("https://nexus.%s/repository/maven-releases/", domainName));
        context.put("oxuscloudRepoSnapshotsUrl", String.format("https://nexus.%s/repository/maven-snapshots/", domainName));
        context.put("groupId", groupId);
        context.put("domainName", domainName);
        context.put("gitlabRepositoryHost", gitlabRepositoryHost);
        context.put("componentName", artifactId);
        context.put("packageName", String.format("%s.service", groupId));
        context.put("dockerImagePrefix", "oxus");
        context.put("sonarProjectKey", String.format("%s:s%s", groupId, artifactId));

        final var tempDirectory = Files.createTempDirectory("oxus-spring-boot-");
        try (InputStream resourceAsStream = Application.class.getClassLoader().getResourceAsStream("java-spring-boot.zip")) {
            ZipUtils.extractStream(resourceAsStream, tempDirectory);
            Templator.templateDirectory(context, tempDirectory, List.of());

            FileUtils.copyDirectory(tempDirectory.toFile(), Path.of("destination").toFile());
        }
        ;
    }
}
