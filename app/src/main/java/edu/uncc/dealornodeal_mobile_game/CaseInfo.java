package edu.uncc.dealornodeal_mobile_game;

public class CaseInfo {
    private int imageViewID;
    private int drawableID;
    private boolean isOpened = false;
    private boolean isMatched = false;

    public CaseInfo(int imageViewID, int drawableID) {
        this.imageViewID = imageViewID;
        this.drawableID = drawableID;
    }

    public int getImageViewID() {
        return imageViewID;
    }

    public void setImageViewID(int imageViewID) {
        this.imageViewID = imageViewID;
    }

    public int getDrawableID() {
        return drawableID;
    }

    public void setDrawableID(int drawableID) {
        this.drawableID = drawableID;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    @Override
    public String toString() {
        return "CaseInfo{" +
                "imageViewID=" + imageViewID +
                ", drawableID=" + drawableID +
                ", isOpened=" + isOpened +
                ", isMatched=" + isMatched +
                '}';
    }
}
