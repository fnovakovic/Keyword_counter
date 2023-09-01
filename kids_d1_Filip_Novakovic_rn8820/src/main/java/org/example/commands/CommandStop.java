package org.example.commands;


import org.example.main.DirectoryCrawler;
import org.example.jobs.JobDispatcher;
import org.example.results.ResultRetriever;
import org.example.fileScanner.FileScanner;

public class CommandStop implements Command {
    private final ResultRetriever resultRetriever;
    private final DirectoryCrawler directoryCrawler;
    private final JobDispatcher jobDispatcher;
    private final FileScanner fileScanner;

    public CommandStop(
            ResultRetriever resultRetriever,
            DirectoryCrawler directoryCrawler,
            JobDispatcher jobDispatcher,
            FileScanner fileScanner
    ) {
        this.resultRetriever = resultRetriever;
        this.directoryCrawler = directoryCrawler;
        this.jobDispatcher = jobDispatcher;
        this.fileScanner = fileScanner;
    }

    @Override
    public void execute() {
        directoryCrawler.stop();
        jobDispatcher.stop();
        fileScanner.stop();
        resultRetriever.stop();
    }

    @Override
    public void setArguments(String args) {
    }

    @Override
    public String getCommandName() {
        return "stop";
    }

    @Override
    public boolean stop() {
        return true;
    }
}