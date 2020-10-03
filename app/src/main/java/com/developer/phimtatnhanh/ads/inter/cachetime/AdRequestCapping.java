package com.developer.phimtatnhanh.ads.inter.cachetime;

public final class AdRequestCapping {

    private Job job;

    private AdRequestCapping(Job job) {
        this.job = job;
    }

    public static AdRequestCapping create(Job job) {
        return new AdRequestCapping(job);
    }

    public void schedule() {
        this.job.execute();
    }
}
