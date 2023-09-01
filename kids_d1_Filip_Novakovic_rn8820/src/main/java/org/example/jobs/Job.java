package org.example.jobs;

import org.example.ext.Visible;
import org.example.ext.Stop;

public interface Job extends Stop, Visible {
    void proceed(Transfer transfer);
}