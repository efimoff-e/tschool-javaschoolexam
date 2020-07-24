package com.tsystems.javaschool.tasks.calculator;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */

    public String evaluate(String statement) {

        if (statement == null || statement.length() == 0)
            return null;

        char[] allSigns = {'+', '-', '/', '*', '(', ')', '.'};
        char[] characters = statement.toCharArray();

        String number = "";
        boolean hasDecimalNumber = false;

        ArrayList<String> allElementsList = new ArrayList<>();

        //write all input Numbers & Signs in allElementList//

        for (char element : characters) {

            boolean inSigns = false;
            for (char symbol : allSigns)
                if (element == symbol) {
                    inSigns = true;
                    break;
                }

            if (inSigns) {
                if (!number.isEmpty()) {
                    if (element == '.') {
                        if (hasDecimalNumber)
                            return null;
                        else {
                            number += element;
                            hasDecimalNumber = true;
                        }
                    } else {
                        allElementsList.add(number);
                        number = "";
                        hasDecimalNumber = false;
                        allElementsList.add(Character.toString(element));
                    }
                } else
                    allElementsList.add(Character.toString(element));
            }

            // sum elements(digits) in number//
            else if (Character.isDigit(element)) {
                number += element;
            } else
                return null;
        }

        if (!number.isEmpty())
            allElementsList.add(number);

        // Create  stacks to hold map with signs & prioritization for signs//
        Stack<Double> numbers = new Stack<>();
        Stack<String> signs = new Stack<>();
        HashMap<String, Integer> signPriority = new HashMap<>();

        signPriority.put("+", 1);
        signPriority.put("-", 1);
        signPriority.put("*", 2);
        signPriority.put("/", 2);

        /* create temporary variables */
        double tempOne;
        double tempTwo;
        String signTemp;

        for (String element : allElementsList) {
            boolean isNumber = Character.isDigit(element.charAt(0));
            if (isNumber) {
                try {
                    numbers.push(Double.parseDouble(element));
                } catch (NumberFormatException e) {
                    return null;
                }
            } else {
                if (!signPriority.containsKey(element)) {

                    if (element.equals(")")) {
                        signTemp = signs.pop();
                        while (!signTemp.equals("(")) {
                            tempOne = numbers.pop();
                            tempTwo = numbers.pop();

                            switch (signTemp) {
                                case ("+"):
                                    numbers.push(tempOne + tempTwo);
                                    break;
                                case ("-"):
                                    numbers.push(tempTwo - tempOne);
                                    break;
                                case ("*"):
                                    numbers.push(tempOne * tempTwo);
                                    break;
                                case ("/"):
                                    if (tempOne == 0)
                                        return null;
                                    else
                                        numbers.push(tempTwo / tempOne);
                                    break;
                            }
                            signTemp = signs.pop();
                        }
                    } else
                        signs.push(element);

                } else {
                    boolean isElementPushed = false;
                    if (!signs.isEmpty()) {
                        while (signPriority.containsKey(signs.lastElement())) {
                            if (signPriority.get(element) <= signPriority.get(signs.lastElement())) {
                                try {
                                    tempOne = numbers.pop();
                                    tempTwo = numbers.pop();
                                    signTemp = signs.pop();
                                    switch (signTemp) {
                                        case ("+"):
                                            numbers.push(tempOne + tempTwo);
                                            break;
                                        case ("-"):
                                            numbers.push(tempTwo - tempOne);
                                            break;
                                        case ("*"):
                                            numbers.push(tempOne * tempTwo);
                                            break;
                                        case ("/"):
                                            if (tempOne == 0)
                                                return null;
                                            else
                                                numbers.push(tempTwo / tempOne);
                                            break;
                                    }
                                } catch (EmptyStackException e) {
                                    return null;
                                }
                            } else {
                                signs.push(element);
                                isElementPushed = true;
                                break;
                            }

                            if (signs.isEmpty())
                                break;
                        }
                    }
                    if (!isElementPushed)
                        signs.push(element);
                }
            }
        }

        //calculations with remaining values in Stack//
        while (!signs.isEmpty()) {
            try {
                signTemp = signs.pop();
                tempOne = numbers.pop();
                tempTwo = numbers.pop();
                switch (signTemp) {
                    case ("+"):
                        numbers.push(tempOne + tempTwo);
                        break;
                    case ("-"):
                        numbers.push(tempTwo - tempOne);
                        break;
                    case ("*"):
                        numbers.push(tempOne * tempTwo);
                        break;
                    case ("/"):
                        if (tempOne == 0)
                            return null;
                        else
                            numbers.push(tempTwo / tempOne);
                        break;
                }
            } catch (EmptyStackException e) {
                return null;
            }
        }

        String result = Double.toString(numbers.pop());

        // If result has no fractional part, then remove it //
        if (result.charAt(result.length() - 1) == '0' && result.charAt(result.length() - 2) == '.')
            result = result.substring(0, result.length() - 2);
        return result;
    }
}
