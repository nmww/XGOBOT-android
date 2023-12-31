package com.luwu.xgobot.mActivity;

import static com.luwu.xgobot.mMothed.PublicMethod.localeLanguage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.luwu.xgobot.BuildConfig;
import com.luwu.xgobot.R;
import com.luwu.xgobot.mActivity.main.XgoMainActivity;
import com.luwu.xgobot.socket.SocketManager;
import com.luwu.xgobot.socket.SocketStateListener;
import com.luwu.xgobot.socket.UdpClient;

import java.time.Duration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NetSettingActivity extends BaseActivity implements SocketStateListener {
    private static final String TAG = "NetSettingActivity";
    private EditText ipEdit,portEdit,cameraPortEdit;
    private TextView stateText,versionText;
    private Button connectBtn;
    private ImageView settingBtn,controlBtn;

    private static final boolean TEST = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        updateLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ipEdit = findViewById(R.id.edit_host);
        String host = SPUtils.getInstance().getString("host","");
        ipEdit.setText(host);

        settingBtn = findViewById(R.id.btn_setting);
        controlBtn = findViewById(R.id.btn_control);
        portEdit = findViewById(R.id.edit_tcp_port);
        cameraPortEdit = findViewById(R.id.edit_camera_port);
        connectBtn = findViewById(R.id.button_connect);
        findViewById(R.id.button_start).setOnClickListener(this::startBroadCastShow);
        findViewById(R.id.button_stop).setOnClickListener(this::stopBroadCastShow);
//        findViewById(R.id.btn_show).setOnLongClickListener(this::show2);
        stateText = findViewById(R.id.text_state);
        versionText =findViewById(R.id.text_version);
        versionText.setText(getAppVersionName(getApplicationContext()));

        connectBtn.setOnClickListener(this::onClick);
        SocketManager.getInstance().setListener(this);
        settingBtn.setOnClickListener(this::onSettingClick);
        controlBtn.setOnClickListener(this::onControlClick);
    }

    private void finish(View view) {
        finish();
    }

    private void onControlClick(View view) {
        Intent intent = new Intent(NetSettingActivity.this, UdpControlActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void onSettingClick(View view) {

        Intent intent = new Intent(NetSettingActivity.this, WifiSettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private boolean show2(View view) {
        findViewById(R.id.button_start).setVisibility(View.VISIBLE);
        findViewById(R.id.button_stop).setVisibility(View.VISIBLE);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: isConnected:" +SocketManager.getInstance().isConnected() );
    }

    private void onClick(View view){
        String hostIp = ipEdit.getText().toString();
        SPUtils.getInstance().put("ipHost",ipEdit.getText().toString());
        int tcpPort = -1;
        int cameraPort = -1;
        try {
            tcpPort = Integer.parseInt(portEdit.getText().toString());
            cameraPort = Integer.parseInt(cameraPortEdit.getText().toString());
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        if (TEST && BuildConfig.DEBUG){
            Intent intent = new Intent(NetSettingActivity.this, XgoMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else if (isCorrectIp(hostIp) && tcpPort>0 && cameraPort > 0){
            SPUtils.getInstance().put("host",hostIp);
            SPUtils.getInstance().put("tcpPort",tcpPort);
            SPUtils.getInstance().put("cameraPort",cameraPort);
            connect(hostIp,tcpPort);
            connectBtn.setEnabled(false);
            new Handler().postDelayed(() -> runOnUiThread(() -> connectBtn.setEnabled(true)),5000);
            Toast toast = Toast.makeText(NetSettingActivity.this,"Connecting ,please wait" , Toast.LENGTH_SHORT);
            toast.show();
        }else {
            if (!isCorrectIp(hostIp)){
                Toast toast = Toast.makeText(NetSettingActivity.this,"IP address is illegal" , Toast.LENGTH_LONG);
                toast.show();

            }
        }
    }

    private void startBroadCastShow(View view){
        try {
            UdpClient.sendBroadCast("1",6001);
        } catch (Exception e) {
            Toast.makeText(this,"sendBroadCastFail",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void stopBroadCastShow(View view){
        try {
            UdpClient.sendBroadCast("0",6001);
        } catch (Exception e) {
            Toast.makeText(this,"sendBroadCastFail",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void connect(String hostIp,int port){
        Log.d(TAG, "connect host: " + hostIp + "   port:" + port);
        SocketManager socketManager = SocketManager.getInstance();
        if (socketManager.isConnected()){
            socketManager.disconnect();
        }
        socketManager.connect(hostIp,port);

    }

    /** * 判断是否为合法IP **/
    public static boolean isCorrectIp(String ipAddress) {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }


    String testString = "start";
    @Override
    public void onStateChange(String newState, boolean connected) {
        testString = testString + "     ---->    " + newState;
        runOnUiThread(() -> stateText.setText(testString));

        if (connected){
            String host = SPUtils.getInstance().getString("host","hostIp");
            runOnUiThread(() -> Toast.makeText(NetSettingActivity.this,"Robot Connected : " + host , Toast.LENGTH_LONG).show());
            Intent intent = new Intent(NetSettingActivity.this, XgoMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else {

            String host = SPUtils.getInstance().getString("host","hostIp");
            int tcpPort = SPUtils.getInstance().getInt("tcpPort",9999);
            int cameraPort = SPUtils.getInstance().getInt("cameraPort",9999);
            runOnUiThread(() -> {
                connectBtn.setEnabled(true);
                Toast.makeText(NetSettingActivity.this,"Connect Fail，please check your network。  host:" + host , Toast.LENGTH_LONG).show();
            });
        }
    }

    @Override
    public void onMsgReceived(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketManager.getInstance().setListener(null);
    }


    /**
     * 获取版本号
     * @return 版本号
     */

    public static String getAppVersionName(Context context) {

        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo p1 = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = p1.versionName;
            if (TextUtils.isEmpty(versionName) || versionName.length() <= 0) {
                return "";
            }else {
                versionName = "v" + versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;

    }
}