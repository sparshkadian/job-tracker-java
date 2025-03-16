package job.tracking.service;

import jakarta.persistence.EntityManager;
import job.tracking.ApplicationStatus;

import java.time.LocalDate;

public class ApplicationStatusService {

    private final EntityManager em;
    private final ApplicationService applicationService;

    public ApplicationStatusService(EntityManager em, ApplicationService applicationService) {
        this.em = em;
        this.applicationService = applicationService;
    }

    public void updateApplicationStatus(int applicationId, String status) {
        applicationService.findApplication(applicationId).ifPresentOrElse(application -> {
            ApplicationStatus applicationStatus = new ApplicationStatus(UtilService.parseApplicationStatus(status), LocalDate.now().plusMonths(1), application);
            executeTransaction(() -> em.persist(applicationStatus));
        }, () -> System.out.printf("Application with ID: %d does not exist.%n", applicationId));
    }

    public void getApplicationStatusHistory(int applicationId) {
        applicationService.findApplication(applicationId).ifPresentOrElse(application -> application.getApplicationStatusList().stream().sorted().forEach(System.out::println),
                () -> System.out.printf("Application with ID: %d does not exist.%n", applicationId));
    }

    public void executeTransaction(Runnable action) {
        var transaction = em.getTransaction();
        try {
            transaction.begin();
            action.run();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }
}
