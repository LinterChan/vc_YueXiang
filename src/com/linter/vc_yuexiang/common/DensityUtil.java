package com.linter.vc_yuexiang.common;

import android.content.Context;

public class DensityUtil {
    public static int sp2px(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale * 0.4); 
    }
}
