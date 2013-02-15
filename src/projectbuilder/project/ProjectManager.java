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

    private final List<Project> projects;
    private final BuildQueue buildQueue;

    public ProjectManager(BuildQueue buildQueue) {
        projects = new ArrayList<>();
        this.buildQueue = buildQueue;
    }

    public void pushJob(JobNotification notification) {
        Project project = getProject(notification.getName());
        buildQueue.pushJob(project, notification);
    }

    private Project getProject(String name) {
        for (Project project : projects) {
            if (project.getName().equals(name))
                return project;
        }

        Project project = new Project(name);
        projects.add(project);
        return project;
    }

    public List<Project> getProjects() {
        return projects;
    }
}
