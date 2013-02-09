/**
 * VoteAdd - Package: net.syamn.voteadd Created: 2012/11/30 6:25:12
 */
package net.syamn.voteadd;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import net.syamn.utils.LogUtil;
import net.syamn.utils.file.FileStructure;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * ConfigurationManager (ConfigurationManager.java)
 * 
 * @author syam(syamn)
 */
public class ConfigurationManager {
    /* Current config.yml File Version! */
    private final int latestVersion = 1;

    private final VoteAdd plugin;

    // private YamlConfiguration conf;
    private FileConfiguration conf;
    private File pluginDir;

    /**
     * Constructor
     */
    public ConfigurationManager(final VoteAdd plugin) {
        this.plugin = plugin;
        this.pluginDir = this.plugin.getDataFolder();
    }

    /**
     * Load config.yml
     */
    public void loadConfig(final boolean initialLoad) throws Exception {
        // create directories
        FileStructure.createDir(pluginDir);

        // get config.yml path
        File file = new File(pluginDir, "config.yml");
        if (!file.exists()) {
            FileStructure.extractResource("/config.yml", pluginDir, false, false, plugin);
            LogUtil.info("config.yml is not found! Created default config.yml!");
        }

        plugin.reloadConfig();
        conf = plugin.getConfig();

        checkver(conf.getInt("ConfigVersion", 1));
    }

    /**
     * Check configuration file version
     */
    private void checkver(final int ver) {
        // compare configuration file version
        if (ver < latestVersion) {
            // first, rename old configuration
            final String destName = "oldconfig-v" + ver + ".yml";
            String srcPath = new File(pluginDir, "config.yml").getPath();
            String destPath = new File(pluginDir, destName).getPath();
            try {
                FileStructure.copyTransfer(srcPath, destPath);
                LogUtil.info("Copied old config.yml to " + destName + "!");
            } catch (Exception ex) {
                LogUtil.warning("Failed to copy old config.yml!");
            }

            // force copy config.yml and languages
            FileStructure.extractResource("/config.yml", pluginDir, true, false, plugin);
            // Language.extractLanguageFile(true);

            plugin.reloadConfig();
            conf = plugin.getConfig();

            LogUtil.info("Deleted existing configuration file and generate a new one!");
        }
    }

    /* ***** Begin Configuration Getters *********************** */
    // General
    public int getCount() {
        return conf.getInt("TriggerCount", 5);
    }
    
    public String getMessage(){
        return conf.getString("VotedMsg", "Thanks for your vote!");
    }

    public List<String> getCommands() {
        return conf.getStringList("RunCommands");
    }

    // Debug
    public boolean isDebug() {
        return conf.getBoolean("Debug", false);
    }
}