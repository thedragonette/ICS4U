package Unit2.Assignment;

import java.util.*;

import static Unit2.Assignment.DijkstraShuntingYard.stringToOperator;

/**********************************************************
 * Program	:  Calculator
 * Author	:  Braydn Moore
 * Due Date	:  October 13th 2016
 * Description	: A calculator that can't do linear equations :(
 ***********************************************************/
public class Main {

    // various objects needed to evaluate the equation
    public static Scanner scanner = new Scanner(System.in);
    public static DijkstraShuntingYard shuntingYard = new DijkstraShuntingYard();
    public static Postfix postfix = new Postfix(stringToOperator);
    public static Map<String, Integer> userVariables = new HashMap<>();


    // checks if number is numeric
    public static boolean isNumeric(String string) {
        // attempts to convert the input to an int, if it recieves and error then it is not numeric and return false
        try
        {int integer = Integer.parseInt(string);}
        catch(NumberFormatException nfe)
        {return false;}
        return true;
    }

    // accepts a string
    public static String acceptString(String message){
        // accepts a string
        String input;
        System.out.print(message);
        input = scanner.nextLine();
        return input;
    }

    // accepts numbers with a min and max range
    public static int acceptNumber(String message, int min, int max){
        String input;
        while (true) {
            // prints the message
            System.out.print(message);
            input = scanner.nextLine();
            // if the input is not a number warn the user and ask again
            if (!isNumeric(input))
                System.out.println("You must enter a number!");
                // if there is not max then just return the number otherwise check that it is
            else if (max!=0 || Integer.parseInt(input)<min)
                if (Integer.parseInt(input)>max)
                    System.out.println("You must enter a number in between "+min+" and "+max);
                    // if the previous if statement returns true and max equals zero the input must be less than the min input
                else if (max == 0)
                    System.out.println("You must enter a number greater than 1");
                else
                    break;
            else
                break;
        }
        return Integer.parseInt(input);
    }

    // cleans up the users equation removing letters, adding spaces, etc.
    public static String equationCleanup(String equation, boolean userVar){
        // allows the user to enter an equation where a bracket next to a number represents multiplication
        equation = equation.replace("(", "*(").replace("**(", "*(");

        // places spaces inbetween operations to optimize for the shunting yard algorithm
        equation = equation.trim().replaceAll(" +", " ");
        for (String operation : stringToOperator.keySet())
            equation = equation.replace(operation, " "+operation+" ");


        // removes all extra spaces
        equation = equation.replace("   ", " ");
        equation = equation.replace("  ", " ");

        // acceptable characters in an equation
        ArrayList<String> acceptableSymbols = new ArrayList<String>(){{
            add("*");
            add("+");
            add("-");
            add("/");
            add("(");
            add(")");
            add("^");
            add(" ");
            add("1");
            add("2");
            add("3");
            add("4");
            add("5");
            add("6");
            add("7");
            add("8");
            add("9");
            add("0");
        }};


        // utilizes the user defined variables in the equation
        if (userVar)
            for (String variable : userVariables.keySet())
                equation = equation.replace(variable, Integer.toString(userVariables.get(variable)));

        // for each char in the string if it is not an acceptable char then remove it
        for (String letter : equation.split(""))
            if (!acceptableSymbols.contains(letter))
                equation = equation.replace(letter,"");

        // return the equation
        return equation;
    }

    // solves the equation
    public static Double solveEquation(String equation, boolean userVar){
        // cleans up the equation
        equation = equationCleanup(equation, userVar);
        // returns the postfix evaluation of the dijkstra shunting yard evaluation
        return postfix.evaluate(shuntingYard.getPostfix(equation));
    }

    // returns true or false depending on if the expression is valid
    public static boolean doesStringHaveKeyInMap(Map<String, DijkstraShuntingYard.OPERATOR> map, String string){
        for (String key : map.keySet())
            if (string.contains(key))
                return true;

        return false;
    }

    // standard evaluation
    public static void standard(){
        // accept equations until the user exits the game mode
        while (true){
            // accept the equation
            String equation = acceptString(">>>");
            // exit if the user wishes
            if (equation.equalsIgnoreCase("quit"))
                return;

            // if the equation ends in a number and contains and equals sign assume that they are setting a user variable
            if (Character.isDigit(equation.charAt(equation.length()-1)) && equation.indexOf("=")!=-1) {
                for (String variable : userVariables.keySet())
                    equation = equation.replace(variable, Integer.toString(userVariables.get(variable)));
                // set user variable
                if (userVariables.containsKey(equation.split("=")[0]))
                    userVariables.replace(equation.split("=")[0], Integer.parseInt(equation.split("=")[1]));
                else
                    userVariables.put(equation.split("=")[0], Integer.parseInt(equation.split("=")[1]));
            }
            // if the user is not setting a variable evaluate the equation
            else
                System.out.printf("%s%s\n", equationCleanup(equation.replace("=",""), true).replace(" ",""), solveEquation(equation, true)==null?"":"="+solveEquation(equation, true));
        }
    }

    // checker code the evaluate equation
    public static void checker(){
        // runs while trye
        String equation;
        while (true){
            // runs until the user enters a valid equation
            while(true) {
                equation = acceptString(">>>");
                if (equation.equalsIgnoreCase("quit"))
                    return;
                else if (equation.contains("="))
                    break;
            }

            // if the equation ends in a number and contains and equals sign assume that they are setting a user variable
            if (Character.isDigit(equation.charAt(equation.length()-1)) && equation.indexOf("=")!=-1 && !doesStringHaveKeyInMap(stringToOperator, equation)) {
                for (String variable : userVariables.keySet())
                    equation = equation.replace(variable, Integer.toString(userVariables.get(variable)));
                // set user variable
                if (userVariables.containsKey(equation.split("=")[0]))
                    userVariables.replace(equation.split("=")[0], Integer.parseInt(equation.split("=")[1]));
                else
                    userVariables.put(equation.split("=")[0], Integer.parseInt(equation.split("=")[1]));
            }
            // if the equation solution matches the assumed value print true
            else if (solveEquation(equation.split("=")[0], true).equals(solveEquation(equation.split("=")[1], true)))
                System.out.println("true");
            // otherwise print false
            else
                System.out.println("false");

        }
    }

    // solve python
    public static HashMap<String, Double> solvePyth() {
        HashMap<String,Double> answer = new HashMap<>();
        // all possible configurations of given angles
        if (userVariables.containsKey("a") && userVariables.containsKey("b")) {
            answer.put("c", Math.pow(userVariables.get("a"), 2) + Math.pow(userVariables.get("b"), 2));
            userVariables.remove("a");
            userVariables.remove("b");
        }
        else if (userVariables.containsKey("a") && userVariables.containsKey("c")) {
            answer.put("b", Math.pow(userVariables.get("c"), 2) - Math.pow(userVariables.get("a"), 2));
            userVariables.remove("a");
            userVariables.remove("c");
        }
        else {
            answer.put("a", Math.pow(userVariables.get("c"), 2) - Math.pow(userVariables.get("b"), 2));
            userVariables.remove("c");
            userVariables.remove("b");
        }
        // return a hashmap with the answer and variable is solved for
        return answer;
    }

    // pythagorean theorum
    public static void pythagorean(){
        String equation;
        HashMap<String, Double> answer;
            // runs until the user wishes to quit
            while (true) {
                // accepts the equation
                equation = acceptString(">>>");
                // quits if the user wishes
                if (equation.equalsIgnoreCase("quit"))
                    return;
                // shows the help
                else if (equation.equalsIgnoreCase("help"))
                    System.out.println("Set the two variables eg. a=12 or c=15. Then enter solve");
                // solve if the user wishes
                else if (equation.equalsIgnoreCase("solve")) {
                    // number of variables the user has entered
                    int numGivenVariables = 0;
                    // for each line length that could be entered in the a right angle triangle add one to the number of angles given
                    for (String item : new String[]{"a", "b", "c"})
                        if (userVariables.containsKey(item))
                            numGivenVariables++;

                    // if the user has provided two lengths solve them
                    if (numGivenVariables==2) {
                        answer = solvePyth();
                        // print out the answer
                        for (String varToSolve:answer.keySet())
                            System.out.printf("%s^2=%.2f\n%1$s=%.2f\n", varToSolve, answer.get(varToSolve), Math.sqrt(answer.get(varToSolve)));
                    }
                    // otherwise the user has not entered the needed variables
                    else if (numGivenVariables>2)
                        System.out.println("You have all the variables needed!");
                    else
                        System.out.println("You didn't give me enough info");
                }
                // if the user is setting their own variables
                else  if (Character.isDigit(equation.charAt(equation.length()-1)) && equation.indexOf("=")!=-1 && !doesStringHaveKeyInMap(stringToOperator, equation)) {
                    // replace the needed variables
                    for (String variable : userVariables.keySet())
                        equation = equation.replace(variable, Integer.toString(userVariables.get(variable)));
                    // set user variable
                    if (userVariables.containsKey(equation.split("=")[0]))
                        userVariables.replace(equation.split("=")[0], Integer.parseInt(equation.split("=")[1]));
                    else
                        userVariables.put(equation.split("=")[0], Integer.parseInt(equation.split("=")[1]));
                }
            }
    }

    // main program
    public static void main(){
        // runs until the user enters 0 as their choice
        while (true){
            // clears the variables defined by the user
            userVariables.clear();
            // enters the mode the user chooses
            int mode = acceptNumber("MENU\n***********************\n1) Standard Calculator\n2) Checker\n3) Pythagorean\n0) Exit\n***********************\nChoice: ", 1,3);

            switch (mode){

                case 0:
                    return;
                case 1:
                    standard();
                    break;
                case 2:
                    checker();
                    break;
                case 3:
                    pythagorean();
                    break;
            }
        }
    }

    public static void main(String[] args){
        // calls main
        main();
    }


}
