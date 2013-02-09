/**
 * VoteAdd - Package: net.syamn.voteadd Created: 2012/11/30 6:09:41
 */
package net.syamn.voteadd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.syamn.utils.LogUtil;
import net.syamn.utils.Metrics;
import net.syamn.utils.Util;
import net.syamn.voteadd.command.AddCommand;
import net.syamn.voteadd.command.BaseCommand;
import net.syamn.voteadd.command.CheckCommand;
import net.syamn.voteadd.command.HelpCommand;
import net.syamn.voteadd.command.ReloadCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * VoteAdd (VoteAdd.java)
 * 
 * @author syam(syamn)
 */
public class VoteAdd extends JavaPlugin {
    // ** Listener **
    // ServerListener serverListener = new ServerListener(this);

    // ** Commands **
    private List<BaseCommand> commands = new ArrayList<BaseCommand>();

    // ** Private Classes **
    private ConfigurationManager config;

    // ** Instance **
    private static VoteAdd instance;

    /**
     * プラグイン起動処理
     */
    @Override
    public void onEnable() {
        instance = this;
        LogUtil.init(this);

        PluginManager pm = getServer().getPluginManager();
        config = new ConfigurationManager(this);

        // loadconfig
        try {
            config.loadConfig(true);
        } catch (Exception ex) {
            LogUtil.warning("an error occured while trying to load the config file.");
            ex.printStackTrace();
        }

        // プラグインを無効にした場合進まないようにする
        if (!pm.isPluginEnabled(this)) { return; }
        
        // load data
        new CountData(this);
        CountData.reloadData();

        // コマンド登録
        registerCommands();

        // メッセージ表示
        PluginDescriptionFile pdfFile = this.getDescription();
        LogUtil.info("version " + pdfFile.getVersion() + " is enabled!");

        setupMetrics(); // mcstats
    }

    /**
     * プラグイン停止処理
     */
    @Override
    public void onDisable() {
        CountData.saveData();
        
        // メッセージ表示
        PluginDescriptionFile pdfFile = this.getDescription();
        LogUtil.info("version " + pdfFile.getVersion() + " is disabled!");
    }

    /**
     * コマンドを登録
     */
    private void registerCommands() {
        // Intro Commands
        commands.add(new HelpCommand());

        // Main Commands
        commands.add(new AddCommand());
        commands.add(new CheckCommand());

        // Admin Commands
        commands.add(new ReloadCommand());
    }

    /**
     * コマンドが呼ばれた
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {
        if (cmd.getName().equalsIgnoreCase("voteadd")) {
            if (args.length == 0) {
                // 引数ゼロはヘルプ表示
                args = new String[] { "help" };
            }
            /*
             * else if (args[0].equalsIgnoreCase("gen")){ args[0] = "generate";
             * }
             */

            outer: for (BaseCommand command : commands.toArray(new BaseCommand[0])) {
                String[] cmds = command.getName().split(" ");
                for (int i = 0; i < cmds.length; i++) {
                    if (i >= args.length || !cmds[i].equalsIgnoreCase(args[i])) {
                        continue outer;
                    }
                    // 実行
                    return command.run(this, sender, args, commandLabel);
                }
            }
            // 有効コマンドなし デフォルトコマンド実行
            new HelpCommand().run(this, sender, args, commandLabel);
            return true;
        }
        return false;
    }

    /**
     * Metricsセットアップ
     */
    private void setupMetrics() {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException ex) {
            LogUtil.warning("cant send metrics data!");
            ex.printStackTrace();
        }
    }
    
    public boolean checkCount(String name){
        int now = CountData.getCount(name);
        if (config.getCount() > now){
            return false;
        }
        
        // execute commands, reset count
        CountData.resetCount(name);
        
        List<String> cmds = config.getCommands();
        if (cmds == null || cmds.size() == 0){
            return true;
        }
        
        for (String cmd : cmds){
            cmd = cmd.replace("%name%", name);
            Util.executeCommandOnConsole(cmd);
        }
        
        return true;
    }

    /* getter */
    /**
     * コマンドを返す
     * 
     * @return List<BaseCommand>
     */
    public List<BaseCommand> getCommands() {
        return commands;
    }

    /**
     * 設定マネージャを返す
     * 
     * @return ConfigurationManager
     */
    public ConfigurationManager getConfigs() {
        return config;
    }

    /**
     * インスタンスを返す
     * 
     * @return VoteAddインスタンス
     */
    public static VoteAdd getInstance() {
        return instance;
    }
}