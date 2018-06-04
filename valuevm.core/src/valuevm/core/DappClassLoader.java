package valuevm.core;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;

/**
 * @author Roman Katerinenko
 */
// todo check case of a class with empty package
public class DappClassLoader extends URLClassLoader {

    public DappClassLoader(URL urlToJar) {
        super(new URL[]{urlToJar}, ClassLoader.getSystemClassLoader());
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    @Override
    protected Class<?> loadClass(String dotClassName, boolean resolve) throws ClassNotFoundException {
        final String pkg = packageOf(dotClassName);
        if (pkg == null) {
            throw new ClassNotFoundException("Package is null for " + dotClassName);
        } else {
            final SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPackageAccess(pkg);
            }
            return super.loadClass(dotClassName, resolve);
        }
    }

    @Override
    protected Class<?> findClass(String dotClassName) throws ClassNotFoundException {
        final String pkg = packageOf(dotClassName);
        if (pkg == null) {
            throw new ClassNotFoundException("Package is null for " + dotClassName);
        } else {
            final SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPackageDefinition(pkg);
            }
            return super.findClass(dotClassName);
        }
    }

    @Override
    protected PermissionCollection getPermissions(CodeSource codesource) {
        final var noPermissions = new Permissions();
        return noPermissions;
    }

    private static String packageOf(String dotClassName) {
        if (dotClassName == null) {
            return null;
        } else {
            final int i = dotClassName.lastIndexOf(".");
            return (i != -1) ? dotClassName.substring(0, i) : null;
        }
    }
}

