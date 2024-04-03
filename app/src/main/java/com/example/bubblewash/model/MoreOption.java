package com.example.bubblewash.model;

public class MoreOption {
    private String optionName;
    private int optionIcon;

    public MoreOption(String optionName, int optionIcon) {
        this.optionName = optionName;
        this.optionIcon = optionIcon;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getOptionIcon() {
        return optionIcon;
    }

    public void setOptionIcon(int optionIcon) {
        this.optionIcon = optionIcon;
    }
}
