/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.queue;

import projectbuilder.project.Project;
import projectbuilder.trigger.JobNotification;

/**
 *
 * @author lachlan
 */
public interface BuildQueue {

    public void addProcessor(BuildProcessor processor);

    public void removeProcessor(BuildProcessor processor);

    public void pushJob(Project project, JobNotification notification);
}
