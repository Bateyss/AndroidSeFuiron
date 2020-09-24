package com.fm.modules.app.menu;

public class OptionsEntity {
    private int id;
    private int resImageId;
    private String optionname;

    public OptionsEntity() {
    }

    public OptionsEntity(int id, int resImageId, String optionname) {
        this.id = id;
        this.resImageId = resImageId;
        this.optionname = optionname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResImageId() {
        return resImageId;
    }

    public void setResImageId(int resImageId) {
        this.resImageId = resImageId;
    }

    public String getOptionname() {
        return optionname;
    }

    public void setOptionname(String optionname) {
        this.optionname = optionname;
    }

}
