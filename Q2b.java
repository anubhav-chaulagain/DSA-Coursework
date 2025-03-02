/*
 Problem: Find the closest pair of points based on Manhattan distance. If multiple pairs have the same distance, choose the lexicographically smallest pair.

Approach:

Iterate through all pairs of points (i, j) where i < j.

Calculate the Manhattan distance between each pair.

Track the pair with the smallest distance.

If distances are equal, choose the pair with the smallest lexicographical order.

Result:

Return the indices of the closest pair.
Time: O(n^2) (checking all pairs).

Space: O(1) (no additional space used).
 */
import java.util.*; // Import the Java utilities package for Arrays class

class Q2b {

    // Function to find the closest pair of points based on Manhattan distance
    public static int[] closestLexicographicalPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length; // Total number of points
        int minDistance = Integer.MAX_VALUE; // Start with the largest possible distance
        int[] result = new int[2]; // Array to store the indices of the closest pair

        // Loop through each point as the first point of the pair
        for (int i = 0; i < n; i++) {
            // Loop through the remaining points to form pairs
            for (int j = i + 1; j < n; j++) {
                // Calculate the Manhattan distance between two points
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // If this pair has a smaller distance, update the result
                if (distance < minDistance) {
                    minDistance = distance; // Update the smallest distance found
                    result[0] = i; // Store the first index
                    result[1] = j; // Store the second index
                }
                // If the distance is the same, check which pair is smaller lexicographically
                else if (distance == minDistance) {
                    // Choose the pair that comes first in dictionary order
                    if (i < result[0] || (i == result[0] && j < result[1])) {
                        result[0] = i;
                        result[1] = j;
                    }
                }
            }
        }
        return result; // Return the final closest pair
    }

    public static void main(String[] args) {
        int[] x_coords = {1, 2, 3, 2, 4}; // X positions of points
        int[] y_coords = {2, 3, 1, 2, 3}; // Y positions of points

        // Call the function to get the closest pair
        int[] result = closestLexicographicalPair(x_coords, y_coords);
        // Print the result showing the indices of the closest pair
        System.out.println(Arrays.toString(result)); // Expected output: [0, 3]
    }

}

// Output
// [0, 3]
