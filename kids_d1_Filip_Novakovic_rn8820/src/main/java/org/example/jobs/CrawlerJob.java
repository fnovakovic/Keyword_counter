package org.example.jobs;


public class CrawlerJob implements Job {
    private final String corpusName;
    private final String absolutePath;

    private final boolean visible;
    private final boolean stopped;

    public CrawlerJob(String corpusName, String absolutePath, boolean visible) {
        this.corpusName = corpusName;
        this.absolutePath = absolutePath;

        this.visible = visible;
        this.stopped = false;
    }

    public CrawlerJob() {
        this.corpusName = null;
        this.absolutePath = null;

        this.visible = false;
        this.stopped = true;
    }

    @Override
    public void proceed(Transfer transfer) {
        transfer.transfer(this);
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
        return this.corpusName;
    }

    public String getAbsolutePath () {
        return this.absolutePath;
    }
}