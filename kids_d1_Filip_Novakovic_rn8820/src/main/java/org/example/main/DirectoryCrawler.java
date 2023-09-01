package org.example.main;



import org.example.jobs.CrawlerJob;
import org.example.jobs.Job;
import org.example.ext.PropertiesLoader;
import org.example.ext.Stoppable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class DirectoryCrawler implements Runnable, Stoppable {
    private final Map<String, Object> properties;
    private final CopyOnWriteArrayList<String> paths;
    private final BlockingQueue<Job> jobQueue;
    private final HashMap<String, Long> lastModified = new HashMap<>();
    private volatile boolean loop = true;


    public DirectoryCrawler(CopyOnWriteArrayList<String> rememberedPaths, BlockingQueue<Job> jobQueue) {
        this.paths = rememberedPaths;
        this.jobQueue = jobQueue;
        this.properties = PropertiesLoader.getInstance().getProperties();
    }

    @Override
    public void run() {
        while (this.loop) {
                    synchronized (this) {
                        try {
                            wait(Long.valueOf(String.valueOf(this.properties.get("dir_crawler_sleep_time"))));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                for (String rememberedPath: this.paths) {
                    this.findCorpusDirAndCreateJob(rememberedPath, false);
                }

        }

        System.out.println("Shutting down crawler");
    }

    @Override
    public void stop() {
        this.loop = false;
        jobQueue.add(new CrawlerJob());

    }

    public void findCorpusDirAndCreateJob(String path, boolean isVisible) {
        ArrayList<File> corpusDirectories = new ArrayList<>();
        File directory = new File(path);


        if (isVisible) {
            System.out.println("Adding dir " + directory.getAbsolutePath());
        }

        if (directory.isDirectory() && directory.getName().startsWith((String) this.properties.get("file_corpus_prefix"))) {
            corpusDirectories.add(directory);
        }

        findAllCorpus(path, corpusDirectories);

        for (File corpusDirectory: corpusDirectories) {
            boolean valid = false;

            File[] corpusFiles = corpusDirectory.listFiles();

                if (corpusFiles != null) {
                    for (File corpusFile: corpusFiles) {
                        try {
                            Long newLastModifiedTime = Files.getLastModifiedTime(Path.of(corpusFile.getAbsolutePath())).toMillis();
                            Long oldLastModifiedTime = this.lastModified.get(corpusFile.getAbsolutePath());

                            this.lastModified.put(corpusFile.getAbsolutePath(), newLastModifiedTime);

                            if (!newLastModifiedTime.equals(oldLastModifiedTime)) {
                                valid = true;
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            if (valid) {
                jobQueue.add(new CrawlerJob(corpusDirectory.getName(), corpusDirectory.getAbsolutePath(), isVisible));
            }
        }
    }

    private void findAllCorpus(String uri, ArrayList<File> corpusDirectories) {
        File dir = new File(uri);

        File[] dirFiles = dir.listFiles();

        if (dirFiles != null) {
            for (File file: dirFiles) {
                if (file.isDirectory()) {
                    if (file.getName().startsWith((String) this.properties.get("file_corpus_prefix"))) {
                        corpusDirectories.add(file);
                    }

                    findAllCorpus(file.getPath(), corpusDirectories);
                }
            }
        }
    }
}