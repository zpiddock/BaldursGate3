package uk.co.innoxium.baldursgate;

import org.apache.commons.math3.exception.NullArgumentException;
import uk.co.innoxium.baldursgate.bg3m.installer.PAKInstaller;
import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.module.AbstractModInstaller;
import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.candor.util.Logger;
import uk.co.innoxium.candor.util.NativeDialogs;
import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.cybernize.archive.Archive;
import uk.co.innoxium.cybernize.archive.ArchiveBuilder;

import java.io.EOFException;
import java.io.File;
import javax.swing.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class BaldursGateModInstaller extends AbstractModInstaller {

    public BaldursGateModInstaller(AbstractModule module) {

        super(module);
    }

    @Override
    public boolean canInstall(Mod mod) {

        return true;
    }

    @Override
    public CompletableFuture<Boolean> install(Mod mod) {

        ModType modType = ModValidator.getTypeFromMod(mod);

        // Should only be null if there was an error reading the file.
        if(modType == null) {

            return CompletableFuture.failedFuture(new NullArgumentException());
        }

        switch(modType) {

            case PAK -> {

                return new PAKInstaller(module, ModType.PAK).installBG3M(mod);
            }
            case PAK_ONLY -> {

                return new PAKInstaller(module, ModType.PAK_ONLY).installBG3M(mod);
            }
            case DATA -> {

                NativeDialogs.showErrorMessage("Loose File Mods are not supported in this version of the BG3 Module\nCheck our nexus page for updates.");
                return CompletableFuture.completedFuture(false);
//                return new LooseInstaller(module).installLoose(mod);
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    @Override
    public boolean uninstall(Mod mod) {

        ModType modType = ModValidator.getTypeFromMod(mod);

        // Should be null if reading a file, OR if the mod is not valid currently.
        if(modType == null) {

            // Since we are uninstalling, return true and warn the user user some files may be left over.
            if(mod.getState() == Mod.State.ENABLED)
                JOptionPane.showMessageDialog(Resources.currentScene, String.format("Mod %s will be removed, however we have detected it MAY be an invalid format\nso some residual files may remain.", mod.getReadableName()));
            return true;
        }

        switch(modType) {

            case PAK -> {

                return new PAKInstaller(module, ModType.PAK).uninstallBG3M(mod);
            }
            case PAK_ONLY -> {

                return new PAKInstaller(module, ModType.PAK_ONLY).uninstallBG3M(mod);
            }
            case DATA -> {

                // TODO: Implement
                // Due to not being able to install a mod of this type, don't do anything here.
//                return new LooseInstaller(module).uninstallLoose(mod);
                return true;
            }
        }

        return false;
    }

    private static class ModValidator {

        public static ModType getTypeFromMod(Mod mod) {

            try {

                if(isZip(mod.getFile())) {

                    ZipFile zip = new ZipFile(mod.getFile());
                    ZipEntry info = zip.getEntry("info.json");
                    if (info != null) {

                        return ModType.PAK; // Mod contains both info.json and any amount of .pak files
                    } else {

                        if (zip.stream().anyMatch(ze -> ze.getName().contains(".pak"))) {

                            return ModType.PAK_ONLY; // Mod only contains a .pak
                        }
                        return ModType.DATA; // Mod does not contain a .pak or info.json
                    }
                } else {

                    Archive modArchive = new ArchiveBuilder(mod.getFile()).type(ArchiveBuilder.ArchiveType.SEVEN_ZIP).build();
                    if(modArchive.getAllArchiveItems().stream().anyMatch(item -> item.getFilePath().contains("info.json"))) {

                        return ModType.PAK;
                    } else if(modArchive.getAllArchiveItems().stream().anyMatch(item -> item.getFilePath().contains(".pak"))) {

                        return ModType.PAK_ONLY;
                    }
                    return ModType.DATA;
                }
            } catch (IOException e) {

                if(e instanceof EOFException) {

                    Logger.info("Could not read header on mod file, most likely corrupt.");
                } else {
                    e.printStackTrace();
                    NativeDialogs.showErrorMessage(String.format("The mod %s is not valid or not currently supported.", mod.getReadableName()));
                }
                return null;
            }
        }
    }

    public static boolean isZip(File file) {

        int fileSignature = 0;
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {

            fileSignature = raf.readInt();
        } catch(IOException e) {

           e.printStackTrace();
           return false;
        }
        return fileSignature == 0x504B0304 || fileSignature == 0x504B0506 || fileSignature == 0x504B0708;
    }

    public enum ModType {
        PAK, // PAK has changes to modsettings.lsx
        DATA, // Data extracts to the Data folder - for loose file mods
        PAK_ONLY // PAK_Only does not make any changes to modsettings.lsx
    }
}
