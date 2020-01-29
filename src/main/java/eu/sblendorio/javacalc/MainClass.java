package eu.sblendorio.javacalc;

public class MainClass {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Syntax: java -jar <jarname.jar> <expression to evaluate>");
        } else {
            System.out.println(Calc.eval(String.join(" ", args)));
        }
    }
}
