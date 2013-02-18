/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.project;

import java.io.File;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author lachlan
 */
public class StandardProjectDaoTest {

    @Test
    public void testNewProject() {
        String name = "Project Project";
        StandardProjectDao instance = new StandardProjectDao();

        Project expResult = new Project(name);
        Project result = instance.newProject(name);

        assertEquals(expResult, result);
        assertFalse(result.getConfig().isEnabled());

        assertTrue(
                new File("projects/project-project").exists());
        assertTrue(
                new File("projects/project-project/project.properties").exists());
    }

    @Test
    public void testGetProjects() {
    }

    @Test
    public void testGetProjectByName() {
    }
}
