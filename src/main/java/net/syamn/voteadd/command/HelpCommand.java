/**
 * Advertise - Package: syam.advertise.command Created: 2012/11/30 6:36:26
 */
package net.syamn.voteadd.command;

import net.syamn.utils.Util;

/**
 * HelpCommand (HelpCommand.java)
 * 
 * @author syam(syamn)
 */
public class HelpCommand extends BaseCommand {
    public HelpCommand() {
        bePlayer = false;
        name = "help";
        argLength = 0;
        usage = "<- show command help";
    }

    @Override
    public void execute() {
        Util.message(sender, "&c===================================");
        Util.message(sender, "&b" + plugin.getDescription().getName() + " Plugin version &3" + plugin.getDescription().getVersion());
        Util.message(sender, " &b<>&f = required, &b[]&f = optional");
        // 全コマンドをループで表示
        for (BaseCommand cmd : plugin.getCommands().toArray(new BaseCommand[0])) {
            cmd.sender = this.sender;
            if (cmd.permission()) {
                Util.message(sender, "&8-&7 /" + command + " &c" + cmd.name + " &7" + cmd.usage);
            }
        }
        Util.message(sender, "&c===================================");

        return;
    }

    @Override
    public boolean permission() {
        return true;
    }
}
