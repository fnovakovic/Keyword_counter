package org.example.jobs;

import org.example.ext.Stoppable;

import java.util.concurrent.BlockingQueue;

public class JobDispatcher implements Runnable, Stoppable {
    private volatile boolean loop = true;

    private final BlockingQueue<Job> jobQueue;

    private final JobTransfer jobTransfer;

    public JobDispatcher (
            BlockingQueue<Job> jobQueue,
            BlockingQueue<CrawlerJob> scannedFilesJobQueue
    ) {
        this.jobTransfer = new JobTransfer(scannedFilesJobQueue);
        this.jobQueue = jobQueue;
    }

    @Override
    public void run() {
        while (this.loop) {
            try {
                Job job = this.jobQueue.take();

                if (job.isStopped()) {
                    break;
                }

                job.proceed(this.jobTransfer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Shutting down job dispatcher");
    }

    @Override
    public void stop() {
        this.loop = false;

        new CrawlerJob().proceed(this.jobTransfer);
    }
}