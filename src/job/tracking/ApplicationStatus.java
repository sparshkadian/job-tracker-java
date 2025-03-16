package job.tracking;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "application_statuses")
public class ApplicationStatus implements Comparable<ApplicationStatus> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JobStatus jobStatus;

    @Column(name = "status_date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    public ApplicationStatus() {}

    public ApplicationStatus(JobStatus jobStatus, LocalDate date, Application application) {
        this.jobStatus = jobStatus;
        this.date = date;
        this.application = application;
    }

    public ApplicationStatus(LocalDate date, Application application) {
        this(JobStatus.APPLIED, date, application);
    }

    public JobStatus getStatus() {
        return jobStatus;
    }

    public LocalDate getDate() {
        return date;
    }


    @Override
    public int hashCode() {
        int result = getStatus().hashCode();
        result = 31 * result + getDate().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApplicationStatus that = (ApplicationStatus) o;
        return this.getStatus() == that.getStatus() &&
                this.getDate().equals(that.getDate());
    }

    @Override
    public String toString() {
        return "ApplicationStatus{" +
                "id=" + id +
                ", jobStatus=" + jobStatus +
                ", date=" + date +
                '}';
    }

    @Override
    public int compareTo(ApplicationStatus applicationStatus) {
        return this.date.compareTo(applicationStatus.date);
    }
}
