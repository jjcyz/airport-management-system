package ui;

import model.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome to XYZ Airport");
            System.out.println("Navigate by selecting the number");
            System.out.println("1 Add passenger info");
            System.out.println("2 Add plane info");
            System.out.println("3 Add flight info");
            Scanner scanner = new Scanner(System.in);
            int select = scanner.nextInt();

            if (select == 1) {
                System.out.println("Please enter the following:");
                System.out.println("Passenger ID: ");
                int id = scanner.nextInt();
                System.out.println("First Name: ");
                String fName = scanner.next();
                System.out.println("Last Name: ");
                String lName = scanner.next();
                Passenger p = new Passenger(id, fName, lName, TravelClasses.BUSINESSCLASS);
            }

        }






    }
}
