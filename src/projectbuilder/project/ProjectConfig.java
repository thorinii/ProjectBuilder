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
    private boolean buildDev;
    private SCSInfo scsInfo;
    private BuildInfo buildInfo;
    private PackagerInfo packagerInfo;
    private UploaderInfo uploaderInfo;

    public ProjectConfig() {
        enabled = false;
        buildDev = true;

        scsInfo = new SCSInfo();
        buildInfo = new BuildInfo();
        packagerInfo = new PackagerInfo();
        uploaderInfo = new UploaderInfo();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getBuildDev() {
        return buildDev;
    }

    public void load(Properties props) {
        enabled = Boolean.parseBoolean(props.getProperty("enabled"));
        buildDev = Boolean.parseBoolean(props.getProperty("build-dev"));

        scsInfo = new SCSInfo(props);
        buildInfo = new BuildInfo(props);
        packagerInfo = new PackagerInfo(props);
        uploaderInfo = new UploaderInfo(props);
    }

    public void save(Properties props) {
        props.setProperty("enabled", Boolean.toString(enabled));
        props.setProperty("build-dev", Boolean.toString(buildDev));

        scsInfo.save(props);
        buildInfo.save(props);
        packagerInfo.save(props);
        uploaderInfo.save(props);
    }

    public SCSInfo getSCSInfo() {
        return scsInfo;
    }

    public BuildInfo getBuildInfo() {
        return buildInfo;
    }

    public PackagerInfo getPackagerInfo() {
        return packagerInfo;
    }

    public UploaderInfo getUploaderInfo() {
        return uploaderInfo;
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

    public static class UploaderInfo {

        private String rpcURL;
        private String rpcUsername;
        private String rpcPassword;
        private String postTitle;
        private String postContent;
        private String[] postTags;

        public UploaderInfo() {
            rpcURL = "";
            rpcUsername = "";
            rpcPassword = "";
            postTitle = "";
            postContent = "";
            postTags = new String[]{};
        }

        public UploaderInfo(String rpcURL, String rpcUsername,
                String rpcPassword, String postTitle, String postContent,
                String[] postTags) {
            this.rpcURL = rpcURL;
            this.rpcUsername = rpcUsername;
            this.rpcPassword = rpcPassword;
            this.postTitle = postTitle;
            this.postContent = postContent;
            this.postTags = postTags;
        }

        public UploaderInfo(Properties props) {
            this(props.getProperty("uploader.rpc-url"),
                 props.getProperty("uploader.rpc-username"),
                 props.getProperty("uploader.rpc-password"),
                 props.getProperty("uploader.post-title"),
                 props.getProperty("uploader.post-content"),
                 props.getProperty("uploader.post-tags").split(";"));
        }

        public void save(Properties props) {
            props.setProperty("uploader.rpc-url", rpcURL);
            props.setProperty("uploader.rpc-username", rpcUsername);
            props.setProperty("uploader.rpc-password", rpcPassword);
            props.setProperty("uploader.post-title", postTitle);
            props.setProperty("uploader.post-content", postContent);
            props.setProperty("uploader.post-tags", concatTags(postTags));
        }

        private String concatTags(String[] postTags) {
            StringBuilder builder = new StringBuilder();

            int i = 0;
            for (String tag : postTags) {
                if (i != 0)
                    builder.append(";");

                builder.append(tag);
                i++;
            }

            return builder.toString();
        }

        public String getRpcURL() {
            return rpcURL;
        }

        public String getRpcUsername() {
            return rpcUsername;
        }

        public String getRpcPassword() {
            return rpcPassword;
        }

        public String getPostTitle() {
            return postTitle;
        }

        public String getPostTitle(String version) {
            return postTitle.replace("%v", version);
        }

        public String getPostContent() {
            return postContent;
        }

        public String getPostContent(String url, String version) {
            return postContent.replace("%u", url).replace("%v", version);
        }

        public String[] getPostTags() {
            return postTags;
        }
    }
}
