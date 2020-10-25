package uk.co.innoxium.baldursgate.bg3m.installer;

import org.apache.commons.io.FileUtils;
import uk.co.innoxium.baldursgate.BaldursGateModule;
import uk.co.innoxium.baldursgate.file.LooseFileVisitor;
import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.cybernize.archive.Archive;
import uk.co.innoxium.cybernize.archive.ArchiveBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicReference;

public class LooseInstaller {

    private final BaldursGateModule module;

    public LooseInstaller(AbstractModule module) {

        this.module = (BaldursGateModule)module;
    }

    public boolean installLoose(Mod mod) {

        // Copy any loose files already found to temp directory
        File bg3LooseTemp = null;
        File modLooseTemp = null;
        try {

            bg3LooseTemp = Files.createTempDirectory("bg3Loose").toFile();
            modLooseTemp = Files.createTempDirectory("modLoose").toFile();

            System.out.println(bg3LooseTemp);
            System.out.println(modLooseTemp);

            File dataDirectory = new File(module.gameHome, "data");
            // We dont want to copy all pak files, due to this being like ~30gb, only the files that could be affected.
            File[] files = dataDirectory.listFiles((fileName) -> {

                if(fileName.isDirectory() && !fileName.getName().equalsIgnoreCase("Localization")) {

                    return true;
                }
                return !fileName.getName().endsWith(".pak") && !fileName.getName().equalsIgnoreCase("Localization");
            });
            for(File file : files) {

                System.out.println(file.getName());
                if(file.isDirectory()) {

                    FileUtils.copyDirectory(file, new File(bg3LooseTemp, file.getName()));
                } else {
                    FileUtils.copyFileToDirectory(file, bg3LooseTemp);
                }
            }
//            FileUtils.copyDirectory(dataDirectory, bg3LooseTemp);

            // Extract archive to separate temp directory
            Archive archive = new ArchiveBuilder(mod.getFile()).outputDirectory(modLooseTemp).type(ArchiveBuilder.ArchiveType.SEVEN_ZIP).build();
            archive.extract();

            // For each file in the in hierarchy, copy it, candor will ask about any files that need overwriting or merging
            // If they are to be merged, handle that in the appropriate file merger.

            LooseFileVisitor visitor = new LooseFileVisitor(this, bg3LooseTemp.toPath());
            Files.walkFileTree(modLooseTemp.toPath(), visitor);



            // Once all files have been handled correctly, copy to DATA folder
            // If any steps above failed, it will not overwrite any files!

            // Clean up temp directories
        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("Something happen?");
        }

        return false;
    }

    public boolean uninstallLoose(Mod mod) {

        // Copy all loose files to temp directory

        // extract mod files to separate temp directory

        // For each file, if it exists, get file merger and attempt to remove any entries

        // Once finished, copy to DATA folder,
        // If any steps fail, nothing will be affected.

        // Clean up temp directories

        return false;
    }

    private String determineFirstFolder(Archive archive) throws IOException {

        AtomicReference<String> ret = new AtomicReference<>();

        archive.getAllArchiveItems().forEach(item -> {

            if(item.isDirectory() && !(item.getFilePath().contains("\\") || item.getFilePath().contains("/"))) {

                ret.set(item.getFilePath());
            }
        });

        return ret.get() != null ? ret.get() : "";
    }
}
