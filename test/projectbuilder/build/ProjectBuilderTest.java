/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.build;

import org.junit.Test;
import org.junit.Ignore;
import projectbuilder.project.Project;
import projectbuilder.project.StandardProjectDao;
import projectbuilder.queue.ProjectBuildException;

/**
 *
 * @author lachlan
 */
public class ProjectBuilderTest {

    @Test
    @Ignore(value = "Too slow for unit tests... more an integration")
    public void testProcess() throws ProjectBuildException {
        StandardProjectDao dao = new StandardProjectDao();

        Project proj = dao.getProjectByName("test");
        if (proj == null) {
            dao.newProject("test");
            return;
        }

        ProjectBuilder builder = new ProjectBuilder(null);

        try {
            builder.process(proj);
        } catch (ProjectBuildException pbe) {
            pbe.getCause().printStackTrace();

            System.err.flush();
            System.out.flush();

            throw pbe;
        }

        System.err.flush();
        System.out.flush();
    }
}
