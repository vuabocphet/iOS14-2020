package com.developer.phimtatnhanh.ads.inter.cachetime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.security.InvalidParameterException;
import java.util.regex.Pattern;

public abstract class Job {

    private WeakReference<Context> contextWeakReference;
    protected SharedPreferences preferences;
    protected SharedPreferences.Editor editor;
    protected String jobId;
    protected String preferenceJobKey;

    @SuppressLint("CommitPrefEdits")
    protected Job(Context context, String jobId) {
        this.validateJobId(jobId);
        this.contextWeakReference = new WeakReference<>(context);
        this.jobId = jobId;
        this.preferenceJobKey = this.prefix() + "_" + this.jobId;
        this.preferences = this.contextWeakReference.get().getSharedPreferences("qrt_job", Context.MODE_PRIVATE);
        this.editor = this.preferences.edit();
    }

    public abstract void execute();

    protected abstract void task();

    protected abstract void skip();

    public abstract boolean mustExecute();

    public abstract void scheduleNext();

    protected abstract String prefix();

    protected abstract String tag();

    public String getJobId() {
        return this.jobId;
    }

    public Context getContext() {
        return this.contextWeakReference.get();
    }

    protected void validateJobId(String jobId) {
        if (TextUtils.isEmpty(jobId)) {
            throw new NullPointerException("Job ID must not null");
        }

        if (!Pattern.compile("^(\\w+_?)+").matcher(jobId).matches()) {
            throw new InvalidParameterException("Invalid Job ID. Pattern must be: [A-Za-z] with _ between words");
        }
    }
}
