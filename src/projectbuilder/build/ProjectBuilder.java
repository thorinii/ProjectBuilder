/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.build;

import projectbuilder.queue.ProjectBuildException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projectbuilder.project.Project;
import projectbuilder.project.ProjectConfig;
import projectbuilder.project.ProjectConfig.BuildInfo;
import projectbuilder.queue.BuildProcessor;
import projectbuilder.queue.BuildQueue;
import projectbuilder.upload.Uploader;

/**
 *
 * @author lachlan
 */
public class ProjectBuilder implements BuildProcessor {

    private static final Logger LOG = Logger.getLogger(ProjectBuilder.class.
            getName());
    private final Uploader uploader;
    private final Packager packager;

    public ProjectBuilder(Uploader uploader) {
        this.uploader = uploader;
        this.packager = new Packager();
    }

    @Override
    public void process(Project project) throws ProjectBuildException {
        LOG.log(Level.INFO, "Building {0}", project);

        ProjectConfig.BuildInfo buildInfo = project.getConfig().getBuildInfo();

        File projectDir = new File("projects/" + project.getProjectDir());
        File buildDir = new File(projectDir, "build");
        File outputDir = new File(projectDir, "out");

        if (!outputDir.exists())
            outputDir.mkdir();

        LOG.log(Level.INFO, "Downloading {0}", project);
        // TODO: downloadProject(project, buildDir);

        LOG.log(Level.INFO, "Compiling {0}", project);
        compileProject(buildDir, buildInfo);

        LOG.log(Level.INFO, "Packaging and Uploading {0}", project);
        packageAndUploadProject(project, buildDir, outputDir);

        LOG.log(Level.INFO, "Build {0} successful", project);
    }

    private void downloadProject(Project project, File buildDir) throws
            ProjectBuildException {
        if (buildDir.exists())
            deleteFile(buildDir);

        List<String> cmd = new ArrayList<>();
        cmd.add("git");
        cmd.add("clone");
        cmd.add(project.getConfig().getSCSInfo().getURI());
        cmd.add(buildDir.getAbsolutePath());

        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        try {
            Process process = processBuilder.start();
            process.waitFor();

            if (process.exitValue() != 0)
                throw new ProjectBuildException("git clone exited with status of "
                        + process.exitValue());
        } catch (IOException ioe) {
            throw new ProjectBuildException("Error running git clone", ioe);
        } catch (InterruptedException ie) {
            throw new ProjectBuildException(
                    "Interrupted while waiting for git clone", ie);
        }
    }

    private void compileProject(File buildDir, BuildInfo buildInfo) throws
            ProjectBuildException {
        try {
            AntRunner antRunner = new AntRunner();
            antRunner.run(
                    new File(buildDir, buildInfo.getBuildFile()),
                    buildInfo.getBuildTarget());
        } catch (IOException ex) {
            throw new ProjectBuildException("Could not build project", ex);
        }
    }

    private void packageAndUploadProject(Project project, File buildDir,
            File outputDir) throws ProjectBuildException {
        File packaged;
        try {
            packaged = packager.pack(project, buildDir, outputDir, "dev");
        } catch (IOException ex) {
            throw new ProjectBuildException("Could not package project", ex);
        }
    }

    private List<String> listTags(File devDir) throws ProjectBuildException {
        List<String> cmd = new ArrayList<>();
        cmd.add("git");
        cmd.add("clone");
        cmd.add("-l");

        ProcessBuilder processBuilder = new ProcessBuilder(cmd);

        try {
            Process process = processBuilder.start();
            List<String> tags = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(
                            process.getInputStream()))) {
                String tag;
                while ((tag = br.readLine()) != null) {
                    tags.add(tag);
                }
            }

            return tags;
        } catch (IOException ioe) {
            LOG.log(Level.WARNING, "Could not list tags", ioe);
            return Collections.emptyList();
        }
    }

    private void deleteFile(File file) {
        if (file.isDirectory()) {
            for (File sub : file.listFiles()) {
                deleteFile(sub);
            }

            file.delete();
        } else {
            file.delete();
        }
    }
}
