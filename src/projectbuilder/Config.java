package projectbuilder;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lachlan
 */
public class Config {

    private static final Logger LOG = Logger.getLogger(Config.class.getName());
    private final Properties props;

    public Config() {
        props = getDefault();

        try {
            loadConfiguration();
            saveConfiguration();
        } catch (IOException ioe) {
            LOG.log(Level.WARNING, "Could not load configuration", ioe);
        }
    }

    private void loadConfiguration() throws IOException {
        try (Reader in = new FileReader("config.properties")) {
            props.load(in);
        }
    }

    private void saveConfiguration() throws IOException {
        try (Writer out = new FileWriter("config.properties")) {
            props.store(out,
                        " Project Builder Configuration\n"
                    + " WARNING: this file will be rewritten every boot\n"
                    + " (keeping all changes, but in different order)");
        }
    }

    public int getListenPort() {
        return Integer.parseInt(props.getProperty("listen-port"));
    }

    private Properties getDefault() {
        Properties properties = new Properties();

        properties.setProperty("listen-port", "6787");

        return properties;
    }
}
