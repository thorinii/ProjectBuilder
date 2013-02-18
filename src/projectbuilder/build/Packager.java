/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import projectbuilder.project.Project;
import projectbuilder.project.ProjectConfig;

/**
 *
 * @author lachlan
 */
public class Packager {

    private static final Logger LOG = Logger.getLogger(Packager.class.getName());

    public File pack(Project project, File buildDir, File outputDir,
            String version) throws IOException {
        ProjectConfig.PackagerInfo packagerInfo = project.getConfig().
                getPackagerInfo();

        File buildOutputJar = new File(buildDir, packagerInfo.getInput());
        Set<String> libraries = new HashSet<>();

        // Collect libraries
        libraries.addAll(Arrays.asList(packagerInfo.getLibraries()));
        libraries.addAll(extractLibrariesFromJar(buildOutputJar));

        // Package
        switch (packagerInfo.getType()) {
            case ZIP:
                return zipProject(packagerInfo, buildDir, buildOutputJar,
                                  libraries, version);
            case JAR:
                return jarProject(packagerInfo, buildDir, buildOutputJar,
                                  libraries, version);
            default:
                LOG.warning("Not sure how to package project");
                return null;
        }
    }

    private File zipProject(ProjectConfig.PackagerInfo packagerInfo,
            File buildDir, File buildOutputJar, Set<String> libraries,
            String version) throws IOException {

        String outputName = packagerInfo.getOutput();
        outputName = outputName.replace("%v", version);

        File output = new File(buildDir, "../out/" + outputName);

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
                        output))) {
            zos.setLevel(9);

            writeZIPEntry(buildOutputJar, "/", zos);

            for (String lib : libraries) {
                writeZIPEntry(new File(buildDir, lib), packagerInfo.
                        getLibsDirectory(), zos);
            }
        }

        return output;
    }

    private void writeZIPEntry(File in, String dir, ZipOutputStream out) throws
            IOException {
        ZipEntry entry = new ZipEntry(dir + in.getName());

        out.putNextEntry(entry);

        byte[] buf = new byte[1024];
        int read;

        try (InputStream is = new FileInputStream(in)) {
            while ((read = is.read(buf)) != -1) {
                out.write(buf, 0, read);
            }
        }

        out.closeEntry();
    }

    private File jarProject(ProjectConfig.PackagerInfo packagerInfo,
            File buildDir, File buildOutputJar, Set<String> libraries,
            String version) throws IOException {

        String outputName = packagerInfo.getOutput();
        outputName = outputName.replace("%v", version);

        File output = new File(buildDir, "../out/" + outputName);

        Manifest manifest = new Manifest(getManifest(buildOutputJar));
        manifest.getMainAttributes().remove("Class-Path");

        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(
                        output), manifest)) {
            jos.setLevel(9);

            writeJARFile(buildOutputJar, jos);

            for (String lib : libraries) {
                writeJARFile(new File(buildDir, lib), jos);
            }
        }

        return output;
    }

    private void writeJARFile(File in, JarOutputStream out) throws IOException {
        JarInputStream jis = new JarInputStream(new FileInputStream(in));

        JarEntry readEntry;
        byte[] buf = new byte[4096];
        int read;

        while ((readEntry = jis.getNextJarEntry()) != null) {
            if (readEntry.isDirectory())
                continue;

            JarEntry newEntry = new JarEntry(readEntry.getName());

            out.putNextEntry(newEntry);

            long leftToRead = readEntry.getSize();

            while (leftToRead > 0) {
                read = jis.read(buf, 0,
                                (leftToRead > buf.length)
                                ? buf.length
                                : (int) leftToRead);

                out.write(buf, 0, read);
                leftToRead -= read;
            }

            out.closeEntry();
        }
    }

    private List<String> extractLibrariesFromJar(File buildOutputJar)
            throws IOException {
        List<String> libraries = new ArrayList<>();
        Manifest manifest = getManifest(buildOutputJar);

        String classpath = manifest.getMainAttributes().getValue("Class-Path");
        if (classpath != null) {
            String[] libs = classpath.split(" ");

            for (String lib : libs) {
                lib = lib.trim();
                if (lib.isEmpty())
                    continue;

                if (lib.endsWith(".jar"))
                    libraries.add(lib);
            }
        }

        return libraries;
    }

    private Manifest getManifest(File jar) throws IOException {
        JarInputStream jis = new JarInputStream(new FileInputStream(jar));
        return jis.getManifest();
    }
}
