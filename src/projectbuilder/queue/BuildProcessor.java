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
public interface BuildProcessor {

    public void process(Project project) throws ProjectBuildException;
}
