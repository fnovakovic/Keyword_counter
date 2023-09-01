package org.example.results;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FileScannerResult implements Result {
    private final String dirCorpusName;
    private final List<Future<Map<String, Integer>>> futureResults;

    private Map<String, Integer> cacheSavedResults = new HashMap<>();

    private final boolean visible;
    private final boolean stopped;

    public FileScannerResult (String dirCorpusName, List<Future<Map<String, Integer>>> futureResults, boolean visible) {
        this.dirCorpusName = dirCorpusName;
        this.futureResults = futureResults;

        this.visible = visible;
        this.stopped = false;
    }

    public FileScannerResult () {
        this.dirCorpusName = null;
        this.futureResults = null;

        this.visible = false;
        this.stopped = true;
    }

    @Override
    public void proceed(FinalTransfer finalTransfer) {
        finalTransfer.transfer(this);
    }

    @Override
    public Map<String, Integer> getResult() {
        if (!cacheSavedResults.isEmpty()) {
            return cacheSavedResults;
        }

        if (futureResults == null) {
            return null;
        }

        Map<String, Integer> res = new HashMap<>();
        for (Future<Map<String, Integer>> fr: futureResults) {
            try {
                Map<String, Integer> r = fr.get();
                for (Map.Entry<String, Integer> e : r.entrySet()) {
                    int count = res.getOrDefault(e.getKey(), 0);

                    res.put(e.getKey(), count + e.getValue());
                }
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Failed to return result");
            }
        }

        if (!res.isEmpty()) {
            cacheSavedResults = res;
        }

        return res;
    }

    @Override
    public boolean getQueryResult() {
        boolean ready = true;
        for (Future<Map<String, Integer>> fr: futureResults) {
            if (!fr.isDone()) {
                ready = false;
                break;
            }
        }

        if (ready) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public boolean isStopped() {
        return this.stopped;
    }

    public String getCorpusName () {
        return this.dirCorpusName;
    }
}
