package com.developer.memory.junk.callback;


import com.developer.memory.junk.model.JunkInfo;

import java.util.ArrayList;

public interface IScanCallback {

    void onStartJunk();

    void onStopJunk();

    void onProgress(JunkInfo info);

    void onProgressCache(JunkInfo info);

    void onProgressApk(JunkInfo info);

    void onProgressTmp(JunkInfo info);

    void onProgressLog(JunkInfo info);

    void onProgressOther(JunkInfo info);

    void onFinish();

    void onErrorJunk(Throwable throwable);
}
