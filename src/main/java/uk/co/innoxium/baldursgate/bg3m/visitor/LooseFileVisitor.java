package uk.co.innoxium.baldursgate.bg3m.visitor;

import com.google.common.io.Files;
import uk.co.innoxium.baldursgate.bg3m.installer.LooseInstaller;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

public class LooseFileVisitor extends SimpleFileVisitor<Path> {

    private final LooseInstaller installer;
    public Path source;

    public ArrayList<String> modFiles = new ArrayList<>();

    public LooseFileVisitor(LooseInstaller installer, Path source) {

        this.installer = installer;
        this.source = source;
    }

    // Print information about
    // each type of file.
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {

        if(source == null) {


        } else {

            String fileName = file.toFile().getAbsolutePath();
            String toReplace = source.toFile().getAbsolutePath();
            String newFileName = fileName.substring(toReplace.length() + 1);
            modFiles.add(newFileName);
        }

        return CONTINUE;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {

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
