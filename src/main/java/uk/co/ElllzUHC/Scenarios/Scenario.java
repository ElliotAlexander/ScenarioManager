package uk.co.ElllzUHC.Scenarios;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Elliot on 29/11/2014.
 */
public class Scenario extends JavaPlugin implements Listener {

    private ArrayList<ScenarioInterface> scenarioList = new ArrayList<ScenarioInterface>();

    private final String prefix = ChatColor.GRAY + "["  + ChatColor.AQUA + "Scenarios" + ChatColor.GRAY + "]";


    private ArrayList<String> notLoadedScenarios = new ArrayList<String>();

    private Logger logger;

    public void onEnable(){

        logger = Bukkit.getLogger();
        logger.info("Scenario Manager has been enabled!");
    }


    public void registerScenario(ScenarioInterface scenario){
        for(ScenarioInterface scenarioInterface : scenarioList){
            if(scenarioInterface.getScenarioName().equalsIgnoreCase(scenario.getScenarioName())){
                logger.log(Level.SEVERE, "Found another instance of " + scenario.getScenarioName() +  ", Reinitiallising scenario.");
                scenarioList.remove(scenarioInterface);
                break;
            }
        }

        if(scenario.getScenarioName().contains(" ")){
            logger.log(Level.SEVERE, "Scenario " + scenario.getScenarioName() + " Contains whitespace in name! This is not supported. Ignoring.");
            notLoadedScenarios.add(scenario.getScenarioName());
            return;
        }

        scenarioList.add(scenario);
        logger.info("Loading plugin " + scenario.getScenarioName() );

        getServer().getPluginManager().registerEvents(scenario, this);

    }



    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("scenario")){
            if (args.length == 0) {
                sender.sendMessage(ChatColor.AQUA + "----------------------------");
                sender.sendMessage(ChatColor.GOLD + "Enabled Scenarios:");
                for (ScenarioInterface scenario : scenarioList) {
                    sender.sendMessage(" - " + ChatColor.AQUA + scenario.getScenarioName() + ChatColor.GRAY + " [" + ChatColor.RED + checkScenarioState(scenario) + ChatColor.GRAY + "] : " + scenario.getScenarioDescription());
                }

                if(notLoadedScenarios.size()>0){
                    sender.sendMessage(ChatColor.GOLD + "Scenarios ignored due to loading errors:");
                    for(String s : notLoadedScenarios){
                        sender.sendMessage(" - " + ChatColor.AQUA + s);
                    }
                }

                sender.sendMessage(ChatColor.AQUA + "----------------------------");
            } else if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(ChatColor.AQUA + "----------------------------");
                sender.sendMessage(ChatColor.GOLD + "Enabled Scenarios:");
                for (ScenarioInterface scenario : scenarioList) {
                    sender.sendMessage(" - " + ChatColor.AQUA + scenario.getScenarioName() + ChatColor.GRAY + " [" + ChatColor.RED + checkScenarioState(scenario) + ChatColor.GRAY + "] : " + scenario.getScenarioDescription());
                }

                if(notLoadedScenarios.size()>0){
                    sender.sendMessage(ChatColor.GOLD + "Scenarios ignored due to loading errors:");
                    for(String s : notLoadedScenarios){
                        sender.sendMessage(" - " + ChatColor.AQUA + s);
                    }
                }
                sender.sendMessage(ChatColor.AQUA + "----------------------------");
            } else if (args[0].equalsIgnoreCase("toggle")) {
                if (args.length != 2&&sender.hasPermission("Scenario.Toggle")) {
                    sender.sendMessage(ChatColor.RED + "Invalid arguments!");
                    return true;
                }

                for (ScenarioInterface scenario : scenarioList) {

                    if (scenario.getScenarioName().equalsIgnoreCase(args[1])) {


                        if(scenario.getScenarioState()){
                            scenario.setScenarioState(false);
                            Bukkit.broadcastMessage(prefix + " " + scenario.getScenarioName() + " has been disabled!");
                        } else {
                            scenario.setScenarioState(true);
                            Bukkit.broadcastMessage(prefix + " " + scenario.getScenarioName() + " has been enabled!");
                        }
                        return true;
                    }

                    // If there are no matches on Scenario name, assume it does not exist.
                }
                sender.sendMessage(prefix + "No Scenarios found!");
                return true;
            } else {
                sender.sendMessage(prefix + " Invalid command!");
                return true;
            }
            return true;
        }
        return true;
    }

    /**
     * Returns on/off message in correct Color.
     * @param scenario
     * @return
     */

    public String checkScenarioState(ScenarioInterface scenario) {
        if (scenario.getScenarioState()) {
            return ChatColor.GREEN + "ON";
        } else {
            return ChatColor.RED + "OFF";
        }
    }
}
