/**
 * Advertise - Package: syam.advertise.command Created: 2012/11/30 6:37:42
 */
package net.syamn.voteadd.command;

import net.syamn.utils.LogUtil;
import net.syamn.utils.Util;
import net.syamn.voteadd.CountData;
import net.syamn.voteadd.Perms;

/**
 * ReloadCommand (ReloadCommand.java)
 * 
 * @author syam(syamn)
 */
public class ReloadCommand extends BaseCommand {
    public ReloadCommand() {
        bePlayer = false;
        name = "reload";
        argLength = 0;
        usage = "<- reload config.yml";
    }

    @Override
    public void execute() {
        try {
            plugin.getConfigs().loadConfig(false);
            CountData.saveData();
            CountData.reloadData();
        } catch (Exception ex) {
            LogUtil.warning("an error occured while trying to load the config file.");
            ex.printStackTrace();
            return;
        }
        Util.message(sender, "&aConfiguration reloaded!");
    }

    @Override
    public boolean permission() {
        return Perms.RELOAD.has(sender);
    }
}