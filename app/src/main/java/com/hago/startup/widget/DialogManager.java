package com.hago.startup.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hago.startup.R;
import com.hago.startup.bean.ApkInfo;
import com.hago.startup.db.bean.ResultInfo;
import com.hago.startup.widget.chooseApk.ChooseApkView;
import com.hago.startup.widget.result.ResultView;

import java.util.List;

/**
 * Created by huangzhilong on 18/9/24.
 */

public class DialogManager {

    private static final String TAG = "DialogLinkManager";

    public Dialog mDialog;
    public Context mContext;
    public AlertDialog.Builder mBuilder;


    public DialogManager(Context context) {
        mContext = context;
        mBuilder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        mDialog = mBuilder.create();
    }

    @TargetApi(17)
    public boolean checkActivityValid() {
        if (mContext == null) {
            return false;
        }
        if (mDialog != null && mDialog.getWindow() == null) {
            return false;
        }
        if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= 17 && mContext instanceof Activity && ((Activity) mContext).isDestroyed()) {
            return false;
        }
        return true;
    }

    public void showOkCancleCancelBigTips(String message, String tips, final OkCancelDialogListener l) {
        showOkCancleCancelBigTips(message, tips, "确认", 0, "取消", 0, true, l);
    }


    public void showOkCancleCancelBigTips(String message, String tips, String okLabel, int okLabelColor, String cancelLabel,
                                          int cancelLabelColor, boolean cancelable, final OkCancelDialogListener l) {
        if (!checkActivityValid()) {
            return;
        }
        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = mBuilder.create();

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_ok_cancel_label_dialog_big_message);

        TextView tip = window.findViewById(R.id.message);
        tip.setText(message);
        TextView message_tips = window.findViewById(R.id.message_tips);
        if (tips != null || tips != "") message_tips.setText(tips);
        TextView ok = window.findViewById(R.id.btn_ok);
        if (okLabelColor != 0) {
            ok.setTextColor(okLabelColor);
        }
        ok.setText(okLabel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (l != null) {
                    l.onOk();
                }
            }
        });

        TextView cancel = window.findViewById(R.id.btn_cancel);
        if (cancelLabelColor != 0) {
            cancel.setTextColor(cancelLabelColor);
        }
        cancel.setText(cancelLabel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (l != null) {
                    l.onCancel();
                }
            }
        });
    }

    public void showChooseApkVersionDialog(final ChooseDialogListener l) {
        if (!checkActivityValid()) {
            return;
        }
        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = mBuilder.create();

        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_choose_view_dialog);
        ChooseApkView view = window.findViewById(R.id.choose_view);
        view.setListener(new ChooseDialogListener() {
            @Override
            public void onCancel() {
                mDialog.dismiss();
                if (l != null) {
                    l.onCancel();
                }
            }

            @Override
            public void ok(List<ApkInfo> results) {
                mDialog.dismiss();
                if (l != null) {
                    l.ok(results);
                }
            }
        });
    }

    public void showResultDialog(List<ResultInfo> resultInfoList, boolean isTarget) {
        if (!checkActivityValid()) {
            return;
        }
        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = mBuilder.create();

        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_result_dialog);
        ResultView view = window.findViewById(R.id.result_view);
        view.setResultList(resultInfoList, isTarget);
    }

    public interface OkCancelDialogListener {

        void onCancel();

        void onOk();
    }


    public interface ChooseDialogListener {

        void onCancel();

        void ok(List<ApkInfo> results);
    }
}
