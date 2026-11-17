import java.io.*;
import java.util.*;

class Passenger {
    String id;
    String name;
    String destination;
    String status;

    Passenger(String id, String name, String destination) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.status = "Waiting";
    }
}

public class BusPassengerManagement {

    static Queue<Passenger> waitingQueue = new LinkedList<>();
    static ArrayList<Passenger> passengers = new ArrayList<>();

    static int nextId = 101;
    static int boardedCount = 0;
    static final int CAPACITY = 40;

    static Scanner sc = new Scanner(System.in);

    static final String FILE_NAME = "passengers.txt";

    // Register Passenger
    static void registerPassenger() {

        System.out.print("Enter passenger name: ");
        String name = sc.nextLine();

        System.out.print("Enter destination: ");
        String destination = sc.nextLine();

        String id = "PS" + nextId;
        nextId++;

        Passenger p = new Passenger(id, name, destination);

        passengers.add(p);
        waitingQueue.add(p);

        System.out.println("Passenger registered. ID: " + id+ " | " + name+ " | " + destination+ " | Queue position: " + waitingQueue.size());
    }

    // Board Next Passenger
    static void boardNextPassenger() {

        if (waitingQueue.isEmpty()) {
            System.out.println("No passengers waiting.");
            return;
        }

        if (boardedCount >= CAPACITY) {
            System.out.println("Bus is full. Boarding not possible.");
            return;
        }

        Passenger p = waitingQueue.poll();

        p.status = "Boarded";
        boardedCount++;

        System.out.println("Passenger " + p.id+ " boarded. Seats left: "+ (CAPACITY - boardedCount));
    }

    // Display Waiting Queue
    static void displayQueue() {

        if (waitingQueue.isEmpty()) {
            System.out.println("No passengers waiting.");
            return;
        }

        System.out.println("Waiting Queue:");

        int position = 1;

        for (Passenger p : waitingQueue) {
            System.out.println(position + ". "+ p.id + " | "+ p.name + " | "+ p.destination + " | "+ p.status);

            position++;
        }
    }

    // Search Passenger by Name
    static void searchPassenger() {

        System.out.print("Enter passenger name to search: ");
        String searchName = sc.nextLine();

        boolean found = false;

        for (Passenger p : passengers) {

            if (p.name.equalsIgnoreCase(searchName)) {

                System.out.println("Found: "+ p.id + " | "+ p.name + " | "+ p.destination + " | Status: "+ p.status);

                found = true;
            }
        }

        if (!found) {
            System.out.println("Passenger not found.");
        }
    }

    // Load data from file
    static void loadData() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br =new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split("\\|", -1);

                if (data.length == 4) {

                    Passenger p = new Passenger(
                            data[0], data[1], data[2]);

                    p.status = data[3];

                    passengers.add(p);

                    if (p.status.equals("Waiting")) {
                        waitingQueue.add(p);
                    } else if (p.status.equals("Boarded")) {
                        boardedCount++;
                    }

                    int number = Integer.parseInt(
                            p.id.substring(2));

                    if (number >= nextId) {
                        nextId = number + 1;
                    }
                }
            }

            System.out.println("Passenger data loaded successfully.");

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading passenger data.");
        }
    }

    // Save data to file
    static void saveData() {

        try (PrintWriter writer =new PrintWriter(new FileWriter(FILE_NAME))) {

            for (Passenger p : passengers) {

                writer.println(p.id + "|"+ p.name + "|"+ p.destination + "|"+ p.status);
            }

            System.out.println("Passenger data saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving passenger data.");
        }
    }

    // Main Method
    public static void main(String[] args) {

        loadData();

        while (true) {

            System.out.println("\n===== BUS PASSENGER MANAGEMENT =====");
            System.out.println("1. Register Passenger");
            System.out.println("2. Board Next Passenger");
            System.out.println("3. Display Waiting Queue");
            System.out.println("4. Search Passenger by Name");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");

            String choice = sc.nextLine();

            switch (choice) {

                case "1":
                    registerPassenger();
                    break;

                case "2":
                    boardNextPassenger();
                    break;

                case "3":
                    displayQueue();
                    break;

                case "4":
                    searchPassenger();
                    break;

                case "5":
                    saveData();
                    System.out.println("Thank you for using the system!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid menu option.");
            }
        }
    }
}
