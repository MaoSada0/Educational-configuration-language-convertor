package ru.qq;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.*;
import java.io.InputStream;
import java.util.*;

public class YamlParser {
    private final static Map<String, Object> flatMap = new LinkedHashMap<>();

    private static Map<String, Object> parseYaml(InputStream inputStream) {
        Yaml yaml = new Yaml(new Constructor() {
            @Override
            protected Object constructObject(Node node) {
                if (node instanceof ScalarNode) {
                    ScalarNode scalar = (ScalarNode) node;
                    if (scalar.getAnchor() != null) {
                        return "&" + scalar.getAnchor() + " " + scalar.getValue();
                    }
                    return scalar.getValue();
                } else if (node instanceof MappingNode) {
                    MappingNode mapping = (MappingNode) node;
                    Map<String, Object> map = new LinkedHashMap<>();
                    for (NodeTuple tuple : mapping.getValue()) {
                        String key = (String) constructObject(tuple.getKeyNode());
                        Object value = constructObject(tuple.getValueNode());
                        map.put(key, value);
                    }

                    return map;
                } else if (node instanceof SequenceNode) {
                    SequenceNode sequence = (SequenceNode) node;
                    List<Object> list = new ArrayList<>();
                    for (Node subNode : sequence.getValue()) {
                        list.add(constructObject(subNode));
                    }
                    if (sequence.getAnchor() != null) {
                        return "&" + sequence.getAnchor() + " " + list;
                    }
                    System.out.println(list);
                    return list;
                } else if (node instanceof AnchorNode) {
                    AnchorNode anchor = (AnchorNode) node;
                    return "*" + anchor.getAnchor();
                }
                return super.constructObject(node);
            }
        });

        Map<String, Object> yamlData = yaml.load(inputStream);
        flattenMap("", yamlData);
        return flatMap;
    }



    private static void flattenMap(String prefix, Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();

            if (entry.getValue() instanceof Map) {
                flattenMap(key, (Map<String, Object>) entry.getValue());
            } else if (entry.getValue() instanceof List list) {
                handleList(key, list);
                //flatMap.put(key, entry.getValue());
            } else {
                flatMap.put(key, entry.getValue());
            }
        }
    }

    private static void handleList(String key, List<?> list) {
        List<Object> results = new ArrayList<>();

        for (Object item : list) {
            if (item instanceof Map<?, ?> itemMap) {
                flattenMapForListItems(key, (Map<String, Object>) itemMap, results);
            } else {
                results.add(Collections.singletonMap(key, item));
            }
        }

        flatMap.put(key, results);
    }

    private static void flattenMapForListItems(String prefix, Map<String, Object> map, List<Object> results) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String newKey = prefix + "." + entry.getKey();

            if (entry.getValue() instanceof Map<?, ?> subMap) {
                flattenMapForListItems(newKey, (Map<String, Object>) subMap, results);
            } else {
                // Store the final key-value mapping in the results
                results.add(Collections.singletonMap(newKey, entry.getValue()));
            }
        }
    }

    public static List<String> parse(String pathToYaml, Structure structure) {
        List<String> ans = new ArrayList<>();

        Set<String> consts = new HashSet<>();

        try (InputStream inputStream = YamlParser.class.getResourceAsStream(pathToYaml)) {
            Map<String, Object> result = parseYaml(inputStream);
            result.forEach((key, value) -> {
                if (value instanceof List v) {
                    List<String> temp = new ArrayList<>();

                    for(Object st: v){

                        if(st instanceof Map<?, ?> mp){
                            mp.forEach((a, b) -> {
                                temp.add(((String) a).replace(key, "") + "=" + b);
                            });
                        }
                    }
                    List<String> updatedList = temp.stream()
                            .map(a -> a.startsWith("=") || a.startsWith(".") ? a.substring(1) : a)
                            .toList();


                    //System.out.println(key + " = " + updatedList);
                    ans.add(key + " = " + updatedList);
                } else {
                    if(value instanceof String vStr){
                        if(vStr.startsWith("&")){
                            if(consts.contains(vStr)){
                                vStr = vStr.replaceFirst("&", "*");
                            } else {
                                consts.add(vStr);
                            }
                        }

                        ans.add(key + " = " + vStr);
                    } else {

                        ans.add(key + " = " + value);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return changeToNew(ans);
    }

    private static List<String> changeToNew(List<String> vars){


        vars = vars.stream()
                .map(x -> {
                    if (x.split("=").length > 1 && x.split("=")[1].trim().startsWith("&")) {
                        return renameToDef(x);
                    }
                    if (x.split("=").length > 1 && x.split("=")[1].trim().startsWith("*")) {
                        return renameToAbs(x);
                    }

                    return x;
                })
                .toList();

        List<String> consts = vars.stream().filter(x -> x.startsWith("(def")).toList();
        List<String> notConsts = vars.stream().filter(x -> !x.startsWith("(def")).toList();

        vars = new ArrayList<>();

        vars.addAll(consts);
        vars.addAll(notConsts);

        return vars;
    }

    private static String renameToDef(String str){
        String[] parts = str.split("=");
        parts = Arrays.stream(parts)
                .map(String::trim)
                .toArray(String[]::new);

        String name = parts[1].substring(1, parts[1].indexOf(" ")).trim();

        String val = parts[1].substring(parts[1].indexOf(" ")).trim();

        return  "(def " + name + " " + val + ");";
    }

    private static String renameToAbs(String str){
        String[] parts = str.split("=");
        parts = Arrays.stream(parts)
                .map(String::trim)
                .toArray(String[]::new);

        String name = parts[1].substring(1, parts[1].indexOf(" ")).trim();

        return parts[0] + " = " + "|" + name + "|";
    }
}
