package org.example.results;

import org.example.ext.Visible;
import org.example.ext.Stop;

import java.util.Map;

public interface Result extends Stop, Visible {
     void proceed(FinalTransfer finalTransfer);
     Map<String, Integer> getResult();
     boolean getQueryResult();
}

