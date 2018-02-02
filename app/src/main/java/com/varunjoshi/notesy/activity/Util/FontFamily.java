package com.varunjoshi.notesy.activity.Util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by varun
 * on 29/8/17.
 */

public class FontFamily {

    private View child;
    private Typeface tfBold, tfLightItalic, tfLight, tfMedium, tfMediumItalic, tfRegular;

    public FontFamily(Context context) {
        tfBold = Typeface.createFromAsset(context.getAssets(), "Poppins-Bold.ttf");
        tfLightItalic = Typeface.createFromAsset(context.getAssets(), "Poppins-LightItalic.ttf");
        tfLight = Typeface.createFromAsset(context.getAssets(), "Poppins-Light.ttf");
        tfMedium = Typeface.createFromAsset(context.getAssets(), "Poppins-Medium.ttf");
        tfMediumItalic = Typeface.createFromAsset(context.getAssets(), "Poppins-MediumItalic.ttf");
        tfRegular = Typeface.createFromAsset(context.getAssets(), "Poppins-Regular.ttf");
    }

    public void setBoldFont(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    child = vg.getChildAt(i);
                    setBoldFont(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(tfBold);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLightFont(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    child = vg.getChildAt(i);
                    setLightFont(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(tfLight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLightItalicFont(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    child = vg.getChildAt(i);
                    setLightItalicFont(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(tfLightItalic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMediumFont(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    child = vg.getChildAt(i);
                    setMediumFont(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(tfMedium);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMediumItalicFont(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    child = vg.getChildAt(i);
                    setMediumItalicFont(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(tfMediumItalic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRegularFont(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    child = vg.getChildAt(i);
                    setRegularFont(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(tfRegular);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
