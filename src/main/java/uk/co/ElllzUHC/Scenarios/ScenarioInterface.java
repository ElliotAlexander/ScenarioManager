package uk.co.ElllzUHC.Scenarios;

import org.bukkit.event.Listener;

/**
 * Created by Elliot on 29/11/2014.
 */
public interface ScenarioInterface extends Listener {

    public void setScenarioState(Boolean b);

    public boolean getScenarioState();

    public String getScenarioName();

    public String getScenarioDescription();

}