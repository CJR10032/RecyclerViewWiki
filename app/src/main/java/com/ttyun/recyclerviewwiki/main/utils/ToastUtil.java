package com.ttyun.recyclerviewwiki.main.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ttyun.recyclerviewwiki.R;

/**
 * 创建者     CJR
 * 创建时间   2017-06-30 10:59
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ToastUtil {
    private static Toast sToast;

    public static void doToast(Context context, String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            //            sToast.getView().setBackgroundColor(0xffff9800);
            View toastView = sToast.getView();
            if (toastView != null) {
                TextView msgTv = (TextView) toastView.findViewById(android.R.id.message);
                if (msgTv != null) {
                    //                    msgTv.setBackgroundColor(0xffff9800);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) msgTv.getLayoutParams();
                    lp.gravity = Gravity.CENTER_VERTICAL;
                }
                if (toastView instanceof LinearLayout) {
                    LinearLayout container = (LinearLayout) toastView;
                    container.setOrientation(LinearLayout.HORIZONTAL);
                    ImageView imageView = new ImageView(context);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                    float density = displayMetrics.density;
                    int width = (int) (density * 25);
                    int height = (int) (density * 25);
                    if (layoutParams == null)
                        layoutParams = new LinearLayout.LayoutParams(width, height);
                    layoutParams.gravity = Gravity.CENTER_VERTICAL;
                    imageView.setLayoutParams(layoutParams);
                    imageView.setImageResource(R.mipmap.ic_launcher);
                    container.addView(imageView, 0);
                }
            }
        } else {
            sToast.setText(msg);
        }
        sToast.show();
    }
}
