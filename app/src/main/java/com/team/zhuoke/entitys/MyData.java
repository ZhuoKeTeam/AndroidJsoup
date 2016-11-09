package com.team.zhuoke.entitys;

/**
 * Created by Yunr on 2016/11/9.
 */

public class MyData {

    private String noteId;//NoteId
    private String expected;//预期收益
    private String current;//当前收益
    private String loss;//止损
    private String price;//价格

    public MyData(String noteId, String expected, String current, String loss, String price) {
        this.noteId = noteId;
        this.expected = expected;
        this.current = current;
        this.loss = loss;
        this.price = price;
    }


    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getLoss() {
        return loss;
    }

    public void setLoss(String loss) {
        this.loss = loss;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return "NoteId:" + getNoteId() + "\n预期收益:" + getExpected() + "\n当前收益:"
                + getCurrent() + "\n止损:" + getLoss() + "\n价格:" + getPrice();
    }
}
