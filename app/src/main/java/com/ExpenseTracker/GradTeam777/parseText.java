package com.ExpenseTracker.GradTeam777;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by shreyas on 12/2/16.
 */

public class parseText {
    private String rawText;
    private double total;
    String[] trainArray = {"total","subtotal","otal","tal","tot","tota","ota","sub","subt","subto","mom", "amount","mount","ount","amo","amou", "paid","aid","pai","due","amt"};

    //constructor
    public parseText(String rawText){
        this.rawText = rawText;
        total = -1;
    }

    public double parseRawText(){
        String[] lines = rawText.split("\n"); // split entire raw string line by line
        for(int i=0; i<lines.length; i++){    // iterate through each line
            String[] element = lines[i].split("\\s+");  // split each line based on blank spaces (tabs, spaces etc)
            for(int j=0;j<element.length;j++){
                // if element contains TOTAL/SUBTOTAL or subtring of it
                if(contais(element[j])){
                    // if next element is $ then skip and check for element after that and convert to double
                    //if(element[j+1].equals("$") || element[j+1].equals(":")){
                    if(element[j+1].matches("[$&+,:;=?@#|'<>.^*()%!-]")){
                        // if is double then return
                        if(isDouble(element[j+2])){
                            total = Double.parseDouble(element[j+2]);
                            return total;
                        }
                        else {
                            // check if first character is $ and remove it
                            if(element[j+2].substring(0,1).equals("$")){
                                element[j+2] = element[j+2].substring(1);
                            }

                            // check if last character is $ and remove it
                            else if(element[j+2].substring(element[j+2].length() - 1).equals("$"))
                                element[j+2] = element[j+2].substring(0,element[j+2].length() - 1);

                            //after reomving or if didn't find any occurenece, check if double and return it
                            if(isDouble(element[j+2])){
                                total = Double.parseDouble(element[j+2]);
                                return total;
                            }
                        }
                    }
                    // else remove $ from the amount and conver to double
                    else{
                        // check if first character is $ and remove it
                        if(element[j+1].substring(0,1).equals("$"))
                            element[j+1] = element[j+1].substring(1);

                        // check if last character is $ and remove it
                        else if(element[j+1].substring(element[j+1].length() - 1).equals("$"))
                            element[j+1] = element[j+1].substring(0,element[j+1].length() - 1);

                        //after reomving or if didn't find any occurenece, check if double and return it
                        if(isDouble(element[j+1])){
                            total = Double.parseDouble(element[j+1]);
                            return total;
                        }
                    }
                }
            }
        }
        return total;
    }

    // functions to check if str contains TOTAL/SUBTOTAL or subtring of it
    private boolean contais(String str){
        for(int i=0;i<trainArray.length;i++){
            if(str.toLowerCase().contains(trainArray[i].toLowerCase())){
                return true;
            }
        }
        return false;
    }

    // check if the string contains double
    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
