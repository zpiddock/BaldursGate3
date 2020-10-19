package uk.co.innoxium.baldursgate;

import uk.co.innoxium.candor.module.AbstractModInstaller;
import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.module.RunConfig;

import java.io.File;

public class BaldursGateModule extends AbstractModule {

    private File game;
    protected File gameHome;

    @Override
    public File getGame() {

        return game;
    }

    @Override
    public File getModsFolder() {

        return new File(gameHome, "data");
    }

    @Override
    public void setGame(File file) {

        this.game = file;
        // Game home is BaldursGate3, game is /bin/bg3.exe
        this.gameHome = file.getParentFile().getParentFile();
    }

    @Override
    public void setModsFolder(File file) {

    }

    @Override
    public String getModuleName() {

        return "bg3";
    }

    @Override
    public String getReadableGameName() {

        return "Baldur's gate 3";
    }

    @Override
    public AbstractModInstaller getModInstaller() {

        return new BaldursGateModInstaller(this);
    }

    @Override
    public boolean requiresModFolderSelection() {

        return false;
    }

    @Override
    public String[] acceptedExe() {

        return new String[] { "baldursgate3" };
    }

    @Override
    public String getModFileFilterList() {

        return "7z,zip,rar,pak";
    }

    @Override
    public RunConfig getDefaultRunConfig() {

        return ModuleSelector.GENERIC_MODULE.getDefaultRunConfig();
    }
}
