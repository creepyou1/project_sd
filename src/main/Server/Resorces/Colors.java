package main.Server.Resorces;

public class Colors {
    String RESET = "\u001B[0m";
    String RED_TEXT = "\u001B[31m";
    String GREEN_TEXT = "\u001B[32m";
    String YELLOW_TEXT = "\u001B[33m";
    String BLACK_BG = "\u001B[40m";
    String WHITE_BG = "\u001B[47m";

    public String change_red(){
        return RED_TEXT;
    }
    public String change_green(){
        return GREEN_TEXT;
    }
    public String yellow_red(){
        return YELLOW_TEXT;
    }
    public String change_back(){
        return RESET;
    }
}
