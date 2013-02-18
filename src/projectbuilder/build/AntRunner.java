/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.build;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.apache.tools.ant.*;

/**
 *
 * @author lachlan
 */
public class AntRunner {

    private static final Logger LOG = Logger.getLogger(AntRunner.class.getName());

    public void run(File buildFile, String target) throws IOException {
        Project project = new Project();
        project.init();

        ProjectHelper.configureProject(project, buildFile);
        //project.addBuildListener(new BuildWarningListener());

        try {
            project.executeTarget(target);
        } catch (BuildException be) {
            throw new IOException("Error in build", be);
        }
    }

    private class BuildWarningListener implements BuildListener {

        @Override
        public void buildStarted(BuildEvent event) {
            LOG.info("Build Started");
        }

        @Override
        public void buildFinished(BuildEvent event) {
            LOG.info("Build Finished");
        }

        @Override
        public void targetStarted(BuildEvent event) {
            System.out.println(event.getTarget().getName().trim());
        }

        @Override
        public void targetFinished(BuildEvent event) {
        }

        @Override
        public void taskStarted(BuildEvent event) {
            System.out.println("  " + event.getTask().getTaskName().trim());
        }

        @Override
        public void taskFinished(BuildEvent event) {
        }

        @Override
        public void messageLogged(BuildEvent event) {
            if (event.getTask() != null) {
                System.out.println("    {" + event.getMessage().trim() + "}");
            } else if (event.getTarget() != null) {
                System.out.println("  {" + event.getMessage().trim() + "}");

            } else {
                System.out.println("{" + event.getMessage().trim() + "}");
            }
        }
    }
}
