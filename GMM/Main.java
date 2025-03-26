//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String[] products = FileInput.readFile(args[0], false, false);
        String[] purchases = FileInput.readFile(args[1], false, false);
        Slots[][] machine=Process.loadGMM(products,(byte)10,args[2]);
        Process.runGMM(purchases,args[2],machine);





    }
}