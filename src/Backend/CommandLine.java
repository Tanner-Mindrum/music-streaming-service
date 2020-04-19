package Backend;

import java.rmi.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.nio.file.*;


public class CommandLine
{
    DFS dfs;
    public CommandLine(int p) throws Exception {
        dfs = new DFS(p);
        
            // User interface:
            // join, ls, touch, delete, read, tail, head, append, move
    }
    
    static public void main(String args[]) throws Exception
    {
        if (args.length < 1 ) {
            throw new IllegalArgumentException("Parameter: <port>");
        }
        CommandLine commandLine=new CommandLine( Integer.parseInt(args[0]));
        
     } 
}
