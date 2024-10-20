package ru.qq;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException {

        StringBuilder answer = new StringBuilder();
        String yamlPath = "C:\\Users\\user\\IdeaProjects\\config_task3\\src\\main\\resources\\temp.yaml";
        String outputFilePath = "C:\\Users\\user\\IdeaProjects\\config_task3\\src\\main\\resources\\temp.txt";
        if(!YamlValidator.isValidYaml(yamlPath))
            return;

        helpFunc(answer, yamlPath);

        String strAns = answer.toString().replace(" :", ":");

        writeToFile(strAns, outputFilePath);

        System.out.println(strAns);
    }

    private static void helpFunc(StringBuilder answer, String filePath) throws IOException {


        String content = new String(Files.readAllBytes(Paths.get(filePath)));

        List<String> lines =  Arrays.stream(content.split("\n")).toList();

        lines.stream().filter(x -> x.contains("#")).forEach(x -> answer.append("=" + x.trim().substring(1)).append("\n"));

        List<String> vars = YamlParser.parse(filePath);

        TreeNode treeNode = new TreeNode("all");

        vars.stream().filter(x -> x.startsWith("(def")).forEach(x -> answer.append(x).append("\n"));

        vars.stream()
                .filter(x -> !x.startsWith("(def"))
                .forEach(x -> {
                            String[] s = ("all." + (x.split("=")[0])).split("\\.");
                            treeNode.insert(
                                    s,
                                    x.substring(x.indexOf("=") + 1).trim()
                            );
                        }
                );


        treeNode.print(0, answer);
    }

    private static void writeToFile(String answer, String outputFilePath) {
        try {
            Files.write(Paths.get(outputFilePath), answer.getBytes(), StandardOpenOption.CREATE);
            openFile(outputFilePath);
        } catch (IOException e) {
            System.err.println("Err with writing output file: " + e.getMessage());
        }
    }

    private static void openFile(String filePath) {
        if (Desktop.isDesktopSupported()) {
            try {
                File file = new File(filePath);
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
            }
        }
    }


}
