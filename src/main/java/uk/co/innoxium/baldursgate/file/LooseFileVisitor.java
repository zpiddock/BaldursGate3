package uk.co.innoxium.baldursgate.file;

import uk.co.innoxium.baldursgate.bg3m.installer.LooseInstaller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

public class LooseFileVisitor extends SimpleFileVisitor<Path> {

    private final LooseInstaller installer;
    private final Path dataFiles;

    public Path folderToCopy = null;

    public LooseFileVisitor(LooseInstaller installer, Path dataFiles) {

        this.installer = installer;
        this.dataFiles = dataFiles;
    }

    // Print information about
    // each type of file.
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {

        return CONTINUE;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {

        System.out.format("Directory: %s%n", dir);

        if(dir.toFile().isDirectory()) {

            String fileName = dir.getFileName().toString();
            if(fileName.equalsIgnoreCase("Data") || fileName.equalsIgnoreCase("Generated")) {

                System.out.println(fileName);
                folderToCopy = new File(dir.toFile().getParentFile(), fileName).toPath();
                return TERMINATE;
            }
        }
        return CONTINUE;
    }

    // If there is some error accessing
    // the file, let the user know.
    // If you don't override this method
    // and an error occurs, an IOException
    // is thrown.
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {

        System.err.println(exc);
        return CONTINUE;
    }
}
