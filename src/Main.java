import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {

    public static void main(String[] args) {

        try(EntityManagerFactory emf = Persistence.createEntityManagerFactory("job-tracker");
            EntityManager em = emf.createEntityManager();
        ) {

            UserService userService = new UserService(em);
            ApplicationStatusService statusService = new ApplicationStatusService(em);
            ApplicationService applicationService = new ApplicationService(em, userService);

//            applicationService.deleteApplication(4);

//            statusService.updateApplicationStatus(4, "Rejected");

//            statusService.getApplicationStatusHistory(4);

//            applicationService.getUserApplications(1);

            applicationService.printAllApplications();

//            applicationService.getUserApplications(1);

//            applicationService.createApplication("Cyber Security Specialist", "Apple", "Full Time",
//                    "Protect systems from cyber threats", 25, null, LocalDate.now(), 1);


//            statusService.updateApplicationStatus(10, "Rejected");

        } catch (Exception e) {
            System.out.println("An Error Occurred: " + e.getMessage());
        }
    }
}
