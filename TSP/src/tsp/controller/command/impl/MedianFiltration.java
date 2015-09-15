package tsp.controller.command.impl;

import tsp.controller.command.Command;
import tsp.controller.command.CommandsContstants;
import tsp.model.context.Context;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by artem on 06.09.15.
 */
public class MedianFiltration extends Command
{
    @Override
    protected boolean canExecute(String command)
    {
        return CommandsContstants.MEDIAN_FILTER.equals((String) command);
    }

    @Override
    public void execute(Context context)
    {
        BufferedImage image=(BufferedImage)context.getModel().getData();

        context.getModel().setData(image);
    }
}
