package job.tracking.cases;

import job.tracking.service.ApplicationService;
import job.tracking.service.ApplicationStatusService;

import java.time.LocalDate;
import java.util.Scanner;

public class ApplicationCases {

    private static final Scanner sc = new Scanner(System.in);

    public static void createApplication(ApplicationService applicationService) {
        System.out.print("UserID: ");
        String userId = sc.nextLine();
        System.out.print("Title(Required): ");
        String title = sc.nextLine();
        System.out.print("Company Name(Required): ");
        String companyName = sc.nextLine();
        System.out.print("jobType(Required): ");
        String jobTYpe = sc.nextLine();
        System.out.print("Source: ");
        String source = sc.nextLine();
        System.out.print("Offered Salary: ");
        String offeredSalary = sc.nextLine();
        System.out.print("Notes: ");
        String notes = sc.nextLine();
        try {
            applicationService.createApplication(title, companyName, jobTYpe, source, (offeredSalary != null) ? Integer.parseInt(offeredSalary): -1,
                    notes, LocalDate.now(), Integer.parseInt(userId));
        } catch (NumberFormatException e) {
            System.out.println("Error Creating Application " + e.getMessage() + ", userID and OfferedSalary must be an integer.");
        }
    }

    public static void deleteApplication(ApplicationService applicationService) {
        System.out.print("ApplicationID: ");
        String id = sc.nextLine();
        try {
            applicationService.deleteApplication(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            System.out.println("Error Deleting Application " + e.getMessage() + ", applicationID must be an integer.");
        }
    }

    public static void printAllApplications(ApplicationService applicationService) {
        applicationService.printAllApplications();
    }

    public static void updateApplicationStatus(ApplicationStatusService statusService) {
        System.out.print("ApplicationID: ");
        String id = sc.nextLine();
        System.out.print("New Status: ");
        String status = sc.nextLine();
        try {
            statusService.updateApplicationStatus(Integer.parseInt(id), status);
        } catch (NumberFormatException e) {
            System.out.println("Error Updating Application Status" + e.getMessage() + ", applicationID must be an integer.");
        }
    }

    public static void applicationStatusHistory(ApplicationStatusService statusService) {
        System.out.print("ApplicationID: ");
        String id = sc.nextLine();
        try {
            statusService.getApplicationStatusHistory(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            System.out.println("Error Fetching Application Status History " + e.getMessage() + ", applicationID must be an integer.");
        }
    }

    public static void bulkDeleteApplications(ApplicationService applicationService) {
        System.out.print("userId: ");
        String userId = sc.nextLine();
        System.out.print("Comma Separated Application Ids: ");
        String ids = sc.nextLine();
        try {
            applicationService.bulkDeleteApplications(Integer.parseInt(userId), ids);
        } catch (NumberFormatException e) {
            System.out.println("Error Fetching Application Status History " + e.getMessage() + ", applicationID must be an integer.");
        }
    }
}
