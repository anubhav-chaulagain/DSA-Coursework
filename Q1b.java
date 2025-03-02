/*
 *Problem: Find the kth smallest product of pairs from two arrays, nums1 and nums2.

Approach:

Use binary search on the product range [-a * b, a * b], where a and b are the maximum absolute values in nums1 and nums2.

For each midpoint mid, count how many products are ≤ mid using a helper method.

Helper Method:

For each element in nums1:

If x > 0, count elements in nums2 where x * nums2[j] ≤ mid.

If x < 0, count elements in nums2 where x * nums2[j] ≤ mid.

If x == 0, all products are 0 (valid if mid ≥ 0).

Binary Search:

Adjust the search range based on whether the count of products ≤ mid is ≥ k.

Result:

The smallest value l where the count of products ≤ l is at least k is the kth smallest product.

Complexity:
Time: O((m + n) * log(max_product))

Space: O(1)
 */

public class Q1b {
    private int[] nums1; // Declare an array to hold the first set of numbers (nums1)
    private int[] nums2; // Declare an array to hold the second set of numbers (nums2)

    // Method to find the kth smallest product of two numbers from nums1 and nums2
    public long kthSmallestProduct(int[] nums1, int[] nums2, long k) {
        this.nums1 = nums1; // Assign the input array nums1 to the class-level variable
        this.nums2 = nums2; // Assign the input array nums2 to the class-level variable
        
        int m = nums1.length; // Length of nums1
        int n = nums2.length; // Length of nums2
        
        // Calculate the maximum absolute values of nums1 and nums2
        int a = Math.max(Math.abs(nums1[0]), Math.abs(nums1[m - 1]));
        int b = Math.max(Math.abs(nums2[0]), Math.abs(nums2[n - 1]));
        
        // Initialize the range [l, r] for binary search based on the product bounds
        long r = (long) a * b;  // Upper bound for the product
        long l = (long) -a * b; // Lower bound for the product
        
        // Binary search loop to find the kth smallest product
        while (l < r) {
            long mid = (l + r) >> 1;  // Compute the middle point (mid) of the current range
            if (count(mid) >= k) { // If the count of products <= mid is greater than or equal to k
                r = mid; // Narrow the search range to the lower half
            } else {
                l = mid + 1; // Narrow the search range to the upper half
            }
        }
        
        return l; // Return the value of l, which is the kth smallest product
    }

    // Helper method to count how many products are less than or equal to a given value p
    private long count(long p) {
        long cnt = 0; // Variable to hold the count of products less than or equal to p
        int n = nums2.length; // Length of nums2
        
        // Iterate over each element in nums1
        for (int x : nums1) {
            if (x > 0) { // If x is positive, find how many products are less than or equal to p
                int l = 0, r = n;
                while (l < r) {
                    int mid = (l + r) >> 1; // Compute the middle index of nums2
                    if ((long) x * nums2[mid] > p) { // If the product is greater than p
                        r = mid; // Narrow the search range to the left side
                    } else {
                        l = mid + 1; // Otherwise, narrow the search range to the right side
                    }
                }
                cnt += l; // Add the count of valid products for positive x
            } else if (x < 0) { // If x is negative, find how many products are less than or equal to p
                int l = 0, r = n;
                while (l < r) {
                    int mid = (l + r) >> 1; // Compute the middle index of nums2
                    if ((long) x * nums2[mid] <= p) { // If the product is less than or equal to p
                        r = mid; // Narrow the search range to the left side
                    } else {
                        l = mid + 1; // Otherwise, narrow the search range to the right side
                    }
                }
                cnt += n - l; // Add the count of valid products for negative x
            } else if (p >= 0) { // If x is 0, all products are 0, which are valid if p >= 0
                cnt += n; // All elements in nums2 contribute to valid products
            }
        }
        
        return cnt; // Return the total count of valid products
    }

    // Main method to test the functionality of the kthSmallestProduct method
    public static void main(String[] args) {
        Q1b q1b = new Q1b(); // Create an instance of the Q1b class

        // Test Case 1
        
        System.out.println(q1b.kthSmallestProduct(new int[]{2, 5}, new int[]{3, 4}, 2)); // Expected output: 8
        
        // Test Case 2
        System.out.println(q1b.kthSmallestProduct(new int[]{-4, -2, 0, 3}, new int[]{2, 4}, 6)); // Expected output: 0
    }
}
