/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.queue;

import projectbuilder.project.Project;

/**
 *
 * @author lachlan
 */
public class BuildRequest {

    private final Project project;
    private final String version;

    /**
     * A new BuildRequest for the HEAD version.
     */
    public BuildRequest(Project project) {
        this(project, "HEAD");
    }

    /**
     * A new BuildRequest for the selected version.
     */
    public BuildRequest(Project project, String version) {
        this.project = project;
        this.version = version;
    }

    public Project getProject() {
        return project;
    }

    public String getVersion() {
        return version;
    }

    public boolean isLatest() {
        return version.equals("HEAD");
    }

    @Override
    public String toString() {
        return "BuildRequest{" + "project=" + project.getName()
                + ", version=" + version + '}';
    }
}
