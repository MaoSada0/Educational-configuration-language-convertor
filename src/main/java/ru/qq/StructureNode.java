package ru.qq;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StructureNode {
    private String partOfVal;
    private String val;
    private boolean isEnd;
    private boolean isStructure;
    private Structure structure;
}
