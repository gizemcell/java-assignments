public class Standard extends Bus{
    private final int[] struct ={2,2};
    private double refundCut;
    private boolean[][] state;
    public boolean[][] getlastState(){
        return state;
    }

    public void setStructOfColumn(){
        this.setStructOfColumn(struct);
    }

    public double getRefundCut() {
        return refundCut;
    }

    public void setRefundCut(float refundCut) {
        this.refundCut = refundCut;
    }

    @Override
    public void initialize(String[] command) {
        this.setID(Integer.parseInt(command[2]));
        this.setFrom(command[3]);
        this.setWhere(command[4]);
        this.setTotalRow(Integer.parseInt(command[5]));
        this.setPrice(Float.parseFloat(command[6]));
        this.setRefundCut(Float.parseFloat(command[7]));
        setStructOfColumn();
        this.setInitialState();
    }
    public void setInitialState(){
        boolean[][] arr=new boolean[getTotalRow()][struct[0]+struct[1]];
        state=arr;
    }
    @Override
    public double sellTicket(int[][] convertedSeat,String path){
        double before=getRevenue();
        for(int i=0;i<convertedSeat.length;i++){
            if(!state[convertedSeat[i][0]][convertedSeat[i][1]]){
                state[convertedSeat[i][0]][convertedSeat[i][1]]=true;
                this.setRevenue(getPrice());
            }
            else{
                for(int j=0;j<i;j++){
                    state[convertedSeat[j][0]][convertedSeat[j][1]] =false;
                }
                this.equalRevenue(before);
                FileOutput.writeToFile(path,"ERROR: One or more seats already sold!",true,true);
                break;
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
                state[convertedSeat[j][0]][convertedSeat[j][1]]=false;
                refund=getPrice()-(getPrice()*refundCut/100);
                this.setRevenue(-refund);
            }
        }
        double change=before-getRevenue();
        return change;
    }
    @Override
    public void cancelVoyage(){
        for(boolean[] seat:state){
            for(int i=0;i<struct[0]+struct[1];i++){
                if(seat[i]){
                    this.setRevenue(-getPrice());;
                }
            }
        }

    }



}
