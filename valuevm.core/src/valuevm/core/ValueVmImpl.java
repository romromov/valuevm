package valuevm.core;

import java.util.concurrent.Callable;

/**
 * The list of protected resources is here: https://docs.oracle.com/javase/7/docs/technotes/guides/security/permissions.html#RuntimePermission
 * NOTE! Must be run with given .policy and .security files.
 * -Djava.security.manager
 * -Djava.security.policy==file:/<path to file>/valuevm.policy
 * -Djava.security.properties==file:/<path to file>/valuevm.security
 * <p>
 * For debug use -Djava.security.debug="access,domain"
 *
 * @author Roman Katerinenko
 */

public class ValueVmImpl {
    public void executeDapp(byte[] jar, String moduleName) throws Exception {
        final var dappLoader = new DappLoader();
        dappLoader.loadJarIntoNewLayer(jar, moduleName);
        final var dappMain = (Callable<?>) dappLoader.getMainClass().getDeclaredConstructor().newInstance();
        dappMain.call();
    }
}