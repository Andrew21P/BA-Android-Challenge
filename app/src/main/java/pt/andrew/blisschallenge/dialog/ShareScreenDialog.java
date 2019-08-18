package pt.andrew.blisschallenge.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import pt.andrew.blisschallenge.R;

/**
 * Created by andrew.fernandes on 18/08/2019
 */
public class ShareScreenDialog {

    Dialog _shareDialog;

    public void initDialog(Context context) {
        _shareDialog= new Dialog(context);
    }

    public void showDialog(String dialogTitle) {
        _shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _shareDialog.setCancelable(false);
        _shareDialog.setContentView(R.layout.dialog_share_screen);
        TextView title = _shareDialog.findViewById(R.id.shareScreenTitle);
        title.setText(dialogTitle);

        TextView closeButton = _shareDialog.findViewById(R.id.shareScreenCloseButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _shareDialog.dismiss();
            }
        });

        _shareDialog.show();
    }

    public void setPositiveButton(View.OnClickListener onClickListener) {
        TextView sendButton = _shareDialog.findViewById(R.id.shareScreenSendButton);
        sendButton.setOnClickListener(onClickListener);
    }

    public String getEmail() {
        EditText email = _shareDialog.findViewById(R.id.shareScreenEmail);
        return email.getText().toString();
    }

    public void showLoader() {
        View loader = _shareDialog.findViewById(R.id.shareScreenLoader);
        View closeButton = _shareDialog.findViewById(R.id.shareScreenCloseButton);
        View sendButton = _shareDialog.findViewById(R.id.shareScreenSendButton);
        View email = _shareDialog.findViewById(R.id.shareScreenEmail);
        closeButton.setVisibility(View.GONE);
        sendButton.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        loader.bringToFront();
    }

    public void dismissDialog() {
        if (_shareDialog != null) {
            _shareDialog.dismiss();
        }
    }

    public void showResume(String resume) {
        View loader = _shareDialog.findViewById(R.id.shareScreenLoader);
        View okButton = _shareDialog.findViewById(R.id.shareScreenOkButton);
        okButton.setVisibility(View.VISIBLE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _shareDialog.dismiss();
            }
        });

        TextView resumeText = _shareDialog.findViewById(R.id.shareScreenResume);
        resumeText.setText(resume);
        resumeText.setVisibility(View.VISIBLE);

        loader.setVisibility(View.GONE);
    }
}
