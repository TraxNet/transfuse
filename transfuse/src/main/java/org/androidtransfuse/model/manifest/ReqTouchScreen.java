package org.androidtransfuse.model.manifest;

import org.androidtransfuse.annotations.LabeledEnum;

public enum ReqTouchScreen implements LabeledEnum {
    UNDEFINED("undefined"),
    NOTOUCH("notouch"),
    STYLUS("stylus"),
    FINGER("finger");

    private String label;

    private ReqTouchScreen(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}