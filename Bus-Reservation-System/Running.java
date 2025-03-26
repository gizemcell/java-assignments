import java.io.FileNotFoundException;

public class Running {
    public static void transportationDriver(String[] content, String path){
        if(content.length==0){
            FileOutput.writeToFile(path,"Z Report:",true,true);
            FileOutput.writeToFile(path,"----------------",true,true);
            FileOutput.writeToFile(path,"No Voyages Available!",true,true);
            FileOutput.writeToFile(path,"----------------",true,false);
            return;
        }
        /**
         * buses: is a array that holds all type of buses.This array is made dynamically.It means that
         * when a new bus is created , the buses array will be changed to include last created bus object.
         */
        Bus[] buses=new Bus[0];
        int lineNo=0;
        for(String line:content){
            lineNo++;
            FileOutput.writeToFile(path,"COMMAND: "+line,true,true);
            String[] elements=line.split("\t");
            /**
             * Assistant class contains several helping methods.One of them is addArray method.
             * This method appropriately adds a new object to buses array considering id order.
             */
            if(!Assistant.usageControl(elements,path)){
                continue;
            }
            if(elements[0].equals("INIT_VOYAGE")){
                    Bus bus=BusType.createType(BusType.valueOf(elements[1].toUpperCase()));
                    bus.initialize(elements);
                    int previousLength= buses.length;
                    buses= Assistant.addArray(bus,buses,path);
                /**
                 * if the length of the buses array doesn't change after the addArray method, it is indicated there are some
                 * problem about a new object.
                 */
                if(buses.length!=previousLength){
                        if(bus instanceof Standard){
                            FileOutput.writeToFile(path,"Voyage "+bus.getID()+" was initialized as a standard (2+2) voyage from " +bus.getFrom()+" to "+bus.getWhere()+" with "+
                                    String.format("%.2f",bus.getPrice())+" TL priced "+(bus.getStructOfColumn()[0]+bus.getStructOfColumn()[1])*bus.getTotalRow()
                                    +" regular seats. Note that refunds will be "+(int)((Standard) bus).getRefundCut()
                                    +"% less than the paid amount.",true,true);
                        }
                        else if(bus instanceof Premium){
                            FileOutput.writeToFile(path,"Voyage "+bus.getID()+" was initialized as a premium (1+2) voyage from "+bus.getFrom()
                                    +" to "+bus.getWhere()+" with "+String.format("%.2f",bus.getPrice())+" TL priced "+bus.getStructOfColumn()[1]*bus.getTotalRow()
                                    +" regular seats and "+String.format("%.2f",((Premium) bus).getPremiumFee())+" TL priced "+bus.getStructOfColumn()[0]*bus.getTotalRow()
                                    +" premium seats. Note that refunds will be "+(int)((Premium) bus).getRefundCut()+"% less than the paid amount.",true,true);
                        }
                        else{
                            FileOutput.writeToFile(path,"Voyage "+bus.getID()+" was initialized as a minibus (2) voyage from "
                                    +bus.getFrom()+" to "+bus.getWhere()+" with "+String.format("%.2f",bus.getPrice())+
                                    " TL priced "+ bus.getStructOfColumn()[0]*bus.getTotalRow()+" regular seats. Note that minibus tickets are not refundable."
                                    ,true,true);
                        }
                    }
            }
            else if(elements[0].equals("SELL_TICKET")){
                /**
                 * findId have two different meaning.One is the returning id of the searched bus object.
                 *  other one is returning null to indicate that there is no bus object with the specified ID.
                 */
                    Bus obj= Assistant.findId(Integer.parseInt(elements[1]),buses);
                    if(obj==null){
                        FileOutput.writeToFile(path,"ERROR: There is no voyage with ID of "+elements[1]+"!",true,true);
                        continue;
                    }
                    String[] seats=elements[2].split("_");
                /**
                 * oneToTwo: converting number seatNumber to row and column version.
                 * if a problem with seat numbers occurs , the method returns null.
                 */
                int[][] convertedSeat= Assistant.oneToTwo(seats,obj);
                    if(convertedSeat==null){
                        FileOutput.writeToFile(path,"ERROR: There is no such a seat!",true,true);
                        continue;
                    }
                /**
                 * earning equal to amount of money come from selling giving seatNumbers.
                 * return 0 : the selling doesn't happen.
                 */
                double earning=obj.sellTicket(convertedSeat,path);
                    if(earning!=(double)0){
                        FileOutput.writeToFile(path,"Seat ",true,false);
                        for(int j=0;j<seats.length;j++){
                            if(j==0){
                                FileOutput.writeToFile(path,seats[0],true,false);
                            }
                            else{
                                FileOutput.writeToFile(path,"-"+seats[j],true,false);
                            }
                        }
                        FileOutput.writeToFile(path," of the Voyage "+obj.getID()+ " from "+obj.getFrom()+" to "+obj.getWhere()+
                                " was successfully sold for "+String.format("%.2f",earning)+" TL.",true,true );
                    }
                }
            else if(elements[0].equals("REFUND_TICKET")){
                    Bus theBus= Assistant.findId(Integer.parseInt(elements[1]),buses);
                    if(theBus==null){
                        FileOutput.writeToFile(path,"ERROR: There is no voyage with ID of "+elements[1]+"!",true,true);
                        continue;
                    }
                    if(theBus instanceof Minibus){
                        FileOutput.writeToFile(path,"ERROR: Minibus tickets are not refundable!",true,true);
                        continue;
                    }
                    String[] seatNos=elements[2].split("_");
                    int[][] convertedSeat= Assistant.oneToTwo(seatNos,theBus);
                    if(convertedSeat==null){
                        FileOutput.writeToFile(path,"ERROR: There is no such a seat!",true,true);
                        continue;
                    }
                    double refundedAmount=theBus.refundTicket(convertedSeat,path);
                    if(refundedAmount!=(double)0){
                           Assistant.writeSeat(path,seatNos);
                            FileOutput.writeToFile(path," of the Voyage "+theBus.getID()+
                                    " from "+theBus.getFrom()+" to "+theBus.getWhere()+" was successfully refunded for "
                                    +String.format("%.2f TL.",refundedAmount),true,true);
                    }

            }
            else if(elements[0].equals("Z_REPORT")){
                FileOutput.writeToFile(path,"Z Report:",true,true);
                FileOutput.writeToFile(path,"----------------",true,true);
                if(buses.length<1){
                    FileOutput.writeToFile(path,"No Voyages Available!",true,true);
                    if(lineNo==content.length){
                        FileOutput.writeToFile(path,"----------------",true,false);
                    }
                    else{
                        FileOutput.writeToFile(path,"----------------",true,true);
                    }
                }
                else{
                    for(Bus theBus:buses){
                        if(theBus!=null){
                            theBus.toString(path);
                            if(lineNo==content.length && theBus==buses[buses.length-1]){
                                FileOutput.writeToFile(path,"----------------",true,false);
                            }
                            else{
                                FileOutput.writeToFile(path,"----------------",true,true);
                            }

                        }
                    }
                }
            }
            else if(elements[0].equals("PRINT_VOYAGE")){
                    Bus theBus=Assistant.findId(Integer.parseInt(elements[1]),buses);
                    if(theBus==null){
                        FileOutput.writeToFile(path,"ERROR: There is no voyage with ID of "+ elements[1]+"!",true,true);
                        continue;
                    }
                    theBus.toString(path);
            }
            else if(elements[0].equals("CANCEL_VOYAGE")){
                int i=0;
                    Bus theBus=Assistant.findId(Integer.parseInt(elements[1]),buses);
                    if(theBus==null){
                        FileOutput.writeToFile(path,"ERROR: There is no voyage with ID of "+elements[1]+"!",true,true);
                        continue;
                    }
                    Bus[] buses2=new Bus[buses.length-1];
                    for(Bus b:buses){
                        if(b!=null && b==theBus){
                            /**
                             * cancelVoyage : makes all refunds and cancels the voyage.
                             */
                            theBus.cancelVoyage();
                            FileOutput.writeToFile(path,"Voyage "+theBus.getID()+" was successfully cancelled!",true,true);
                            FileOutput.writeToFile(path,"Voyage details can be found below:",true,true);
                            theBus.toString(path);
                            theBus=null;
                        }
                        else{
                            buses2[i]=b;
                            i++;
                        }
                    }
                    buses=buses2;}
            else{
                FileOutput.writeToFile(path,"ERROR: There is no command namely "+elements[0]+"!",true,true );
            }
        }
        if(!content[content.length-1].equals("Z_REPORT")){
            FileOutput.writeToFile(path,"Z Report:",true,true);
            FileOutput.writeToFile(path,"----------------",true,true);
            if(buses.length<1){
                FileOutput.writeToFile(path,"No Voyages Available!",true,true);
                FileOutput.writeToFile(path,"----------------",true,false);
            }
            else{
                for(Bus obj:buses){
                    obj.toString(path);
                    if(buses[buses.length-1]==obj){
                        FileOutput.writeToFile(path,"----------------",true,false);
                    }
                    else{
                        FileOutput.writeToFile(path,"----------------",true,true);
                    }
                }

            }
        }



    }}
