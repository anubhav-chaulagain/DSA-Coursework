/*
    Problem:
    You need to crawl a large number of web pages to gather data or index content. Crawling each page
    sequentially can be time-consuming and inefficient.
    Goal:
    Create a web crawler application that can crawl multiple web pages concurrently using multithreading to
    improve performance.
    Tasks:
    Design the application:
    Create a data structure to store the URLs to be crawled.
    Implement a mechanism to fetch web pages asynchronously.
    Design a data storage mechanism to save the crawled data.
    Create a thread pool:
    Use the ExecutorService class to create a thread pool for managing multiple threads.
    Submit tasks:
    For each URL to be crawled, create a task (e.g., a Runnable or Callable object) that fetches the web page
    and processes the content.
    Submit these tasks to the thread pool for execution.
    Handle responses:
    Process the fetched web pages, extracting relevant data or indexing the content.
    Handle errors or exceptions that may occur during the crawling process.
    Manage the crawling queue:
    Implement a mechanism to manage the queue of URLs to be crawled, such as a priority queue or a
    breadth-first search algorithm.
    By completing these tasks, you will create a multithreaded web crawler that can efficiently crawl large
    numbers of web page

 */

 import java.awt.*;
 import java.io.BufferedReader;
 import java.io.InputStreamReader;
 import java.net.HttpURLConnection;
 import java.net.URL;
 import java.util.*;
 import java.util.List;
 import java.util.concurrent.*;
 import javax.swing.*;
 
 public class Q6b {
 
     private static Queue<String> urlQueue = new LinkedList<>();
     private static List<String> crawledData = new ArrayList<>();
     private static ExecutorService executorService;
     private static JTextArea outputArea;
     private static JTextField urlField;
     private static JButton startButton, addButton, pauseButton, resumeButton, stopButton;
     private static JProgressBar progressBar;
     private static int totalUrls = 0;
     private static volatile boolean isPaused = false; // Flag to control pause/resume
     private static volatile boolean isStopped = false; // Flag to control stop
     private static List<Future<?>> activeTasks = new ArrayList<>(); // To manage ongoing tasks
 
     public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> {
             Q6b app = new Q6b();
             JFrame frame = new JFrame("Swing Web Crawler");
             frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
             frame.setSize(600, 400);
             frame.add(app.createUI());
             frame.setVisible(true);
         });
     }
 
     // Create the UI components
     public JPanel createUI() {
         JPanel panel = new JPanel();
         panel.setLayout(new BorderLayout());
 
         // Control Panel for URL input and buttons
         JPanel controlPanel = new JPanel();
         urlField = new JTextField(25);
         addButton = new JButton("Add URL");
         startButton = new JButton("Start Crawler");
         pauseButton = new JButton("Pause");
         resumeButton = new JButton("Resume");
         stopButton = new JButton("Stop");
 
         // Initially disable pause/resume/stop buttons
         pauseButton.setEnabled(false);
         resumeButton.setEnabled(false);
         stopButton.setEnabled(false);
 
         // Add action listeners to buttons
         addButton.addActionListener(e -> addUrl(urlField.getText()));
         startButton.addActionListener(e -> startCrawler());
         pauseButton.addActionListener(e -> pauseCrawler());
         resumeButton.addActionListener(e -> resumeCrawler());
         stopButton.addActionListener(e -> stopCrawler());
 
         controlPanel.add(urlField);
         controlPanel.add(addButton);
         controlPanel.add(startButton);
         controlPanel.add(pauseButton);
         controlPanel.add(resumeButton);
         controlPanel.add(stopButton);
 
         // Output Area to display crawl results
         outputArea = new JTextArea(10, 50);
         outputArea.setEditable(false);
         JScrollPane scrollPane = new JScrollPane(outputArea);
 
         // Progress Bar for visualizing progress
         progressBar = new JProgressBar(0, 100);
         progressBar.setValue(0);
         progressBar.setStringPainted(true);
 
         // Add components to main panel
         panel.add(controlPanel, BorderLayout.NORTH);
         panel.add(scrollPane, BorderLayout.CENTER);
         panel.add(progressBar, BorderLayout.SOUTH);
 
         return panel;
     }
 
     // Add URL to the crawl queue
     // Add URL to the crawl queue
     private void addUrl(String url) {
         if (url != null && !url.trim().isEmpty()) {
             // Check if the URL already has a protocol (http:// or https://)
             if (!url.startsWith("http://") && !url.startsWith("https://")) {
                 url = "http://" + url; // Prepend http:// if the protocol is missing
             }
 
             urlQueue.add(url);
             totalUrls++;
             outputArea.append("Added URL: " + url + "\n");
             urlField.setText("");
             startButton.setEnabled(true); // Enable the start button after adding a URL
         }
     }
 
     // Start the crawling process
     private void startCrawler() {
         if (executorService == null || executorService.isShutdown()) {
             executorService = Executors.newFixedThreadPool(5); // Reinitialize thread pool
         }
 
         isStopped = false; // Reset stop flag
         outputArea.append("\nStarting the crawl...\n");
         startButton.setEnabled(false);
         pauseButton.setEnabled(true);
         stopButton.setEnabled(true);
         progressBar.setMaximum(totalUrls);
 
         while (!urlQueue.isEmpty() && !isStopped) {
             String url = urlQueue.poll();
             if (url != null) {
                 activeTasks.add(executorService.submit(new CrawlTask(url)));
             }
         }
     }
 
     // Pause the crawling process
     private void pauseCrawler() {
         if (!isPaused) {
             isPaused = true;
             pauseButton.setEnabled(false);
             resumeButton.setEnabled(true);
             outputArea.append("\nCrawl paused.\n");
         }
     }
 
     // Resume the crawling process
     private void resumeCrawler() {
         if (isPaused) {
             isPaused = false;
             pauseButton.setEnabled(true);
             resumeButton.setEnabled(false);
             outputArea.append("\nResuming the crawl...\n");
 
             if (executorService == null || executorService.isShutdown()) {
                 executorService = Executors.newFixedThreadPool(5);
             }
 
             while (!urlQueue.isEmpty() && !isStopped) {
                 String url = urlQueue.poll();
                 if (url != null) {
                     activeTasks.add(executorService.submit(new CrawlTask(url)));
                 }
             }
         }
     }
 
     // Stop the crawling process
     private void stopCrawler() {
         isStopped = true;
 
         for (Future<?> task : activeTasks) {
             task.cancel(true);
         }
         activeTasks.clear();
 
         if (executorService != null) {
             executorService.shutdownNow();
         }
 
         outputArea.append("\nCrawl stopped.\n");
         pauseButton.setEnabled(false);
         resumeButton.setEnabled(false);
         stopButton.setEnabled(false);
 
         // Re-enable the start button to allow restarting
         startButton.setEnabled(true);
     }
 
     // Crawl Task that performs the crawling for a single URL
     static class CrawlTask implements Runnable {
         private String url;
 
         public CrawlTask(String url) {
             this.url = url;
         }
 
         @Override
         public void run() {
             try {
                 // Check if crawling is paused
                 while (isPaused) {
                     Thread.sleep(100); // Wait while paused
                 }
 
                 // Fetch the page content and extract title
                 String pageContent = fetchPageContent(url);
                 String title = extractTitle(pageContent);
 
                 // Synchronize the access to crawled data list
                 synchronized (crawledData) {
                     crawledData.add("URL: " + url + " | Title: " + title);
                 }
 
                 // Update progress bar and append to output
                 SwingUtilities.invokeLater(() -> {
                     progressBar.setValue(progressBar.getValue() + 1);
                     outputArea.append("Crawled: " + url + " | Title: " + title + "\n");
                 });
 
             } catch (Exception e) {
                 SwingUtilities
                         .invokeLater(() -> outputArea.append("Failed to crawl " + url + ": " + e.getMessage() + "\n"));
             }
         }
 
         // Fetch the content of the page
         private String fetchPageContent(String urlString) throws Exception {
             URL url = new URL(urlString);
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             connection.setRequestMethod("GET");
             connection.connect();
 
             BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             StringBuilder pageContent = new StringBuilder();
             String line;
             while ((line = reader.readLine()) != null) {
                 pageContent.append(line);
             }
             reader.close();
             return pageContent.toString();
         }
 
         // Extract the title of the web page
         private String extractTitle(String pageContent) {
             int titleStart = pageContent.indexOf("<title>");
             int titleEnd = pageContent.indexOf("</title>");
             if (titleStart != -1 && titleEnd != -1) {
                 return pageContent.substring(titleStart + 7, titleEnd);
             }
             return "No Title Found";
         }
     }
 
     // Initialize the ExecutorService when the app is created
     static {
         executorService = Executors.newFixedThreadPool(5); // Create a thread pool with 5 threads
     }
 }
