package ui;

/* This is the Main class of the program */


import model.Aircraft;

public class Main {

    public static void main(String[] args) {
        Aircraft ac = new Aircraft("Jessica");
        ac.printSeats();
        new MainDashboard();
    }



}
