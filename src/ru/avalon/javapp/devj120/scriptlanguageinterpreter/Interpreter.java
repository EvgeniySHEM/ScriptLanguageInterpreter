package ru.avalon.javapp.devj120.scriptlanguageinterpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Interpreter {

    private final Map<String, Integer> variables = new HashMap<>();

    public void fileProcessing(String fileName) throws IOException, IllegalAccessException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String s;
            while ((s = br.readLine()) != null) {
                if (s.startsWith("#") || s.startsWith(" "))
                    continue;

                if (s.startsWith("set ")) {
                    int start = s.indexOf('$'),
                            finish = s.length();
                    s = s.substring(start, finish);
                    String[] words = s.split(" ");
                    int value = 0;
                    OUTER:
                    for (int j = 0; j < words.length; j++) {
                        if (words[j].startsWith("$")) {
                            if (!variables.containsKey(words[j])) {
                                variables.put(words[j], 0);
                                continue;
                            }
                        }

                        if (words[j].equals("=")) {
                            INNER:
                            for (int i = 2; i < words.length; i++) {
                                j++;
                                if (words[i].startsWith("$")) {
                                    if (!variables.containsKey(words[i])) {
                                        throw new IllegalAccessException("You have entered an unknown variable");
                                    }

                                    if (variables.containsKey(words[i])) {
                                        value = variables.get(words[i]);
                                        continue INNER;
                                    }
                                }

                                if (words[i].equals("+")) {
                                    i++;
                                    j++;
                                    if (words[i].startsWith("$") && !variables.containsKey(words[i])) {
                                        throw new IllegalAccessException("You have entered an unknown variable");
                                    }

                                    if (variables.containsKey(words[i])) {
                                        value += variables.get(words[i]);
                                        continue INNER;
                                    }

                                    int p = 0;
                                    while (p < words[i].length() && Character.isDigit(words[i].charAt(p)))
                                        p++;

                                    if (p == words[i].length()) {
                                        value += Integer.parseInt(words[i]);
                                        continue INNER;
                                    }
                                    else {
                                        throw new IllegalAccessException("You have entered an unknown variable");
                                    }
                                }

                                if (words[i].equals("-")) {
                                    i++;
                                    j++;

                                    if(words[i].startsWith("$") && !variables.containsKey(words[i])) {
                                            throw new IllegalAccessException("You have entered an unknown variable");
                                    }

                                    if (variables.containsKey(words[i])) {
                                        value -= variables.get(words[i]);
                                        continue INNER;
                                    }

                                    int p = 0;
                                    while (p < words[i].length() && Character.isDigit(words[i].charAt(p)))
                                        p++;

                                    if (p == words[i].length()) {
                                        value -= Integer.parseInt(words[i]);
                                        continue INNER;
                                    }
                                    else {
                                        throw new IllegalAccessException("You have entered an unknown variable");
                                    }
                                }
                                int p = 0;
                                while (p < words[i].length() && Character.isDigit(words[i].charAt(p)))
                                    p++;

                                if (p == words[i].length()) {
                                    value = Integer.parseInt(words[i]);
                                    continue INNER;
                                }
                                else {
                                    throw new IllegalAccessException("Вы ввели неизвестную переменную");
                                }

                            }
                        }
                        variables.put(words[0], value);
                    }
                }

                if (s.startsWith("print ")) {
                    String result = "";

                    int start = s.indexOf('"'),
                            finish = s.length();
                    s = s.substring(start, finish);

                    int p = 0;
                    while (p < s.length()) {
                        // пропускаем пробелы и запятые
                        while (p < s.length() && (s.charAt(p) == ' ' || s.charAt(p) == ',')) {
                            p++;
                        }

                        if( s.charAt(p) == '"') {
                            start = ++p;
                            p++;
                            while (s.charAt(p) != '"')
                                p++;
                            if ( p > start)
                                result += s.substring(start, p);
                            p++;
                            continue;
                        }

                        if( s.charAt(p) == '$') {
                            start = p;
                            p++;

                            String var = "";
                            while (p < s.length() && s.charAt(p) != ',')
                                p++;
                            if ( p > start)
                                var += s.substring(start, p);

                            if (var.startsWith("$") && !variables.containsKey(var)) {
                                throw new IllegalAccessException("Вы ввели неизвестную переменную");
                            }

                            if (variables.containsKey(var)) {
                                result += variables.get(var);
                            }
                        }
                    }
                    System.out.println(result);
                }
            }
        }
    }
}
