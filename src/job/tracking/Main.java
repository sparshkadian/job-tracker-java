package job.tracking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import job.tracking.cases.ApplicationCases;
import job.tracking.cases.UserCases;
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
                    case "1" -> UserCases.createUser(userService);
                    case "2" -> UserCases.updateAccountDetails(userService);
                    case "3" -> UserCases.updatePassword(userService);
                    case "4" -> UserCases.printUserApplications(applicationService);
                    case "5" -> UserCases.deleteAccount(userService);
                    case "6" -> UserCases.printAllUsers(userService);
                    case "7" -> ApplicationCases.createApplication(applicationService);
                    case "8" -> ApplicationCases.deleteApplication(applicationService);
                    case "9" -> ApplicationCases.updateApplicationStatus(statusService);
                    case "10" -> ApplicationCases.applicationStatusHistory(statusService);
                    case "11" -> ApplicationCases.bulkDeleteApplications(applicationService);
                    case "12" -> ApplicationCases.printAllApplications(applicationService);
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

    public static void printMenu() {
        String menu = """
                    User Functionalities                        Application Functionalities
                    1.) Create a User                           7.) Create Application
                    2.) Update Account Details                  8.) Delete Application
                    3.) Change Password                         9.) Update Application Status
                    4.) Print User Applications                 10.) Get Application Status History
                    5.) Delete Account                          11.) Bulk Delete Applications
                    6.) Print all Users                         12.) Print All Applications
                    Press M to view Menu
                    Press Q to quit
                    """;
        System.out.print(menu);
    }
}
