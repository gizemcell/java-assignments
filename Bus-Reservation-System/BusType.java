public enum BusType {
    PREMIUM,STANDARD,MINIBUS;
    public static Bus createType(BusType obj){
        if(obj==PREMIUM){
            return new Premium();
        } else if (obj==STANDARD) {
            return new Standard();
        } else{
            return new Minibus();
        }
    }
}
