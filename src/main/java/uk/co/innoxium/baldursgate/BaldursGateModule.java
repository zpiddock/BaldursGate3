package uk.co.innoxium.baldursgate;

import uk.co.innoxium.candor.module.AbstractModInstaller;
import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.candor.module.RunConfig;
import uk.co.innoxium.candor.util.WindowUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class BaldursGateModule extends AbstractModule {

    public static boolean loaded = false;

    public BaldursGateModule() {

    }

    private File game;
    public File gameHome;
    public static final File bg3Docs =
            new File(FileSystemView.getFileSystemView().getDefaultDirectory().getAbsoluteFile(),
                    "Larian Studios/Baldur's Gate 3");
    public static File playerProfiles = new File(bg3Docs, "PlayerProfiles");

    @Override
    public File getGame() {

        return game;
    }

    @Override
    public File getModsFolder() {

        return new File(bg3Docs, "Mods");
    }

    @Override
    public void setGame(File file) {

        BG3Settings.init();

        this.game = file;
        // Game home is BaldursGate3, game is /bin/bg3.exe
        this.gameHome = file.getParentFile().getParentFile();
        if(!loaded && BG3Settings.playerProfile.isEmpty()) {

            PlayerProfileSelector profileSelector = new PlayerProfileSelector(playerProfiles);
            profileSelector.setVisible(true);
            WindowUtils.mainFrame.setFocusable(false);

//            NativeDialogs.showInfoDialog("Baldur's Gate 3 Module",
//                    "Please select the player profile to use\nClick ok to continue.",
//                    "ok",
//                    "info",
//                    true);
//            File playerProfile = NativeDialogs.openPickFolder(playerProfiles);

//                BG3Settings.playerProfile = playerProfile.getAbsolutePath();
            loaded = true;
        }
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

        return "Baldur's Gate 3";
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

        return new String[] { "bg3", "bg3_dx11" };
    }

    @Override
    public String getModFileFilterList() {

        return "zip,rar";
    }

    @Override
    public RunConfig getDefaultRunConfig() {

        return new RunConfig("Game") {

            @Override
            public String getStartCommand() {

                return getGame().getAbsolutePath();
            }

            @Override
            public String getProgramArgs() {

                return "";
            }

            @Override
            public String getWorkingDir() {

                return new File(getGame().getParentFile().getParentFile(), "Launcher").getAbsolutePath();
            }
        };
    }
}
