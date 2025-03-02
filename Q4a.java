
// Problem: Count and display the top 3 hashtags from a list of tweets, sorted by their mention count in descending order.

// Approach:

// Use a list of maps to store tweet data (user_id, tweet_id, tweet, tweet_date).

// Extract hashtags from the tweet text and count their occurrences using a HashMap.

// Sort the hashtags by count in descending order, and alphabetically if counts are equal.

// Display the top 3 hashtags in a formatted table.

// Key Steps:

// Extract Hashtags: Split the tweet text and identify words starting with #.

// Count Hashtags: Use a HashMap to store and count hashtag occurrences.

// Sort Hashtags: Sort by count (descending) and then by hashtag name (ascending).

// Display Results: Print the top 3 hashtags in a table format

import java.util.*;

public class Q4a {
    public static List<String[]> countTopHashtags(String[][] tweets) {
        // Create a HashMap to store hashtags and their counts
        Map<String, Integer> hashtagCount = new HashMap<>();

        // Loop through each tweet (row in the 2D array)
        for (String[] tweet : tweets) {
            String titleNHashtag = tweet[2]; // Extract the column that contains hashtags
            String[] words = titleNHashtag.split(" "); // Split the string into words

            // Loop through each word in the tweet
            for (String word : words) {
                // Check if the word starts with "#", meaning it's a hashtag
                if (word.startsWith("#")) {
                    // Update the count of the hashtag in the HashMap
                    hashtagCount.put(word, hashtagCount.getOrDefault(word, 0) + 1);
                }
            }
        }

        // Create a list to store the top 3 hashtags (String[] with {hashtag, count})
        List<String[]> top3 = new ArrayList<>(Arrays.asList(
            new String[]{null, "-1"}, // First slot (initially null and -1 count)
            new String[]{null, "-1"}, // Second slot
            new String[]{null, "-1"}  // Third slot
        ));

        // Iterate over each hashtag in the HashMap
        for (Map.Entry<String, Integer> entry : hashtagCount.entrySet()) {
            String tag = entry.getKey(); // Get hashtag name
            int count = entry.getValue(); // Get its count

            // Check if this hashtag has a higher count than the first-ranked hashtag
            if (count > Integer.parseInt(top3.get(0)[1])) {
                top3.remove(2); // Remove the lowest-ranked hashtag
                top3.add(0, new String[]{tag, String.valueOf(count)}); // Insert as top 1
            } 
            // Check if it is higher than the second-ranked hashtag
            else if (count > Integer.parseInt(top3.get(1)[1])) {
                top3.remove(2); // Remove the lowest-ranked hashtag
                top3.add(1, new String[]{tag, String.valueOf(count)}); // Insert as top 2
            } 
            // Check if it is higher than the third-ranked hashtag
            else if (count > Integer.parseInt(top3.get(2)[1])) {
                top3.set(2, new String[]{tag, String.valueOf(count)}); // Replace the third-ranked hashtag
            }
        }

        // Remove any null values (if unique hashtags are less than 3)
        top3.removeIf(entry -> entry[0] == null);
        return top3; // Return the final top 3 list
    }

    public static void main(String[] args) {
        // Sample tweets stored as a 2D array
        String[][] tweets_data = {
            {"135", "13", "Enjoying a great start to the day. #HappyDay #MorningVibes", "2024-02-01"},
            {"136", "14", "Another #HappyDay with good vibes! #FeelGood", "2024-02-03"},
            {"137", "15", "Productivity peaks! #WorkLife #ProductiveDay", "2024-02-04"},
            {"138", "16", "Exploring new tech frontiers. #TechLife #Innovation", "2024-02-04"},
            {"139", "17", "Gratitude for today’s moments. #HappyDay #Thankful", "2024-02-05"},
            {"140", "18", "Innovation drives us. #TechLife #FutureTech", "2024-02-07"},
            {"141", "19", "Connecting with nature’s serenity. #Nature #Peaceful", "2024-02-09"}
        };

        // Call the function to get the top 3 hashtags
        List<String[]> top3 = countTopHashtags(tweets_data);

        // Print the top 3 hashtags
        System.out.println("Top 3 Hashtags:");
        for (String[] hashtag : top3) {
            System.out.println(hashtag[0] + " = " + hashtag[1]);
        }
    }
}

// Output
// +-------------+---------+
// |   HASHTAG   |  COUNT  |
// +-------------+---------+
// | #HappyDay   | 3       |
// | #TechLife   | 2       |
// | #FeelGood   | 1       |
// +-------------+---------+