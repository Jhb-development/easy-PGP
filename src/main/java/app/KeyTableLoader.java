package app;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class KeyTableLoader {
    public static Object[][] loadKeyPairs() {
        File keysDir = new File("src/main/resources/keys");
        if (!keysDir.exists() || !keysDir.isDirectory()) {
            return new Object[0][3];
        }
        File[] files = keysDir.listFiles();
        Map<String, String[]> keyMap = new HashMap<>();
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                if (name.endsWith("_public.asc")) {
                    String identity = name.substring(0, name.length() - 11);
                    keyMap.putIfAbsent(identity, new String[2]);
                    keyMap.get(identity)[0] = name;
                } else if (name.endsWith("_private.asc")) {
                    String identity = name.substring(0, name.length() - 12);
                    keyMap.putIfAbsent(identity, new String[2]);
                    keyMap.get(identity)[1] = name;
                }
            }
        }
        Object[][] data = new Object[keyMap.size()][3];
        int i = 0;
        for (Map.Entry<String, String[]> entry : keyMap.entrySet()) {
            data[i][0] = entry.getKey();
            data[i][1] = entry.getValue()[0] != null ? entry.getValue()[0] : "";
            data[i][2] = entry.getValue()[1] != null ? entry.getValue()[1] : "";
            i++;
        }
        return data;
    }
}
