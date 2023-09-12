package org.len;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DotenvLoader {
    public static Map<String, String> loadEnvVariables() {
        String filename = "C:\\Users\\Prawira\\IdeaProjects\\be-surface-tracking\\src\\org\\len\\.env";
        Map<String, String> envVariables = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    envVariables.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return envVariables;
    }
}
