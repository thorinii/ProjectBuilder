/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.build;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tools.ant.*;

/**
 *
 * @author lachlan
 */
public class AntRunner {

    private static final Logger LOG = Logger.getLogger(AntRunner.class.getName());

    public void run(File buildFile, String target) throws IOException {
        LOG.fine("Initialising Ant Project");
        Project project = new Project();
        project.initProperties();
        project.addBuildListener(new BuildWarningListener());

        LOG.log(Level.FINE, "Reading {0}", buildFile.getPath());
        ProjectHelper.configureProject(project, buildFile);

        try {
            LOG.log(Level.FINE, "Executing {0}", target);
            project.executeTarget(target);
        } catch (BuildException be) {
            throw new IOException("Error in build", be);
        }
    }

    private class BuildWarningListener implements BuildListener {

        @Override
        public void buildStarted(BuildEvent event) {
            LOG.fine("Build Started");
        }

        @Override
        public void buildFinished(BuildEvent event) {
            LOG.fine("Build Finished");
        }

        @Override
        public void targetStarted(BuildEvent event) {
            LOG.fine(event.getTarget().getName().trim());
        }

        @Override
        public void targetFinished(BuildEvent event) {
        }

        @Override
        public void taskStarted(BuildEvent event) {
            LOG.fine("  " + event.getTask().getTaskName().trim());
        }

        @Override
        public void taskFinished(BuildEvent event) {
        }

        @Override
        public void messageLogged(BuildEvent event) {
            if (event.getTask() != null) {
                LOG.fine("    {" + event.getMessage().trim() + "}");
            } else if (event.getTarget() != null) {
                LOG.fine("  {" + event.getMessage().trim() + "}");

            } else {
                LOG.fine("{" + event.getMessage().trim() + "}");
            }
        }
    }
}
