package ru.qq;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YamlExample {
    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/temp.yaml";

        String content = new String(Files.readAllBytes(Paths.get(filePath)));

        List<String> lines =  Arrays.stream(content.split("\n")).toList();

        lines.stream().filter(x -> x.contains("#")).forEach(x -> System.out.println(x.trim()));
    }
}
