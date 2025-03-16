package job.tracking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import job.tracking.service.ApplicationService;
import job.tracking.service.ApplicationStatusService;
import job.tracking.service.UserService;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        try(EntityManagerFactory emf = Persistence.createEntityManagerFactory("job-tracker");
            EntityManager em = emf.createEntityManager();
        ) {

            UserService userService = new UserService(em);
            ApplicationService applicationService = new ApplicationService(em, userService);
            ApplicationStatusService statusService = new ApplicationStatusService(em, applicationService);

            boolean exit = false;
            printMenu();
            do {
                System.out.print("Enter your Choice: ");
                String choice = scanner.nextLine().toLowerCase();
                switch (choice) {
                    case "1" -> createUserCase(userService);
                    case "2" -> updateAccountDetailsCase(userService);
                    case "3" -> updatePasswordCase(userService);
                    case "4" -> printUserApplicationsCase(applicationService);
                    case "5" -> deleteAccountCase(userService);
                    case "6" -> printAllUsersCase(userService);
                    case "m", "menu" -> printMenu();
                    case "q", "quit" -> {
                        exit = true;
                        System.out.println("Shutting Down..");
                    }
                    default -> System.out.println("Invalid Choice");
                }
            } while (!exit);

        } catch (Exception e) {
            System.out.println("An Error Occurred: " + e.getMessage());
        }
    }

    public static void createUserCase(UserService userService) {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        userService.addUser(name, email, password);
    }

    public static void updateAccountDetailsCase(UserService userService) {
        class UserDetails {
            public static int getUserId() throws NumberFormatException{
                System.out.print("UserID: ");
                String userId = scanner.nextLine();
                return Integer.parseInt(userId);
            }
            public static String getName() {
                System.out.print("New Name: ");
                return scanner.nextLine();
            }
            public static String getAvatar() {
                System.out.print("New AvatarUrl: ");
                return scanner.nextLine();
            }
        }
        System.out.println("""
                    1.) Update name
                    2.) update Avatar
                    3.) update Both""");
        System.out.print("Enter Choice: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> {
                try {
                    userService.changeName(UserDetails.getName(), UserDetails.getUserId());
                } catch (NumberFormatException e) {
                    System.out.println("Error updating Name: " + e.getMessage() + ", userId must be an integer.");
                }
            }
            case "2" -> {
                try {
                    userService.changeAvatar(UserDetails.getAvatar(), UserDetails.getUserId());
                } catch (NumberFormatException e) {
                    System.out.println("Error updating avatar: " + e.getMessage() + ", userId must be an integer.");
                }
            }
             case "3" -> {
                 try {
                     userService.changeName(UserDetails.getName(), UserDetails.getUserId()).changeAvatar(UserDetails.getAvatar(), UserDetails.getUserId());
                 } catch (NumberFormatException e) {
                     System.out.println("Error updating User: " + e.getMessage() + ", userId must be an integer.");
                 }
             }
            default -> System.out.println("Invalid Choice");
        }
    }

    public static void updatePasswordCase(UserService userService) {
        System.out.print("UserID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Old Password: ");
        String oldPassword = scanner.nextLine();
        System.out.print("Enter New Password: ");
        String newPassword = scanner.nextLine();
       try {
           userService.changePassword(oldPassword, newPassword, Integer.parseInt(userId));
       } catch (NumberFormatException e){
           System.out.println("Error Changing Password " + e.getMessage() + ", userID must be an integer.");
       }
    }

    public static void printUserApplicationsCase(ApplicationService applicationService) {
        System.out.print("UserID: ");
        String userId = scanner.nextLine();
        try {
            applicationService.getUserApplications(Integer.parseInt(userId));
        } catch (NumberFormatException e) {
            System.out.println("Error Deleting account: " + e.getMessage() + ", UserId must be an integer");
        }
    }

    public static void deleteAccountCase(UserService userService) {
        System.out.print("UserID: ");
        String userId = scanner.nextLine();
        try {
            userService.deleteAccount(Integer.parseInt(userId));
        } catch (NumberFormatException e) {
            System.out.println("Error Deleting account: " + e.getMessage() + ", UserId must be an integer");
        }
    }

    public static void printAllUsersCase(UserService userService) {
        userService.printUsers();
    }

    public static void printMenu() {
        String menu = """
                    User Functionalities                        Application Functionalities
                    1.) Create a User                           Coming Soon
                    2.) Update Account Details
                    3.) Change Password
                    4.) Print User Applications
                    5.) Delete Account
                    6.) Print all Users
                    Press M to view Menu
                    Press Q to quit
                    """;
        System.out.print(menu);
    }
}
