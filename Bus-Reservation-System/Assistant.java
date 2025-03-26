public class Assistant {
    public static Bus[] addArray(Bus obj,Bus[] listOfBus,String path){
        Bus[] buses =new Bus[listOfBus.length+1];
        int i=0;
        boolean added=true;
        if(obj.getID()<1 ){
            FileOutput.writeToFile(path,"ERROR: "+obj.getID()+" is not a positive integer, ID of a voyage must be a positive integer!",true,true);
            return listOfBus;
        }
        for(Bus element:listOfBus){
            if(element!=null && obj.getID()==element.getID()){
                FileOutput.writeToFile(path,"ERROR: There is already a voyage with ID of "+obj.getID()+"!",true,true);
                return listOfBus;
            }
            else if(element!=null && obj.getID()<element.getID() && added){
                buses[i]=obj;
                added=false;
                i++;
            }
            if(element!=null){
                buses[i]=element;
                i++;
            }
        }
        if(added){
            buses[i]=obj;
        }
        return buses;
    }
    public static Bus findId(int iD,Bus[] list){
        for(Bus obj:list){
            if(obj.getID()==iD){
                return obj;
            }
        }
        return null;
    }
    public static int[][] oneToTwo(String[] seats, Bus obj){
        int[][] rowColumns=new int[seats.length][2];
        int i=0;
        for(String sN:seats){
            rowColumns[i]=obj.convertIndex(Integer.parseInt(sN));
            if(rowColumns[i][0]>obj.getTotalRow()-1) {
                return null;
            }
            i++;
            }
        return rowColumns;
    }

    /**
     *usageControl checks the correctness of the format of the given command.
     * if returns true, the command line passes all controls.
     */
    public static boolean usageControl(String[] command,String path){
        boolean succesion=true;
        if(command[0].equals("INIT_VOYAGE")){
            if(command[1].equals("Minibus")){
                if(command.length !=7){
                    FileOutput.writeToFile(path,"ERROR: Erroneous usage of \"INIT_VOYAGE\" command!",true,true);
                    succesion=false;
                }
                else if(!continuousId(command[2]) || Integer.parseInt(command[2])<1){
                    FileOutput.writeToFile(path,"ERROR: "+command[2]+" is not a positive integer, ID of a voyage must be a positive integer!",true,true);
                    succesion=false;
                }
                else if(!isString(command[3]) || !isString(command[4])){
                    FileOutput.writeToFile(path,"ERROR: Erroneous usage of \"INIT_VOYAGE\" command!",true,true);
                    succesion=false;
                }
                else if(Integer.parseInt(command[5])<1){
                    FileOutput.writeToFile(path,"ERROR: "+command[5]+" is not a positive integer, number of seat rows of a voyage must be a positive integer!",true,true);
                    succesion=false;
                }
                else if(Float.parseFloat(command[6])<1){
                    FileOutput.writeToFile(path,"ERROR: "+command[6]+" is not a positive number, price must be a positive number!",true,true);
                    succesion=false;

                }
            }
            else if(command[1].equals("Standard")){
                if(command.length !=8){
                    FileOutput.writeToFile(path,"ERROR: Erroneous usage of \"INIT_VOYAGE\" command!",true,true);
                    succesion=false;
                }
                else if(!continuousId(command[2]) || Integer.parseInt(command[2])<1){
                    FileOutput.writeToFile(path,"ERROR: "+command[2]+" is not a positive integer, ID of a voyage must be a positive integer!",true,true);
                    succesion=false;
                }
                else if(!isString(command[3]) || !isString(command[4])){
                    FileOutput.writeToFile(path,"ERROR: Erroneous usage of \"INIT_VOYAGE\" command!",true,true);
                    succesion=false;
                }
                else if(Integer.parseInt(command[5])<1){
                    FileOutput.writeToFile(path,"ERROR: "+command[5]+" is not a positive integer, number of seat rows of a voyage must be a positive integer!",true,true);
                    succesion=false;
                }
                else if(Float.parseFloat(command[6])<1){
                    FileOutput.writeToFile(path,"ERROR: "+command[6]+" is not a positive number, price must be a positive number!",true,true);
                    succesion=false;
                }
                else if(command[2].contains(".") || Integer.parseInt(command[7])<0 || Integer.parseInt(command[7])>100){
                    FileOutput.writeToFile(path,"ERROR: "+command[6]+" is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!",true,true);
                    succesion=false;
                }


            }
            else if(command[1].equals("Premium")){
                if(command.length !=9){
                    FileOutput.writeToFile(path,"ERROR: Erroneous usage of \"INIT_VOYAGE\" command!",true,true);
                    succesion=false;
                }
                else if(!continuousId(command[2]) || Integer.parseInt(command[2])<1){
                    FileOutput.writeToFile(path,"ERROR: "+command[2]+" is not a positive integer, ID of a voyage must be a positive integer!",true,true);
                    succesion=false;
                }
                else if(!isString(command[3]) || !isString(command[4])){
                    FileOutput.writeToFile(path,"ERROR: Erroneous usage of \"INIT_VOYAGE\" command!",true,true);
                    succesion=false;
                }
                else if(Integer.parseInt(command[5])<1){
                    FileOutput.writeToFile(path,"ERROR: "+command[5]+" is not a positive integer, number of seat rows of a voyage must be a positive integer!",true,true);
                    succesion=false;
                }
                else if(Float.parseFloat(command[6])<1){
                    FileOutput.writeToFile(path,"ERROR: "+command[6]+" is not a positive number, price must be a positive number!",true,true);
                    succesion=false;
                }
                else if(command[2].contains(".") || Integer.parseInt(command[7])<0 || Integer.parseInt(command[7])>100){
                    FileOutput.writeToFile(path,"ERROR: "+command[7]+" is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!",true,true);
                    succesion=false;
                }
                else if(Integer.parseInt(command[8])<0){
                    FileOutput.writeToFile(path,"ERROR: "+command[8]+" is not a non-negative integer, premium fee must be a non-negative integer!",true,true);
                    succesion=false;
                }
            }
            else{
                FileOutput.writeToFile(path,"ERROR: Erroneous usage of \"INIT_VOYAGE\" command!",true,true);
                succesion=false;
            }

        }
        else if(command[0].equals("REFUND_TICKET")){
            if(command.length<3){
                FileOutput.writeToFile(path,"ERROR: Erroneous usage of \"REFUND_TICKET\" command!",true,true);
                succesion=false;
            }
            else if(command[1].contains(".") || Integer.parseInt(command[1])<1){
                FileOutput.writeToFile(path,"ERROR: "+command[1]+" is not a positive integer, ID of a voyage must be a positive integer!",true,true);
                succesion=false;
            }
            else if(!isPositiveInteger(command[2],path)){
                succesion=false;
            }

        }
        else if(command[0].equals("Z_REPORT")){
            if(command.length>1){
                FileOutput.writeToFile(path,"ERROR: Erroneous usage of \"Z_REPORT\" command!",true,true);
                succesion=false;
            }

        }
        else if(command[0].equals("PRINT_VOYAGE")){
            if(command.length!=2){
                FileOutput.writeToFile(path,"ERROR: Erroneous usage of \"PRINT_VOYAGE\" command!",true,true);
                succesion=false;
            }
            else if(!continuousId(command[1]) || Integer.parseInt(command[1])<1){
                FileOutput.writeToFile(path,"ERROR: "+command[1]+" is not a positive integer, ID of a voyage must be a positive integer!",true,true);
                succesion=false;
            }
        }
        else if(command[0].equals("CANCEL_VOYAGE")){
            if(command.length!=2){
                FileOutput.writeToFile(path,"ERROR: Erroneous usage of \"CANCEL_VOYAGE\" command!",true,true);
                succesion=false;
            }
            else if(!continuousId(command[1]) || Integer.parseInt(command[1])<1){
                FileOutput.writeToFile(path,"ERROR: "+command[1]+" is not a positive integer, ID of a voyage must be a positive integer!",true,true);
                succesion=false;
            }
        }
        else if(command[0].equals("SELL_TICKET")){
            if(command.length<3){
                FileOutput.writeToFile(path,"ERROR: Erroneous usage of \"SELL_TICKET\" command!",true,true);
                succesion=false;}
            else if(!continuousId(command[1]) || Integer.parseInt(command[1])<1){
                FileOutput.writeToFile(path,"ERROR: "+command[1]+" is not a positive integer, ID of a voyage must be a positive integer!",true,true);
                succesion=false;
            }
            else if(!isPositiveInteger(command[2],path)){
                succesion=false;
            }
        }
        return succesion;
    }

    /**
     *isString : it is created for the string completely made by letters.
     * returns true(it is a string) or false(it contains some other characters)
     */
    private static boolean isString(String s){
        String[] arr=s.split(" ");
        for(String str:arr){
            char[] chars=str.toCharArray();
            for(char c:chars){
                if(Character.isLetter(c)){
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isPositiveInteger(String seat,String path){
        String[] seats=seat.split("_");
        for(int i=0;i<seats.length;i++){
            if(seats[i].contains(".") || Integer.parseInt(seats[i])<1){
                FileOutput.writeToFile(path,"ERROR: "+seats[i]+" is not a positive integer, seat number must be a positive integer!",true,true);
                return false;
            }
        }
        return true;
    }
    private static boolean continuousId(String s){
        char[] chars=s.toCharArray();
        for(char c:chars){
           if(c>='0' && c<='9'){
           } else{
               return false;
           }
        }
        return true;
    }
    public static void writeSeat(String path,String[] seats){
        FileOutput.writeToFile(path, "Seat ",true,false);
        if(seats.length==1){
            FileOutput.writeToFile(path,seats[0],true,false);
        }
        else{
            FileOutput.writeToFile(path,seats[0],true,false);
            for(int i=1;i<seats.length;i++){
                FileOutput.writeToFile(path,"-"+seats[i],true,false);
            }

        }
    }


}
