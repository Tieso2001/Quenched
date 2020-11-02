package net.tieso2001.quenched.hydration;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HydrationManager {

    public static List<HydrationItem> hydrationItems = new ArrayList<>();
    private static String path;

    public static void setPath(Path path) {
        HydrationManager.path = path.toString();
    }

    public static void readHydrationObjects() {
        List<Path> hydrationPaths = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.filter(Files::isRegularFile).forEach(hydrationPaths::add);
        } catch (IOException ignored) {}

        for (Path path : hydrationPaths) {
            readHydrationItem(path.getFileName().toString());
        }
    }

    private static void readHydrationItem(String filename) {
        JsonElement jsonElement = getJsonElement(filename);
        if (jsonElement != null) {
            String json = jsonElement.toString();
            HydrationItem hydrationItem = new Gson().fromJson(json, HydrationItem.class);
            hydrationItems.add(hydrationItem);
        }
    }

    private static JsonElement getJsonElement(String filename) {
        File directory = new File(path + File.separator + "quenched");
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File(path + File.separator + "quenched", filename);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));;

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(bufferedReader);
        return element;
    }
}
