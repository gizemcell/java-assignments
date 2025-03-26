public abstract class Bus implements Transportation {
    private double revenue;
    private int[] structOfColumn;
    private int totalRow;
    private int ID;
    private String from;
    private String where;
    private double price;

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = this.revenue+revenue;
    }

    /**
     *Although setRevenue adds changes into this.revenue,equalRevenue directly equals the given revenue to this.revenue
     */
    public void equalRevenue(double revenue) {
        this.revenue = revenue;
    }

    public int[] getStructOfColumn() {
        return structOfColumn;
    }

    public void setStructOfColumn(int[] structOfColumn) {
        this.structOfColumn = structOfColumn;
    }
    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *toString method exists for writing information of Bus objects to the output file
     */
    public void toString(String path){
        FileOutput.writeToFile(path, "Voyage "+getID(), true, true);
        FileOutput.writeToFile(path, getFrom()+"-"+getWhere(), true, true);
        for(boolean[] row: getlastState()){
            int i=0;
            while(i<structOfColumn[0]){
                if(row[i]){
                    if(i!=0){
                        FileOutput.writeToFile(path, " X", true, false);
                    }
                    else{
                        FileOutput.writeToFile(path, "X", true, false);
                    }
                }
                else{
                    if(i!=0){
                        FileOutput.writeToFile(path, " *", true, false);
                    }
                    else{
                        FileOutput.writeToFile(path, "*", true, false);
                    }
                }
                i++;
            }
            if(structOfColumn[1]!=0){
                FileOutput.writeToFile(path, " |", true, false);
            }
            while(i<structOfColumn[0]+structOfColumn[1]){
                if(row[i]){
                    FileOutput.writeToFile(path, " X", true, false);
                }
                else{
                    FileOutput.writeToFile(path, " *", true, false);
                }
                i++;
            }
            FileOutput.writeToFile(path, "", true, true);
        }
        FileOutput.writeToFile(path,String.format("Revenue: %.2f",this.getRevenue()),true,true);

    }
    public abstract boolean[][] getlastState();

    /**
     * converts seatNo to row and column version
     * @return an array contains row and column number.
     */
    public int[] convertIndex(int seatNo) {
        seatNo=seatNo-1;
        int column=seatNo%(this.getStructOfColumn()[0]+this.getStructOfColumn()[1]);
        int row=(seatNo-column)/(this.getStructOfColumn()[0]+this.getStructOfColumn()[1]);
        int[] indixes={row,column};
        return indixes;
    }

    }



