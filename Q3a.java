/*
Problem: Find the minimum total cost to connect all devices, where:

Each device has a module cost.

Devices can be connected via connections with associated costs.

Approach:

Using Kruskal's algorithm to find the Minimum Spanning Tree (MST) of the connections.

Adding the cheapest module cost to the MST cost to ensure all devices are connected.

Steps:

Union-Find Data Structure: Used to manage disjoint sets and detect cycles.

Sort Connections: Sort all connections by cost in ascending order.

Build MST:

Iterating through sorted connections.

Adding the connection to the MST if it doesn't form a cycle.

Sum the costs of the selected connections.

Add Module Cost: Add the cost of the cheapest module to the MST cost.

Result:

The minimum total cost to connect all devices.
Complexity:
Time: O(E log E) (sorting connections + Union-Find operations).

Space: O(V) (Union-Find data structure).
 */
import java.util.*; // Importing necessary classes from the Java utility package

public class Q3a { // Define the main class Qno3A
    // Define the Edge class to represent an edge between two devices with a cost
    static class Edge {
        int device1; // Device 1
        int device2; // Device 2
        int cost;    // Cost of connection

        // Constructor to initialize an edge
        public Edge(int device1, int device2, int cost) {
            this.device1 = device1; 
            this.device2 = device2;
            this.cost = cost;
        }
    }

    // Define the UnionFind class for Union-Find data structure
    static class UnionFind {
        int[] parent; // Array to keep track of the parent of each device
        int[] rank;   // Array to keep track of the rank (depth) of each device's tree

        // Constructor to initialize the UnionFind structure
        public UnionFind(int n) {
            parent = new int[n]; 
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; // Initially, each device is its own parent
                rank[i] = 0;    // Initially, all ranks are 0
            }
        }

        // Find method to find the representative (root) of the set containing device u
        public int find(int u) {
            if (parent[u] != u) {
                parent[u] = find(parent[u]); // Path compression: make the tree flat
            }
            return parent[u]; // Return the root of u
        }

        // Union method to join the sets of u and v
        public boolean union(int u, int v) {
            int rootU = find(u); // Find the root of u
            int rootV = find(v); // Find the root of v
            if (rootU != rootV) { // If they are in different sets, union them
                // Union by rank: attach the smaller tree under the larger tree
                if (rank[rootU] > rank[rootV]) {
                    parent[rootV] = rootU; 
                } else if (rank[rootU] < rank[rootV]) {
                    parent[rootU] = rootV;
                } else {
                    parent[rootV] = rootU;
                    rank[rootU]++; // Increase rank if trees are of the same size
                }
                return true; // Successfully unioned
            }
            return false; // They are already in the same set
        }
    }

    // Method to calculate the minimum total cost to connect all devices
    public static int minTotalCost(int n, int[] modules, int[][] connections) {
        List<Edge> edges = new ArrayList<>(); // List to hold all the edges

        // Convert the connections into Edge objects
        for (int[] connection : connections) {
            edges.add(new Edge(connection[0] - 1, connection[1] - 1, connection[2])); // Convert to 0-indexed
        }

        // Sort the edges in increasing order of their cost (for Kruskal's algorithm)
        edges.sort(Comparator.comparingInt(edge -> edge.cost));

        UnionFind uf = new UnionFind(n); // Initialize UnionFind for n devices
        int totalCost = 0; // Variable to track the total cost of the minimum spanning tree (MST)

        // Apply Kruskal's algorithm: process edges in increasing cost order
        for (Edge edge : edges) {
            if (uf.union(edge.device1, edge.device2)) { // If the devices are in different sets, union them
                totalCost += edge.cost; // Add the cost of this edge to the total cost
            }
        }

        // Add the cost of the cheapest module to the total cost
        int modulesCost = Arrays.stream(modules).min().getAsInt(); // Find the minimum cost of modules
        totalCost += modulesCost; // Add the module cost to the total cost

        return totalCost; // Return the total cost
    }

    // Main method to run the program
    public static void main(String[] args) {
        int n = 3; // Number of devices
        int[] modules = {1, 2, 2}; // Cost of modules for each device
        int[][] connections = {{1, 2, 1}, {2, 3, 1}}; // List of connections (device1, device2, cost)

        // Call the minTotalCost method to find the minimum cost to connect all devices
        int result = minTotalCost(n, modules, connections);
        
        // Print the result
        System.out.println("Minimum total cost to connect all devices: " + result); // Expected output: 4
    }
}

// output
// Minimum total cost to connect all devices: 3