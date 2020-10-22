package uk.co.innoxium.baldursgate.bg3m.installer;

import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.module.AbstractModule;

public class LooseInstaller {

    private final AbstractModule module;

    public LooseInstaller(AbstractModule module) {

        this.module = module;
    }

    public boolean installLoose(Mod mod) {

        return false;
    }

    public boolean uninstallLoose(Mod mod) {

        return false;
    }
}
