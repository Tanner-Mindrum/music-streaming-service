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
            System.out.println("1. move");
            System.out.println("2. join");
            System.out.println("3. list");
            System.out.println("4. touch");
            System.out.println("5. read");
            System.out.println("10. quit");
            System.out.println("\nEnter command: ");

            String input = in.nextLine().trim();

            if (input.equals("join") || input.equals("2")) {
                System.out.println("Enter port: ");
                dfs.join("localhost", Integer.parseInt(in.nextLine().trim()));
            }
            else if (input.equals("list") || input.equals("3")) {
                System.out.println("\n" + dfs.ls());
            }
            else if (input.equals("touch") || input.equals("4")) {
                System.out.println("Enter file name: ");
                dfs.touch(in.nextLine().trim());
            }
            else if (input.equals("move") || input.equals("1")) {
                System.out.println("Enter old file name: ");
                String oldFileName = in.nextLine().trim();
                System.out.println("Enter new file name: ");
                String newFileName = in.nextLine().trim();
                dfs.mv(oldFileName, newFileName);
            }
            else if (input.equals("read") || input.equals("5")) {
                System.out.println("Enter file name: ");
                String fileName = in.nextLine().trim();
                System.out.println("Enter page number: ");
                int pgNum = Integer.parseInt(in.nextLine().trim());
                dfs.read(fileName, pgNum);
                System.out.println(new String(dfs.read(fileName, pgNum)));
            }
            else if (input.equals("quit") || input.equals("10")) {
                running = false;
                System.out.println("\nThank you for using Groovee!");
                System.exit(0);
            }
        }
    }
}
