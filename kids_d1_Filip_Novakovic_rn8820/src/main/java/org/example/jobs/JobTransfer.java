package org.example.jobs;

import java.util.concurrent.BlockingQueue;

public class JobTransfer implements Transfer {
    private final BlockingQueue<CrawlerJob> scannedFilesJobQueue;

    public JobTransfer(
            BlockingQueue<CrawlerJob> scannedFilesJobQueue

    ) {
        this.scannedFilesJobQueue = scannedFilesJobQueue;
    }

    @Override
    public void transfer(CrawlerJob crawlerJob) {scannedFilesJobQueue.add(crawlerJob);}

}