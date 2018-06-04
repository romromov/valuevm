package valuevm.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static valuevm.core.FileUtils.getFSRootDirFor;
import static valuevm.core.FileUtils.putToTempDir;

/**
 * @author Roman Katerinenko
 */
class DappClasses {
    private final String mainClass;
    private final Map<String, byte[]> classes;
    private final Path pathToModule;

    private DappClasses(String mainClass, Map<String, byte[]> classes, Path pathToModule) {
        this.mainClass = mainClass;
        this.classes = classes;
        this.pathToModule = pathToModule;
    }

    static DappClasses ofJar(byte[] jar) throws IOException {
        Objects.requireNonNull(jar);
        //todo fix puts jar to temp dir twice
        String mainClass = readMainClassQualifiedNameFrom(jar);
        final var dappReader = new DAppReaderWriter();
        Map<String, byte[]> classNameToBytes = dappReader.readClassesFromJar(jar);
        return new DappClasses(mainClass, classNameToBytes, dappReader.getPathToJar());
    }

    private static String readMainClassQualifiedNameFrom(byte[] jar) throws IOException {
        final Path pathToJar = putToTempDir(jar, "valuevmtemp", "dapp-temp.jar");
        final Path rootInJar = getFSRootDirFor(pathToJar);
        final var container = new ArrayList<String>(1);
        Files.walkFileTree(rootInJar, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.getFileName().toString().equalsIgnoreCase("MANIFEST.MF")) {
                    container.add(extractMainClassNameFrom(file));
                    return FileVisitResult.TERMINATE;
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return container.size() == 1 ? container.get(0) : null;
    }

    private static String extractMainClassNameFrom(Path file) {
        final var propertyKey = "Main-Class";
        try (InputStream in = Files.newInputStream(file)) {
            final var properties = new Properties();
            properties.load(in);
            Object result = properties.get(propertyKey);
            return result.toString();
        } catch (IOException e) {
            //todo add logging
            System.out.print(String.format("Can't find property %s in jar %s", propertyKey, file));
        }
        return null;
    }


    Map<String, byte[]> getClasses() {
        return Collections.unmodifiableMap(classes);
    }

    String getMainClass() {
        return mainClass;
    }

    public Path getPathToModule() {
        return pathToModule;
    }
}