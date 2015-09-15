package tsp.controller;

import tsp.controller.command.Command;
import tsp.controller.command.impl.MedianFiltration;
import tsp.model.Model;
import tsp.model.context.Context;

import java.util.LinkedList;

/**
 * Created by artem on 04.09.15.
 */
public class Controller
{
    private LinkedList<Command> commandLinkedList;

    public Controller(){
        // TODO (arli0415) add adding commands to list
        commandLinkedList = new LinkedList<>();
        commandLinkedList.add(new MedianFiltration());
    }

    public void executeCommand(Context context)
    {
        for (Command cmd : commandLinkedList)
        {
            if (cmd.isApplicable(context))
            {
                cmd.execute(context);
            }
        }
    }
}
