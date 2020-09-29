package com.developer.phimtatnhanh.util;


import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.app.AppContext;

public class PixelUtil {


    public static int getDimenPixelFromPosition(int i) {
        switch (i) {
            case 1:
                return getDimenPixel(R.dimen._1sdp);
            case 2:
                return getDimenPixel(R.dimen._2sdp);
            case 3:
                return getDimenPixel(R.dimen._3sdp);
            case 4:
                return getDimenPixel(R.dimen._4sdp);
            case 5:
                return getDimenPixel(R.dimen._5sdp);
            case 6:
                return getDimenPixel(R.dimen._6sdp);
            case 7:
                return getDimenPixel(R.dimen._7sdp);
            case 8:
                return getDimenPixel(R.dimen._8sdp);
            case 9:
                return getDimenPixel(R.dimen._9sdp);
            case 10:
                return getDimenPixel(R.dimen._10sdp);
            case 11:
                return getDimenPixel(R.dimen._11sdp);
            case 12:
                return getDimenPixel(R.dimen._12sdp);
            case 13:
                return getDimenPixel(R.dimen._13sdp);
            case 14:
                return getDimenPixel(R.dimen._14sdp);
            case 15:
                return getDimenPixel(R.dimen._15sdp);
            case 16:
                return getDimenPixel(R.dimen._16sdp);
            case 17:
                return getDimenPixel(R.dimen._17sdp);
            case 18:
                return getDimenPixel(R.dimen._18sdp);
            case 19:
                return getDimenPixel(R.dimen._19sdp);
            case 20:
                return getDimenPixel(R.dimen._20sdp);
            case 21:
                return getDimenPixel(R.dimen._21sdp);
            case 22:
                return getDimenPixel(R.dimen._22sdp);
            case 23:
                return getDimenPixel(R.dimen._23sdp);
            case 24:
                return getDimenPixel(R.dimen._24sdp);
            case 25:
                return getDimenPixel(R.dimen._25sdp);
            case 26:
                return getDimenPixel(R.dimen._26sdp);
            case 27:
                return getDimenPixel(R.dimen._27sdp);
            case 28:
                return getDimenPixel(R.dimen._28sdp);
            case 29:
                return getDimenPixel(R.dimen._29sdp);
            case 30:
                return getDimenPixel(R.dimen._30sdp);
            case 31:
                return getDimenPixel(R.dimen._31sdp);
            case 32:
                return getDimenPixel(R.dimen._32sdp);
            case 33:
                return getDimenPixel(R.dimen._33sdp);
            case 34:
                return getDimenPixel(R.dimen._34sdp);
            case 35:
                return getDimenPixel(R.dimen._35sdp);
            case 36:
                return getDimenPixel(R.dimen._36sdp);
            case 37:
                return getDimenPixel(R.dimen._37sdp);
            case 38:
                return getDimenPixel(R.dimen._38sdp);
            case 39:
                return getDimenPixel(R.dimen._39sdp);
            case 40:
                return getDimenPixel(R.dimen._40sdp);
            case 41:
                return getDimenPixel(R.dimen._41sdp);
            case 42:
                return getDimenPixel(R.dimen._42sdp);
            case 43:
                return getDimenPixel(R.dimen._43sdp);
            case 44:
                return getDimenPixel(R.dimen._44sdp);
            case 45:
                return getDimenPixel(R.dimen._45sdp);
            case 46:
                return getDimenPixel(R.dimen._46sdp);
            case 47:
                return getDimenPixel(R.dimen._47sdp);
            case 48:
                return getDimenPixel(R.dimen._48sdp);
            case 49:
                return getDimenPixel(R.dimen._49sdp);
            case 50:
                return getDimenPixel(R.dimen._50sdp);
        }
        return 0;
    }

    private static int getDimenPixel(int p) {
        return AppContext.get().getContext().getResources().getDimensionPixelOffset(p);
    }

}
