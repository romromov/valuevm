package valuevm.testresource.test1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

/**
 * @author Roman Katerinenko
 */
public class CheckRuntimePolicies implements Callable<Void> {
    @Override
    public Void call() {
        tryAccessRuntime();
        tryAccessFiles();
        tryAccessClassLoader();
        tryAttackThread();
        tryAccessOtherClasses();
        return null;
    }

    private void tryAccessRuntime() {
        try {
            Runtime.getRuntime().addShutdownHook(Thread.currentThread());
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            Runtime.getRuntime().exec("pwd");
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        } catch (IOException ioe) {
            throw new IllegalStateException("Calling this is prohibited");
        }
        try {
            Runtime.getRuntime().load("./eveil_lib");
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            Runtime.getRuntime().halt(10);
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
    }

    private void tryAccessFiles() {
        try {
            Files.createTempDirectory("evil_dir");
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        } catch (IOException ioe) {
            throw new IllegalStateException("Calling this is prohibited");
        }
        try {
            Files.createFile(Paths.get("./evil.file.txt"));
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        } catch (IOException ioe) {
            throw new IllegalStateException("Calling this is prohibited");
        }
    }

    private void tryAccessOtherClasses() {
        try {
            this.getClass().getClassLoader().loadClass("evilpackage.Attacker");
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("ClassNotFound but should be SecurityException");
        }
    }

    private void tryAttackThread() {
        try {
            // stop OTHER thread groupa
            Thread.currentThread().getThreadGroup().getParent().stop();
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            // destroya OTHER thread groupa
            Thread.currentThread().getThreadGroup().getParent().destroy();
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
    }

    private void tryAccessClassLoader() {
        try {
            ClassLoader.getSystemClassLoader();
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            ClassLoader.getPlatformClassLoader();
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            Thread.currentThread().getContextClassLoader();
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            new ClassLoader() {
            };
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }
        try {
            this.getClass().getClassLoader().getParent();
            throw new IllegalStateException("Calling this is prohibited");
        } catch (SecurityException e) {
        }

    }
}