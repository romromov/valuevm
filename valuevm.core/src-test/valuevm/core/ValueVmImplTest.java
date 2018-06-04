package valuevm.core;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Roman Katerinenko
 */
public class ValueVmImplTest {
    private static final String PATH_TO_JAR = "../test_resources/build/valuevm.testresource.test1.jar";
    private static final String MODULE_NAME = "valuevm.testresource.test1";

    @Test
    public void when_accessToProtectedCode_then_securityExceptionMustBeCaought() throws Exception {
        final byte[] jar = Files.readAllBytes(Paths.get(PATH_TO_JAR));
        new ValueVmImpl().executeDapp(jar, MODULE_NAME);
    }
}