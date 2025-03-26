import java.io.FileNotFoundException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class BookingSystem {
    public static void main(String[] args){
        if(args.length!=2){
            System.out.println("ERROR: This program works exactly with two command line arguments," +
                    " the first one is the path to the input file whereas the second one is the path to the output file." +
                    " Sample usage can be as follows: \"java8 BookingSystem input.txt output.txt\". " +
                    "Program is going to terminate!");
        }
        else{
            String[] content = FileInput.readFile(args[0], true, true);
            if(content==null){
                System.out.println("ERROR: This program cannot read from the "+args[0]+", " +
                        "either this program does not have read permission to read that file or " +
                    "file does not exist. Program is going to terminate!");
                return;
            }
            FileOutput.writeToFile(args[1],"",false,false);
            Running.transportationDriver(content,args[1]);

    }
    }

}
