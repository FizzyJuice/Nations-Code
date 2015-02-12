package nations.core.nation_manager;

public class NChunk {

    private Nation nation;
    private String name;
    private double power;

    public NChunk(Nation nation, String name, double power) {
        this.nation = nation;
        this.name = name;
        this.power = power;
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    public String getName() {
        return name;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

}
