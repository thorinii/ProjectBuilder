/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.project;

import java.util.ArrayList;
import java.util.List;
import projectbuilder.queue.BuildQueue;
import projectbuilder.trigger.JobNotification;

/**
 *
 * @author lachlan
 */
public class ProjectManager {

    private final ProjectDao projectDao;
    private final BuildQueue buildQueue;

    public ProjectManager(ProjectDao projectDao,
            BuildQueue buildQueue) {
        this.projectDao = projectDao;
        this.buildQueue = buildQueue;
    }

    public void pushJob(JobNotification notification) {
        Project project = getProject(notification.getName());

        if (project.isEnabled())
            buildQueue.pushJob(project, notification);
    }

    private Project getProject(String name) {
        Project project = projectDao.getProjectByName(name);
        if (project != null)
            return project;
        else
            return projectDao.newProject(name);
    }

    public List<Project> getProjects() {
        return projectDao.getProjects();
    }
}
