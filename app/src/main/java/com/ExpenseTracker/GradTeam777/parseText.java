package com.ExpenseTracker.GradTeam777;

/**
 * Created by shreyas on 12/2/16.
 */

public class parseText {
    private String rawText;
    private int total;

    public parseText(String rawText){
        this.rawText = rawText;
        total = -1;
    }

    public int parseRawText(){
        String[] lines = rawText.split("\n"); // split entire raw string line by line
        for(int i=0; i<lines.length; i++){    // iterate through each line
            String[] element = lines[i].split("\\s+");  // split each line based on blank spaces (tabs, spaces etc)
            for(int j=0;j<element.length;j++){
                //element[j].replaceAll()
                if(element[j].equalsIgnoreCase("Total") || element[j].equalsIgnoreCase("SubTotal")){
                    if(element[j+1]=="$"){
                        total = Integer.parseInt(element[j+2]);
                        return total;
                    }
                    else{
                        element[j+1] = element[j+1].substring(1);
                        total = Integer.parseInt(element[j+1]);
                        return total;
                    }
                }
            }
        }
        return total;
    }

}
