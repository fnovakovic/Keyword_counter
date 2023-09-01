package org.example.commands;


import org.example.results.FileScannerResult;
import java.util.Map;

public class QueryFileResultCommand implements Command {
    private String args;

    private final Map<String, FileScannerResult> scannedFileResults;
    private Map<String, Map<String, Integer>> cacheFileSummery;

    public QueryFileResultCommand (
            Map<String, FileScannerResult> scannedFileResults,
            Map<String, Map<String, Integer>> cacheFileSummery
    ) {
        this.scannedFileResults = scannedFileResults;
        this.cacheFileSummery = cacheFileSummery;
    }

    @Override
    public void execute() {
        boolean flag = false;
        if (args.equals("summary")) {
            this.callSummary();
            return;
        }

        FileScannerResult fileScannerResult = scannedFileResults.get(this.args);
        if (fileScannerResult != null) {
            flag = fileScannerResult.getQueryResult();
        }

        if (flag == false) {
            System.out.println("Corpus: [" + this.args + "] is not ready");

        }else{
            System.out.println("Corpus: [" + this.args + "] is ready");
        }

    }

    private void callSummary () {
        boolean flag = false;
        for (Map.Entry<String, FileScannerResult> entry: scannedFileResults.entrySet()) {
            FileScannerResult fileScannerResult = entry.getValue();
            flag = fileScannerResult.getQueryResult();
        }

        if (flag == false) {
            System.out.println("Summary is not ready yet");
        }else{
            System.out.println("Summary is ready");
        }


    }

    @Override
    public void setArguments(String args) {
        this.args = args;
    }

    @Override
    public String getCommandName() {
        return "file";
    }

    @Override
    public boolean stop() {
        return false;
    }
}
