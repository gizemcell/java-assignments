public class Slots {
    /**
     * @amount amount of products in the slot.
     */
    private String type;
    private byte amount;
    private float price;
    private float protein,fat,carb;

    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getCarb() {
        return carb;
    }

    public void setCarb(float carb) {
        this.carb = carb;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte getAmount() {
        return amount;
    }

    public void setAmount(int changing) {
        this.amount = (byte)(this.amount +changing);
    }

    public Slots(String type,float price,float protein,float carb,float fat){
        this.price=price;
        this.type=type;
        this.protein=protein;
        this.fat=fat;
        this.carb=carb;
        this.amount=0;
    }
    public float totalCalorie(){
        return 4*this.protein+ 4*this.carb+ 9*this.fat;
    }
}
