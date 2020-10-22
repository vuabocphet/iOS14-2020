package com.developer.memory.junk.model;

import java.util.ArrayList;

public class JunkGroup {
    public static final int GROUP_PROCESS = 0;
    public static final int GROUP_CACHE = 1;
    public static final int GROUP_APK = 2;
    public static final int GROUP_TMP = 3;
    public static final int GROUP_LOG = 4;
    public static final int GROUP_ADV = 5;
    public static final int GROUP_OTHER = 6;

    public String mName;
    public long mSize;
    public ArrayList<JunkInfo> mChildren;
}
