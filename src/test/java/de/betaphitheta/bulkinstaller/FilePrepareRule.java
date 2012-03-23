/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.betaphitheta.bulkinstaller;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.w3c.dom.ls.LSException;

/**
 *
 * @author peter
 */
public class FilePrepareRule implements MethodRule {

    public static final String[] expectedFiles = {"test.jar", "test.war"};
    private final File baseDirectory;

    public FilePrepareRule(String baseFolder) {
        baseDirectory = new File(baseFolder);
    }

    public Statement apply(final Statement base, FrameworkMethod method, Object target) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                if (!baseDirectory.exists() && !baseDirectory.mkdirs()) {
                    throw new IOException("Directory " + baseDirectory.getAbsolutePath() + " does not exists and could not created!");
                }
                String[] list = baseDirectory.list();
                List<String> asList = Arrays.asList(list);

                for (String expectedFile : expectedFiles) {
                    if (!asList.contains(baseDirectory.getAbsolutePath() + File.separatorChar + expectedFile)) {
                        new File(baseDirectory.getAbsolutePath() + File.separatorChar + expectedFile).createNewFile();
                    }
                }

                base.evaluate();

                for (String expectedFile : expectedFiles) {
                    new File(baseDirectory.getAbsolutePath() + File.separatorChar + expectedFile).delete();
                }
                
                baseDirectory.delete();

            }
        };
    }
}
