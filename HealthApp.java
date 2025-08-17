import java.util.*;
import java.io.*;

class DailyLog implements Serializable {
    private static final long serialVersionUID = 1L;
    String date; // YYYY-MM-DD
    String name;
    int age;
    String sex;
    double weight; // lbs
    int heightFeet;
    int heightInches;
    String weightClass;
    int calorieGoal;
    String[] mealNames = {"Breakfast", "Lunch", "Dinner", "Snacks"};
    String[] mealDescriptions = new String[4];
    int[] mealCalories = new int[4];
    int totalCaloriesEaten;
    String exerciseType;
    int caloriesBurned;
    int netCalories;
    int caloriesLeft;
    String message;
}

public class HealthApp {

    static Scanner scanner = new Scanner(System.in);
    static List<DailyLog> logs = new ArrayList<>();
    static final String FILE_NAME = "health_logs.dat";
    static String currentUser;   // Track the active user
    static List<DailyLog> userLogs = new ArrayList<>(); // Logs for that user only

    public static void main(String[] args) {
        // Load saved logs if available
        loadLogs();

        // Ask for username at the start
        System.out.print("Enter your name to load your data: ");
        currentUser = scanner.next();

        // Get only logs for this user
        for (DailyLog log : logs) {
            if (log.name.equalsIgnoreCase(currentUser)) {
                userLogs.add(log);
            }
        }

        System.out.println("Welcome, " + currentUser + "!");
        System.out.println("Loaded " + userLogs.size() + " logs for you.");

        boolean running = true;
        while (running) {
            System.out.println("\n=== Health Tracker (" + currentUser + ") ===");
            System.out.println("1. Enter Daily Data");
            System.out.println("2. View Specific Day");
            System.out.println("3. View Monthly Progress");
            System.out.println("4. View Weight Trend");
            System.out.println("5. Save & Exit");
            System.out.print("Choose an option: ");
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    enterDailyData();
                    break;
                case 2:
                    viewSpecificDay();
                    break;
                case 3:
                    viewMonthlyProgress();
                    break;
                case 4:
                    viewWeightTrend();
                    break;
                case 5:
                    saveLogs();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void enterDailyData() {
        DailyLog log = new DailyLog();

        System.out.print("Enter date (YYYY-MM-DD): ");
        log.date = scanner.next();

        log.name = currentUser; // Always tied to current user

        System.out.print("Enter your age: ");
        log.age = getIntInput();

        System.out.print("Enter your sex (M/F): ");
        log.sex = scanner.next().toUpperCase();

        System.out.print("Enter your weight (lbs): ");
        log.weight = getDoubleInput();

        System.out.print("Enter your height - feet: ");
        log.heightFeet = getIntInput();
        System.out.print("Enter your height - inches: ");
        log.heightInches = getIntInput();

        double heightMeters = ((log.heightFeet * 12) + log.heightInches) * 0.0254;
        double weightKg = log.weight * 0.453592;
        log.weightClass = getWeightClass(weightKg, heightMeters);

        System.out.println("Do you want the app to suggest a calorie goal? (Y/N): ");
        String choice = scanner.next();
        if (choice.equalsIgnoreCase("Y")) {
            log.calorieGoal = calculateCalorieGoal(log.age, log.sex, weightKg, heightMeters);
            System.out.println("Suggested Calorie Goal: " + log.calorieGoal + " cal");
        } else {
            System.out.print("Enter your calorie goal for the day: ");
            log.calorieGoal = getIntInput();
        }

        log.totalCaloriesEaten = 0;
        scanner.nextLine(); // clear buffer
        for (int i = 0; i < log.mealNames.length; i++) {
            System.out.print("What did you have for " + log.mealNames[i] + "?: ");
            log.mealDescriptions[i] = scanner.nextLine();
            System.out.print("How many calories was that?: ");
            log.mealCalories[i] = getIntInput();
            scanner.nextLine(); // clear buffer
            log.totalCaloriesEaten += log.mealCalories[i];
        }

        System.out.print("What exercise did you do today?: ");
        log.exerciseType = scanner.nextLine();
        System.out.print("How many calories did you burn?: ");
        log.caloriesBurned = getIntInput();

        log.netCalories = log.totalCaloriesEaten - log.caloriesBurned;
        log.caloriesLeft = log.calorieGoal - log.netCalories;

        if (log.netCalories <= log.calorieGoal) {
            log.message = "You have met your calorie goal for the day! Keep up the great work!!";
        } else {
            log.message = "You have gone over your calorie limit for the day. You will do better tomorrow.";
        }

        // Add to both user logs and global logs
        userLogs.add(log);
        logs.add(log);
        sortLogsByDate(userLogs);

        // Immediately save and display the full summary
        saveLogs();
        displayLog(log);
        System.out.println("Daily data saved!");
    }

    private static int calculateCalorieGoal(int age, String sex, double weightKg, double heightM) {
        double heightCm = heightM * 100;
        double bmr;
        if (sex.equals("M")) {
            bmr = 10 * weightKg + 6.25 * heightCm - 5 * age + 5;
        } else {
            bmr = 10 * weightKg + 6.25 * heightCm - 5 * age - 161;
        }

        System.out.println("Choose your activity level:");
        System.out.println("1. Sedentary (little or no exercise)");
        System.out.println("2. Light (light exercise/sports 1-3 days/week)");
        System.out.println("3. Moderate (moderate exercise 3-5 days/week)");
        System.out.println("4. Active (hard exercise 6-7 days/week)");
        System.out.println("5. Very Active (physical job, intense training)");
        int activity = getIntInput();

        double multiplier;
        switch (activity) {
            case 1: multiplier = 1.2; break;
            case 2: multiplier = 1.375; break;
            case 3: multiplier = 1.55; break;
            case 4: multiplier = 1.725; break;
            case 5: multiplier = 1.9; break;
            default: multiplier = 1.2; break;
        }

        double tdee = bmr * multiplier;

        System.out.println("What is your goal?\n1. Lose Weight\n2. Maintain Weight\n3. Gain Weight");
        int goal = getIntInput();

        if (goal == 1) tdee -= 500;
        else if (goal == 3) tdee += 500;

        return (int) Math.round(tdee);
    }

    private static void viewSpecificDay() {
        System.out.print("Enter date to view (YYYY-MM-DD): ");
        String date = scanner.next();
        boolean found = false;

        for (DailyLog log : userLogs) {
            if (log.date.equals(date)) {
                displayLog(log);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("No data found for that date.");
        }
    }

    private static void viewMonthlyProgress() {
        Map<String, List<DailyLog>> monthlyData = new HashMap<>();

        for (DailyLog log : userLogs) {
            String ym = log.date.substring(0, 7); // YYYY-MM
            monthlyData.putIfAbsent(ym, new ArrayList<>());
            monthlyData.get(ym).add(log);
        }

        for (String month : monthlyData.keySet()) {
            List<DailyLog> monthLogs = monthlyData.get(month);
            if (monthLogs.isEmpty()) continue;

            double startWeight = monthLogs.get(0).weight;
            double endWeight = monthLogs.get(monthLogs.size() - 1).weight;
            double weightChange = endWeight - startWeight;

            int totalCaloriesEaten = 0;
            int totalCaloriesBurned = 0;
            for (DailyLog log : monthLogs) {
                totalCaloriesEaten += log.totalCaloriesEaten;
                totalCaloriesBurned += log.caloriesBurned;
            }

            System.out.println("\nMonth: " + month);
            System.out.println("Start Weight: " + startWeight + " lbs");
            System.out.println("End Weight: " + endWeight + " lbs");
            System.out.printf("Weight Change: %.1f lbs\n", weightChange);
            System.out.println("Total Calories Eaten: " + totalCaloriesEaten);
            System.out.println("Total Calories Burned: " + totalCaloriesBurned);
        }
    }

    private static void viewWeightTrend() {
        if (userLogs.isEmpty()) {
            System.out.println("No data to show.");
            return;
        }

        double startWeight = userLogs.get(0).weight;
        double endWeight = userLogs.get(userLogs.size() - 1).weight;
        double totalChange = endWeight - startWeight;

        for (DailyLog log : userLogs) {
            System.out.println(log.date + ": " + log.weight + " lbs");
        }
        System.out.printf("Total weight change: %.1f lbs\n", totalChange);
    }

    private static void displayLog(DailyLog log) {
        System.out.println("\n===== Daily Summary (" + log.date + ") =====");
        System.out.println("Name: " + log.name);
        System.out.println("Age: " + log.age);
        System.out.println("Sex: " + log.sex);
        System.out.println("Weight: " + log.weight + " lbs");
        System.out.println("Height: " + log.heightFeet + " ft " + log.heightInches + " in");
        System.out.println("Weight Class: " + log.weightClass);
        System.out.println("Calorie Goal: " + log.calorieGoal);
        for (int i = 0; i < log.mealNames.length; i++) {
            System.out.println(log.mealNames[i] + ": " + log.mealDescriptions[i] +
                    " (" + log.mealCalories[i] + " cal)");
        }
        System.out.println("Total Calories Eaten: " + log.totalCaloriesEaten);
        System.out.println("Exercise: " + log.exerciseType +
                " (" + log.caloriesBurned + " cal burned)");
        System.out.println("Net Calories: " + log.netCalories);
        System.out.println("Calories Left for the Day: " + log.caloriesLeft);
        System.out.println("Message: " + log.message);
    }

    private static String getWeightClass(double weightKg, double heightM) {
        double bmi = weightKg / (heightM * heightM);
        if (bmi < 18.5) return "Underweight";
        else if (bmi < 25) return "Normal weight";
        else if (bmi < 30) return "Overweight";
        else return "Obese";
    }

    private static void sortLogsByDate(List<DailyLog> logList) {
        Collections.sort(logList, Comparator.comparing(a -> a.date));
    }

    private static void saveLogs() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(logs);
            System.out.println("Logs saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving logs: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadLogs() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            logs = (List<DailyLog>) ois.readObject();
            System.out.println("Loaded " + logs.size() + " total logs from file.");
        } catch (Exception e) {
            System.out.println("Error loading logs: " + e.getMessage());
        }
    }

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static double getDoubleInput() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Enter a valid number: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
