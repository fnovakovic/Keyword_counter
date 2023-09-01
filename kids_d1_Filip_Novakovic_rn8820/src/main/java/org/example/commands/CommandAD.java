package org.example.commands;


import org.example.main.DirectoryCrawler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommandAD implements Command {
    private String args;
    private final CopyOnWriteArrayList<String> paths;
    private final DirectoryCrawler directoryCrawler;

    public CommandAD(CopyOnWriteArrayList<String> paths, DirectoryCrawler directoryCrawler) {
        this.paths = paths;
        this.directoryCrawler = directoryCrawler;
    }

    @Override
    public void execute() {
        if (Files.notExists(Path.of(this.args))) {
            System.out.println("Couldn't find path: " + this.args);
            return;
        }

        if (!paths.contains(this.args)) {
            paths.add(this.args);
        }

        directoryCrawler.findCorpusDirAndCreateJob(this.args, true);
    }

    @Override
    public void setArguments(String args) {
        this.args = args;
    }

    @Override
    public String getCommandName() {
        return "ad";
    }

    @Override
    public boolean stop() {
        return false;
    }
}