import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import java.util.ArrayList;

public class PlayGame {
    private int haul;
    private int money;
    private static double lastX=100;
    private static double lastY=25;
    private boolean gameOver=false;
    ArrayList<Lavas> lavas;
    ArrayList<Boundaries> bounds;
    ImageView adam;
    TranslateTransition fallTransition;
    private int ind;
    public PlayGame(ArrayList<Lavas> lavas, ArrayList<Boundaries> bounds,int ind,ImageView adam){
        this.lavas=lavas;
        this.bounds=bounds;
        this.ind=ind;
        this.adam=adam;
    }
    public ImageView getAdam(){
        return adam;
    }
    public void response(KeyCode keyCode, ObservableList<Node> children,ArrayList<Valuables> valuables,Shape set,boolean gravity){
        switch (keyCode){
            case RIGHT:
                if(lastY==70 && lastX==700){
                    adam.setImage(new Image("assets/drill/drill_60.png"));
                    adam.setTranslateX(adam.getTranslateX()+50);
                    lastX=750;
                    drilling(children,lastX,lastY,valuables,set);
                }
                else if(lastX<700){
                    adam.setImage(new Image("assets/drill/drill_60.png"));
                    lastX=lastX+50;
                    if(drilling(children,lastX,lastY,valuables,set)==1){
                        lastX=lastX-50;
                    }
                    else{
                        adam.setTranslateX(adam.getTranslateX()+50);
                    }

                }
                break;
            case LEFT:
                if(lastY==70 && lastX==50){
                    adam.setImage(new Image("assets/drill/drill_01.png"));
                    adam.setTranslateX(adam.getTranslateX()-50);
                    lastX=0;
                    drilling(children,lastX,lastY,valuables,set);

                }
                else if(lastX>50){
                    adam.setImage(new Image("assets/drill/drill_01.png"));
                    lastX=lastX-50;
                    if(drilling(children,lastX,lastY,valuables,set)==1){
                        lastX=lastX+50;
                    }
                    else{
                        adam.setTranslateX(adam.getTranslateX()-50);
                    }
                }
                break;
            case UP:
                if(noDrill(children)){
                    adam.setTranslateY(adam.getTranslateY()-50);
                    lastY=lastY-50;
                }
                break;
            case DOWN:
                if(lastX<750 && lastX>0 && lastY<570){
                    lastY=lastY+50;
                    if(drilling(children,lastX,lastY,valuables,set)==1){
                        lastY=lastY-50;
                    }
                    else{
                        adam.setTranslateY(adam.getTranslateY()+50);
                    }
                }
                break;
    }
    if(gameOver){
        gameDone(children);
    }
    if(gravity && implyGravity(children)){
        startFalling(children);
    }
    }
    public void startFalling(ObservableList<Node> children) {
        // Create a TranslateTransition to move adam downwards
        fallTransition = new TranslateTransition(Duration.seconds(1), adam);
        fallTransition.setByY(50);
        fallTransition.setOnFinished(event -> {
            // Check if gravity is still applied after the fall
            if (implyGravity(children)) {
                startFalling(children); // Continue falling if gravity is still applied
            } else {
                // Gravity is no longer applied, stop falling
                System.out.println("Falling stopped");
            }
        });
        fallTransition.play(); // Start the falling animation
    }
    //implyGravity returns true if there is empty space to fall below user.
    public boolean implyGravity(ObservableList<Node> children){
        int index=findNodeIndex(children,lastX,lastY+50);
        if(index!=-1 && children.get(index) instanceof Rectangle){
            lastY=((Rectangle)children.get(index)).getY();
            return true;
        }
        return false;
    }
    //if noDrill returns false, you cannot go upward because upward drilling is not allowed.
    private boolean noDrill(ObservableList<Node> children){
        int index=findNodeIndex(children,lastX,lastY-50);
        if(index!=-1 && children.get(index) instanceof Rectangle){
            return true;
        }
        return false;
    }
    //findNodeIndex returns index of specific node.
    private int findNodeIndex(ObservableList<Node> children, double x, double y) {
        if(((Canvas)children.get(3)).getLayoutY()==y){
            return 3;
        }
        else{
            for(int i=4;i<children.size()-1;i++){
                if(!(children.get(i) instanceof Rectangle)){
                    ImageView image=(ImageView) children.get(i);
                    if(image.getX()==x && image.getY()==y){
                        return i;
                    }
                }
                else{
                    Rectangle rectangle=((Rectangle) children.get(i));
                    if (rectangle.getX()==x && rectangle.getY()==y){
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    //score method updates haul and money if the node is valuable image/
    //if img is lava , the game will done
    //if img is bound, the user cannot move further.
    private int score(ArrayList<Valuables> valuables, ImageView img){
        for(Lavas lava:lavas){
            if(lava.getImg().getImage()==img.getImage()){
                gameOver=true;
                return -1;
            }
        }
        for(Boundaries bound:bounds){
            if(bound.getImg().getImage()==img.getImage()){
                return 1;
            }
        }
        for(Valuables value:valuables){
            if(value.getImg().getImage()==img.getImage()){
                haul=haul+value.getWeight();
                money=money+value.getWorth();
                return 0;
            }
        }
        return 2;
    }
    public boolean getGameOver(){
        return this.gameOver;
    }
    public void setGameOver(boolean tf){
        gameOver=tf;
    }
    //gameDone exits the game when the user touches lava.
    public void gameDone(ObservableList<Node> children){
            children.clear();
            Label gameDone=new Label("GAME OVER");
            gameDone.setAlignment(Pos.CENTER);
            gameDone.setPrefSize(800,675);
            gameDone.setFont(new Font(50));
            gameDone.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            children.add(gameDone);
        }
        //fuelRunOut exits the game when fuel is run out.
    public void fuelRunOut(ObservableList<Node> children){
        children.clear();
        Rectangle rect=new Rectangle(800,675);
        rect.setFill(Color.GREEN);
        Label collected=new Label();
        collected.setText("Collected Money: "+this.money);
        collected.setFont(new Font(40));
        collected.setStyle("-fx-text-fill: white;");
        collected.setLayoutX(220);
        collected.setLayoutY(140);
        collected.setPrefSize(600,375);
        Label gameDone=new Label("GAME OVER");
        gameDone.setLayoutX(300);
        gameDone.setLayoutY(120);
        gameDone.setPrefSize(400,300);
        gameDone.setFont(new Font(40));
        gameDone.setStyle("-fx-text-fill: white;");
        children.addAll(rect,gameDone,collected);
    }

    private int drilling(ObservableList<Node> children, double x, double y, ArrayList<Valuables> valuables,Shape set){
        int index=findNodeIndex(children,x,y);
        if(index>4){
            if(!(children.get(index) instanceof Rectangle)){
                if(score(valuables,(ImageView) children.get(index))==1){
                    return 1;
                }
                if(set.getRemainingFuel()<100.000){
                    fuelRunOut(children);
                }
                else{
                    set.setRemainingFuel(-100);
                    ((Label) children.get(2)).setText("haul:"+haul);
                    ((Label) children.get(1)).setText("money:"+money);
                    Rectangle rect=new Rectangle(x,y,50,50);
                    rect.setFill(Color.BROWN);
                    children.set(index,rect);
                    return 0;
                }
            }
        }
        return 2;
    }

}
