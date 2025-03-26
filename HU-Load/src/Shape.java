import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * this class is created to build shape of stage
 * setUp method returns rootNode/
 */
public class Shape {
    //lavas and bounds list that contains lava and bound nodes to be added to scene
    private ArrayList<Lavas> lavas;
    private ArrayList<Boundaries> bounds;
    private static final double INITIAL_FUEL = 10000.000; // Initial fuel value
    private double remainingFuel = INITIAL_FUEL;
    private PlayGame playGame;
    Timeline fuelTimeline;
    public Group setUp(ArrayList<Valuables> valuables){
        Group rootNode=new Group();
        Canvas canvas=new Canvas(800,675);
        GraphicsContext gc= canvas.getGraphicsContext2D();
        Label fuelLabel = new Label("fuel:" + String.format("%.3f", remainingFuel));
        Label haul=new Label("haul: 0");
        Label money=new Label("money: 0");
        haul.setPrefSize(150,25);
        haul.setFont(new Font("Arial", 18));
        haul.setLayoutY(25);
        money.setPrefSize(150,30);
        money.setLayoutY(50);
        money.setFont(new Font("Arial", 18));
        money.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
        haul.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
        fuelLabel.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
        fuelLabel.setFont(new Font("Arial", 18));
        // fuelTimeLine that changes fuels time by time
        fuelTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.1), e -> {
                    remainingFuel-= 0.001; // Decrement the fuel value
                    fuelLabel.setText(String.format("fuel:%.3f", remainingFuel)); // Update the label with the remaining fuel
                    if (remainingFuel <= 0) {
                        fuelTimeline.stop();
                        playGame.setGameOver(true);
                        playGame.fuelRunOut(rootNode.getChildren());
                    }
                })
        );
        fuelTimeline.setCycleCount((int) (INITIAL_FUEL / 0.001));
        fuelTimeline.play();
        fuelLabel.setLayoutX(0);
        fuelLabel.setLayoutY(0);
        fuelLabel.setPrefSize(150,25);
        rootNode.getChildren().addAll(fuelLabel,money,haul);
        createSky(gc);
        rootNode.getChildren().add(canvas);
        lavas=new ArrayList<>();
        bounds=new ArrayList<>();
        createLavaBound();
        settingStage(rootNode);
        update(rootNode,valuables);
        return rootNode;
    }
    public void setPlayGame(PlayGame playGame){
        this.playGame=playGame;

    }
    public void setRemainingFuel(double remanin){
        remainingFuel=remainingFuel+remanin;
    }
    public double getRemainingFuel(){
        return remainingFuel;
    }
    // settingStage forms stage by adding one by one and set X and Y coordinates.
    private void settingStage(Group rootNode){
        addingHorizontal("assets/underground/top_01.png",75, rootNode.getChildren());
        for(int i=1;i<11;i++){
            createComplex(rootNode.getChildren(),75+i*50);
        }
        addingHorizontal("assets/underground/obstacle_01.png",75+50*11, rootNode.getChildren());
    }
    // createSky is a sky is created by drawing partial of scene
    private void createSky(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillRect(150, 0, 800, 80);
    }

    /**
     * update method inserts lavas . bounds and valuable objects by randomly.
     */
    private void update(Group rootNode,ArrayList<Valuables> valuables){
        ArrayList<Integer> pairs=new ArrayList<>(createIndexes());
        ArrayList<Valuables> sublist=createValuables(valuables);
       int a=0;
        for(int i=0;i<9;i++){
            ImageView existingNode = (ImageView) rootNode.getChildren().get(pairs.get(i));
            existingNode.setImage(lavas.get(a).getImg().getImage());
            a++;
        }
        a=0;
        for(int i=9;i<15;i++){
            ImageView existingNode = (ImageView) rootNode.getChildren().get(pairs.get(i));
            existingNode.setImage(sublist.get(a).getImg().getImage());
            a++;
        }
        a=0;
        for(int i=15;i<19;i++){
            ImageView existingNode = (ImageView) rootNode.getChildren().get(pairs.get(i));
            existingNode.setImage(bounds.get(a).getImg().getImage());
            a++;
        }
    }
    // createLavaBound creates objects that is bounds or lavas.
    // It is made for detecting whether the node is bound or lava easily by using instance of keyword.
    private void createLavaBound(){
        for(int i=0;i<9;i++){
            Lavas lava=new Lavas("lava_01",new ImageView("assets/underground/lava_01.png"));
            lavas.add(lava);
        }
        for(int i=0;i<6;i++){
            Boundaries obst=new Boundaries("obstacle",new ImageView("assets/underground/obstacle_03.png"));
            bounds.add(obst);
        }
    }
    public ArrayList<Lavas> getLavas(){
        return lavas;
    }
    public ArrayList<Boundaries> getBounds(){
        return bounds;
    }
    // createIndexes returns HashSet contains random indexes different each other.
    //later this list uses at update method to locate objects.(lava,bound,valuable)
    private HashSet<Integer> createIndexes(){
        HashSet<Integer> indexes=new HashSet<>();
        HashSet<Integer> forbidden=new HashSet<>();
        int number=4;
        for(int i=0;i<12;i++){
            forbidden.add(number);
            number=number+15;
            forbidden.add(number);
            number+=1;
        }
        while(indexes.size()<19){
            int row=randomNumber(5,178);
            if(!forbidden.contains(row)){
                indexes.add(row);
            }
        }
        return indexes;
    }
    //takeIndexSet allow you taking set that contains random number.
    // it allows customizing min,max,amount(HashSet size).
    private HashSet<Integer> takeIndexSet(int min,int max,int amount){
        HashSet<Integer> indexes=new HashSet<>();
        int size=0;
        while(size<amount){
            int index=randomNumber(min,max);
            indexes.add(index);
            size=indexes.size();
        }
        return indexes;
    }
    private ArrayList<Valuables> createValuables(ArrayList<Valuables> valuables){
        ArrayList<Valuables> sublist=new ArrayList<>();
        HashSet<Integer> indexes=takeIndexSet(0,valuables.size()-1,6);
        for(int ind:indexes){
            sublist.add(valuables.get(ind));
        }
        return sublist;
        }

    private int randomNumber(int min,int max){
        int randomNum = (int)(Math.random() * (max - min + 1)) + min;
        return randomNum;
    }
    //addingHorizontal adds object next to one.
    //it keeps constant y coordinates.
    public void addingHorizontal(String path,int y,ObservableList<Node> children){
        for(int k=0;k<16;k++){
            ImageView image=new ImageView(path);
            image.setFitWidth(50); // Adjust image width
            image.setFitHeight(50);
            image.setY(y);
            image.setX(k*50);
            children.add(image);
        }
    }
    //createComplex adds different object horizontally.
    public void createComplex(ObservableList<Node> children,int y){
        String path;
        for(int j=0;j<16;j++){
            if(j==0 || j==15){
                path="assets/underground/obstacle_01.png";
            }
            else{
                path="assets/underground/soil_01.png";
            }
            ImageView img=new ImageView(path);
            img.setFitWidth(50); // Adjust image width
            img.setFitHeight(50);
            img.setX(j*50);
            img.setY(y);
            children.add(img);
        }

    }
}
