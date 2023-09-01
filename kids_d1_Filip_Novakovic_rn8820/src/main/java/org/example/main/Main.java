package org.example.main;



import org.example.commands.Command;
import org.example.jobs.CrawlerJob;
import org.example.jobs.Job;
import org.example.jobs.JobDispatcher;
import org.example.results.FileScannerResult;
import org.example.results.Result;
import org.example.results.ResultRetriever;
import org.example.fileScanner.FileScanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {


    private static final Map<String, FileScannerResult> scannedFileResults = new HashMap<>();
    private static final Map<String, Map<String, Integer>> cacheFiles = new HashMap<>();
    private static final CopyOnWriteArrayList<String> paths = new CopyOnWriteArrayList<>();
    private static final BlockingQueue<Job> jobQueue = new LinkedBlockingQueue<>();
    private static final BlockingQueue<Result> resultQueue = new LinkedBlockingQueue<>();
    private static final BlockingQueue<CrawlerJob> scannedFilesJobQueue = new LinkedBlockingQueue<>();

    private static ResultRetriever resultRetriever;
    private static DirectoryCrawler directoryCrawler;
    private static JobDispatcher jobDispatcher;
    private static FileScanner fileScanner;

    public static void main (String[] args) {
       load();
    }
    private static void load(){
        initRR();
        initDC();
        initJD();
        initFS();

        scan();
    }

    private static void scan () {
        Scanner sc = new Scanner(System.in);

        App app = new App(
                paths,
                scannedFileResults,
                cacheFiles,
                resultRetriever,
                directoryCrawler,
                jobDispatcher,
                fileScanner
        );

        while (true) {
            String args = sc.nextLine();

            Command command = app.getCommand(args);
            command.execute();

            if (command.stop()) {
                break;
            }
        }

        sc.close();
        System.out.println("Shuttitng down main");
    }
    private static void initDC () {
        directoryCrawler = new DirectoryCrawler(paths, jobQueue);
        Thread thread = new Thread(directoryCrawler, "DirectoryCrawler");
        thread.start();
    }

    private static void initJD() {
        jobDispatcher = new JobDispatcher(jobQueue, scannedFilesJobQueue);
        Thread thread = new Thread(jobDispatcher, "JobDispatcher");
        thread.start();
    }

    private static void initFS() {
        fileScanner = new FileScanner(scannedFilesJobQueue, resultQueue);
        Thread thread = new Thread(fileScanner, "FileScanner");
        thread.start();
    }

    private static void initRR() {
        resultRetriever = new ResultRetriever(
                resultQueue,
                scannedFileResults,
                cacheFiles
        );
        Thread thread = new Thread(resultRetriever, "ResultRetriever");
        thread.start();
    }


}
