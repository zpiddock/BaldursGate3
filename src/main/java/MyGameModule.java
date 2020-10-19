import uk.co.innoxium.candor.module.AbstractModInstaller;
import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.candor.module.RunConfig;

import java.io.File;

public class MyGameModule extends AbstractModule {

    @Override
    public File getGame() {
        return null;
    }

    @Override
    public File getModsFolder() {
        return null;
    }

    @Override
    public void setGame(File file) {

    }

    @Override
    public void setModsFolder(File file) {

    }

    @Override
    public String getModuleName() {
        return null;
    }

    @Override
    public String getReadableGameName() {
        return null;
    }

    @Override
    public AbstractModInstaller getModInstaller() {
        return new MyGameModInstaller(this);
    }

    @Override
    public boolean requiresModFolderSelection() {
        return false;
    }

    @Override
    public String[] acceptedExe() {
        return new String[0];
    }

    @Override
    public String getModFileFilterList() {
        return null;
    }

    @Override
    public RunConfig getDefaultRunConfig() {
        return null;
    }
}
