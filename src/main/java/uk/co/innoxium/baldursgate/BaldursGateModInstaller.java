package uk.co.innoxium.baldursgate;

import org.apache.commons.io.FileUtils;
import uk.co.innoxium.baldursgate.bg3m.BG3MInstaller;
import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.module.AbstractModInstaller;
import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.util.NativeDialogs;
import uk.co.innoxium.candor.util.Utils;
import uk.co.innoxium.cybernize.archive.Archive;
import uk.co.innoxium.cybernize.archive.ArchiveBuilder;

import java.io.IOException;

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

        BG3MInstaller installer = new BG3MInstaller(module);
        if(installer.verifyMod(mod)) {

            return installer.installBG3M(mod);
        } else {

            NativeDialogs.showInfoDialog("Baldur's Gate 3 Module",
                    String.format("The mod %s is not a valid bg3 mod, and cannot be installed.\nView the authors instructions", mod.getReadableName()),
                    "ok",
                    "error",
                    true);
            return false;
        }
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

        BG3MInstaller installer = new BG3MInstaller(module);
        return installer.uninstallBG3M(mod);
    }
}
