
import java.util.*; 

public class Q4b {

    // Method to calculate the minimum roads required to collect all packages
    public static int minRoads(int[] packages, int[][] roads) {
        int n = packages.length; // Number of cities (nodes)

        // Initialize graph with adjacency lists
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>()); // Creating adjacency list for each city
        }

        // Build the graph by adding roads between cities
        for (int[] road : roads) {
            graph.get(road[0]).add(road[1]); // Adding bidirectional edge
            graph.get(road[1]).add(road[0]);
        }

        // Initialize a variable to store the minimum roads count
        int minRoads = Integer.MAX_VALUE;

        // Iterate through each city to start collecting packages
        for (int start = 0; start < n; start++) {
            int[] distances = bfs(graph, start, n); // Get distances from start city using BFS
            Set<Integer> collected = new HashSet<>(); // Set to keep track of collected packages

            // Check cities that can be collected within 2 roads distance
            for (int i = 0; i < n; i++) {
                if (distances[i] <= 2 && packages[i] == 1) {
                    collected.add(i); // Marking package as collected
                }
            }

            // List of cities that are still left for collection
            List<Integer> remaining = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (packages[i] == 1 && !collected.contains(i)) {
                    remaining.add(i);
                }
            }

            // If all packages are collected, no roads are needed
            if (remaining.isEmpty()) {
                minRoads = Math.min(minRoads, 0);
            } else {
                // Try collecting remaining packages with one more road
                for (int next : graph.get(start)) {
                    int roadsUsed = 1; // Starting with the next road
                    Set<Integer> newCollected = new HashSet<>(collected); // Copy collected set
                    int[] nextDistances = bfs(graph, next, n); // Get distances from the next city

                    // Collect packages from cities within 2 roads distance from the next city
                    for (int i = 0; i < n; i++) {
                        if (nextDistances[i] <= 2 && packages[i] == 1) {
                            newCollected.add(i);
                        }
                    }

                    // Check if all remaining packages are collected
                    boolean allCollected = true;
                    for (int i = 0; i < n; i++) {
                        if (packages[i] == 1 && !newCollected.contains(i)) {
                            allCollected = false;
                            break;
                        }
                    }

                    // If all packages are collected, update minimum roads
                    if (allCollected) {
                        roadsUsed += nextDistances[start]; // Add the return road distance
                        minRoads = Math.min(minRoads, roadsUsed);
                    }
                }
            }
        }

        // If no valid solution was found, return -1; otherwise, return the minimum roads used
        return minRoads == Integer.MAX_VALUE ? -1 : minRoads;
    }

    // Helper method for BFS to find distances from the start city
    private static int[] bfs(List<List<Integer>> graph, int start, int n) {
        int[] distances = new int[n]; // Array to store the distances to other cities
        Arrays.fill(distances, -1); // Initialize all distances as -1
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start); // Start BFS from the starting city
        distances[start] = 0; // Distance to itself is 0

        // Perform BFS to calculate distances
        while (!queue.isEmpty()) {
            int curr = queue.poll(); // Get the current city
            for (int neighbor : graph.get(curr)) { // Visit all connected cities (neighbors)
                if (distances[neighbor] == -1) { // If city hasn't been visited
                    distances[neighbor] = distances[curr] + 1; // Update its distance
                    queue.add(neighbor); // Add the city to the queue for further exploration
                }
            }
        }
        return distances; // Return the array of distances from the start city
    }

    // Main method to run tests on the algorithm
    public static void main(String[] args) {
        // Test case 1: 6 cities, some with packages to be collected
        int[] packages1 = { 1, 0, 0, 0, 0, 1 }; // Cities with packages to be collected
        int[][] roads1 = { { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 } }; // Roads connecting cities
        System.out.println("Test 1: " + minRoads(packages1, roads1)); // Expected output: 1

        // Test case 2: 8 cities with a more complex road network and packages
        int[] packages2 = { 0, 0, 0, 1, 1, 0, 0, 1 };
        int[][] roads2 = { { 0, 1 }, { 0, 2 }, { 1, 3 }, { 1, 4 }, { 2, 5 }, { 5, 6 }, { 5, 7 } };
        System.out.println("Test 2: " + minRoads(packages2, roads2)); // Expected output: 2
    }
}

