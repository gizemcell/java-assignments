public class Process {
    private final int space = 10;
    public static Slots[][] loadGMM(String[] products, byte space,String outputPath) {
        FileOutput.writeToFile(outputPath, "", false, false);
        /**
         * allVisited is created to check whether points visits all the slots of the machine.
         * @param full if full is 1 ,it means machine is full.If full is 2 , it means machine is full
         * ,and it were tried one more product to put.
         */
        boolean allVisited = false;
        Slots[][] machine = new Slots[6][4];
        byte column = 0;
        byte row = 0;
        byte full=0;
        for (String line : products) {
            String[] splitLine = line.split("\t");
            byte track= designSlots(machine, splitLine[0], space, row, column, Float.parseFloat(splitLine[1]), splitLine[2], allVisited);
            if (track == 5) {
                if (column == 3) {
                    column = 0;
                    row = (byte)(row + 1);
                } else {
                    column = (byte)(column + 1);
                }
            }
            else if(track==3){
                FileOutput.writeToFile(outputPath, "INFO: There is no available place to put "+splitLine[0], true, true);
            }
            if (row == 6) {
                allVisited = true;
                row = 5;
                column = 3;
                }
            if (fill(machine) == -1) {
                full++;
                if(full==2){
                FileOutput.writeToFile(outputPath,"INFO: The machine is full!",true,true);
                return machine;
                }
            }
        }
        return machine;
    }


    public static void runGMM(String[] purchases, String outputPath, Slots[][] machine) {
        FileOutput.writeToFile(outputPath,"-----Gym Meal Machine-----",true,true);
        for (Slots[] object : machine) {
            for (byte i = 0; i < 4; i++) {
                if (object[i] != null) {
                    FileOutput.writeToFile(outputPath, object[i].getType() + "(" + Math.round(object[i].totalCalorie()) + ", " + object[i].getAmount() + ")" + "___", true, false);
                } else {
                    FileOutput.writeToFile(outputPath, "___" + "(0, 0)___", true, false);
                }
            }
            FileOutput.writeToFile(outputPath, "", true, true);
        }
        FileOutput.writeToFile(outputPath, "----------", true, true);
        for (String line : purchases) {
            FileOutput.writeToFile(outputPath, "INPUT: " + line, true, true);
            String[] splitted = line.split("\t");
            String[] money = splitted[1].split(" ");

            if(!checkMoney(money)){
                FileOutput.writeToFile(outputPath, "INFO: Invalid money, please try again." , true, true);
                continue;
            }
            int totalMoney = 0;
            for (String mon: money) {
                totalMoney = totalMoney + Integer.parseInt(mon);
            }
            if (splitted[2].equalsIgnoreCase("NUMBER")) {
                if (Integer.parseInt(splitted[3]) > 23 || Integer.parseInt(splitted[3])<0) {
                    FileOutput.writeToFile(outputPath, "INFO: Number cannot be accepted. Please try again with another number.", true, true);
                    FileOutput.writeToFile(outputPath, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                    continue;
                }
                byte[] points = convertion(Byte.parseByte(splitted[3]));
                if (machine[points[0]][points[1]] != null && machine[points[0]][points[1]].getAmount() != 0) {
                    if (machine[points[0]][points[1]].getPrice() <= totalMoney) {
                        float reminder = totalMoney - machine[points[0]][points[1]].getPrice();
                        machine[points[0]][points[1]].setAmount(-1);
                        FileOutput.writeToFile(outputPath, "PURCHASE: You have bought one " + machine[points[0]][points[1]].getType(), true, true);
                        FileOutput.writeToFile(outputPath, "RETURN: Returning your change: " + (int)reminder + " TL", true, true);
                    } else {
                        FileOutput.writeToFile(outputPath, "INFO: Insufficient money, try again with more money.", true, true);
                        FileOutput.writeToFile(outputPath, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                    }
                } else {
                    FileOutput.writeToFile(outputPath, "INFO: This slot is empty, your money will be returned.", true, true);
                    FileOutput.writeToFile(outputPath, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                }
            } else {
                byte[] points = findPoints(machine, splitted[2], Float.parseFloat(splitted[3]));
                if (points[0] == -1) {
                    FileOutput.writeToFile(outputPath, "INFO: Product not found, your money will be returned.", true, true);
                    FileOutput.writeToFile(outputPath, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                } else {
                    if (machine[points[0]][points[1]].getPrice() <= totalMoney) {
                        machine[points[0]][points[1]].setAmount(-1);
                        float reminder = totalMoney - machine[points[0]][points[1]].getPrice();
                        FileOutput.writeToFile(outputPath, "PURCHASE: You have bought one " + machine[points[0]][points[1]].getType(), true, true);
                        FileOutput.writeToFile(outputPath, "RETURN: Returning your change: " + (int)reminder + " TL", true, true);
                    }
                    else{
                        FileOutput.writeToFile(outputPath, "INFO: Insufficient money, try again with more money.", true, true);
                        FileOutput.writeToFile(outputPath, "RETURN: Returning your change: " + totalMoney + " TL", true, true);
                    }
                }
            }
        }
        FileOutput.writeToFile(outputPath,"-----Gym Meal Machine-----",true,true);
        for (Slots[] object : machine) {
            for (int i = 0; i < 4; i++) {
                if (object[i] != null) {
                    if(object[i].getAmount()==0){
                        FileOutput.writeToFile(outputPath, "___" + "(0, 0)___", true, false);
                    }
                    else{
                        FileOutput.writeToFile(outputPath, object[i].getType() + "(" + Math.round(object[i].totalCalorie()) + ", " + object[i].getAmount() + ")" + "___", true, false);
                    }
                } else {
                    FileOutput.writeToFile(outputPath, "___" + "(0, 0)___", true, false);
                }
            }
            FileOutput.writeToFile(outputPath, "", true, true);
        }
        FileOutput.writeToFile(outputPath, "----------", true, true);
    }

    /**
     * designSlots initially look for previous slots whether there is same type with giving name argument.
     * then it chechs space .If those conditionals don't happen it creates new Slots type object.
     * @param allVisited truth of allVisited prevent creating new object on machine.
     * @return 2 means row and column number shouldn't be increased .The product is put one of the earlier slots.
     * @return 3 means the product cannot be replaced.
     */
    public static byte designSlots(Slots[][] machine, String name, byte space, byte row, byte column, float price, String nutrition, boolean allVisited) {
        String[] nutries = nutrition.split(" ");
        for (int i = 0; i < row + 1; i++) {
            for (int j = 0; j < 4; j++) {
                if (machine[i][j] != null && machine[i][j].getType().equalsIgnoreCase(name)) {
                    if (machine[i][j].getAmount() < space) {
                        machine[i][j].setAmount(1);
                        return 2;
                    }
                }
            }
        }
        if (!allVisited) {
            machine[row][column] = new Slots(name, price, Float.parseFloat(nutries[0]), Float.parseFloat(nutries[1]), Float.parseFloat(nutries[2]));
            machine[row][column].setAmount(1);
            return 5;
        }
        return 3;
    }

    public static int fill(Slots[][] machine) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (machine[i][j].getAmount() != 10) {
                    return 0;
                }
            }
        }
        return -1;
    }

    /**
     * findPoints function returns points of row and column relating giving type
     * and considers calorie restriction and existing space
     * I selected byte primitive type cause of limited machine capacity
     */
    public static byte[] findPoints(Slots[][] machine, String type, float calorie) {
        for (byte i = 0; i < 6; i++) {
            for (byte j = 0; j < 4; j++) {
                if (machine[i][j] != null) {
                    if (type.equalsIgnoreCase("protein")) {
                        if (machine[i][j].getProtein() <= calorie + 5 && machine[i][j].getProtein() >= calorie - 5 && machine[i][j].getAmount()>0) {
                            byte[] points = {i, j};
                            return points;
                        }
                    } else if (type.equalsIgnoreCase("carb")) {
                        if (machine[i][j].getCarb() <= calorie + 5 && machine[i][j].getCarb() >= calorie - 5 && machine[i][j].getAmount()>0) {
                            byte[] points = {i, j};
                            return points;
                        }
                    } else if (type.equalsIgnoreCase("fat")) {
                        if (machine[i][j].getFat() <= calorie + 5 && machine[i][j].getFat() >= calorie - 5 && machine[i][j].getAmount()>0) {
                            byte[] points = {i, j};
                            return points;
                        }
                    } else {
                        if (machine[i][j].totalCalorie() <= calorie + 5 && machine[i][j].totalCalorie() >= calorie - 5 && machine[i][j].getAmount()>0) {
                            byte[] points = {i, j};
                            return points;
                        }
                    }
                }
            }
        }
        byte[] points = {-1, -1};
        return points;
    }

    /**
     * convertion function converts slotNumber which is selected by user to row and column form.
     */
    public static byte[] convertion(byte slotNumber) {
        byte column = (byte)(slotNumber % 4);
        byte row = (byte)(slotNumber / 4);
        byte[] points = {row, column};
        return points;
    }
    public static boolean checkMoney(String[] money){
        for(String mon:money){
            int moni=Integer.parseInt(mon);
            if(moni==1 || moni==5 || moni==10 || moni==20 || moni==50 || moni==100 || moni==200){
            }
            else{
                return false;
            }
        }
        return true;
    }

}
