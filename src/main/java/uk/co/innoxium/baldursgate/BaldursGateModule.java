package uk.co.innoxium.baldursgate;

import uk.co.innoxium.candor.game.Game;
import uk.co.innoxium.candor.module.AbstractModInstaller;
import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.candor.module.RunConfig;
import uk.co.innoxium.candor.util.Logger;
import uk.co.innoxium.candor.util.WindowUtils;
import uk.co.innoxium.candor.window.dialog.SwingDialogs;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;


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

            // SUPER HACK TO ADD SWITCH BEFORE CANDOR VERSION 0.3.0
            // TODO: Switch to pattern matching instanceof in candor 0.3.0
//            if(Resources.currentScene instanceof ModScene) {
//
//                ModScene scene = (ModScene) Resources.currentScene;
//
//                try {
//
//                    Field fileMenu = scene.getClass().getField("fileMenu");
//                    fileMenu.setAccessible(true);
//                    JMenu _fileMenu = (JMenu) fileMenu.get(new JMenu());
//
//                    JMenuItem switchProfile = new JMenuItem("Switch Player Profile");
//                    switchProfile.addActionListener(e -> {
//
//                        PlayerProfileSelector newProfileSelector = new PlayerProfileSelector(playerProfiles);
//                        newProfileSelector.setVisible(true);
//                        WindowUtils.mainFrame.setFocusable(false);
//                    });
//
//                    _fileMenu.add(switchProfile);
//                } catch(NoSuchFieldException | IllegalAccessException e) {
//
//                    e.printStackTrace();
//                }
//            }

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
    public String getReadableGameName(Game game) {

        return getReadableGameName();
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

    @Override
    public ArrayList<JMenuItem> getMenuItems() {

        ArrayList<JMenuItem> list = new ArrayList<>();
        list.add(new InstallModFixer());
        list.add(new LSLib());

        return list;
    }

    public static class InstallModFixer extends JMenuItem {

        public InstallModFixer() {

            this.setText("Install Mod Fixer");
            this.addActionListener(e -> {

                if(SwingDialogs.showConfirmDialog("BG3 Module", "Are you sure you wish to install the bg3 mod fixer?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                    Logger.info("Attempting to download and install mod fixer");
                    // in the future we may use the Nexus API to find the latest version
                }
            });
        }
    }

    public static class LSLib extends JMenuItem {

        public LSLib() {

            this.setText("Install/Update LSLib by Norbyte");
            this.setToolTipText("LSLib super-powers the Baldur's Gate 3 Module");
            this.addActionListener(e -> {

                // Steps
                // Show confirm dialog
                // Check if already installed
                //      if yes - check if update
                //              if yes - download update
                //              if no - inform user LSLib is up to date
                //      if no - download latest version
                // Extract downloaded archive to <candor-path>\tools\bg3\lslib
            });
        }
    }
}
