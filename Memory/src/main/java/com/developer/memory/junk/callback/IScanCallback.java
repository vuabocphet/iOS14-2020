package com.developer.memory.junk.callback;


import com.developer.memory.junk.model.JunkInfo;

import java.util.ArrayList;

public interface IScanCallback {

    void onStartJunk();

    void onStopJunk();

    void onProgress(JunkInfo info);

    void onFinish(ArrayList<JunkInfo> junkInfos,ArrayList<JunkInfo> sysCaches);

    void onErrorJunk(Throwable throwable);
}
