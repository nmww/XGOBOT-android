package com.luwu.xgobot.mActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.luwu.xgobot.R;
import com.luwu.xgobot.data.DataHelper;
import com.luwu.xgobot.data.RobotFunction;
import com.luwu.xgobot.mMothed.mToast;
import com.luwu.xgobot.weight.DebugDialog;

import static com.luwu.xgobot.mMothed.PublicMethod.hideBottomUIDialog;
import static com.luwu.xgobot.mMothed.PublicMethod.hideBottomUIMenu;
import static com.luwu.xgobot.mMothed.PublicMethod.localeLanguage;

public class DebugActivity extends AppCompatActivity {

    private ImageView debugBtnBack,debugImgMsg;
    private long mlastClickTime;
    private TextView debugBtnConfirm, debugBtnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debugnew);
        InitView();
        ShowDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBottomUIMenu(DebugActivity.this);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(DebugActivity1.this,"退出调试模式",Toast.LENGTH_SHORT).show();
//    }

    private void InitView() {
        BtnListener mListener = new BtnListener();
        debugBtnConfirm = findViewById(R.id.debugBtnConfirm);
        debugBtnFinish = findViewById(R.id.debugBtnFinish);
        debugBtnBack = findViewById(R.id.debugBtnBack);
        debugImgMsg= findViewById(R.id.debugImgMsg);
        debugBtnConfirm.setOnClickListener(mListener);
        debugBtnFinish.setOnClickListener(mListener);
        debugBtnBack.setOnClickListener(mListener);
        debugImgMsg.setOnClickListener(mListener);
        debugImgMsg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Intent intent = new Intent(DebugActivity.this, TestBtActivity.class);
//                startActivity(intent);
                return true;
            }
        });
    }
    private DebugDialog mDebug_dialog;
    private void ShowDialog() {
        if(mDebug_dialog == null){
            mDebug_dialog = new DebugDialog(this);
        }
        hideBottomUIDialog(mDebug_dialog);
        mDebug_dialog.show();
      /*  final AlertDialog dialog = new AlertDialog.Builder(DebugActivity.this).create();
        dialog.setContentView(R.layout.dialog_debug);
        //为了解决onResume不调用的问题
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideBottomUIMenu(DebugActivity.this);
            }
        });
        hideBottomUIDialog(dialog);
        TextView confirmtv = dialog.findViewById(R.id.debug_confirm_tv);
        confirmtv.setOnClickListener(v -> {
            dialog.dismiss();
        });
        *//*switch(localeLanguage){
            case "zh":
                dialog.setMessage("点击【进入标定模式】卸载舵机，此时您可以转动关节到指定位置\n点击【完成标定】将记录当前位置为初始位置，并恢复站姿");
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
            default:
                dialog.setMessage("Click [Calibrate] to unload the steering gear. At this time, you can rotate the joint to the specified position\n" +
                        "\n" +
                        "Click [Calibrated] to record the current position as the initial position and restore the standing posture");
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        }*//*
        dialog.setCancelable(true);
        dialog.show();*/
    }

    private class BtnListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.debugBtnConfirm:
                    RobotFunction.setDebugMode(true);
                    switch(localeLanguage){
                        case "zh":
                            mToast.show(DebugActivity.this,"已进入标定模式");
                            break;
                        default:
                            mToast.show(DebugActivity.this,"Calibration mode on");
                    }
                    break;
                case R.id.debugBtnFinish:
                    if(!isFastDoubleClick()){
                        RobotFunction.setDebugMode(false);
                        switch(localeLanguage){
                            case "zh":
                                mToast.show(DebugActivity.this,"完成标定");
                                break;
                            default:
                                mToast.show(DebugActivity.this,"Calibrated");
                        }
                    }
                    break;
                case R.id.debugImgMsg:
                    ShowDialog();
                    break;
                case R.id.debugBtnBack:
                    DataHelper.addMessage(new byte[]{0x04, (byte) 0x02});//不保存退出
                    finish();
                    break;
            }
        }
    }

    //标定点击间隔为1s
    private boolean isFastDoubleClick(){
        long time = System.currentTimeMillis();
        long timeD = time - this.mlastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;   }
        this.mlastClickTime = time;
        return false;
    }
}
