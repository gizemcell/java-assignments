import javafx.application.Application;
import javafx.scene.input.*;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.Group;
import javafx.scene.image.*;
import java.util.ArrayList;
import javafx.stage.Stage;

public class Main extends Application {

    ImageView user;
    boolean gameOver=false;

    public static ArrayList<Valuables> valuables=new ArrayList<>();
    public static void main(String[] args) {
        String[] content=FileInput.readFile("/Users/gizemcelik/IdeaProjects/Game/src/assets/atributes_of_valuables.txt", false,false);
        for(int i=1;i<content.length;i++){
            String[] cont=content[i].split("\t");
            String path="assets/underground/valuable_"+cont[0].toLowerCase()+".png";
            Valuables valuable=new Valuables(cont[0],Integer.parseInt(cont[1]),Integer.parseInt(cont[2]),new ImageView(path));
            valuables.add(valuable);
        }
        launch();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Shape set=new Shape();
        Group rootNode=set.setUp(valuables);
        Scene myScene=new Scene(rootNode,800,675);
        primaryStage.setScene(myScene);
        user =new ImageView("assets/drill/drill_01.png");
        user.setX(100);
        user.setY(25);
        rootNode.getChildren().add(user);
        int ind=rootNode.getChildren().indexOf(user);
        PlayGame playGame=new PlayGame(set.getLavas(),set.getBounds(),ind, user);
            myScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(!gameOver){
                        playGame.response(event.getCode(), rootNode.getChildren(),valuables,set,true);
                        gameOver=playGame.getGameOver();
                    }
                }
            });
        primaryStage.show();
    }




    }
