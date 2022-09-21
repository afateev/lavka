package ru.ip_fateev.lavka.Inventory;

import static android.app.job.JobScheduler.RESULT_SUCCESS;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class DownloadDataJobService extends JobService {
    public static int JOB_ID = 1001;

    public DownloadDataJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        scheduleDownloadDataJob(getApplicationContext());

        DownloadService.startActionDownload(getApplicationContext(), "", "");

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        scheduleDownloadDataJob(getApplicationContext());

        return false;
    }

    public static void scheduleDownloadDataJob(Context context) {
        ComponentName jobService = new ComponentName(context, DownloadDataJobService.class);
        JobInfo.Builder jobBuilder = new JobInfo.Builder(JOB_ID, jobService);
        jobBuilder.setMinimumLatency(TimeUnit.SECONDS.toMillis(10));
        jobBuilder.setOverrideDeadline(TimeUnit.SECONDS.toMillis(60));
        jobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        jobBuilder.setRequiresDeviceIdle(false);
        jobBuilder.setRequiresCharging(false);
        jobBuilder.setBackoffCriteria(TimeUnit.SECONDS.toMillis(10), JobInfo.BACKOFF_POLICY_LINEAR);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int result = jobScheduler.schedule(jobBuilder.build());

        if (result == RESULT_SUCCESS) {
            //Toast.makeText(context, "Download data job scheduled.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "ERROR while schedule download data job.", Toast.LENGTH_SHORT).show();
        }
    }
}