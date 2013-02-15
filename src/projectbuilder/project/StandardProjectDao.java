/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lachlan
 */
public class StandardProjectDao implements ProjectDao {

    private static final Logger LOG = Logger.getLogger(StandardProjectDao.class.
            getName());
    private final List<Project> projects;

    public StandardProjectDao() {
        projects = new ArrayList<>();

        try {
            loadProjects();
        } catch (IOException ioe) {
            LOG.log(Level.WARNING, "Could not load projects", ioe);
        }
    }

    private void loadProjects() throws IOException {
        
    }

    @Override
    public Project newProject(String name) {
        Project project = new Project(name);
        projects.add(project);
        return project;
    }

    @Override
    public List<Project> getProjects() {
        return projects;
    }

    @Override
    public Project getProjectByName(String name) {
        for (Project project : projects)
            if (project.getName().equals(name))
                return project;
        return null;
    }
}
