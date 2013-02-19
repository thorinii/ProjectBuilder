/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.queue;

/**
 *
 * @author lachlan
 */
public interface BuildProcessor {

    public void process(BuildRequest buildRequest) throws ProjectBuildException;
}
