public class Premium extends Bus{
    private final int[] struct={1,2};
    private double premiumFee;
    private boolean[][] state;
    private float refundCut;
    public boolean[][] getlastState(){
        return state;
    }

    public void setStructOfColumn(){
        this.setStructOfColumn(struct);
    }
    public float getRefundCut() {
        return refundCut;
    }

    public void setRefundCut(float refundCut) {
        this.refundCut = refundCut;
    }

    public double getPremiumFee() {
        return premiumFee;
    }

    public void setPremiumFee(double premiumFee) {
        this.premiumFee = (this.getPrice()*premiumFee)/100+this.getPrice();
    }

    @Override
    public void initialize(String[] command) {
        this.setID(Integer.parseInt(command[2]));
        this.setFrom(command[3]);
        this.setWhere(command[4]);
        this.setTotalRow(Integer.parseInt(command[5]));
        this.setPrice(Float.parseFloat(command[6]));
        this.setRefundCut(Float.parseFloat(command[7]));
        this.setPremiumFee(Float.parseFloat(command[8]));
        this.setInitialState();
        this.setStructOfColumn();

    }
    @Override
    public void setInitialState(){
        boolean[][] arr=new boolean[getTotalRow()][struct[0]+struct[1]];
        state=arr;
    }
    @Override
    public double sellTicket(int[][] convertedSeat,String path){
        double before=getRevenue();
        for(int i=0;i<convertedSeat.length;i++){
            if(!state[convertedSeat[i][0]][convertedSeat[i][1]]){
                if(isPremium(convertedSeat[i][1])){
                    this.setRevenue(getPremiumFee());
                }
                else{
                    this.setRevenue(getPrice());
                }
                state[convertedSeat[i][0]][convertedSeat[i][1]]=true;
            }
            else{
                for(int j=0;j<i;j++){
                    state[convertedSeat[j][0]][convertedSeat[j][1]]=false;
                }
                this.equalRevenue(before);
                FileOutput.writeToFile(path,"ERROR: One or more seats already sold!",true,true);
                return 0;
            }
        }
        double change=getRevenue()-before;
        return change;
    }

    @Override
    public double refundTicket(int[][] convertedSeat,String path){
        double before=this.getRevenue();
        double refund;
        for(int j=0;j<convertedSeat.length;j++){
            if(!state[convertedSeat[j][0]][convertedSeat[j][1]]){
                FileOutput.writeToFile(path,"ERROR: One or more seats are already empty!",true,true);
                for(int k=0;k<j;k++){
                    state[convertedSeat[k][0]][convertedSeat[k][1]]=true;
                }
                this.equalRevenue(before);
                return 0;
            }
            else{
                if(isPremium(convertedSeat[j][1])){
                    refund=premiumFee-(premiumFee*refundCut)/100;
                    state[convertedSeat[j][0]][convertedSeat[j][1]]=false;

                }
                else{
                    refund=getPrice()-(getPrice()*refundCut/100);
                    state[convertedSeat[j][0]][convertedSeat[j][1]]=false;
                }

            }
            this.setRevenue(-refund);
        }
        double change=before-getRevenue();
        return change;
    }

    private boolean isPremium(int column){
        if(column==0){
            return true;
        }
        return false;
    }
    @Override
    public void cancelVoyage(){
        for(boolean[] seat:state){
            for(int i=0;i<struct[0]+struct[1];i++){
                if(seat[i]){
                    if(i==0){
                        this.setRevenue(-premiumFee);
                    }
                    else{
                        this.setRevenue(-getPrice());
                    }
                }
            }
        }
    }


}
