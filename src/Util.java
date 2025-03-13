import java.util.function.Function;

public class Util {

    private Util() {}

    public static Application.JobType parseJobType(String jobType) {
        Function<String, Application.JobType> jobTypeFunction = type -> {
            String[] words = jobType.toUpperCase().split(" ");
            if (words.length > 1) {
                return Application.JobType.valueOf(String.join("_", words));
            } else return Application.JobType.valueOf(words[0]);
        };
        return jobTypeFunction.apply(jobType);
    }


    public static ApplicationStatus.Status parseApplicationStatus(String status) {
        Function<String, ApplicationStatus.Status> statusFunction = s -> {
            String[] words = s.toUpperCase().split(" ");
            if (words.length > 1) {
                return ApplicationStatus.Status.valueOf(String.join("_", words));
            }
            return ApplicationStatus.Status.valueOf(words[0]);
        };
        return statusFunction.apply(status);
    }
}