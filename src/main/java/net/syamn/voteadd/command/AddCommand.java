/**
 * Advertise - Package: syam.advertise.command Created: 2012/11/30 11:11:14
 */
package net.syamn.voteadd.command;

import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.syamn.utils.StrUtil;
import net.syamn.utils.Util;
import net.syamn.utils.exception.CommandException;
import net.syamn.voteadd.CountData;
import net.syamn.voteadd.Perms;


/**
 * AddCommand (AddCommand.java)
 * 
 * @author syam(syamn)
 */
public class AddCommand extends BaseCommand {
    public AddCommand() {
        bePlayer = false;
        name = "add";
        argLength = 1;
        usage = "<name> [count] <- add vote count";
    }

    @Override
    public void execute() throws CommandException {
        int count = 1;
        boolean set = false;
        
        if (args.size() > 1){
            if (!StrUtil.isInteger(args.get(1))){
                throw new CommandException("&c" + args.get(1) + " is not number!");
            }
            count = Integer.valueOf(args.get(1));
            if (count <= 0){
                throw new CommandException("&cInvalid number: " + count);
            }
            set = true;
        }
        
        Player target = Bukkit.getPlayerExact(args.get(0));
        if (target == null || !target.isOnline()){
            throw new CommandException("&c" + args.get(0) + " is not online!");
        }
        
        if (!set){
            if (Perms.MULTIPLIER3.has(target)){
                count = 3;
            }else if (Perms.MULTIPLIER2.has(target)){
                count = 2;
            }
        }        
        
        CountData.addCount(target.getName(), count);
        if (!plugin.checkCount(target.getName())){
            int now = CountData.getCount(target.getName());
            String msg = config.getMessage().replace("%now%", String.valueOf(now));
            
            if (msg.length() > 0){
                Util.message(target, msg);
            }
            Util.message(sender, "&aAdded vote count (" + count + ") for player " + target.getName() + " (" + now + "/" + config.getCount() + ")");
        }else{
            Util.message(sender, "&aAdded vote count (" + count + ") for player " + target.getName() + ", commands executed!");
        }   
    }

    @Override
    public boolean permission() {
        return Perms.ADD.has(sender);
    }
}