/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.build;

import java.util.logging.Level;
import java.util.logging.Logger;
import projectbuilder.project.Project;
import projectbuilder.queue.BuildProcessor;
import projectbuilder.queue.BuildQueue;

/**
 *
 * @author lachlan
 */
public class ProjectBuilder implements BuildProcessor {

    private static final Logger LOG = Logger.getLogger(ProjectBuilder.class.
            getName());
    private final BuildQueue buildQueue;

    public ProjectBuilder(BuildQueue buildQueue) {
        this.buildQueue = buildQueue;
    }

    @Override
    public void process(Project project) {
        LOG.log(Level.INFO, "Building {0}", project);
        
        
    }
}
