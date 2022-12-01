package ru.avalon.javapp.devj120.scriptlanguageinterpreter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        if (args.length == 0) {
            System.out.println("Usage: java ru.avalon.javapp.devj120.scriptlanguageinterpreter." +
                    "Main <file1> there is no file to read. " +
                    "The file name with the script text is specified when the application is launched via the command\n" +
                    "line argument");
            return;
        }

        Interpreter interpreter = new Interpreter();

        for(String arg : args) {
            try {
                interpreter.fileProcessing(arg);
            } catch (IOException e) {
                System.out.println("Error reading file " + arg + ":");
                System.out.println(e.getMessage());
                System.out.println("Finishing.");
                return;
            }
        }
    }
}
