package ru.qq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class TreeNode {
    private String partOfVal;
    private String val;
    private boolean isEnd;
    List<TreeNode> children;

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

    public void print(int numSpaces, StringBuilder answer) {
        if (partOfVal.equals("all")) {
            children.forEach(x -> x.print(numSpaces, answer));
            return;
        }

        answer.append(generateSpaces(numSpaces));
        answer.append(partOfVal);

        if (val != null && val.startsWith("[") && val.endsWith("]")) {
            answer.append("[").append("\n");
            change(val, numSpaces + 1, answer);
            answer.append(generateSpaces(numSpaces));
            answer.append("]");
        } else if (isEnd) {
            answer.append(": ").append(val).append(";");
        }

        answer.append("\n");


        if (!children.isEmpty()) {
            answer.append(generateSpaces(numSpaces));
            if (children.size() > 1) {
                answer.append("{").append("\n");
            }
            children.forEach(child -> child.print(numSpaces + 1, answer));
            if (children.size() > 1) {
                answer.append(generateSpaces(numSpaces));
                answer.append("}").append("\n");
            }
        }
    }


    private void change(String val, int numSpaces, StringBuilder answer) {
        TreeNode tree = new TreeNode("all");
        val = val.substring(1, val.length() - 1);

        String[] pathes = val.split(",(?![^\\[\\]]*\\])");

        Arrays.stream(pathes).forEach(x -> {
            x = x.trim();
            if(x.contains("=")){
                String firstPart = x.substring(0, x.indexOf("=")).trim();
                String lastPart = x.substring(x.indexOf("=") + 1).trim();
                tree.insert(("all."+ firstPart).split("\\."), lastPart);
            } else {
                tree.insert(("all."+ x).split("\\."), x);
            }

        });

        tree.print(numSpaces, answer);
    }

    private String generateSpaces(int numSpaces) {
        return IntStream.range(0, numSpaces)
                .mapToObj(x -> "  ")
                .collect(Collectors.joining());
    }

    private String getPart() {
        return partOfVal;
    }
}

