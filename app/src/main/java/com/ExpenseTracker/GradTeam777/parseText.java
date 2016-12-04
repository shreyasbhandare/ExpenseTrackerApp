package com.ExpenseTracker.GradTeam777;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by shreyas on 12/2/16.
 */

public class parseText {
    private String rawText;
    private double total;
    private String[] trainArray = {"total","otal","tal","tot","tota","ota","sumom","mom"};
    //private ArrayList<String>subString = new ArrayList<Integer>(Arrays.asList(""));

    public parseText(String rawText){
        this.rawText = rawText;
        total = -1;
    }

    public double parseRawText(){
        String[] lines = rawText.split("\n"); // split entire raw string line by line
        for(int i=0; i<lines.length; i++){    // iterate through each line
            String[] element = lines[i].split("\\s+");  // split each line based on blank spaces (tabs, spaces etc)
            for(int j=0;j<element.length;j++){
                //element[j].replaceAll()
                //if(element[j].equalsIgnoreCase("Total") || element[j].equalsIgnoreCase("SubTotal")){
                if(contais(element[j])){
                    if(element[j+1]=="$"){
                        if(isDouble(element[j+2])){
                            total = Double.parseDouble(element[j+2]);
                            return total;
                        }
                    }
                    else{
                        element[j+1] = element[j+1].substring(1);
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

    private boolean contais(String str){
        for(int i=0;i<trainArray.length;i++){
            if(str.toLowerCase().contains(trainArray[i].toLowerCase())){
                return true;
            }
        }
        return false;
    }

    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
