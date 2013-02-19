/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.project;

import java.net.URI;
import java.util.Properties;

/**
 *
 * @author lachlan
 */
public class ProjectConfig {

    private boolean enabled;
    private SCSInfo scsInfo;
    private BuildInfo buildInfo;
    private PackagerInfo packagerInfo;

    public ProjectConfig() {
        enabled = false;
        scsInfo = new SCSInfo();
        buildInfo = new BuildInfo();
        packagerInfo = new PackagerInfo();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void load(Properties props) {
        enabled = Boolean.parseBoolean(props.getProperty("enabled"));
        scsInfo = new SCSInfo(props);
        buildInfo = new BuildInfo(props);
        packagerInfo = new PackagerInfo(props);
    }

    public void save(Properties props) {
        props.setProperty("enabled", Boolean.toString(enabled));
        scsInfo.save(props);
        buildInfo.save(props);
        packagerInfo.save(props);
    }

    public BuildInfo getBuildInfo() {
        return buildInfo;
    }

    public PackagerInfo getPackagerInfo() {
        return packagerInfo;
    }

    public SCSInfo getSCSInfo() {
        return scsInfo;
    }

    public static class SCSInfo {

        private String uri;

        public SCSInfo() {
            uri = "";
        }

        public SCSInfo(String uri) {
            this.uri = uri;
        }

        public SCSInfo(Properties props) {
            this(props.getProperty("scs.uri"));
        }

        public void save(Properties props) {
            props.setProperty("scs.uri", uri);
        }

        public String getURI() {
            return uri;
        }
    }

    public static class BuildInfo {

        private String buildFile;
        private String buildTarget;

        public BuildInfo() {
            buildFile = "";
            buildTarget = "";
        }

        public BuildInfo(String buildFile, String buildTarget) {
            this.buildFile = buildFile;
            this.buildTarget = buildTarget;
        }

        public BuildInfo(Properties props) {
            this(props.getProperty("build.build-file"),
                 props.getProperty("build.build-target"));
        }

        public void save(Properties props) {
            props.setProperty("build.build-file", buildFile);
            props.setProperty("build.build-target", buildTarget);
        }

        public String getBuildFile() {
            return buildFile;
        }

        public String getBuildTarget() {
            return buildTarget;
        }
    }

    public static class PackagerInfo {

        public enum PackagingType {

            ZIP, JAR
        }
        private String input;
        private PackagingType type;
        private String output;
        private String[] libraries;
        private String libsDirectory;

        public PackagerInfo() {
            input = "";
            type = PackagingType.ZIP;
            output = "";
            libraries = new String[0];
            libsDirectory = "/";
        }

        public PackagerInfo(String input, PackagingType type, String output,
                String[] libraries, String libsDirectory) {
            this.input = input;
            this.type = type;
            this.output = output;
            this.libraries = libraries;
            this.libsDirectory = libsDirectory;
        }

        public PackagerInfo(Properties props) {
            this(props.getProperty("packager.input"),
                 PackagingType.valueOf(props.getProperty("packager.type", "ZIP")),
                 props.getProperty("packager.output"),
                 props.getProperty("packager.libs").split(";"),
                 props.getProperty("packager.libs-dir", "/"));
        }

        public void save(Properties props) {
            props.setProperty("packager.input", input);
            props.setProperty("packager.type", type.toString());
            props.setProperty("packager.output", output);
            props.setProperty("packager.libs", librariesToString());
            props.setProperty("packager.libs-dir", libsDirectory);
        }

        private String librariesToString() {
            StringBuilder builder = new StringBuilder();
            for (String lib : libraries)
                builder.append(lib).append(';');

            return builder.toString();
        }

        public String getInput() {
            return input;
        }

        public String getOutput() {
            return output;
        }

        public String getOutput(String version) {
            return output.replace("%v", version);
        }

        public PackagingType getType() {
            return type;
        }

        public String[] getLibraries() {
            return libraries;
        }

        public String getLibsDirectory() {
            return libsDirectory;
        }
    }
}
