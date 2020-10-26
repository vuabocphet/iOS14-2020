package com.developer.memory.junk.callback;


import com.developer.memory.junk.model.JunkInfo;

import java.util.ArrayList;

public interface IScanCallback {

    void onCreateJunk(int typeScanJunk) throws Exception;

    void onStartJunk(int typeScanJunk) throws Exception;

    void onCompleteJunk(int typeScanJunk) throws Exception;

    void onStopJunk() throws Exception;

    void onProgressCache(JunkInfo info) throws Exception;

    void onProgressApk(JunkInfo info) throws Exception;

    void onProgressTmp(JunkInfo info) throws Exception;

    void onProgressLog(JunkInfo info) throws Exception;

    void onProgressOther(JunkInfo info) throws Exception;

    void onFinish() throws Exception;

    void onErrorJunk(Throwable throwable) throws Exception;

}
