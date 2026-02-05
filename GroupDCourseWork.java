package uk.groupdcoursework;

import java.util.Scanner;

public class GroupDCourseWork {

    public static void main(String[] args) {

        //user should be to input marks out of 100
        Scanner userInput = new Scanner(System.in);

        int student = 1; // initialize the student
        
        //counters for grade from 1 to 9
        int grade1 = 0, grade2 = 0, grade3 = 0, grade4 = 0, grade5 = 0, grade6 = 0, grade7 = 0, grade8 = 0, grade9 = 0;

        while (student <= 5) {
            System.out.print("Enter the students score: ");
            
            int studentScore = userInput.nextInt();

            // Validate input - check if score is greater than 100
            if (studentScore > 100) {
                System.out.println("Error: Marks cannot be greater than 100. Please try again.");
                continue; // Skip the rest of the loop and ask for the same student again
            }
            
            // Also check for negative scores if needed
            if (studentScore < 0) {
                System.out.println("Error: Marks cannot be negative. Please try again.");
                continue;
            }

            int grade = 0;
            String remark = " ";

            //Determine grade based on mark
            if (studentScore >= 80 && studentScore <= 100) {
                grade = 1;
                remark = "D1";
                grade1++;
            } else if (studentScore >= 75 && studentScore <= 79) {
                grade = 2;
                remark = "D2";
                grade2++;
            } else if (studentScore >= 66 && studentScore <= 74) {
                grade = 3;
                remark = "C3";
                grade3++;
            } else if (studentScore >= 60 && studentScore <= 65) {
                grade = 4;
                remark = "C4";
                grade4++;
            } else if (studentScore >= 50 && studentScore <= 59) {
                grade = 5;
                remark = "C5";
                grade5++;
            } else if (studentScore >= 45 && studentScore <= 49) {
                grade = 6;
                remark = "C6";
                grade6++;
            } else if (studentScore >= 35 && studentScore <= 44) {
                grade = 7;
                remark = "P7";
                grade7++;
            } else if (studentScore >= 30 && studentScore <= 34) {
                grade = 8;
                remark = "P8";
                grade8++;
            } else if (studentScore >= 0 && studentScore <= 29) { // Explicitly define the range
                grade = 9;
                remark = "F";
                grade9++;
            }

            System.out.println("The student score is " + studentScore + " and grade is " + grade + " and remark " + remark);
            student++; // Only increment if input was valid
        }
       
        // Optional: Print grade distribution
        System.out.println("\nGrade Distribution:");
        System.out.println("Grade 1 (D1): " + grade1);
        System.out.println("Grade 2 (D2): " + grade2);
        System.out.println("Grade 3 (C3): " + grade3);
        System.out.println("Grade 4 (C4): " + grade4);
        System.out.println("Grade 5 (C5): " + grade5);
        System.out.println("Grade 6 (C6): " + grade6);
        System.out.println("Grade 7 (P7): " + grade7);
        System.out.println("Grade 8 (P8): " + grade8);
        System.out.println("Grade 9 (F): " + grade9);
    }
}