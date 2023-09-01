package org.example.results;


import org.example.ext.Stoppable;


import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ResultRetriever implements Runnable, Stoppable {
    private volatile boolean loop = true;

    private final BlockingQueue<Result> resultQueue;

    private final Map<String, FileScannerResult> scannedFileResults;

    private final Map<String, Map<String, Integer>> cacheFileSummery;

    private final FinalTransferResult finalTransferResult;

    public ResultRetriever(
            BlockingQueue<Result> resultQueue,
            Map<String, FileScannerResult> scannedFileResults,
            Map<String, Map<String, Integer>> cacheFileSummery
    ) {
        this.cacheFileSummery = cacheFileSummery;


        this.resultQueue = resultQueue;

        this.scannedFileResults = scannedFileResults;

        this.finalTransferResult = new FinalTransferResult(scannedFileResults);
    }

    @Override
    public void run() {
        while (this.loop) {
            try {
                Result res = this.resultQueue.take();

                if (res.isStopped()) {
                    break;
                }

                res.proceed(this.finalTransferResult);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Shutting down RR");
    }

    @Override
    public void stop() {
        this.loop = false;
    }
}