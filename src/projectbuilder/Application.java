package projectbuilder;

import projectbuilder.build.ProjectBuilder;
import projectbuilder.project.ProjectManager;
import projectbuilder.queue.BuildQueue;
import projectbuilder.queue.StandardBuildQueue;

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
        projectManager = new ProjectManager(buildQueue);

        builder = new ProjectBuilder(buildQueue);
        buildQueue.addProcessor(builder);
    }

    public Config getConfig() {
        return config;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }
}
