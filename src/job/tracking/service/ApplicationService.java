package job.tracking.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import job.tracking.Application;
import job.tracking.ApplicationStatus;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public class ApplicationService {

    private final EntityManager em;
    private final UserService userService;

    private final String GET_ALL_APPLICATIONS = """ 
            Select a.id as id, a.title as title, a.companyName as company, a.jobType as jobType, a.source as source,
            a.offeredSalary as salary, a.applyDate as date FROM Application as a""";


    public ApplicationService(EntityManager em, UserService userService) {
        this.em = em;
        this.userService = userService;
    }

    public void createApplication(String title, String companyName, String jobType , String source, int offeredSalary, String notes, LocalDate applyDate, int userId) {
        userService.findUser(userId).ifPresentOrElse(user -> {
            Application application = new Application(title, companyName, UtilService.parseJobType(jobType), source,
                    offeredSalary, notes, applyDate, user);

            var transaction = em.getTransaction();
            try {
                transaction.begin();
                em.persist(application);
                String fetchApplication = "SELECT a FROM Application a ORDER BY a.id DESC";
                var newApplication = em.createQuery(fetchApplication, Application.class).setMaxResults(1).getSingleResult();
                ApplicationStatus applicationStatus = new ApplicationStatus(newApplication.getApplyDate(), newApplication);
                em.persist(applicationStatus);
                transaction.commit();
            } catch (Exception exc) {
                transaction.rollback();
                System.out.println("Failed to create application: " + exc.getMessage());
            }
        }, () -> System.out.printf("User with ID: %d does not exist.%n", userId));

    }

    public void deleteApplication(int applicationId) {
        findApplication(applicationId).ifPresentOrElse(application -> {
            executeTransaction(() -> em.remove(application));
            System.out.printf("Application with ID: %d has been deleted.%n", applicationId);
        }, () -> System.out.printf("Application with ID: %d does not exist.%n", applicationId));
    }

    public void getUserApplications(int userId) {
        userService.findUser(userId).ifPresentOrElse(user -> {
            Set<Application> applicationList = user.getApplications();
            if (applicationList.isEmpty()) System.out.printf("User With ID: %d has no Applications.%n", userId);
            else applicationList.stream().sorted().forEach(System.out::println);
            },() -> System.out.printf("User with Id: %d does not exist.%n", userId));
    }

    public void printAllApplications() {
        var applications = em.createQuery(GET_ALL_APPLICATIONS, Tuple.class).getResultList();
        if(applications.isEmpty()) {
            System.out.println("No Applications!!");
            return;
        }
        System.out.printf("%-5s%-20s%-20s%-15s%-50s%-20s%s%n","ID", "Title", "Company Name", "Job Type", "Source", "Offered Salary", "Apply Date");
        applications.forEach(application -> System.out.printf("%-5s%-20s%-20s%-15s%-50s%-20s%s%n",application.get("id"), application.get("title"),
                application.get("company"), application.get("jobType"), application.get("source"), application.get("salary"), application.get("date")));
    }

    public Optional<Application> findApplication(int applicationId) {
        return Optional.ofNullable(em.find(Application.class, applicationId));
    }

    public  void bulkDeleteApplications(int userId, String applicationIds) {
        userService.findUser(userId).ifPresentOrElse(user -> {
            String query = "DELETE FROM applications WHERE user_id = %d AND id IN (%s)".formatted(userId, applicationIds);
            executeTransaction(() -> em.createNativeQuery(query).executeUpdate());
        }, () -> System.out.printf("User with Id: %d does not exist.%n", userId));
    }

    public void executeTransaction(Runnable action) {
        var transaction = em.getTransaction();
        try {
            transaction.begin();
            action.run();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Error Executing Transaction: " + e.getMessage());
        }
    }
}
