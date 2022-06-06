package domain;

/**
 * 이 도메인은 제가 테스트용으로 만든 거라 신경 안 쓰셔도 됩니다
 */
public class HillShadeGrid {

    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;
    private final double dsmX;
    private final double dsmY;
    private final int hillShade = 100;

    public HillShadeGrid(double x1, double y1, double x2, double y2, double dsmX, double dsmY) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.dsmX = dsmX;
        this.dsmY = dsmY;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }

    public double getDsmX() {
        return dsmX;
    }

    public double getDsmY() {
        return dsmY;
    }

    public int getHillShade() {
        return hillShade;
    }
}
