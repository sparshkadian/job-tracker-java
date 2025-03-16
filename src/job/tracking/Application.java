package job.tracking;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "applications")
public class Application implements Comparable<Application> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false)
    private JobType jobType;

    @Column(nullable = false)
    private String source;

    @Column(name = "offered_salary")
    private int offeredSalary;

    @Column
    private String notes;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ApplicationStatus> applicationStatusList;


    public Application() {}

    public Application(String title, String companyName, JobType jobType ,String source, int offeredSalary, String notes, LocalDate applyDate, User user) {
        this.title = title;
        this.companyName = companyName;
        this.jobType = jobType;
        this.source = source;
        this.offeredSalary = offeredSalary;
        this.notes = notes;
        this.applyDate = applyDate;
        this.user = user;
        this.applicationStatusList = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public JobType getJobType() {
        return jobType;
    }

    public String getSource() {
        return source;
    }

    public int getOfferedSalary() {
        return offeredSalary;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDate getApplyDate() {
        return applyDate;
    }

    public Set<ApplicationStatus> getApplicationStatusList() {
        return applicationStatusList;
    }

    public void addApplicationStatusList(ApplicationStatus applicationStatus) {
        if(applicationStatusList.add(applicationStatus)) {
            System.out.printf("New status added for %s.%n", companyName);
        } else {
            System.out.println("Status already Exists.");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, companyName, applyDate);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || this.getClass() != o.getClass()) return false;
        Application application = (Application) o;
        return Objects.equals(this.title, application.title) &&
                Objects.equals(this.companyName, application.companyName) &&
                Objects.equals(this.applyDate, application.applyDate);
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", companyName='" + companyName + '\'' +
                ", jobType=" + jobType +
                ", source='" + source + '\'' +
                ", offeredSalary=" + offeredSalary +
                ", notes='" + notes + '\'' +
                ", applyDate=" + applyDate +
                '}' + '\n';
    }

    @Override
    public int compareTo(Application application) {
        return this.applyDate.compareTo(application.applyDate);
    }
}
