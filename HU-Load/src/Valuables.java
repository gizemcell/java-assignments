import javafx.scene.image.ImageView;

public class Valuables extends Elements{
    private int worth;
    private int weight;
    public Valuables(String name,int worth,int weight,ImageView path){
        super(name,path);
        this.weight=weight;
        this.worth=worth;

    }
    public int getWorth() {
        return worth;
    }

    public void setWorth(int worth) {
        this.worth = worth;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
