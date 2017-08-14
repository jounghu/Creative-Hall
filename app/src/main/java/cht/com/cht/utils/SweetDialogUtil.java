package cht.com.cht.utils;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2016/11/18.
 */
public class SweetDialogUtil {
    /**
     * @param context 上下文
     * @param type 对话框类型
     * @param title  对话框标题
     * @param Content 对话框内容
     */
    public static void showSweetDialog(Context context,int type,String title,String Content){
        new SweetAlertDialog(context,type)
                .setTitleText(title)
                .setContentText(Content)
                .show();

    }
}
