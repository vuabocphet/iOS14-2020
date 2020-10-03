package com.developer.phimtatnhanh.ads.inter.cachetime;

import android.content.Context;
import android.util.Log;

import java.security.InvalidParameterException;

public abstract class FixedDelayJob extends Job {

    private int fixedDelay;

    public FixedDelayJob(Context context, String jobId, int fixedDelay) {
        super(context, jobId);
        if (fixedDelay <= 0) {
            throw new InvalidParameterException("Invalid Fixed Delay Value. Must be > 0");
        }
        this.fixedDelay = fixedDelay;
    }

    @Override
    public void execute() {
        if (!this.mustExecute()) {
            Log.e(this.tag(), "[" + this.preferenceJobKey + "] Skip Job Execute...");
            this.skip();
            return;
        }
        Log.i(this.tag(), "[" + this.preferenceJobKey + "] Execute Job...");
        this.scheduleNext();
        this.task();
    }

    @Override
    public boolean mustExecute() {
        return this.preferences.getInt(this.preferenceJobKey, 0) % this.fixedDelay == 0;
    }

    @Override
    public void scheduleNext() {
        int val = this.preferences.getInt(this.preferenceJobKey, 0);
        this.editor.putLong(this.preferenceJobKey, val + 1).apply();
    }

    @Override
    protected String prefix() {
        return "fixed_delay";
    }

    @Override
    protected String tag() {
        return "[FixedDelayJob]";
    }
}
