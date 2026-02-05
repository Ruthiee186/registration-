/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.groupdcoursework;
import java.util.Scanner; // 

public class AuctionSystem {
    
    public static void main(String[] arg){
        
        // 1: System to accept details of the vehicle
        // 2: Accet 3 bidders and their prices.
        // 3: Pick the highest bidder.
        // 4: Accept deposts and expenses.
        // 5: get calculations
        
        //scanner input to allow the user input details for the auction
        Scanner input = new Scanner(System.in);
        
        //need to capture vehicle details
        System.out.print("Enter vehicle registration number:  ");
        String regNo = input.nextLine();
        
        System.out.print("Enter vehicle cost: ");
        double vehicleCost = input.nextDouble(); 
        
        // Bidders
        System.out.print("Enter Bidder 1 price: ");
        double bid1 = input.nextDouble();
        
        System.out.print("Enter Bidder 2 price: ");
        double bid2 = input.nextDouble();
        
        System.out.print("Enter Bidder 3 price: ");
        double bid3 = input.nextDouble();
        
        //Find the highest bidder, (variabl highestBid stores the the higest bidder)
        double highestBid = bid1;

        if (bid2 > highestBid)
            highestBid = bid2;

        if (bid3 > highestBid)
            highestBid = bid3;
        
         // Getting deposits and expenses
        System.out.println("Enter total deposits made: ");
        double deposits = input.nextDouble();

        System.out.println("Enter expenses incurred: ");
        double expenses = input.nextDouble();
        
        
        // Getting the Balance
        double balance = highestBid - deposits;
        
        //  Capture the Profit or Loss
        double profitLoss = highestBid - (vehicleCost + expenses);
        
        // Output
        System.out.println("\nVehicle Registration: " + regNo);
        System.out.println("Highest Bid: " + highestBid);
        System.out.println("Balance Left: " + balance);
        
         if (profitLoss > 0) {
            System.out.println("Profit Made: " + profitLoss);
        } else {
            System.out.println("Loss Made: " + profitLoss);
        }
         
        
    }
    
}