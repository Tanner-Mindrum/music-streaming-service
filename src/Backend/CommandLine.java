package Backend;

import sun.misc.IOUtils;

import java.nio.charset.StandardCharsets;
import java.rmi.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.nio.file.*;


public class CommandLine extends Thread {
    public DFS dfs;

    public CommandLine(int p, int portToConnectTo) throws Exception {
        dfs = new DFS(p);
        dfs.join("localhost", portToConnectTo);
        start();
    }
    
    public static void main(String[] args) throws Exception  {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter new port: ");
        int newPortJoining = Integer.parseInt(in.nextLine().trim());
        System.out.println("Enter port to connect to: ");
        int portToConnectTo = Integer.parseInt(in.nextLine().trim());
        new CommandLine(newPortJoining, portToConnectTo);
    }

    public void run() {
        Scanner in = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("Command menu:\n-------------");
            System.out.println("1. move");
            System.out.println("2. join");
            System.out.println("3. list");
            System.out.println("4. touch");
            System.out.println("5. read");
            System.out.println("6. append");
            System.out.println("7. head");
            System.out.println("8. tail");
            System.out.println("9. delete");
            System.out.println("10. quit");
            System.out.println("11. print");
            System.out.println("\nEnter command: ");

            String input = in.nextLine().trim();

            if (input.equals("join") || input.equals("2")) {
                System.out.println("Enter port: ");
                try {
                    dfs.join("localhost", Integer.parseInt(in.nextLine().trim()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (input.equals("list") || input.equals("3")) {
                try {
                    System.out.println("\n" + dfs.ls());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (input.equals("touch") || input.equals("4")) {
                System.out.println("Enter file name: ");
                try {
                    dfs.touch(in.nextLine().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (input.equals("move") || input.equals("1")) {
                System.out.println("Enter old file name: ");
                String oldFileName = in.nextLine().trim();
                System.out.println("Enter new file name: ");
                String newFileName = in.nextLine().trim();
                try {
                    dfs.mv(oldFileName, newFileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (input.equals("read") || input.equals("5")) {
                System.out.println("Enter file name: ");
                String fileName = in.nextLine().trim();
                System.out.println("Enter page number: ");
                int pgNum = Integer.parseInt(in.nextLine().trim());
                try {
                    System.out.println(new String(dfs.read(fileName, pgNum)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (input.equals("append") || input.equals("6")) {
                System.out.println("Enter file name to append to: ");
                String fileName = in.nextLine().trim();
                System.out.println("Enter data to append: ");
                String fileToAppend = in.nextLine().trim();
                FileInputStream f = null;
                try {
                    f = new FileInputStream(fileToAppend);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                File appendingFile = new File(fileToAppend);
                byte[] data = new byte[(int) appendingFile.length()];
                try {
                    f.read(data);
                    f.close();
                    dfs.append(fileName, fileToAppend);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else if (input.equals("head") || input.equals("7")) {
                System.out.println("Enter file name: ");
                String fileName = in.nextLine().trim();
                try {
                    System.out.println(new String(dfs.head(fileName)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (input.equals("tail") || input.equals("8")) {
                System.out.println("Enter file name: ");
                String fileName = in.nextLine().trim();
                try {
                    System.out.println(new String(dfs.tail(fileName)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (input.equals("delete") || input.equals("9")) {
                System.out.println("Enter file name: ");
                String fileName = in.nextLine().trim();
                try {
                    dfs.delete(fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (input.equals("print")) {
                dfs.chord.print();
            }
            else if (input.equals("quit") || input.equals("10")) {
                running = false;
                System.out.println("\nThank you for using Groovee!");
                System.exit(0);
            }
        }
    }
}
