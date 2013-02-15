/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.project;

/**
 *
 * @author lachlan
 */
public class ProjectConfig {

    private boolean enabled;

    public ProjectConfig() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
