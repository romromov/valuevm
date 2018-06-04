package valuevm.core;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static java.util.Arrays.sort;

/**
 * @author Roman Katerinenko
 */
public class DAppReaderWriterTest {

    private static final String[] expectedReadClasses = {
            "com.example.twoclasses.C1",
            "com.example.twoclasses.C1$NestedClass",
            "com.example.twoclasses.C1$NestedInterface",
            "com.example.twoclasses.C1$InnerClass",
            "com.example.twoclasses.C2",
            "com.example.twoclasses.C2$1",
            "com.example.twoclasses.C2$NestedEnum",
            "com.example.twoclasses.JavaAccessor",
            "com.example.twoclasses.Main",
            "com.example.twoclasses.TestAnnotation",
            "com.example.twoclasses.innerpackage.C3"
    };

    static {
        sort(expectedReadClasses);
    }

    @Test
    public void checkExpectedClassesReadFromJar() throws IOException {
        final var module = "com.example.twoclasses";
        final var pathToJar = String.format("%s/%s.jar", "../test_resources/build", module);
        Map<String, byte[]> actualClasses = new DAppReaderWriter().readClassesFromJar(pathToJar);
        checkIfMatchExpected(actualClasses);
    }

    private static void checkIfMatchExpected(Map<String, byte[]> actual) {
        Assert.assertEquals(expectedReadClasses.length, actual.size());
        final var actualClassesArray = actual.keySet().toArray();
        sort(actualClassesArray);
        Assert.assertArrayEquals(expectedReadClasses, actualClassesArray);
    }
}