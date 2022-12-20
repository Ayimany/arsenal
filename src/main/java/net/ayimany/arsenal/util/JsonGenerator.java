package net.ayimany.arsenal.util;

import java.io.*;
import java.util.ArrayList;

/**
 * Utility class to generate JSON item model files based on existent Java files.
 **/
public class JsonGenerator {

    public static final String BASE_ITEM_DIRECTORY = "src/main/java/net/ayimany/arsenal/items/";
    public static final String BASE_MODEL_DIRECTORY = "src/main/resources/assets/arsenal/models/item/";
    public static final String[] INCLUDED_DIRECTORIES = {
            "ammo","firearm"
    };

    public static File[] getFilesInDirectory(String path) throws IOException {
        File dir = new File(path);

        if (!dir.isDirectory())
            throw new IOException("Path does not point to a directory.");

        return dir.listFiles();
    }

    public static ArrayList<File> getFilesInSubdirectories(String path) throws IOException {
        ArrayList<File> files = new ArrayList<>();

        File[] dirAtPath = getFilesInDirectory(path);

        for (File file: dirAtPath)
            if (file.isDirectory()) files.addAll(getFilesInSubdirectories(file.getPath()));
            else files.add(file);

        return files;
    }

    public static String upperCamelCaseToLowerSnakeCase(String name) {
        StringBuilder result = new StringBuilder();

        for (char c: name.toCharArray()) {

            if (c >= 'A' && c <= 'Z') {
                c = (char)(c - 'A' + 'a');
                result.append('_');
            }

            result.append(c);
        }

        result.deleteCharAt(0);

        return result.toString();
    }

    public static String removeFileExtension(String filename) {
        return filename.substring(0, filename.indexOf("."));
    }

    public static String generateFirearmModel(String firearmName) {
        return """
{
    "parent": "item/handheld",
    "textures": {
        "layer0": "arsenal:item/FIREARM"
    }
}
""".replace("FIREARM", firearmName);
    }

    public static String generateAmmoUnitModel(String unitName) {
        return """
{
    "parent": "item/generated",
    "textures": {
        "layer0": "arsenal:item/UNIT"
    }
}
""".replace("UNIT", unitName);
    }


    // Since this is a simple tool, some IOExceptions will not be handled.
    // They shall rarely come up.
    public static void main(String[] args) throws IOException {
        ArrayList<File> files = new ArrayList<>();

        for (String included: INCLUDED_DIRECTORIES) {
            String fullPath = BASE_ITEM_DIRECTORY + included;
            files.addAll(getFilesInSubdirectories(fullPath));
        }

        for (File file: files) {
            String jsonFileName = upperCamelCaseToLowerSnakeCase(file.getName()).replace(".java", ".json");
            File jsonFile = new File(BASE_MODEL_DIRECTORY + jsonFileName);

            if (jsonFile.exists()) continue;
            boolean created = jsonFile.createNewFile();

            if (created) System.out.println("Successfully created " + jsonFileName);
            else System.out.println("Failed to create " + jsonFileName);

            String type = file.getPath().split("/")[7];

            FileWriter writer = new FileWriter(jsonFile);

            switch (type) {
                case "firearm" -> writer.write(generateFirearmModel(removeFileExtension(jsonFileName)));
                case "ammo" -> writer.write(generateAmmoUnitModel(removeFileExtension(jsonFileName)));
            }

            writer.close();

        }


    }
}
