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

    public void userInterface() throws Exception {
        Scanner in = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("Command menu:\n-------------");
            System.out.println("1. join");
            System.out.println("2. ls (list)");
            System.out.println("3. touch");
            System.out.println("10. quit");
            System.out.println("\nEnter command: ");

            String input = in.nextLine().trim();

            if (input.equals("join")) {
                System.out.println("Enter port: ");
                dfs.join("localhost", Integer.parseInt(in.nextLine().trim()));
            }
            else if (input.equals("ls")) {
                System.out.println("\n" + dfs.ls());
            }
            else if (input.equals("touch")) {
                System.out.println("Enter file name: ");
                dfs.touch(in.nextLine().trim());
            }
            else if (input.equals("quit")) {
                running = false;
                System.exit(0);
            }
        }
    }
}
