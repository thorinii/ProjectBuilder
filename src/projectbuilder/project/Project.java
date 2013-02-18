/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.project;

import java.util.Objects;

/**
 *
 * @author lachlan
 */
public class Project {

    private final String name;
    private final ProjectConfig config;

    public Project(String name) {
        this.name = name;
        this.config = new ProjectConfig();
    }

    public Project(String name, ProjectConfig config) {
        this.name = name;
        this.config = config;
    }

    public String getName() {
        return name;
    }

    public ProjectConfig getConfig() {
        return config;
    }

    public boolean isEnabled() {
        return config.isEnabled();
    }

    public String getProjectDir() {
        return name.toLowerCase().replace(" ", "-");
    }

    @Override
    public String toString() {
        return "Project{" + "name=" + name + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Project other = (Project) obj;
        if (!Objects.equals(this.name, other.name))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.name);
        return hash;
    }
}
