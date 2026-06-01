package com.group20.Config;

import java.util.ArrayList;
import java.util.List;

public class SeatLayoutConfig {

    public static List<String> generateSeatNumbers() {
        List<String> seats = new ArrayList<>();

         char[] rows = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' };
        int seatsPerRow = 10;  

        for (char row : rows) {
            for (int seatNumber = 1; seatNumber <= seatsPerRow; seatNumber++) {
                seats.add(row + "-" + seatNumber); 
            }
        }

        return seats;
    }
}
