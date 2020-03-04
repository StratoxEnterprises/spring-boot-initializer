package cz.stratox;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Application {
    public static void main(String[] args) throws IOException {

    	if(args.length != 2) {
    		System.out.println("example: java -jar spring-boot-initializer.jar spring-boot-demo cz.oxus.cvut");
    		System.exit(1);
    	}
    	
        final var destination = Path.of("destination").toFile();
        if(destination.exists()) {
        	FileUtils.deleteDirectory(destination);
        }

        final var groupId = args[1];
        final var artifactId = args[0];

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

            FileUtils.copyDirectory(tempDirectory.toFile(), destination);
        } finally {
            FileUtils.deleteQuietly(tempDirectory.toFile());
        }

    }
}
