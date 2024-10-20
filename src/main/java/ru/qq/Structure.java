package ru.qq;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Structure {
    StructureNode structureNode;
    List<Structure> children;
    List<String> consts;

    public Structure(StructureNode structureNode) {
        this.structureNode = structureNode;
        this.children = new ArrayList<>();
    }

    public Structure() {
    }

    public void insert(String[] parts, String val) {
        if (parts.length == 0) return;

        if (parts.length == 1) {
            structureNode.setPartOfVal(parts[0]);
            structureNode.setVal(val);
            structureNode.setEnd(true);
            return;
        }

        for (Structure child : children) {
            if (child.structureNode.getPartOfVal().equals(parts[1])) {
                child.insert(Arrays.copyOfRange(parts, 1, parts.length), val);
                return;
            }
        }


        Structure newChild = new Structure(StructureNode.builder().partOfVal(parts[1]).build());
        children.add(newChild);
        newChild.insert(Arrays.copyOfRange(parts, 1, parts.length), val);
    }

//    public void insert(String[] parts) {
//        if (parts.length == 0) return;
//
//        if (parts.length == 1) {
//            structureNode.setPartOfVal(parts[0]);
//            structureNode.setVal(val);
//            structureNode.setEnd(true);
//            return;
//        }
//
//        for (Structure child : children) {
//            if (child.structureNode.getPartOfVal().equals(parts[1])) {
//                child.insert(Arrays.copyOfRange(parts, 1, parts.length), val);
//                return;
//            }
//        }
//
//
//        Structure newChild = new Structure(StructureNode.builder().partOfVal(parts[1]).build());
//        children.add(newChild);
//        newChild.insert(Arrays.copyOfRange(parts, 1, parts.length), val);
//    }
}
