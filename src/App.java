public class App {
    public static void main(String[] args) throws Exception {
        String str = System.console().readLine("Enter string : ");
        Evaluator evaluator = new Evaluator();
        System.out.println(evaluator.evaluate(str));
    }
}
