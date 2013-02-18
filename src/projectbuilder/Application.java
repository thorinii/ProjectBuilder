package projectbuilder;

import projectbuilder.build.ProjectBuilder;
import projectbuilder.project.ProjectManager;
import projectbuilder.project.StandardProjectDao;
import projectbuilder.queue.BuildQueue;
import projectbuilder.queue.StandardBuildQueue;
import projectbuilder.upload.Uploader;

/**
 *
 * @author lachlan
 */
public class Application {

    private final Config config;
    private final BuildQueue buildQueue;
    private final ProjectManager projectManager;
    private final ProjectBuilder builder;

    public Application() {
        config = new Config();
        buildQueue = new StandardBuildQueue();
        projectManager = new ProjectManager(new StandardProjectDao(), buildQueue);

        builder = new ProjectBuilder(null);
        buildQueue.addProcessor(builder);
    }

    public Config getConfig() {
        return config;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }
}
