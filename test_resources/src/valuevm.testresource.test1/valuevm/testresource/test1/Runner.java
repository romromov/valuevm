package valuevm.testresource.test1;

import java.util.concurrent.Callable;

/**
 * @author Roman Katerinenko
 */
public class Runner implements Callable<Void> {

    @Override
    public Void call() throws Exception {
        new CheckRuntimePolicies().call();
        new CheckPoliciesForSystem().call();
        return null;
    }
}
