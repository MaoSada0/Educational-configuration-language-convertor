package ru.qq;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.parser.ParserException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class YamlValidator {

    public static boolean isValidYaml(String filePath) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = new FileInputStream(filePath)) {
            yaml.load(inputStream);
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (ParserException e) {
            System.err.println("Syntax err: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Err: " + e.getMessage());
        }
        return false;
    }
}
