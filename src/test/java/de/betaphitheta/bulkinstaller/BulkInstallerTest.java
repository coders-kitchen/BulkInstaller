/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.betaphitheta.bulkinstaller;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Bundle;
import org.junit.Before;
import org.junit.After;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.osgi.framework.BundleException;
import org.osgi.framework.Version;

/**
 *
 * @author peter
 */
public class BulkInstallerTest {

    ByteArrayOutputStream errStream;
    ByteArrayOutputStream outStream;
    BulkInstaller installer;
    Bundle bundle;
    BundleContext context;

    @Rule
    public FilePrepareRule r = new FilePrepareRule("/tmp/testJar");
    
    public BulkInstallerTest() {
    }

    @Before
    public void setUp() throws BundleException {
        errStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errStream));
        
        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));
        bundle = mock(Bundle.class);
        context = mock(BundleContext.class);
        
        when(bundle.getBundleId()).thenReturn(new Long(1));
        when(bundle.getSymbolicName()).thenReturn("de.betaphitheta.bulkinstaller.Test");
        when(bundle.getVersion()).thenReturn(new Version("1.0.0"));
        
        
        installer = new BulkInstaller(context);
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void bulkInstallFailed() throws BundleException {
        when(context.installBundle("file:/tmp/testJar/test.jar")).thenThrow(new BundleException("Bundle not found!"));
        installer.bulkInstall("/tmp/testJar", "test.jar");
        String errMessage = errStream.toString().trim();
        assertTrue(errMessage.contains("Bundle not found!"));
    }

    @Test
    public void bulkInstallNoFilesFound() {
        installer.bulkInstall("/tmp/testJar", ".*.jra");
        String errorMessage = errStream.toString().trim();
        assertEquals("No matching files found in /tmp/testJar", errorMessage);
    }
    
    @Test
    public void bulkInstallDirectoryNotFound() {
        installer.bulkInstall("/tmper/", ".*.jar");
        String errorMessage = errStream.toString().trim();
        assertEquals("Directory /tmper/ not found!", errorMessage);
    }

    @Test
    public void bulkInstallWithSuffixes() throws BundleException {
        when(context.installBundle("file:/tmp/testJar/test.jar")).thenReturn(bundle);
        when(context.installBundle("file:/tmp/testJar/test.war")).thenReturn(bundle);
        String[] suffixes = {"jar", "war"};
        installer.bulkInstall("/tmp/testJar", "test", suffixes);
        String infoMessage = outStream.toString().trim();
        assertEquals("de.betaphitheta.bulkinstaller.Test with version 1.0.0 installed under id 1\nde.betaphitheta.bulkinstaller.Test with version 1.0.0 installed under id 1", infoMessage);
    }

    @Test
    public void bulkInstallWithoutSuffixes() throws BundleException {
        when(context.installBundle("file:/tmp/testJar/test.jar")).thenReturn(bundle);
        installer.bulkInstall("/tmp/testJar", "test.jar");
        String infoMessage = outStream.toString().trim();
        assertEquals(infoMessage, "de.betaphitheta.bulkinstaller.Test with version 1.0.0 installed under id 1");
    }
}
