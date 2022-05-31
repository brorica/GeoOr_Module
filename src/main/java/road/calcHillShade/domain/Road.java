package road.calcHillShade.domain;

public class Road {

    private int id;
    private int TotalHillShadeSum;
    private int gridOverlapsCount;

    public Road(int id) {
        this.id = id;
        TotalHillShadeSum = 0;
        gridOverlapsCount = 0;
    }

    public void matchGrid(int hillShade) {
        TotalHillShadeSum += hillShade;
        gridOverlapsCount++;
    }

    public int getAverage() {
        if(TotalHillShadeSum == 0 && gridOverlapsCount == 0)
            return 0;
        return TotalHillShadeSum / gridOverlapsCount;
    }

    public int getId() {
        return id;
    }
}
