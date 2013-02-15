/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.project;

import java.util.List;

/**
 *
 * @author lachlan
 */
public interface ProjectDao {

    public Project newProject(String name);

    public List<Project> getProjects();

    public Project getProjectByName(String name);
    
}
