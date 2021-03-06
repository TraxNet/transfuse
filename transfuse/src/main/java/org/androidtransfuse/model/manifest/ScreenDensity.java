package org.androidtransfuse.model.manifest;

import org.androidtransfuse.annotations.LabeledEnum;

public enum ScreenDensity implements LabeledEnum {
    LDPI("ldpi"),
    MDPI("mdpi"),
    HDPI("hdpi"),
    XHDPI("xhdpi");

    private String label;

    private ScreenDensity(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}