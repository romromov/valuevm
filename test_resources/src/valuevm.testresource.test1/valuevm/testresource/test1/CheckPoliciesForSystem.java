package valuevm.testresource.test1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

import static java.lang.String.format;

/**
 * @author Roman Katerinenko
 */
public class CheckPoliciesForSystem implements Callable<Void> {
    private int expectedExceptionsCount;
    private int actualExceptionsCount;

    public Void call() throws Exception {
        callViaLanguage();
        callViaReflection();
        return null;
    }

    private void callViaLanguage() {
        try {
            System.setIn(null);
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            System.getProperties();
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            System.setSecurityManager(null);
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            System.exit(19);
            throw new IllegalStateException();
        } catch (SecurityException e) {
        }
        try {
            System.setProperty("abc", "");
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            System.setOut(null);
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            System.setErr(null);
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        // todo it's null
//        try {
//            System.console();
//            throw new IllegalStateException();
//        } catch (SecurityException e) {
//        }
        // todo it's null
//        try {
//            System.inheritedChannel();
//            throw new IllegalStateException();
//        } catch (SecurityException e) {
//        }
        // todo it's null
//        try {
//            System.getSecurityManager().checkAccess(Thread.currentThread());
//            throw new IllegalStateException();
//        } catch (SecurityException e) {
//        }
        // todo is it a problem ?
//        try {
//            System.currentTimeMillis();
//            throw new IllegalStateException();
//        } catch (SecurityException e) {
//        }
        // todo is it a problem ?
//        try {
//            System.nanoTime();
//            throw new IllegalStateException();
//        } catch (SecurityException e) {
//        }
        // todo is it a problem ?
//        try {
//            System.arraycopy(new int[1], 0, new int[1], 0, 0);
//            throw new IllegalStateException();
//        } catch (SecurityException e) {
//        }
        // todo is it a problem ?
//        try {
//            System.identityHashCode(null);
//            throw new IllegalStateException();
//        } catch (SecurityException e) {
//        }
        // todo is it a problem ?
//        try {
//            System.lineSeparator();
//            throw new IllegalStateException();
//        } catch (SecurityException e) {
//        }
        try {
            System.setProperties(null);
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            System.getProperty("af");
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            System.getProperty("adf", "adf");
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            System.clearProperty("asd");
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            System.getenv(null);
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            System.getenv();
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
    }

    private void callViaReflection() throws Exception {
        tryInvokeSystemMethod("setIn", null);
        tryInvokeSystemMethod("getProperties");
        tryInvokeSystemMethod("setSecurityManager", null);
        tryInvokeSystemMethod("exit", 10);
        tryInvokeSystemMethod("setProperty", "", "");
        tryInvokeSystemMethod("setOut", null);
        tryInvokeSystemMethod("setErr", null);
        tryInvokeSystemMethod("console");
        tryInvokeSystemMethod("inheritedChannel");
        tryInvokeSystemMethod("getSecurityManager");
        tryInvokeSystemMethod("currentTimeMillis");
        tryInvokeSystemMethod("nanoTime");
        tryInvokeSystemMethod("arraycopy", null, 1, null, 1, 1);
        tryInvokeSystemMethod("identityHashCode", null);
        tryInvokeSystemMethod("lineSeparator");
        tryInvokeSystemMethod("setProperties", null);
        tryInvokeSystemMethod("getProperty", null, null);
        tryInvokeSystemMethod("getProperty", null);
        tryInvokeSystemMethod("clearProperty", null);
        tryInvokeSystemMethod("getenv", null);
        tryInvokeSystemMethod("getenv");
        if (actualExceptionsCount != expectedExceptionsCount) {
            throw new IllegalStateException(format("Expected %d security exceptions but found %d", expectedExceptionsCount, actualExceptionsCount));
        }
    }

    private void tryInvokeSystemMethod(String name, Object... args) throws InvocationTargetException, IllegalAccessException {
        expectedExceptionsCount++;
        try {
            findMethodByName(name).invoke(null, args);
        } catch (SecurityException e) {
            actualExceptionsCount++;
        }
    }

    private static Method findMethodByName(String name, Object... args) {
        return Arrays
                .stream(System.class.getDeclaredMethods())
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .get();
    }
}