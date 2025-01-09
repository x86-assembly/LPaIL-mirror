package net.codemania;

import net.codemania.cli.Logger;
import net.codemania.codegeneration.x86_assembly.Generator;
import net.codemania.lexing.Lexer;
import net.codemania.parsing.Parser;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        System.exit(mainWrapper(args));
    }

    public static BufferedReader getFileReader(File file) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            Logger.err(String.format("File %s not found!", file.getName()));
            return null;
        }
        return reader;
    }

    public static int mainWrapper(String[] args) {


        if (args.length != 1) {
            Logger.err("Too " + (args.length > 1 ? "many" : "few") + " arguments (expected `lpail <filename>`)");
            System.exit(1);
            return 1;
        }

        File file = new File(args[0]);
        BufferedReader reader = getFileReader(file);
        if (reader == null) {
            return 2;
        }
        String program;
        try {
            program = new Generator().generateRootNode(new Parser(new Lexer(reader, args[0])).parse());
        } catch (Exception e) {
            Logger.err(e);
            return 3;
        }
        try {
            FileWriter fw = new FileWriter("out.asm");
            fw.write(program);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            Logger.err(e);
            return 2;
        }


        return 0;
    }

}
