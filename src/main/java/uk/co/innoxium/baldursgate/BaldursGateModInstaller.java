package uk.co.innoxium.baldursgate;

import org.apache.commons.io.FileUtils;
import uk.co.innoxium.baldursgate.bg3m.installer.LooseInstaller;
import uk.co.innoxium.baldursgate.bg3m.installer.PAKInstaller;
import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.module.AbstractModInstaller;
import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.candor.util.NativeDialogs;
import uk.co.innoxium.cybernize.archive.Archive;
import uk.co.innoxium.cybernize.archive.ArchiveBuilder;

import java.io.IOException;
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
    public boolean install(Mod mod) {

        ModType modType = ModValidator.getTypeFromMod(mod);

        // Should only be null if there was an error reading the file.
        if(modType == null) {

            return false;
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
                return false;
//                return new LooseInstaller(module).installLoose(mod);
            }
        }
        return false;

//        PAKInstaller installer = new PAKInstaller(module);
//        if(installer.verifyMod(mod)) {
//
//            return installer.installBG3M(mod);
//        } else {
//
//            // Currently we throw an error, we should treat it as a data mod?
//
//            NativeDialogs.showInfoDialog("Baldur's Gate 3 Module",
//                    String.format("The mod %s is not a valid bg3 mod, and cannot be installed.\nView the authors instructions", mod.getReadableName()),
//                    "ok",
//                    "error",
//                    true);
//            return false;
//        }
//        if(Utils.getExtension(mod.getFile()).equalsIgnoreCase("pak")) {
//
//            return installPak(mod);
//        }
//
//        try {
//
//            return installArchive(mod);
//        } catch (IOException e) {
//
//            e.printStackTrace();
//            return false;
//        }
    }

    private boolean installPak(Mod mod) {

        try {

            FileUtils.copyFileToDirectory(mod.getFile(), module.getModsFolder());
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean installArchive(Mod mod) throws IOException {

        Archive archive = new ArchiveBuilder(mod.getFile()).outputDirectory(module.getModsFolder()).type(ArchiveBuilder.ArchiveType.SEVEN_ZIP).build();
        return archive.extract();
    }

    @Override
    public boolean uninstall(Mod mod) {

        ModType modType = ModValidator.getTypeFromMod(mod);

        // Should only be null if there was an error reading the file.
        if(modType == null) {

            return false;
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

                ZipFile zip = new ZipFile(mod.getFile());
                ZipEntry info = zip.getEntry("info.json");
                if(info != null) {

                    return ModType.PAK; // Mod contains both info.json and any amount of .pak files
                } else {

                    if(zip.stream().anyMatch(ze -> ze.getName().contains(".pak"))) {

                        return ModType.PAK_ONLY; // Mod only contains a .pak
                    }
                    return ModType.DATA; // Mod does not contain a .pak or info.json
                }
            } catch (IOException e) {

                e.printStackTrace();
                NativeDialogs.showErrorMessage(String.format("The mod %s is not valid or not currently supported.", mod.getReadableName()));
                return null;
            }
        }
    }

    public enum ModType {
        PAK, // PAK has changes to modsettings.lsx
        DATA, // Data extracts to the Data folder - for loose file mods
        PAK_ONLY // PAK_Only does not make any changes to modsettings.lsx
    }
}
