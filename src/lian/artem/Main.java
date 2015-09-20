package lian.artem;

import lian.artem.tree.BinarySearchTree;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Random;

import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.DOMConfiguration;
import tsp.AppSingleton;
import voce.*;

public class Main {

    public static void main(String[] args) throws RemoteException, AlreadyBoundException
    {
        DOMConfigurator.configure("/home/artem/IdeaProjects/Custom/config/log4j-config.xml");
	// write your code here
        AppSingleton.getInstance().execute();


    }
}
