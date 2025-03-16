package job.tracking.cases;

import job.tracking.service.ApplicationService;
import job.tracking.service.UserService;

import java.util.Scanner;

public class UserCases {

    private static final Scanner scanner = new Scanner(System.in);

    public static void createUser(UserService userService) {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        userService.addUser(name, email, password);
    }

    public static void updateAccountDetails(UserService userService) {
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

    public static void updatePassword(UserService userService) {
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

    public static void printUserApplications(ApplicationService applicationService) {
        System.out.print("UserID: ");
        String userId = scanner.nextLine();
        try {
            applicationService.getUserApplications(Integer.parseInt(userId));
        } catch (NumberFormatException e) {
            System.out.println("Error Deleting account: " + e.getMessage() + ", UserId must be an integer");
        }
    }

    public static void deleteAccount(UserService userService) {
        System.out.print("UserID: ");
        String userId = scanner.nextLine();
        try {
            userService.deleteAccount(Integer.parseInt(userId));
        } catch (NumberFormatException e) {
            System.out.println("Error Deleting account: " + e.getMessage() + ", UserId must be an integer");
        }
    }

    public static void printAllUsers(UserService userService) {
        userService.printUsers();
    }
}
