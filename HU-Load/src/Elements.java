import javafx.scene.image.ImageView;

public abstract class Elements {
    private String name;
    ImageView img;
    public Elements(String name, ImageView path){
        this.name=name;
        this.img=path;

    }
    public ImageView getImg() {
        return img;
    }
    public void setImg(ImageView img) {
        this.img = img;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
