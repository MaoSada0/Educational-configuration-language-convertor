package ru.qq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Main {
    public static void main(String[] args) {



        List<String> vars = YamlParser.parse("/temp.yaml", new Structure());

        TreeNode treeNode = new TreeNode("all");

       // vars.stream().filter(x -> x.startsWith("(def")).forEach(System.out::println);

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


        //treeNode.print(0);
    }


}

//
//another : [
//one:
//te:
//fsd: fds,
//das: fd,
//ewt: fsd,
//two: 2,
//three: 3
//        ]