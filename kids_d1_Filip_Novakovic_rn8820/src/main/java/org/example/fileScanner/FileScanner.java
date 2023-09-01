package org.example.fileScanner;

import org.example.jobs.CrawlerJob;
import org.example.results.FileScannerResult;
import org.example.results.Result;
import org.example.ext.PropertiesLoader;
import org.example.ext.Stoppable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class FileScanner implements Runnable, Stoppable {
    private final Map<String, Object> properties;

    private final BlockingQueue<CrawlerJob> scannedFilesJobQueue;
    private final BlockingQueue<Result> resultQueue;

    private final ExecutorService threadPool;
    private final ExecutorCompletionService<Map<String, Integer>> completionService;

    private volatile boolean loop = true;

    public FileScanner (BlockingQueue<CrawlerJob> scannedFilesJobQueue, BlockingQueue<Result> resultQueue) {
        this.scannedFilesJobQueue = scannedFilesJobQueue;
        this.resultQueue = resultQueue;

        this.threadPool = Executors.newCachedThreadPool();
        this.completionService = new ExecutorCompletionService<>(this.threadPool);
        this.properties = PropertiesLoader.getInstance().getProperties();

    }

    @Override
    public void run() {
        while (this.loop) {
            try {
                CrawlerJob crawlerJob = this.scannedFilesJobQueue.take();

                if (crawlerJob.isStopped()) {
                    break;
                }

                work(crawlerJob.getCorpusName(), crawlerJob.getAbsolutePath(), crawlerJob.isVisible());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.threadPool.shutdownNow();

        System.out.println("Shutting down file scanner");
    }

    @Override
    public void stop() {
        this.loop = false;

        resultQueue.add(new FileScannerResult());
    }

    private void work(String corpusName, String absolutePath, boolean visible) {
        List<File> files = new ArrayList<>();
        List<Future<Map<String, Integer>>> results = new ArrayList<>();

        if (visible) {
            System.out.println("Starting file scan for file|" + corpusName);
        }

        File[] txtFiles = new File(absolutePath).listFiles();

        long sizeLimit = (Integer) this.properties.get("file_scanning_size_limit");
        long currentSize = 0;

        if (txtFiles != null) {

            for (File f : txtFiles) {
                currentSize = currentSize + f.length();
                files.add(f);

                if (currentSize < sizeLimit) {
                    count(results,files);
                    currentSize = 0;
                    files.clear();
                }
            }
            if (!files.isEmpty()) {
                count(results,files);
            }

            FileScannerResult fileScannerResult = new FileScannerResult(corpusName, results, visible);
            resultQueue.add(fileScannerResult);
        }
    }

    private void count(List<Future<Map<String, Integer>>> results,List<File> files ) {
        List<String> searchKeywords = (List<String>) properties.get("keywords");
        FileScannerWorker scannerWorker = new FileScannerWorker(searchKeywords, files);
        Future<Map<String, Integer>> res = this.completionService.submit(scannerWorker);

        results.add(res);
    }
}