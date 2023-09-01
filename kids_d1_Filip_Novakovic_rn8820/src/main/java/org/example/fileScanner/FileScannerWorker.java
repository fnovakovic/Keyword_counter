package org.example.fileScanner;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class FileScannerWorker implements Callable<Map<String, Integer>> {
    private final List<String> keywords;
    private final List<File> txtFiles;

    public FileScannerWorker(List<String> keywords, List<File> txtFiles) {
        this.keywords = keywords;
        this.txtFiles = txtFiles;
    }

    @Override
    public Map<String, Integer> call() {
        Map<String, Integer> results = new HashMap<>();

        for (String key: keywords) {
            results.put(key, 0);
        }

        for (File f: this.txtFiles) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f.getAbsolutePath()));

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] words = line.split("\\s+");

                    for (String word: words) {
                        if (word != null && keywords.contains(word)) {
                            int count = results.getOrDefault(word, 0);
                            results.put(word, count + 1);
                        }
                    }
                }

                reader.close();
            } catch (Exception e) {
                System.out.println("File: [" + f.getPath() + "] cannot be read");
            }
        }

        return results;
    }
}