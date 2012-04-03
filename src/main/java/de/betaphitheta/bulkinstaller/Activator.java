package de.betaphitheta.bulkinstaller;

import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * This activator simply registers the bulkInstall command at the tutorial scope.
 * @author peter
 */
public class Activator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        Hashtable props = new Hashtable();
        props.put("osgi.command.scope", "tutorial");
        props.put("osgi.command.function", new String[] {
            "bulkInstall" });
        context.registerService(BulkInstaller.class.getName(), new BulkInstaller(context), props);
    }

    public void stop(BundleContext context) throws Exception {
    }

}
