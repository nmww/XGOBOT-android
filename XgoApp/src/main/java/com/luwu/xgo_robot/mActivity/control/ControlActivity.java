package com.luwu.xgo_robot.mActivity.control;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.FragmentUtils;
import com.luwu.xgo_robot.R;
import com.luwu.xgo_robot.mActivity.control.fragment.AdvanceedFragment;
import com.luwu.xgo_robot.mActivity.control.fragment.MotionFragment;
import com.luwu.xgo_robot.mActivity.control.fragment.NormalFragment;
import com.luwu.xgo_robot.mActivity.control.fragment.SingleLegFragment;
import com.luwu.xgo_robot.mActivity.control.fragment.XYZFragment;
import com.luwu.xgo_robot.weight.DebugDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>文件描述：<p>
 * <p>作者：zhangyibin<p>
 * <p>创建时间：2022/9/08<p>
 */
public class ControlActivity extends AppCompatActivity {
    private Fragment /*advancedFragment,normalFramgent,*/singlegFragment, xyzFragment, motionFragment;
    private TextView mNormal_tv, mAdvance_tv, mSingleg_tv, mXYZ_tv, mMotion_tv;
    private ImageView mDebug_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlnew);

        initFragment();

        initView();
    }

    private DebugDialog mDebugDialog;

    private void initView() {
        mDebug_img = findViewById(R.id.controlnew_debug_img);
        mDebug_img.setOnClickListener(v -> {
            if (mDebugDialog == null) {
                mDebugDialog = new DebugDialog(this);
            }
            if (mDebugDialog.isShowing()) {
                mDebugDialog.dismiss();
            }
            mDebugDialog.show();
        });


        FragmentUtils.showHide(motionFragment);
       /* mNormal_tv = findViewById(R.id.control_normal_tv);
        mNormal_tv.setOnClickListener(v -> {
            FragmentUtils.hideAllShowFragment(normalFramgent);
        });
        mAdvance_tv = findViewById(R.id.control_advance_tv);
        mAdvance_tv.setOnClickListener(v -> {
            FragmentUtils.hideAllShowFragment(advancedFragment);
        });*/
        mSingleg_tv = findViewById(R.id.control_singleg_tv);
        mSingleg_tv.setOnClickListener(v -> FragmentUtils.showHide(singlegFragment,mList));
        mXYZ_tv = findViewById(R.id.control_xyz_tv);
        mXYZ_tv.setOnClickListener(v -> FragmentUtils.showHide(xyzFragment,mList));
        mMotion_tv = findViewById(R.id.control_motion_tv);
        mMotion_tv.setOnClickListener(v -> FragmentUtils.showHide(motionFragment,mList));
    }

    private List<Fragment> mList = new ArrayList<>();

    private void initFragment() {
        singlegFragment = new SingleLegFragment();
        xyzFragment = new XYZFragment();
        motionFragment = new MotionFragment();
        mList.add(singlegFragment);
        mList.add(xyzFragment);
        mList.add(motionFragment);
        FragmentUtils.add(getSupportFragmentManager(), motionFragment, R.id.control_fragment, false);
        FragmentUtils.add(getSupportFragmentManager(), singlegFragment, R.id.control_fragment, false);
        FragmentUtils.add(getSupportFragmentManager(), xyzFragment, R.id.control_fragment, false);
        FragmentUtils.hide(motionFragment);
        FragmentUtils.hide(singlegFragment);
        FragmentUtils.hide(xyzFragment);
    }
}
