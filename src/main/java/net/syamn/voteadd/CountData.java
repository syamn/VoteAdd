package net.syamn.voteadd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.syamn.utils.LogUtil;
import net.syamn.utils.StrUtil;
import net.syamn.utils.file.TextFileHandler;

public class CountData {
    private static final String fileName = "data.ncsv";
    private static Map<String, Integer> map = new HashMap<String, Integer>();

    private static VoteAdd plugin;
    private static ConfigurationManager config;
    private static File file;

    public CountData(VoteAdd instance) {
        plugin = instance;
        config = plugin.getConfigs();
        
        file = new File(plugin.getDataFolder(), fileName);
    }

    public static boolean reloadData() {
        try {
            TextFileHandler datafile = new TextFileHandler(file);
            map.clear();
            
            List<String> list = datafile.readLines();
            String[] data;
            String[] coord;

            int line = 0;
            // 行を格納したリストが空になるまで繰り返す
            while (!list.isEmpty()) {
                line++;
                String thisLine = list.remove(0);
                
                // デリミタで配列に分ける
                data = thisLine.split(":");
                
                if (data.length != 2) {
                    LogUtil.warning("Skipping data line " + line + ": incorrect format");
                    continue;
                }

                if (!StrUtil.isInteger(data[1])){
                    LogUtil.warning("Skipping data line " + line + ": " + data[1] + " is not number!");
                    continue;
                }
                
                map.put(data[0], Integer.valueOf(data[1]));
            }
        } catch (Exception ex){
            if (config.isDebug()){
                ex.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public static boolean saveData() {
        try {
            TextFileHandler datafile = new TextFileHandler(file);
            List<String> wdata = new ArrayList<String>();
    
            for (Entry<String, Integer> entry : map.entrySet()) {
                wdata.add(entry.getKey() + ":" + String.valueOf(entry.getValue()));
            }
            
            datafile.writeLines(wdata);
        } catch (IOException ex) {
            if (config.isDebug()){
                ex.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public static int getCount(String name) {
        name = name.toLowerCase(Locale.ENGLISH);
        if (map.containsKey(name)){
            return map.get(name);
        }else{
            return 0;
        }
    }

    public static void addCount(String name, int count) {
        name = name.toLowerCase(Locale.ENGLISH);
        if (map.containsKey(name)){
            count = map.get(name) + count;
        }
        map.put(name, count);
    }
    
    public static void resetCount(String name){
        name = name.toLowerCase(Locale.ENGLISH);
        if (map.containsKey(name)){
            map.remove(name);
        }
    }
    
    public static void dispose(){
        map.clear();
        
        plugin = null;
        config = null;
        file = null;
    }
}
