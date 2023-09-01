package org.example.results;

import java.util.Map;

public class FinalTransferResult implements FinalTransfer {
    private final Map<String, FileScannerResult> scannedFileResults;

    public FinalTransferResult(
            Map<String, FileScannerResult> scannedFileResults
    ) {
        this.scannedFileResults = scannedFileResults;
    }

    @Override
    public void transfer(FileScannerResult scannedFileResults) {
        this.scannedFileResults.put(scannedFileResults.getCorpusName(), scannedFileResults);
    }


}