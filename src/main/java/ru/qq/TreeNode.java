package ru.qq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;


public class TreeNode {
    private String partOfVal;
    private String val;
    private boolean isEnd;
    List<TreeNode> children;


    public TreeNode(String part, String val) {
        this.partOfVal = part;
        this.val = val;
        children = new ArrayList<>();
    }


    public TreeNode(String partOfVal, String val, boolean isEnd, List<TreeNode> children) {
        this.partOfVal = partOfVal;
        this.val = val;
        this.isEnd = isEnd;
        this.children = children;
    }

    public TreeNode(String part) {
        this.partOfVal = part;
        this.children = new ArrayList<>();
    }


    public void insert(String[] parts, String val) {
        if (parts.length == 0) return;

        if (parts.length == 1) {
            this.partOfVal = parts[0];
            this.val = val;
            this.isEnd = true;
            return;
        }

        for (TreeNode child : children) {
            if (child.getPart().equals(parts[1])) {
                child.insert(Arrays.copyOfRange(parts, 1, parts.length), val);
                return;
            }
        }


        TreeNode newChild = new TreeNode(parts[1]);
        children.add(newChild);
        newChild.insert(Arrays.copyOfRange(parts, 1, parts.length), val);
    }

    public void print(int numSpaces){
        if(partOfVal.equals("all")){
            children.forEach(x -> x.print(numSpaces));
            return;
        }

        printSpaces(numSpaces);

        System.out.print(partOfVal);
        if(val != null && val.startsWith("[") && val.endsWith("]"))
            change(val);

        if(isEnd)
            System.out.print(": " + val);



        if(children.size() > 1) {
            printSpaces(numSpaces);
            System.out.print("{");
        }
        System.out.println();
        children.forEach(x -> x.print(numSpaces + 1));
        if(children.size() > 1){
            printSpaces(numSpaces);
            System.out.println("}");
        }
    }

    private void change(String val) {
        TreeNode tree = new TreeNode("all");
        val = val.substring(1);
        val = val.substring(0, val.length() - 1);
        String[] pathes = val.split(",");
        System.out.println(Arrays.toString(pathes));
        Arrays.stream(pathes).forEach(x -> {
            String[] temp = x.split("=");
            tree.insert(("all."+ temp[0]).split("\\."), pathes[2]);
        });

        tree.print(0);
    }

    private void printSpaces(int numSpaces){
        IntStream.range(0, numSpaces).forEach(x -> System.out.print("  "));
    }

    private String getPart() {
        return partOfVal;
    }
}

