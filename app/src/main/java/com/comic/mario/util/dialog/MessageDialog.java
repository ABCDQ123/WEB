package com.comic.mario.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.comic.mario.R;

public class MessageDialog extends Dialog {

    private View view;
    private Context context;

    public MessageDialog(@NonNull Context context) {
        super(context, R.style.UZMDialog);
        this.context = context;
    }

    private TextView tv_dialog_message_title;
    private TextView tv_dialog_message_cancel;
    private TextView tv_dialog_message_sure;
    private String title;
    private String sure;
    private String cancel;

    private MessageDialogInterface messageDialogInterface;
    private MessageDialogCancelInterface messageDialogCancelInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_message, null);
        setContentView(view);
        tv_dialog_message_title = findViewById(R.id.tv_dialog_message_title);
        tv_dialog_message_cancel = findViewById(R.id.tv_dialog_message_cancel);
        tv_dialog_message_sure = findViewById(R.id.tv_dialog_message_sure);
        tv_dialog_message_title.setText(title);
        tv_dialog_message_sure.setText(sure);
        tv_dialog_message_cancel.setText(cancel);
        tv_dialog_message_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageDialogInterface.goSure();
                dismiss();
            }
        });
        tv_dialog_message_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageDialogCancelInterface != null)
                    messageDialogCancelInterface.goCancel();
                dismiss();
            }
        });
    }

    public void setText(String title, String sure, String cancel) {
        this.title = title;
        this.sure = sure;
        this.cancel = cancel;
    }

    public void setOnClick(MessageDialogInterface messageDialogInterface) {
        this.messageDialogInterface = messageDialogInterface;
    }

    public void setOnClick(MessageDialogCancelInterface messageDialogCancelInterface) {
        this.messageDialogCancelInterface = messageDialogCancelInterface;
    }

    public interface MessageDialogInterface {
        void goSure();
    }

    public interface MessageDialogCancelInterface {
        void goCancel();
    }


}
