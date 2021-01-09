package com.apk_home_service.customer.UI.custom;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.utill.CommonUtill;


/**
 * Created by Admin on 1/10/2018.
 */

public class MessageDialog extends Dialog {


    public enum confirmTask {
        LOGOUT, EXIT, NONE, COMING_SOON
    }

    ;

    ConfirmationDialogListener confirmationDialogListener;
    confirmTask task;
    TextView dialogTitle, dialogMsg;
    TextView noBtn, yesBtn;
    String message, title, yesStr = "Confirm", noStr = "Cancel";
    boolean isCancel = true, isConfirm = true;

    public MessageDialog(@NonNull Context context,
                         ConfirmationDialogListener confirmationDialogListener,
                         confirmTask task, String message, String title) {
        super(context);
        this.confirmationDialogListener = confirmationDialogListener;
        this.task = task;
        this.message = message;
        this.title = title;
        init(context);
    }

    public MessageDialog(@NonNull Context context,
                         ConfirmationDialogListener confirmationDialogListener,
                         confirmTask task, String message, String title, String noStr,
                         String yesStr, boolean isCancel, boolean isConfirm) {
        super(context);
        this.confirmationDialogListener = confirmationDialogListener;
        this.task = task;
        this.message = message;
        this.title = title;
        this.isCancel = isCancel;
        this.isConfirm = isConfirm;
        this.yesStr = yesStr;
        this.noStr = noStr;
        init(context);
    }


    public MessageDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MessageDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected MessageDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.message_dialog_layout);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogTitle = (TextView) findViewById(R.id.titleTxt);
        dialogMsg = (TextView) findViewById(R.id.msgTxt);
        noBtn = (TextView) findViewById(R.id.cancelTxt);
        yesBtn = (TextView) findViewById(R.id.confirmTxt);

//        dialogMsg.setTextSize(context.getResources().getDimension(R.dimen.small_txt));

        if (task == confirmTask.COMING_SOON) {
            dialogMsg.setTextSize(context.getResources().getDimension(R.dimen.small_txt));
            dialogMsg.setTextColor(context.getResources().getColor(R.color.colorGreen));
        }

//        else {
//            dialogMsg.setTextSize(context.getResources().getDimension(R.dimen.normal_txt));
//            dialogMsg.setTextColor(context.getResources().getColor(R.color.colorGrey));
//        }

        dialogMsg.setText(message);
        dialogTitle.setText(title);
        noBtn.setText(noStr);
        yesBtn.setText(yesStr);


        if (isCancel)
            noBtn.setVisibility(View.VISIBLE);
        else
            noBtn.setVisibility(View.GONE);


        if (isConfirm)
            yesBtn.setVisibility(View.VISIBLE);
        else
            yesBtn.setVisibility(View.GONE);


        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                CommonUtill.print("yesBtn ==" + task);
                confirmationDialogListener.onYesButtonClicked(task);
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                CommonUtill.print("noBtn ==" + task);
                confirmationDialogListener.onNoButtonClicked(task);
            }
        });
        show();
    }

    public interface ConfirmationDialogListener {
        public void onYesButtonClicked(confirmTask task);

        public void onNoButtonClicked(confirmTask task);
    }
}
