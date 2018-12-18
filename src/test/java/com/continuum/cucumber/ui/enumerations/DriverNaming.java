package com.continuum.cucumber.ui.enumerations;

import lombok.Getter;

@Getter
public enum DriverNaming {

    CHROME("CHROME", "chrome"), FIREFOX("FIREFOX", "firefox"), IE("IEXPLORER", "Internet Explorer"), EDGE("EDGE", "edge");

    private String selenideName;
    private String wdmName;

    DriverNaming(String wdmName, String selenideName) {
        this.selenideName = selenideName;
        this.wdmName = wdmName;
    }
}
