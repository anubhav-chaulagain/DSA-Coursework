/*
    You are given a class NumberPrinter with three methods: printZero, printEven, and printOdd.
    These methods are designed to print the numbers 0, even numbers, and odd numbers, respectively.
    Task:
    Create a ThreadController class that coordinates three threads:
    5. ZeroThread: Calls printZero to print 0s.
    6. EvenThread: Calls printEven to print even numbers.
    7. OddThread: Calls printOdd to print odd numbers.
    These threads should work together to print the sequence "0102030405..." up to a specified number n.
The output should be interleaved, ensuring that the numbers are printed in the correct order.
 */

/*
    Approach
    Use Three Threads: One for printing 0, one for even numbers, and one for odd numbers.
    Synchronization: Use a shared lock to control execution order.
    Wait-Notify Mechanism:
    printZero() waits for its turn (when count % 2 == 0).
    printOdd() waits for count % 4 == 1.
    printEven() waits for count % 4 == 3.
    After printing, each thread increments count and notifies all waiting threads.
    Ensuring Proper Order: Threads execute alternately, ensuring the output format 0 X 0 Y 0 Z ....
    Time Complexity:
    O(n) – Each number (zero, odd, even) is printed exactly once in sequence.

    Space Complexity:
    O(1) – Only a few variables and synchronization objects are used, independent of n. 🚀
*/

// A class responsible for printing numbers in a synchronized manner.
class NumberPrinter {
    // Method to print zero
    public void printZero() {
        System.out.print("0");
    }

    // Method to print even numbers
    public void printEven(int num) {
        System.out.print(num);
    }

    // Method to print odd numbers
    public void printOdd(int num) {
        System.out.print(num);
    }
}

// A controller class to manage the correct sequence of printing
class ThreadController {
    private final int n; // The number limit for printing
    private int count = 0; // A counter to keep track of the printing sequence
    private final NumberPrinter printer; // Reference to the NumberPrinter class
    private final Object lock = new Object(); // A lock object for synchronization

    // Constructor to initialize the number limit and the printer object
    public ThreadController(int n, NumberPrinter printer) {
        this.n = n;
        this.printer = printer;
    }

    // Thread function to print zero in the required sequence
    public void printZero() {
        synchronized (lock) {
            for (int i = 0; i < n; i++) {
                // Wait until it's this thread's turn (when count is even)
                while (count % 2 != 0) {
                    try {
                        lock.wait(); // Pause and wait for the turn
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                printer.printZero(); // Print "0"
                count++; // Move to the next step
                lock.notifyAll(); // Wake up other waiting threads
            }
        }
    }

    // Thread function to print even numbers in the correct order
    public void printEven() {
        synchronized (lock) {
            for (int i = 2; i <= n; i += 2) {
                // Wait until it's time to print an even number
                while (count % 4 != 3) {
                    try {
                        lock.wait(); // Pause and wait for the turn
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                printer.printEven(i); // Print an even number
                count++; // Move to the next step
                lock.notifyAll(); // Wake up other waiting threads
            }
        }
    }

    // Thread function to print odd numbers in the correct order
    public void printOdd() {
        synchronized (lock) {
            for (int i = 1; i <= n; i += 2) {
                // Wait until it's time to print an odd number
                while (count % 4 != 1) {
                    try {
                        lock.wait(); // Pause and wait for the turn
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                printer.printOdd(i); // Print an odd number
                count++; // Move to the next step
                lock.notifyAll(); // Wake up other waiting threads
            }
        }
    }
}

// Main class to start the threads and execute the printing sequence
public class Q6a {
    public static void main(String[] args) {
        int n = 5; // Limit up to which numbers will be printed
        NumberPrinter printer = new NumberPrinter(); // Create an instance of NumberPrinter
        ThreadController controller = new ThreadController(n, printer); // Create a controller for synchronization

        // Create threads for printing zero, even, and odd numbers
        Thread zeroThread = new Thread(controller::printZero);
        Thread evenThread = new Thread(controller::printEven);
        Thread oddThread = new Thread(controller::printOdd);

        // Start all the threads
        zeroThread.start();
        evenThread.start();
        oddThread.start();
    }
}

// output: 0102030405