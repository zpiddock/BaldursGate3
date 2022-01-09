package uk.co.innoxium.baldursgate;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.cybernize.setting.Config;
import uk.co.innoxium.cybernize.setting.Setting;
import uk.co.innoxium.cybernize.setting.SettingsHandler;
import uk.co.innoxium.cybernize.setting.SettingsHolder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@SettingsHolder(id = "bg3", ext ="toml")
public class BG3Settings {

    @Setting(category = "profile")
    @Setting.Comment("The path to player profile to use for installing mods to")
    // This should not be relative, and should be absolute
    public static String playerProfile = "";

    public static void init() {

        SettingsHandler.addHolder(BG3Settings.class);

        try {

            SettingsHandler.load();
        } catch (IllegalAccessException e) {

            e.printStackTrace();
        }
    }

    @Config
    public static CommentedFileConfig getConfig() {

        return CommentedFileConfig
                .builder(new File(Resources.CONFIG_PATH, "bg3/bg3Settings.toml"))
                .autosave()
                .autoreload()
                .charset(StandardCharsets.UTF_8)
                .onFileNotFound((file, cfgFormat) -> {

                    Files.createDirectories(file.getParent());
                    Files.createFile(file);
                    cfgFormat.initEmptyFile(file);
                    return false;
                })
                .build();
    }
}
