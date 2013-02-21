/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.build;

import projectbuilder.queue.ProjectBuildException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tools.ant.BuildException;
import projectbuilder.project.Project;
import projectbuilder.project.ProjectConfig;
import projectbuilder.project.ProjectConfig.BuildInfo;
import projectbuilder.queue.BuildProcessor;
import projectbuilder.queue.BuildQueue;
import projectbuilder.queue.BuildRequest;
import projectbuilder.upload.Uploader;

/**
 *
 * @author lachlan
 */
public class ProjectBuilder implements BuildProcessor {

    private static final Logger LOG = Logger.getLogger(ProjectBuilder.class.
            getName());
    private final BuildQueue buildQueue;
    private final Uploader uploader;
    private final Packager packager;

    public ProjectBuilder(BuildQueue buildQueue, Uploader uploader) {
        this.buildQueue = buildQueue;
        this.uploader = uploader;
        this.packager = new Packager();
    }

    @Override
    public void process(BuildRequest request) throws ProjectBuildException {
        LOG.log(Level.INFO, "Building {0}", request);

        Project project = request.getProject();
        ProjectConfig.BuildInfo buildInfo = project.getConfig().getBuildInfo();

        File projectDir = new File("projects/" + project.getProjectDir());
        File buildDir = new File(projectDir, "build");
        File outputDir = new File(projectDir, "out");

        Git git = new Git(buildDir);

        if (!outputDir.exists())
            outputDir.mkdir();

        downloadProject(request, buildDir, git);
        buildOtherVersions(request, git, outputDir);

        if (project.getConfig().getBuildDev()) {
            compileProject(buildDir, buildInfo);
            packageAndUploadProject(request, buildDir, outputDir);
        }

        LOG.log(Level.INFO, "Build {0} successful", request);
    }

    private void downloadProject(BuildRequest request, File buildDir, Git git) throws
            ProjectBuildException {
        LOG.info("Downloading");

        Project project = request.getProject();
        ProjectConfig.SCSInfo scsInfo = project.getConfig().getSCSInfo();

        try {
            if (buildDir.exists()) {
                git.updateFromRemote();
            } else {
                git.clone(scsInfo.getURI());
            }

            if (request.isLatest()) {
                git.switchToHead();
            } else {
                git.switchToTag(request.getVersion());
            }
        } catch (IOException ioe) {
            throw new ProjectBuildException("Error downloading project", ioe);
        }
    }

    private void buildOtherVersions(BuildRequest request,
            Git git, File outputDir) throws ProjectBuildException {
        LOG.info("Looking for new versions");

        if (!request.isLatest() || buildQueue == null)
            return;

        Project project = request.getProject();

        List<String> tags;
        try {
            tags = git.listTags();
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Could not get tags", ex);
            return;
        }

        LOG.log(Level.FINE, "Found tags: {0}", tags);

        for (String tag : tags) {
            File tagBuild = new File(outputDir,
                                     project.getConfig().getPackagerInfo().
                    getOutput(tag));

            if (!tagBuild.exists()) {
                LOG.log(Level.INFO, "Pushing Job {0} {1}", new Object[]{project,
                            tag});
                buildQueue.pushJob(new BuildRequest(project, tag));
            }
        }
    }

    private void compileProject(File buildDir, BuildInfo buildInfo) throws
            ProjectBuildException {
        LOG.info("Compiling");

        try {
            AntRunner antRunner = new AntRunner();
            antRunner.run(
                    new File(buildDir, buildInfo.getBuildFile()),
                    buildInfo.getBuildTarget());
        } catch (IOException | BuildException ex) {
            throw new ProjectBuildException("Could not build project", ex);
        }
    }

    private void packageAndUploadProject(BuildRequest request, File buildDir,
            File outputDir) throws ProjectBuildException {
        LOG.info("Packaging...");

        File packaged;
        try {
            packaged = packager.pack(request, buildDir, outputDir);
        } catch (IOException ex) {
            throw new ProjectBuildException("Could not package project", ex);
        }

        LOG.info("...and Uploading");
        try {
            uploader.upload(request, packaged);
        } catch (IOException ex) {
            throw new ProjectBuildException("Could not upload project", ex);
        }
    }
}