package tsp.controller.command;

import tsp.model.context.Context;

/**
 * Created by artem on 04.09.15.
 */
public abstract class Command
{
    protected abstract boolean canExecute(String command);

    public boolean isApplicable(Context context){
        return canExecute(context.getCommand());
    }
    public abstract void execute(Context context);
}
