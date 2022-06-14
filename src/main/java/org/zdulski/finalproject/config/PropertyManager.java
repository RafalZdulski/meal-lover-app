package org.zdulski.finalproject.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

public class PropertyManager {
    final static Logger LOG = LogManager.getLogger(PropertyManager.class);

    private final static String DEFAULT_PROPERTIES_FILE = "src/main/resources/default.properties";
    private final static String APP_PROPERTIES_FILE = "src/main/resources/app.properties";

    private Properties defaultProps = new Properties();
    private Properties appProps = null;

    private Hashtable listeners = null;


    private static Object lock = new Object();
    private static PropertyManager instance  = null;

    private PropertyManager() {
    }

    public static PropertyManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new PropertyManager();
                    instance.loadProperties();
                }
            }
        }
        return (instance);
    }

    private void loadProperties() {

        // create and load default properties

        FileInputStream in = null;
        try {
            in = new FileInputStream(DEFAULT_PROPERTIES_FILE);
            defaultProps.load(in);
            in.close();
        } catch (IOException e) {
            LOG.error("could not load default properties");
            e.printStackTrace();
            System.exit(1);
        }

        // create application properties with default
        appProps = new Properties(defaultProps);

        try {
            // user/application properties
            in = new FileInputStream(APP_PROPERTIES_FILE);
            appProps.load(in);
            in.close();
        } catch (Throwable th) {
            LOG.error("could not load user app properties");
        }

    }

    public void storeProperties() throws IOException {

        FileOutputStream out = new FileOutputStream(DEFAULT_PROPERTIES_FILE);
        defaultProps.store(out, "---Default properties---");
        out.close();


        out = new FileOutputStream(APP_PROPERTIES_FILE);
        appProps.store(out, "---App/User properties---");
        out.close();

    }

    public String getProperty(String key) {
        String val = null;
        if (key != null) {
            if (appProps != null)
                val = (String)appProps.getProperty(key);
            if (val == null) {
                val = defaultProps.getProperty(key);
            }
        }
        return (val);

    }

    /**
     * Sets Application/User String properties; default property values cannot be set.
     */
    public void setProperty(String key, String val) {

        ArrayList list  = null;
        Object oldValue = null;

        oldValue = getProperty(key);

        appProps.setProperty(key, val);
        if (listeners.containsKey(key)) {
            list = (ArrayList)listeners.get(key);
            int len = list.size();
            if (len > 0) {
                PropertyChangeEvent evt = new PropertyChangeEvent(this, key, oldValue, val);
                for (int i=0; i < len; i++) {
                    if (list.get(i) instanceof PropertyChangeListener)
                        ((PropertyChangeListener)list.get(i)).propertyChange(evt);
                }
            }
        }

    }

    public boolean addListener (String key, PropertyChangeListener listener) {
        boolean added = false;
        ArrayList list = null;
        if (listeners == null)
            listeners = new Hashtable();

        if (!listeners.contains(key)) {
            list = new ArrayList();
            added = list.add(listener);
            listeners.put(key, list);
        } else {
            list = (ArrayList)listeners.get(key);
            added = list.add(listener);
        }
        return (added);
    }

    public void removeListener (PropertyChangeListener listener) {
        if (listeners != null && listeners.size() > 0)
            listeners.remove(listener);
    }
}
