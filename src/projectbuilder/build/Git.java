/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.build;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import projectbuilder.queue.ProjectBuildException;

/**
 *
 * @author lachlan
 */
public class Git {

    private final File repoDirectory;

    public Git(File repoDirectory) {
        this.repoDirectory = repoDirectory;
    }

    public void clone(String remote) throws IOException {
        List<String> cmd = new ArrayList<>();
        cmd.add("git");
        cmd.add("clone");
        cmd.add(remote);
        cmd.add(repoDirectory.getAbsolutePath());

        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

        try {
            Process process = processBuilder.start();
            process.waitFor();

            if (process.exitValue() != 0)
                throw new IOException("git download exited with status of "
                        + process.exitValue());
        } catch (InterruptedException ie) {
            throw new IOException("Interrupted while waiting for git clone", ie);
        }
    }

    public void updateFromRemote() throws IOException {
        List<String> cmd = new ArrayList<>();
        cmd.add("git");
        cmd.add("pull");
        cmd.add("origin");
        cmd.add("master");

        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        processBuilder.directory(repoDirectory);

        try {
            Process process = processBuilder.start();
            process.waitFor();

            if (process.exitValue() != 0)
                throw new IOException("git download exited with status of "
                        + process.exitValue());
        } catch (InterruptedException ie) {
            throw new IOException("Interrupted while waiting for git clone", ie);
        }
    }

    public void switchToHead() throws IOException {
        switchCommit("HEAD");
    }

    public void switchToTag(String tag) throws IOException {
        switchCommit("tags/" + tag);
    }

    private void switchCommit(String commit) throws IOException {
        List<String> cmd = new ArrayList<>();
        cmd.add("git");
        cmd.add("checkout");
        cmd.add(commit);

        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        processBuilder.directory(repoDirectory);

        try {
            Process process = processBuilder.start();
            process.waitFor();

            if (process.exitValue() != 0)
                throw new IOException("git download exited with status of "
                        + process.exitValue());
        } catch (InterruptedException ie) {
            throw new IOException("Interrupted while waiting for git clone", ie);
        }
    }

    public List<String> listTags() throws IOException {
        List<String> cmd = new ArrayList<>();
        cmd.add("git");
        cmd.add("tag");
        cmd.add("-l");

        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        processBuilder.directory(repoDirectory);

        Process process = processBuilder.start();
        List<String> tags = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
            String tag;
            while ((tag = br.readLine()) != null) {
                tag = tag.trim();
                if (!tag.isEmpty())
                    tags.add(tag);
            }
        }

        return tags;
    }

    public File getRepoDirectory() {
        return repoDirectory;
    }
}
