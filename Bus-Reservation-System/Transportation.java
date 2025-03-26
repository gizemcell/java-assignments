public interface Transportation {
    /**
     * @return if returns 0 , it means that the process of refund is failed
     * or the class doesn't give permission to refund.
     * otherwise it returns the loose amount of money from refunding.
     */
    double refundTicket(int[][] convertedSeat,String path);

    /**
     Each subclass contains its own unique object constructor like setting different number of values
     initialize method has "command" argument that includes all information relating object
     */
    void initialize(String[] command);

    /**
     * setInitialState each subclass creates own array holds state of seats (empty or full)
     * because they have distinct structure of array like different column number.
     * it is used just once.Other methods in class change states of the bus.
     */
    void setInitialState();

    /**
     *
     * @param convertedSeat contains rows and columns that is converted from seat numbers
     * @param path to be written file
     * @return if it returns 0 ,it means there is a problem occurs while trying selling ,
     * otherwise it returns amount of money come from this selling process.
     */
    double sellTicket(int[][] convertedSeat,String path);

    /**
     * cancelVoyage: it cancels voyage and refunds all occupied seats.
     * then it changes amount of revenue.
     */
    void cancelVoyage();

}
