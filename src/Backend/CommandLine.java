package Backend;

import java.rmi.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.nio.file.*;


public class CommandLine {
    DFS dfs;
    public CommandLine(int p) throws Exception {
        dfs = new DFS(p);
        userInterface();

        // User interface:
        // join, ls, touch, delete, read, tail, head, append, move
    }
    
    public static void main(String[] args) throws Exception  {
//        if (args.length < 1 ) {
//            throw new IllegalArgumentException("Parameter: <port>");
//        }
        //new CommandLine(Integer.parseInt(args[0]));
        new CommandLine(2000);
    }

    public void userInterface() throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Command menu:\n-------------");
        System.out.println("1. ls (list)");
        System.out.println("\nEnter command: ");

        if (in.nextLine().equals("ls")) {
            dfs.ls();
        }

    }
}
