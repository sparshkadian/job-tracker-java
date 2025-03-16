package job.tracking.service;

import job.tracking.JobStatus;
import job.tracking.JobType;

import java.util.HashMap;
import java.util.Map;

public class UtilService {

    private UtilService() {}

    public static JobType parseJobType(String jobType) {
        Map<String, JobType> jobTypeMap = new HashMap<>();
        jobTypeMap.put("Full Time", JobType.FULL_TIME);
        jobTypeMap.put("Internship", JobType.INTERNSHIP);
        jobTypeMap.put("Part Time", JobType.PART_TIME);
        jobTypeMap.put("Contract", JobType.CONTRACT);

        JobType result = jobTypeMap.get(jobType);
        if(result == null) throw new RuntimeException("Invalid Job Type");
        return result;
    }


    public static JobStatus parseApplicationStatus(String jobStatus) {
        Map<String, JobStatus> jobStatusMap = new HashMap<>();
        jobStatusMap.put("Applied", JobStatus.APPLIED);
        jobStatusMap.put("In Progress", JobStatus.IN_PROGRESS);
        jobStatusMap.put("Offer Received", JobStatus.OFFER_RECEIVED);
        jobStatusMap.put("Rejected", JobStatus.REJECTED);

        JobStatus result = jobStatusMap.get(jobStatus);
        if(result == null) throw new RuntimeException("Invalid Job Status");
        return result;
    }
}