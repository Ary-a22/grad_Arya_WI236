import java.util.Scanner;

public class LayoutManagementMain {

    static Scanner sc;
    static int loggedInOwnerId;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("\n========= LAYOUT MAINTENANCE SYSTEM =========");
            System.out.println("1. Admin Login");
            System.out.println("2. Site Owner Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    if (adminLogin()) {
                        adminMenu();
                    } else {
                        System.out.println("Invalid admin credentials");
                    }
                    break;
                case 2:
                    if (ownerLogin()) {
                        ownerMenu();
                    } else {
                        System.out.println("Invalid owner credentials");
                    }
                    break;
                case 3:
                    System.out.println("Exiting application...");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    /* ================= AUTH ================= */

    static boolean adminLogin() {
        System.out.print("Enter Admin ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        return AuthDAO.validateAdmin(id, password);
    }

    static boolean ownerLogin() {
        System.out.print("Enter Owner ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        if (AuthDAO.validateOwner(id, password)) {
            loggedInOwnerId = id;
            return true;
        }
        return false;
    }

    /* ================= ADMIN ================= */

    static void adminMenu() {
        int choice;
        do {
            System.out.println("\n========= ADMIN MENU =========");
            System.out.println("1. Manage Owners");
            System.out.println("2. View All Sites");
            System.out.println("3. Maintenance");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    manageOwnersMenu();
                    break;
                case 2:
                    System.out.println("View All Sites (with filters)");
                    break;
                case 3:
                    maintenanceMenu();
                    break;
                case 4:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 4);
    }

    /* ================= OWNER ================= */

    static void ownerMenu() {
        int choice;
        do {
            System.out.println("\n========= SITE OWNER MENU =========");
            System.out.println("1. My Site");
            System.out.println("2. My Profile");
            System.out.println("3. Maintenance");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    ownerSiteMenu();
                    break;
                case 2:
                    ownerProfileMenu();
                    break;
                case 3:
                    ownerMaintenanceMenu();
                    break;
                case 4:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 4);
    }

    /* ================= OWNER : SITE ================= */

    static void ownerSiteMenu() {
        int choice;
        do {
            System.out.println("\n--- MY SITE ---");
            System.out.println("1. View Sites");
            System.out.println("2. Update Site Details (Request)");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    ownerSiteViewMenu();
                    break;
                case 2:
                    updateSiteRequestMenu();
                    break;
                case 3:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
    }

    static void ownerSiteViewMenu() {
        int choice;
        do {
            System.out.println("\n--- VIEW SITES ---");
            System.out.println("1. All Sites");
            System.out.println("2. Search Site");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Viewing all my sites");
                    break;
                case 2:
                    searchSiteMenu();
                    break;
                case 3:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
    }

    static void updateSiteRequestMenu() {
        int choice;
        do {
            System.out.println("\n--- UPDATE SITE REQUEST ---");
            System.out.println("1. Update Site Type");
            System.out.println("2. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Requesting site type update");
                    break;
                case 2:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 2);
    }

    /* ================= OWNER : PROFILE ================= */

    static void ownerProfileMenu() {
        int choice;
        do {
            System.out.println("\n--- MY PROFILE ---");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile (Request)");
            System.out.println("3. Change Password");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    OwnerDAO.viewOwnerProfile(loggedInOwnerId);
                    break;
                case 2:
                    updateProfileRequestMenu();
                    break;
                case 3:
                    requestPasswordChangeMenu();
                    break;
                case 4:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 4);
    }

    static void requestPasswordChangeMenu() {
        sc.nextLine();
        System.out.print("Enter Current Password: ");
        String currentPassword = sc.nextLine();
        System.out.print("Enter New Password: ");
        String newPassword = sc.nextLine();
        System.out.print("Confirm New Password: ");
        String confirmPassword = sc.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match");
            return;
        }

        boolean updated = AuthDAO.changePassword(loggedInOwnerId, currentPassword, newPassword);
        if (updated) {
            System.out.println("Password changed successfully");
        } else {
            System.out.println("Current password incorrect");
        }
    }

    static void updateProfileRequestMenu() {
        int choice;
        do {
            System.out.println("\n--- UPDATE PROFILE REQUEST ---");
            System.out.println("1. Update Name");
            System.out.println("2. Update Phone");
            System.out.println("3. Update Email");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Requesting name update");
                    break;
                case 2:
                    System.out.println("Requesting phone update");
                    break;
                case 3:
                    System.out.println("Requesting email update");
                    break;
                case 4:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 4);
    }

    /* ================= OWNER : MAINTENANCE ================= */

    static void ownerMaintenanceMenu() {
        int choice;
        do {
            System.out.println("\n--- MAINTENANCE ---");
            System.out.println("1. View Pending Amount");
            System.out.println("2. Payment History");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    ownerPendingMenu();
                    break;
                case 2:
                    ownerPaymentHistoryMenu();
                    break;
                case 3:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
    }

    static void ownerPendingMenu() {
        int choice;
        do {
            System.out.println("\n--- VIEW PENDING AMOUNT ---");
            System.out.println("1. All Sites");
            System.out.println("2. Search Site");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Viewing pending amount for all sites");
                    break;
                case 2:
                    searchSiteMenu();
                    break;
                case 3:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
    }

    static void ownerPaymentHistoryMenu() {
        int choice;
        do {
            System.out.println("\n--- PAYMENT HISTORY ---");
            System.out.println("1. All Payments");
            System.out.println("2. Search Site");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Viewing all payment history");
                    break;
                case 2:
                    searchSiteMenu();
                    break;
                case 3:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
    }

    /* ================= COMMON ================= */

    static void searchSiteMenu() {
        int choice;
        do {
            System.out.println("\n--- SEARCH SITE ---");
            System.out.println("1. By ID");
            System.out.println("2. By Type");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Search by Site ID");
                    break;
                case 2:
                    System.out.println("Search by Site Type");
                    break;
                case 3:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
    }

    /* ================= ADMIN : MAINTENANCE ================= */

    static void maintenanceMenu() {
        int choice;
        do {
            System.out.println("\n===== MAINTENANCE =====");
            System.out.println("1. View Pending");
            System.out.println("2. Collect Payment");
            System.out.println("3. Reports");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewPendingMenu();
                    break;
                case 2:
                    collectPaymentMenu();
                    break;
                case 3:
                    reportsMenu();
                    break;
                case 4:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 4);
    }

    static void viewPendingMenu() {
        int choice;
        do {
            System.out.println("\n--- VIEW PENDING ---");
            System.out.println("1. All Sites");
            System.out.println("2. Search Site");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Viewing pending payments for all sites");
                    break;
                case 2:
                    searchSiteMenu();
                    break;
                case 3:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
    }

    static void collectPaymentMenu() {
        int choice;
        do {
            System.out.println("\n--- COLLECT PAYMENT ---");
            System.out.println("1. All Sites");
            System.out.println("2. Search Site");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Collecting payment from all sites");
                    break;
                case 2:
                    searchSiteMenu();
                    break;
                case 3:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
    }

    static void reportsMenu() {
        int choice;
        do {
            System.out.println("\n--- REPORTS ---");
            System.out.println("1. Paid Sites");
            System.out.println("2. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Showing all paid maintenance records");
                    break;
                case 2:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 2);
    }

    /* ================= ADMIN : OWNERS ================= */

    static void manageOwnersMenu() {
        int choice;
        do {
            System.out.println("\n===== MANAGE OWNERS =====");
            System.out.println("1. Add Owner");
            System.out.println("2. View All Owners");
            System.out.println("3. Edit Owner");
            System.out.println("4. Remove Owner");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addOwner();
                    break;
                case 2:
                    viewAllOwners();
                    break;
                case 3:
                    editOwner();
                    break;
                case 4:
                    removeOwner();
                    break;
                case 5:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 5);
    }

    /* ================= ADMIN : OWNERS ACTIONS ================= */

    static void viewAllOwners() {
        OwnerDAO.viewAllOwners();
    }

    static void addOwner() {
        sc.nextLine();
        System.out.print("Enter Owner ID: ");
        int ownerId = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Owner Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Set Initial Password: ");
        String password = sc.nextLine();

        boolean success = OwnerDAO.addOwner(ownerId, name, phone, email, password);
        if (success) {
            System.out.println("Owner added successfully");
        } else {
            System.out.println("Failed to add owner");
        }
    }

    static void editOwner() {
        sc.nextLine();
        System.out.print("Enter Owner ID to edit: ");
        int ownerId = sc.nextInt();
        sc.nextLine();

        Owner owner = OwnerDAO.getOwnerById(ownerId);
        if (owner == null) {
            System.out.println("Owner not found");
            return;
        }

        System.out.println("\nCurrent Owner Details:");
        System.out.println("Name  : " + owner.getName());
        System.out.println("Phone : " + owner.getPhone());
        System.out.println("Email : " + owner.getEmail());

        System.out.println("(leave blank to keep same)");
        System.out.print("Enter New Name: ");
        String name = sc.nextLine();

        System.out.print("Enter New Phone: ");
        String phone = sc.nextLine();

        System.out.print("Enter New Email: ");
        String email = sc.nextLine();

        if (name.isBlank()) name = owner.getName();
        if (phone.isBlank()) phone = owner.getPhone();
        if (email.isBlank()) email = owner.getEmail();

        boolean updated = OwnerDAO.editOwner(ownerId, name, phone, email);
        if (updated) {
            System.out.println("Owner updated successfully");
        } else {
            System.out.println("Update failed");
        }
    }

    static void removeOwner() {
        sc.nextLine();
        System.out.print("Enter Owner ID to remove: ");
        int ownerId = sc.nextInt();
        sc.nextLine();

        Owner owner = OwnerDAO.getOwnerById(ownerId);
        if (owner == null) {
            System.out.println("Owner not found");
            return;
        }

        System.out.println("\nOwner Details:");
        System.out.println("ID    : " + owner.getOwnerId());
        System.out.println("Name  : " + owner.getName());
        System.out.println("Phone : " + owner.getPhone());
        System.out.println("Email : " + owner.getEmail());

        System.out.print("\nAre you sure you want to delete this owner? (Y/N): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            boolean removed = OwnerDAO.removeOwner(ownerId);
            if (removed) {
                System.out.println("Owner removed successfully");
            } else {
                System.out.println("Owner removal failed");
            }
        } else {
            System.out.println("Deletion cancelled");
        }
    }
}
