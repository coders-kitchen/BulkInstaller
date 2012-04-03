/*
 *Copyright 2012 Peter Daum
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */

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
