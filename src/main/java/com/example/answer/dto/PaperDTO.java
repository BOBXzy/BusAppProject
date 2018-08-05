package com.example.answer.dto;

public class PaperDTO {
    private long id;
    private String title;
    private String desc;
    public PaperDTO(long id,String title, String desc){
        this.id = id;
        this.desc=desc;
        this.title=title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
