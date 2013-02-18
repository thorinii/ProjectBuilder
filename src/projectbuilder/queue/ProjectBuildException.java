/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.queue;

/**
 *
 * @author lachlan
 */
public class ProjectBuildException extends Exception {

    public ProjectBuildException() {
    }

    public ProjectBuildException(String message) {
        super(message);
    }

    public ProjectBuildException(String message, Throwable cause) {
        super(message, cause);
    }
}
