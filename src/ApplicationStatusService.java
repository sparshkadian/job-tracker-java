import jakarta.persistence.EntityManager;

import java.time.LocalDate;

public class ApplicationStatusService {

    private final EntityManager em;

    public ApplicationStatusService(EntityManager em) {
        this.em = em;
    }

    public void updateApplicationStatus(int applicationId, String status) {
        Application application = em.find(Application.class, applicationId);
        if(application == null) {
            System.out.printf("Application with ID: %d does not exist.", applicationId);
            return;
        }

        ApplicationStatus applicationStatus = new ApplicationStatus(Util.parseApplicationStatus(status), LocalDate.now(), application);
        var transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(applicationStatus);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Failed to update Transaction: " + e.getMessage());
        }
    }

    public void getApplicationStatusHistory(int applicationId) {
        Application application = em.find(Application.class, applicationId);
        if(application == null) {
            System.out.printf("Application with ID: %d does not exist.", applicationId);
            return;
        }
        application.getApplicationStatusList().stream().sorted().forEach(System.out::println);
    }
}
