package com.continuum.cucumber.ui.enumerations;

import com.continuum.cucumber.shell.Shell;

public enum Timeouts {
    EXTRA_SMALL(1), SMALL(5), MEDIUM(10), LONG(20), EXTRA_LONG(30);
    public int milliseconds;
    public int seconds;

    Timeouts(int multi) {
        seconds = Shell.Application.getEnvironment().getTimeoutsMultiplicator() * multi;
        milliseconds = 1000 * seconds;

    }
}