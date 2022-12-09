package domain;

public class Hexagon {

    private int totalHeight;
    private int dsmCount;

    public Hexagon(int height) {
        totalHeight = height;
        dsmCount = 1;
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
