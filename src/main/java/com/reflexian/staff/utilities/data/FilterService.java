package com.reflexian.staff.utilities.data;

import com.google.gson.Gson;
import com.reflexian.staff.Staff;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

public class FilterService {

    private static HashSet<String> words = new HashSet<>();


    public static boolean isFiltered(String word) {
        return words.contains(word.toLowerCase());
    }

    public static void addWord(String word) {
        words.add(word.toLowerCase());
        save();
    }

    public static void removeWord(String word) {
        words.remove(word.toLowerCase());
        save();
    }

    public static void save() {
        // save using json to dataFolder/words.json
        Gson gson = new Gson();
        String json = gson.toJson(words);

        // save to file
        try (FileWriter writer = new FileWriter(Staff.getInstance().getDataFolder()+"/words.json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        String filePath = Staff.getInstance().getDataFolder() + "/words.json";
        if (Files.exists(Paths.get(filePath))) {
            try (FileReader reader = new FileReader(filePath)) {
                words = new Gson().fromJson(reader, HashSet.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
