/*
    You have a team of n employees, and each employee is assigned a performance rating given in the
    integer array ratings. You want to assign rewards to these employees based on the following rules:
    Every employee must receive at least one reward.
    Employees with a higher rating must receive more rewards than their adjacent colleagues.
    Goal:
    Determine the minimum number of rewards you need to distribute to the employees.
    Input:
    ratings: The array of employee performance ratings.
    Output:
    The minimum number of rewards needed to distribute. 
*/
/*
    Approach:

    Initialization: Create an array rewards of the same length as ratings, initialized to 1 since each employee must receive at least one reward.

    Left-to-Right Pass:

    Traverse the ratings array from left to right.
    If an employee has a higher rating than their left neighbor, they receive more rewards than the left neighbor.
    Right-to-Left Pass:

    Traverse the ratings array from right to left.
    If an employee has a higher rating than their right neighbor, update their reward to be the maximum of its current value and one more than the right neighbor.
    Result Calculation: Sum all values in the rewards array to get the minimum total rewards required.

    Complexity:

    Time Complexity: O(n) – We traverse the array twice.
    Space Complexity: O(n) – We use an additional array to store rewards.
 */

public class Q2a {
    // Function to calculate the minimum rewards required based on ratings
    public static int minRewards(int[] ratings) {
        // Edge case: If ratings array is null or empty, return 0
        if (ratings == null || ratings.length == 0) {
            return 0;
        }

        int n = ratings.length; // Store the number of employees
        int[] rewards = new int[n]; // Array to keep track of rewards for each employee

        /* Step 1: Initialize all rewards to 1
        Each employee must get at least one reward */
        for (int i = 0; i < n; i++) {
            rewards[i] = 1;
        }

        /* Step 2: Traverse from left to right
        If an employee has a higher rating than the previous one,
        they should get more rewards than the previous employee */
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }

        /* Step 3: Traverse from right to left
        If an employee has a higher rating than the next one,
        they should get more rewards than the next employee
        However, we take the max because the left-to-right pass already assigned rewards */
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }

        // Step 4: Calculate the total minimum rewards needed
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward; // Add up all rewards
        }

        return totalRewards; // Return the final result
    }

    public static void main(String[] args) {
        // Test Case 1
        int[] ratings1 = { 1, 0, 2 };
        System.out.println(minRewards(ratings1)); // Expected output: 5

        // Test Case 2
        int[] ratings2 = { 1, 2, 2 };
        System.out.println(minRewards(ratings2)); // Expected output: 4
    }
}

/*
    Example 1:
Input: ratings = [1, 0, 2]
Output: 5
Explanation: You can allocate rewards as follows:
- First employee: 2 rewards
- Second employee: 1 reward
- Third employee: 2 rewards
Total rewards required = 5

Example 2:
Input: ratings = [1, 2, 2]
Output: 4
Explanation: You can allocate rewards as follows:
- First employee: 1 reward
- Second employee: 2 rewards
- Third employee: 1 reward
The third employee gets 1 reward because it satisfies both conditions.
Total rewards required = 4
 */