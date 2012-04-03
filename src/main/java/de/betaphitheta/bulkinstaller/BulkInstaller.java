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
import org.apache.felix.service.command.Descriptor;

/**
 * This class represents the implementation of a bulk install command for the gogo shell of Apache Felix.
 * 
 * It provides to methods. One for installing bundles from a directory, where the jar files matches a given pattern.
 * The other one does the same, but also takes care of a set of file suffixes for selection of bundle jars.
 * @author peter
 */
public class BulkInstaller {

    BundleContext bundleContext;

    public BulkInstaller(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Descriptor("installs every bundle from a directory, which matches a filename pattern")
    public void bulkInstall(@Descriptor("the directory where the files are located") String directory,
            @Descriptor("the basic pattern for filenames") String filePatter) {
        bulkInstall(directory, filePatter, null);
    }

    @Descriptor("installs every bundle from a directory, which matches a filename pattern and one of the suffixes")
    public void bulkInstall(@Descriptor("the directory where the files are located") String directory,
            @Descriptor("the basic pattern for filenames") String filePatter,
            @Descriptor("list of file suffixes") String[] suffixes) {
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
