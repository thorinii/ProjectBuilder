package projectbuilder.trigger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import projectbuilder.Application;

/**
 *
 * @author lachlan
 */
public class JobTriggerListener {

    private static final Logger LOG = Logger.getLogger(JobTriggerListener.class.
            getName());
    private final Application app;
    private final Thread thread;
    private ServerSocket serverSocket;

    public JobTriggerListener(Application application) {
        this.app = application;
        this.thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    acceptSockets();
                } catch (IOException ioe) {
                    LOG.log(Level.SEVERE, "Errors accepting clients", ioe);
                }
            }
        }, "Job Trigger Listener");
    }

    public void start() throws IOException {
        if (serverSocket != null)
            throw new IllegalStateException("Already started");

        serverSocket = new ServerSocket(app.getConfig().getListenPort());
        thread.start();
    }

    private void acceptSockets() throws IOException {
        while (true) {
            Socket client = serverSocket.accept();
            handle(client);
        }
    }

    private void handle(Socket client) throws IOException {
        Thread handler = new Thread(
                new JobTriggerHandler(app, client),
                "Job Trigger Handler " + client.getInetAddress().getHostAddress());
        handler.start();
    }
}
