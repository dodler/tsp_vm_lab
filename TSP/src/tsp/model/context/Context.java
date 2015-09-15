package tsp.model.context;

import tsp.model.Model;

/**
 * Created by artem on 04.09.15.
 */
public class Context
{
    private String command;
    private Model model;

    public Model getModel()
    {
        return model;
    }

    /**
     * methods set model
     * @param model
     */
    public void setModel(Model model)
    {
        this.model = model;
    }

    public String getCommand()
    {
        return command;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }
}
