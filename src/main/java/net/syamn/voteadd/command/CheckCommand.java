/**
 * VoteAdd - Package: net.syamn.voteadd.command
 * Created: 2013/02/09 12:12:06
 */
package net.syamn.voteadd.command;

import net.syamn.utils.Util;
import net.syamn.utils.exception.CommandException;
import net.syamn.voteadd.CountData;
import net.syamn.voteadd.Perms;

/**
 * CheckCommand (CheckCommand.java)
 * @author syam(syamn)
 */
public class CheckCommand extends BaseCommand {
    public CheckCommand() {
        bePlayer = false;
        name = "check";
        argLength = 1;
        usage = "<name> <- check count";
    }

    @Override
    public void execute() throws CommandException {
        int count = CountData.getCount(args.get(0));
        Util.message(sender, "&6[VoteAdd] &aCount '" + args.get(0) + "'&f: &a" + count + "&7 / " + config.getCount());
    }

    @Override
    public boolean permission() {
        return Perms.CHECK.has(sender);
    }
}
