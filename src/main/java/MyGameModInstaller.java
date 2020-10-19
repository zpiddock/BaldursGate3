import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.module.AbstractModInstaller;
import uk.co.innoxium.candor.module.AbstractModule;

public class MyGameModInstaller extends AbstractModInstaller {

    public MyGameModInstaller(AbstractModule module) {
        super(module);
    }

    @Override
    public boolean canInstall(Mod mod) {
        return false;
    }

    @Override
    public boolean install(Mod mod) {
        return false;
    }

    @Override
    public boolean uninstall(Mod mod) {
        return false;
    }
}
