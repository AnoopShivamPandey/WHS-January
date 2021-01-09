package com.apk_home_service.customer.UI.custom;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.utill.CommonUtill;


/**
 * Created by Admin on 1/10/2018.
 */

public class OfferDialog extends Dialog {


    public enum confirmTask {
        NONE, ORDER_NOW
    }

    ;

    ConfirmationDialogListener confirmationDialogListener;
    confirmTask task;
    TextView yesBtn;
    ImageView noBtn;
    String yesStr = "Confirm";
    boolean isConfirm = true;

    public OfferDialog(@NonNull Context context,
                       ConfirmationDialogListener confirmationDialogListener,
                       confirmTask task) {
        super(context);
        this.confirmationDialogListener = confirmationDialogListener;
        this.task = task;
        init(context);
    }

    public OfferDialog(@NonNull Context context,
                       ConfirmationDialogListener confirmationDialogListener,
                       confirmTask task,String yesStr, boolean isConfirm) {
        super(context);
        this.confirmationDialogListener = confirmationDialogListener;
        this.task = task;
        this.isConfirm = isConfirm;
        this.yesStr = yesStr;
        init(context);
    }


    public OfferDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public OfferDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected OfferDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.offer_dialog_layout);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        yesBtn = (TextView) findViewById(R.id.confirmTxt);
        noBtn = (ImageView) findViewById(R.id.closeImg);
        yesBtn.setText(yesStr);

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
                CommonUtill.print("yesBtn ==" + task);
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
