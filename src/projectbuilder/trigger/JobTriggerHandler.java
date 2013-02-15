package projectbuilder.trigger;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import projectbuilder.Application;
import projectbuilder.trigger.JobNotification.Build;

/**
 *
 * @author lachlan
 */
public class JobTriggerHandler implements Runnable {

    private static final Logger LOG = Logger.getLogger(JobTriggerHandler.class.
            getName());
    private final Application app;
    private final BufferedReader reader;

    public JobTriggerHandler(Application app, Socket client) throws IOException {
        this.app = app;
        reader = new BufferedReader(new InputStreamReader(
                client.getInputStream()));
    }

    @Override
    public void run() {
        try {
            try {
                readData();
            } finally {
                reader.close();
            }
        } catch (IOException ioe) {
            LOG.log(Level.WARNING, "Error reading trigger", ioe);
        }
    }

    private void readData() throws IOException {
        StringBuilder lines = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            lines.append(line).append('\n');
        }

        Gson gson = new Gson();
        JobNotification notification = gson.fromJson(lines.toString(),
                                                     JobNotification.class);
        process(notification);
    }

    private void process(JobNotification notification) {
        Build build = notification.getBuild();
        if (build.isFinished() && build.isSuccessful()) {
            LOG.log(Level.INFO, "Triggering Build for {0}",
                    notification.getName());

            app.getProjectManager().pushJob(notification);
        }
    }
}