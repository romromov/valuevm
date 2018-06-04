package valuevm.core;

import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import static java.lang.String.format;

/**
 * @author Roman Katerinenko
 */
class DappLoader {
    private Class<?> dAppMainClass;

    void loadJarIntoNewLayer(byte[] jar, String rootModuleName) throws IOException {
        final var dappClasses = DappClasses.ofJar(jar);
        final var dAppModulesFinder = ModuleFinder.of(dappClasses.getPathToModule());
        final var emptyFinder = ModuleFinder.of();
        final ModuleLayer bootLayer = ModuleLayer.boot();
        final Configuration dAppLayerConfig = bootLayer.configuration().resolve(dAppModulesFinder, emptyFinder, List.of(rootModuleName));
        final var classLoader = new DappClassLoader(urlOf(dappClasses.getPathToModule()));
        final Function<String, ClassLoader> moduleToLoaderMapper = (name) -> classLoader;
        final ModuleLayer dAppLayer = bootLayer.defineModules(dAppLayerConfig, moduleToLoaderMapper);
        try {
            dAppMainClass = dAppLayer.findModule(rootModuleName)
                    .orElseThrow(() -> new Exception("Module not found"))
                    .getClassLoader()
                    .loadClass(dappClasses.getMainClass());
        } catch (Exception e) {
            final var msg = format("Unable to load dApp. Start module:'%s', main class:'%s', module path:'%s'",
                    rootModuleName, dappClasses.getMainClass(), dappClasses.getPathToModule());
            // todo logging
            System.err.println(msg);
        }

    }

    private static URL urlOf(Path fileSystemPath) throws MalformedURLException {
        return new URL("file:" + fileSystemPath);
    }

    Class<?> getMainClass() {
        return dAppMainClass;
    }
}
