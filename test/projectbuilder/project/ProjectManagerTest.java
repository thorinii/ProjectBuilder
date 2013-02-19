/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.project;

import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;
import projectbuilder.queue.BuildProcessor;
import projectbuilder.queue.BuildQueue;
import projectbuilder.queue.BuildRequest;
import projectbuilder.trigger.JobNotification;

/**
 *
 * @author lachlan
 */
public class ProjectManagerTest {

    @Test
    public void testNewProject() {
        ProjectManager manager = new ProjectManager(new DummyProjectDao(),
                                                    new DummyBuildQueue());
        JobNotification notification = makeNotification();

        manager.pushJob(notification);

        Project expected = new Project("New Project");

        List<Project> projects = manager.getProjects();
        assertEquals(1, projects.size());
        assertTrue("Project not created on push", projects.contains(
                expected));
    }

    @Test
    public void testExistingProject() {
        ProjectManager manager = new ProjectManager(new DummyProjectDao(),
                                                    new DummyBuildQueue());
        JobNotification notification = makeNotification();

        manager.pushJob(notification);
        manager.pushJob(notification);

        List<Project> projects = manager.getProjects();
        assertEquals(1, projects.size());
    }

    private JobNotification makeNotification() {
        return new JobNotification(
                "New Project",
                "URL",
                new JobNotification.Build(
                "URL/build/34",
                34,
                JobNotification.Build.PHASE_FINISHED,
                JobNotification.Build.STATUS_SUCCESS,
                "build/34"));
    }

    private static class DummyProjectDao implements ProjectDao {

        private final List<Project> projects;

        public DummyProjectDao() {
            projects = new ArrayList<>();
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

    private static class DummyBuildQueue implements BuildQueue {

        @Override
        public void addProcessor(BuildProcessor processor) {
        }

        @Override
        public void removeProcessor(BuildProcessor processor) {
        }

        @Override
        public void pushJob(Project project, JobNotification notification) {
        }

        @Override
        public void pushJob(BuildRequest buildRequest) {
        }
    }
}
