/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.betaphitheta.bulkinstaller;

import java.io.File;
import java.io.FilenameFilter;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

/**
 *
 * @author peter
 */
public class BulkInstaller {

    BundleContext bundleContext;

    public BulkInstaller(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public void bulkInstall(String directory, String filePatter) {
        bulkInstall(directory, filePatter, null);
    }

    public void bulkInstall(String directory, String filePatter, String[] suffixes) {
        File f = new File(directory);
        if (!f.exists()) {
            System.err.println("Directory " + directory + " not found!");
            return;
        }


        String suffixesPattern = "";
        if (suffixes != null) {
            for (String suffix : suffixes) {
                if (suffixesPattern.length() != 0) {
                    suffixesPattern += "|";
                }
                suffixesPattern += suffix;
            }
        }
        if (suffixesPattern.length() != 0) {
            suffixesPattern = ".?(" + suffixesPattern + ")";
        }
            
        
        String[] list = f.list(new PatternFilter(filePatter + suffixesPattern));
        if (list == null || list.length == 0) {
            System.err.println("No matching files found in " + directory);
            return;
        }

        for (String file : list) {
            try {
                Bundle installed = bundleContext.installBundle("file:" + directory + File.separatorChar + file);
                System.out.println(installed.getSymbolicName() + " with version " + installed.getVersion().toString() + " installed under id " + installed.getBundleId());
            } catch (BundleException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    private class PatternFilter implements FilenameFilter {

        private final String pattern;

        PatternFilter(String pattern) {
            this.pattern = pattern;
        }

        public boolean accept(File dir, String name) {
            return name.matches(pattern);
        }
    }
}
