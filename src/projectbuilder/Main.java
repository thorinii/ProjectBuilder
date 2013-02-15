package projectbuilder;

import java.io.IOException;
import java.util.logging.*;
import projectbuilder.trigger.JobTriggerHandler;
import projectbuilder.trigger.JobTriggerListener;

/**
 *
 * @author lachlan
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        setupLogger();

        LOG.info("Starting up");

        Application app = new Application();

        JobTriggerListener listener = new JobTriggerListener(app);

        try {
            listener.start();
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, "Could not start trigger listener", ioe);
        }
        
        LOG.info("Ready");
    }

    private static void setupLogger() {
        Logger root = Logger.getLogger("projectbuilder");
        root.setLevel(Level.INFO);

        root.setUseParentHandlers(false);
        root.addHandler(new ConsoleHandler());

        try {
            FileHandler errorLogFile = new FileHandler(
                    "projectbuilder-error.txt");
            errorLogFile.setFormatter(new SimpleFormatter());
            errorLogFile.setLevel(Level.WARNING);

            root.addHandler(errorLogFile);
        } catch (IOException ioe) {
            LOG.log(Level.WARNING, "Cannot open log file", ioe);
        }

        try {
            FileHandler activityLogFile = new FileHandler(
                    "projectbuilder-activity.txt");
            activityLogFile.setFormatter(new SimpleFormatter());
            activityLogFile.setLevel(Level.INFO);

            root.addHandler(activityLogFile);
        } catch (IOException ioe) {
            LOG.log(Level.WARNING, "Cannot open log file", ioe);
        }
    }
}
