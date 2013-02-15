/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.trigger;

/**
 *
 * @author lachlan
 */
public class JobNotification {

    private String name;
    private String url;
    private Build build;

    public JobNotification(String name, String url, Build build) {
        this.name = name;
        this.url = url;
        this.build = build;
    }

    @Override
    public String toString() {
        return "JobNotification{" + "name=" + name + ", url=" + url + ", build=" + build + '}';
    }

    public Build getBuild() {
        return build;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public static class Build {

        public static final String PHASE_STARTED = "STARTED";
        public static final String PHASE_COMPLETED = "COMPLETED";
        public static final String PHASE_FINISHED = "FINISHED";
        public static final String STATUS_SUCCESS = "SUCCESS";
        private String full_url;
        private int number;
        private String phase;
        private String status;
        private String url;

        public Build(String full_url, int number, String phase, String status,
                String url) {
            this.full_url = full_url;
            this.number = number;
            this.phase = phase;
            this.status = status;
            this.url = url;
        }

        @Override
        public String toString() {
            return "Build{" + "full_url=" + full_url + ", number="
                    + number + ", phase=" + phase + ", status="
                    + status + ", url=" + url + '}';
        }

        public String getFullURL() {
            return full_url;
        }

        public int getNumber() {
            return number;
        }

        public String getPhase() {
            return phase;
        }

        public String getStatus() {
            return status;
        }

        public String getUrl() {
            return url;
        }

        public boolean isFinished() {
            return phase != null && phase.equals(PHASE_FINISHED);
        }

        public boolean isSuccessful() {
            return status != null && status.equals(STATUS_SUCCESS);
        }
    }
}
