package domain;

public class Hexagon {

    private int totalHeight = 0;
    private int dsmCount = 0;

    public Hexagon() {
    }

    public void addDsmInfo(int height) {
        this.totalHeight += height;
        this.dsmCount += 1;
    }

    public int getAverageHeight() {
        if(this.dsmCount == 0) {
            return -1;
        }
        return this.totalHeight / this.dsmCount;
    }
}
