/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
        File projectsDir = new File("projects");
        if (!projectsDir.exists()) {
            projectsDir.mkdir();
            return;
        }

        for (File projectDir : projectsDir.listFiles()) {
            if (!projectDir.isDirectory())
                continue;

            File configFile = new File(projectDir, "project.properties");
            if (configFile.exists())
                loadProject(configFile);
        }
    }

    private void loadProject(File configFile) throws IOException {
        Properties props = new Properties();
        try (Reader reader = new FileReader(configFile)) {
            props.load(reader);
        }

        Project project = new Project(props.getProperty("name"));
        project.getConfig().load(props);
        projects.add(project);
    }

    /**
     * Creates a new project and saves a blank config file.
     */
    @Override
    public Project newProject(String name) {
        LOG.log(Level.INFO, "Creating new project: {0}", name);

        Project project = new Project(name);

        Properties props = new Properties();
        props.setProperty("name", name);
        project.getConfig().save(props);

        File projectDir = new File("projects/" + project.getProjectDir());
        if (!projectDir.exists())
            projectDir.mkdir();

        File configFile = new File(projectDir, "/project.properties");
        try (Writer writer = new FileWriter(configFile)) {
            props.store(writer, "When finished setting up " + name
                    + ", set 'enabled' to true");
        } catch (IOException ioe) {
            LOG.log(Level.WARNING, "Could not save project", ioe);
        }

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
