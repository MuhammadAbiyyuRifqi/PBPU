import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MedicineCRUDApp {
    private static final String FILE_PATH = "medicines.txt";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static List<Medicine> medicineList = new ArrayList<>();

    public static void main(String[] args) {
        loadMedicineData();

        while (true) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    addMedicine();
                    break;
                case 2:
                    listMedicines();
                    break;
                case 3:
                    updateMedicine();
                    break;
                case 4:
                    deleteMedicine();
                    break;
                case 5:
                    saveMedicineData();
                    System.out.println("Exiting the application.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void displayMenu() {
        System.out.println("Medicine CRUD Application");
        System.out.println("1. Add Medicine");
        System.out.println("2. List Medicines");
        System.out.println("3. Update Medicine");
        System.out.println("4. Delete Medicine");
        System.out.println("5. Exit");
    }

    private static int getUserChoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }

    private static void addMedicine() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the medicine name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the expiry date (yyyy-MM-dd): ");
        String expiryDateStr = scanner.nextLine();

        try {
            Medicine medicine = new Medicine(name, dateFormat.parse(expiryDateStr));
            medicineList.add(medicine);
            System.out.println("Medicine added: " + medicine);
        } catch (Exception e) {
            System.out.println("Invalid date format. Medicine not added.");
        }
    }

    private static void listMedicines() {
        if (medicineList.isEmpty()) {
            System.out.println("No medicines found!");
            return;
        }

        System.out.println("Medicines:");
        for (int i = 0; i < medicineList.size(); i++) {
            System.out.println((i + 1) + ". " + medicineList.get(i));
        }
    }

    private static void updateMedicine() {
        listMedicines();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the medicine number to update: ");
        int medicineNumber = scanner.nextInt();
        scanner.nextLine();

        if (medicineNumber >= 1 && medicineNumber <= medicineList.size()) {
            System.out.print("Enter the new medicine name: ");
            String newName = scanner.nextLine();
            System.out.print("Enter the new expiry date (yyyy-MM-dd): ");
            String newExpiryDateStr = scanner.nextLine();

            try {
                Medicine medicine = medicineList.get(medicineNumber - 1);
                medicine.setName(newName);
                medicine.setExpiryDate(dateFormat.parse(newExpiryDateStr));
                System.out.println("Medicine updated: " + medicine);
            } catch (Exception e) {
                System.out.println("Invalid date format. Medicine not updated.");
            }
        } else {
            System.out.println("Invalid medicine number.");
        }
    }

    private static void deleteMedicine() {
        listMedicines();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the medicine number to delete: ");
        int medicineNumber = scanner.nextInt();

        if (medicineNumber >= 1 && medicineNumber <= medicineList.size()) {
            Medicine deletedMedicine = medicineList.remove(medicineNumber - 1);
            System.out.println("Medicine deleted: " + deletedMedicine);
        } else {
            System.out.println("Invalid medicine number.");
        }
    }

    private static void loadMedicineData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            medicineList = (List<Medicine>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Ignore if the file doesn't exist yet or if deserialization fails.
        }
    }

    private static void saveMedicineData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(medicineList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Medicine implements Serializable {
        private String name;
        private Date expiryDate;

        public Medicine(String name, Date expiryDate) {
            this.name = name;
            this.expiryDate = expiryDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(Date expiryDate) {
            this.expiryDate = expiryDate;
        }

        @Override
        public String toString() {
            return "Medicine: " + name + " (Expiry Date: " + dateFormat.format(expiryDate) + ")";
        }
    }
}