package me.skrilled.api.manager;

import me.skrilled.utils.IMC;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager implements IMC {
    private static File dir;

    static {
        final File mcDataDir = mc.mcDataDir;
        FileManager.dir = new File(mcDataDir, sense.skrilledSense());
    }


    public static File getConfigFile(String name) {
        final File file = new File(FileManager.dir, String.format("%s.txt", name));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
        return file;
    }

    public static void init() {
        if (!FileManager.dir.exists()) {
            FileManager.dir.mkdir();
        }
    }

    public static List<String> read(String file) {
        final List<String> out = new ArrayList<>();
        try {
            if (!FileManager.dir.exists()) {
                FileManager.dir.mkdir();
            }
            final File f = new File(FileManager.dir, file);
            if (!f.exists()) {
                f.createNewFile();
            }
            try (FileInputStream fis = new FileInputStream(f)) {
                try (InputStreamReader isr = new InputStreamReader(fis)) {
                    try (BufferedReader br = new BufferedReader(isr)) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            out.add(line);
                        }
                    }
                }
                fis.close();
                return out;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static void save(String file, String content) {
        try {
            final File f = new File(FileManager.dir, file);
            if (!f.exists()) {
                f.createNewFile();
            }
            try (FileWriter writer = new FileWriter(f)) {
                writer.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
