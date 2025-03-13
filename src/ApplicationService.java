import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;

import java.time.LocalDate;
import java.util.Optional;

public class ApplicationService {

    private final EntityManager em;
    private final UserService userService;

    public ApplicationService(EntityManager em, UserService userService) {
        this.em = em;
        this.userService = userService;
    }

    public void createApplication(String title, String companyName, String jobType , String source, int offeredSalary, String notes, LocalDate applyDate, int userId) {
//        userService.findUser(userId).ifPresent(user -> {
//            String query = "SELECT a FROM applications as a WHERE title = :title AND company_name = :companyName AND apply_date = :applyDate WHERE user_id = :userId";
//            try {
//                em.createNativeQuery(query, Application.class).setParameter("title", title)
//                        .setParameter("companyName", companyName).setParameter("applyDate", applyDate).getSingleResult();
//                System.out.println("Application already exists");
//            } catch (NoResultException e) {
//                Application application = new Application(title, companyName, Util.parseJobType(jobType), source,
//                        offeredSalary, notes, applyDate, user);
//
//                var transaction = em.getTransaction();
//                try {
//                    transaction.begin();
//                    em.persist(application);
//                    String fetchApplication = "SELECT a FROM Application a ORDER BY a.id DESC";
//                    var newApplication = em.createQuery(fetchApplication, Application.class).setMaxResults(1).getSingleResult();
//                    System.out.println(newApplication);
//                    ApplicationStatus applicationStatus = new ApplicationStatus(newApplication.getApplyDate(), newApplication);
//                    em.persist(applicationStatus);
//                    transaction.commit();
//                } catch (Exception exc) {
//                    transaction.rollback();
//                    System.out.println("Failed to create application: " + exc.getMessage());
//                }
//            }
//        });

        userService.findUser(userId).ifPresentOrElse(user -> {
            Application application = new Application(title, companyName, Util.parseJobType(jobType), source,
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
        }, () -> System.out.printf("User with ID: %d does not exist", userId));

    }

    public void deleteApplication(int applicationId) {
        findApplication(applicationId).ifPresentOrElse(application -> {
            executeTransaction(() -> em.remove(application));
            System.out.printf("Application with ID: %d has been deleted", applicationId);
        }, () -> System.out.printf("Application with ID: %d does not exist.", applicationId));
    }

    public void getUserApplications(int userId) {
        userService.findUser(userId).ifPresentOrElse(user -> user.getApplications().stream().sorted().forEach(System.out::println),
                () -> System.out.printf("User with Id: %d does not exist.", userId));
    }

    public void printAllApplications() {
        String query = "Select a.id as id, a.title as title, a.companyName as company, a.jobType as jobType, a.source as source, " +
                "a.offeredSalary as salary, a.applyDate as date FROM Application as a";
        var applications = em.createQuery(query, Tuple.class).getResultList();
        System.out.printf("%-5s%-20s%-20s%-15s%-50s%-20s%s%n","ID", "Title", "Company Name", "Job Type", "Source", "Offered Salary", "Apply Date");
        applications.forEach(application -> System.out.printf("%-5s%-20s%-20s%-15s%-50s%-20s%s%n",application.get("id"), application.get("title"),
                application.get("company"), application.get("jobType"), application.get("source"), application.get("salary"), application.get("date")));
    }

    public Optional<Application> findApplication(int applicationId) {
        return Optional.ofNullable(em.find(Application.class, applicationId));
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
