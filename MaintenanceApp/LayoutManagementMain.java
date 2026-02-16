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
                    if (adminLogin()) adminMenu();
                    else System.out.println("Invalid admin credentials");
                    break;
                case 2:
                    if (ownerLogin()) ownerMenu();
                    else System.out.println("Invalid owner credentials");
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
            System.out.println("4. Manage Requests");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    manageOwnersMenu();
                    break;
                case 2:
                    viewAllSitesMenu();
                    break;
                case 3:
                    maintenanceMenu();
                    break;
                case 4:
                    manageRequestsMenu();
                    break;
                case 5:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 5);
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
                    SiteDAO.viewSitesByOwner(loggedInOwnerId);
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

    static void searchSiteMenu() {
        int choice;
        do {
            System.out.println("\n--- SEARCH SITE ---");
            System.out.println("1. Search by Site ID");
            System.out.println("2. Search by Type");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Site ID: ");
                    int siteId = sc.nextInt();
                    SiteDAO.viewSiteById(siteId);
                    break;
                case 2:
                    sc.nextLine();
                    System.out.print("Enter Site Type: ");
                    String type = sc.nextLine();
                    SiteDAO.viewSitesByType(type);
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
            System.out.println("--- UPDATE SITE REQUEST ---");
            System.out.println("1. Request Site Type Change");
            System.out.println("2. View My Site Requests");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    sc.nextLine();
                    System.out.print("Enter Site ID: ");
                    int siteId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter New Site Type: ");
                    String newType = sc.nextLine();
                    RequestDAO.createSiteTypeRequest(loggedInOwnerId, siteId, newType);
                    break;
                case 2:
                    RequestDAO.viewRequestsByOwner(loggedInOwnerId);
                    break;
                case 3:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
    }

    /* ================= OWNER : PROFILE ================= */

    static void ownerProfileMenu() {
        int choice;
        do {
            System.out.println("\n--- MY PROFILE ---");
            System.out.println("1. View Profile");
            System.out.println("2. Change Password");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    OwnerDAO.viewOwnerProfile(loggedInOwnerId);
                    break;
                case 2:
                    requestPasswordChangeMenu();
                    break;
                case 3:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
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
        if (updated) System.out.println("Password changed successfully");
        else System.out.println("Current password incorrect");
    }

    /* ================= ADMIN : SITES ================= */

    static void viewAllSitesMenu() {
        int choice;
        do {
            System.out.println("\n===== VIEW ALL SITES =====");
            System.out.println("1. View All Sites");
            System.out.println("2. Filter by Type");
            System.out.println("3. Filter by Ownership Status");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    SiteDAO.viewAllSites();
                    break;
                case 2:
                    sc.nextLine();
                    System.out.print("Enter Site Type: ");
                    String type = sc.nextLine();
                    SiteDAO.viewSitesByType(type);
                    break;
                case 3:
                    System.out.println("1. Owned Sites");
                    System.out.println("2. Unowned Sites");
                    int status = sc.nextInt();
                    SiteDAO.viewSitesByOwnership(status == 1);
                    break;
                case 4:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 4);
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
            System.out.println("5. Assign / Unassign Site");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addOwner();
                    break;
                case 2:
                    OwnerDAO.viewAllOwners();
                    break;
                case 3:
                    editOwner();
                    break;
                case 4:
                    removeOwner();
                    break;
                case 5:
                    assignUnassignMenu();
                    break;
                case 6:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 6);
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
        if (success) System.out.println("Owner added successfully");
        else System.out.println("Failed to add owner");
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
        if (updated) System.out.println("Owner updated successfully");
        else System.out.println("Update failed");
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
            if (removed) System.out.println("Owner removed successfully");
            else System.out.println("Owner removal failed");
        } else {
            System.out.println("Deletion cancelled");
        }
    }

    /* ================= ADMIN : ASSIGN / UNASSIGN ================= */

    static void assignUnassignMenu() {
        int choice;
        do {
            System.out.println("\n--- ASSIGN / UNASSIGN SITE ---");
            System.out.println("1. Assign Site to Owner");
            System.out.println("2. Unassign Site from Owner");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    assignSiteToOwner();
                    break;
                case 2:
                    unassignSiteFromOwner();
                    break;
                case 3:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
    }

    static void assignSiteToOwner() {
        System.out.print("Enter Site ID: ");
        int siteId = sc.nextInt();
        System.out.print("Enter Owner ID: ");
        int ownerId = sc.nextInt();

        boolean success = SiteDAO.assignSite(siteId, ownerId);
        if (success) System.out.println("Site assigned successfully");
        else System.out.println("Site assignment failed");
    }

    static void unassignSiteFromOwner() {
        System.out.print("Enter Site ID: ");
        int siteId = sc.nextInt();

        boolean success = SiteDAO.unassignSite(siteId);
        if (success) System.out.println("Site unassigned successfully");
        else System.out.println("Site unassignment failed");
    }
        /* ================= ADMIN : REQUESTS ================= */

    static void manageRequestsMenu() {
        int choice;
        do {
            System.out.println("===== MANAGE REQUESTS =====");
            System.out.println("1. View All Pending Requests");
            System.out.println("2. Approve Request");
            System.out.println("3. Reject Request");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    RequestDAO.viewAllPendingRequests();
                    break;
                case 2:
                    System.out.print("Enter Request ID to approve: ");
                    int approveId = sc.nextInt();
                    RequestDAO.approveRequest(approveId);
                    break;
                case 3:
                    System.out.print("Enter Request ID to reject: ");
                    int rejectId = sc.nextInt();
                    RequestDAO.rejectRequest(rejectId);
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
            System.out.println("\n--- OWNER MAINTENANCE ---");
            System.out.println("1. View My Pending Bills");
            System.out.println("2. Pay Maintenance");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    MaintenanceDAO.viewPendingByOwner(loggedInOwnerId);
                    break;
                case 2:
                    ownerPaymentMenu();
                    break;
                case 3:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
    }

    static void ownerPaymentMenu() {
        int choice;
        do {
            System.out.println("\n--- PAY MAINTENANCE ---");
            System.out.println("1. Pay Full Amount");
            System.out.println("2. Pay Partial Amount");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    payFullAmount();
                    break;
                case 2:
                    payPartialAmount();
                    break;
                case 3:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
    }

    static void payFullAmount() {
        System.out.print("Enter Bill ID to pay fully: ");
        int billId = sc.nextInt();
        MaintenanceDAO.payFullAmount(loggedInOwnerId, billId);
    }

    static void payPartialAmount() {
        System.out.print("Enter Bill ID: ");
        int billId = sc.nextInt();
        System.out.print("Enter Amount to Pay: ");
        double amount = sc.nextDouble();
        MaintenanceDAO.payPartialAmount(loggedInOwnerId, billId, java.math.BigDecimal.valueOf(amount));
    }

    /* ================= ADMIN : MAINTENANCE ================= */

    static void maintenanceMenu() {
        int choice;
        do {
            System.out.println("\n===== MAINTENANCE =====");
            System.out.println("1. View Pending");
            System.out.println("2. Collect Payment");
            System.out.println("3. Reports (Transactions)");
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
                    maintenanceReportsMenu();
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
            System.out.println("1. View All");
            System.out.println("2. View by Type");
            System.out.println("3. View by Owner");
            System.out.println("4. View by Size");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    MaintenanceDAO.viewPendingAll();
                    break;
                case 2:
                    sc.nextLine();
                    System.out.print("Enter Site Type: ");
                    String type = sc.nextLine();
                    MaintenanceDAO.viewPendingByType(type);
                    break;
                case 3:
                    System.out.print("Enter Owner ID: ");
                    int ownerId = sc.nextInt();
                    MaintenanceDAO.viewPendingByOwner(ownerId);
                    break;
                case 4:
                    System.out.print("Enter Minimum Area: ");
                    double area = sc.nextDouble();
                    MaintenanceDAO.viewPendingBySize(area);
                    break;
                case 5:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 5);
    }

    static void collectPaymentMenu() {
        int choice;
        do {
            System.out.println("\n--- COLLECT PAYMENT ---");
            System.out.println("1. Collect from All");
            System.out.println("2. Collect from Type");
            System.out.println("3. Collect from Owner");
            System.out.println("4. Collect from Size");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    MaintenanceDAO.collectFromAll();
                    break;
                case 2:
                    sc.nextLine();
                    System.out.print("Enter Site Type: ");
                    String type = sc.nextLine();
                    MaintenanceDAO.collectFromType(type);
                    break;
                case 3:
                    System.out.print("Enter Owner ID: ");
                    int ownerId = sc.nextInt();
                    MaintenanceDAO.collectFromOwner(ownerId);
                    break;
                case 4:
                    System.out.print("Enter Minimum Area: ");
                    double area = sc.nextDouble();
                    MaintenanceDAO.collectFromSize(area);
                    break;
                case 5:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 5);
    }

    static void maintenanceReportsMenu() {
        int choice;
        do {
            System.out.println("\n--- MAINTENANCE REPORTS ---");
            System.out.println("1. View All Transactions");
            System.out.println("2. View by Owner");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    MaintenanceDAO.viewAllTransactions();
                    break;
                case 2:
                    System.out.print("Enter Owner ID: ");
                    int ownerId = sc.nextInt();
                    MaintenanceDAO.viewTransactionsByOwner(ownerId);
                    break;
                case 3:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
    }
}
