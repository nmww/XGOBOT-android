package com.luwu.xgobot.mActivity.aimode;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luwu.xgobot.R;
import com.luwu.xgobot.data.AiModeBean;
import com.luwu.xgobot.mActivity.BaseActivity;
import com.luwu.xgobot.mActivity.aimode.adapter.AiModeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>文件描述：<p>
 * <p>作者：zhangyibin<p>
 * <p>创建时间：2022/9/12<p>
 */
public class AiModeActivity extends BaseActivity {
    private List<AiModeBean> mList = new ArrayList<>();
    private RecyclerView mRv;
    private ImageView mBack_img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aimodulenew);

        initData();

        mRv = findViewById(R.id.aimode_rv);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);
        AiModeAdapter adapter = new AiModeAdapter(mList);
        mRv.setLayoutManager(layoutManager);
        mRv.setAdapter(adapter);

        mBack_img = findViewById(R.id.aimodule_back_img);
        mBack_img.setOnClickListener(v -> finish());


    }

    private void initData() {

        AiModeBean aiModeBean;

        aiModeBean = new AiModeBean();
        aiModeBean.setImage(R.drawable.icon_ballfollew);
        aiModeBean.setName("小球跟随");


        aiModeBean = new AiModeBean();
        aiModeBean.setImage(R.drawable.icon_followface);
        aiModeBean.setName("人脸跟踪");
        mList.add(aiModeBean);
        aiModeBean = new AiModeBean();
        aiModeBean.setImage(R.drawable.icon_boneidentification);
        aiModeBean.setName("骨骼识别");
        mList.add(aiModeBean);
        aiModeBean = new AiModeBean();
        aiModeBean.setImage(R.drawable.icon_gesturerecognition);
        aiModeBean.setName("手势识别");
        mList.add(aiModeBean);
        aiModeBean = new AiModeBean();
        aiModeBean.setImage(R.drawable.icon_facemask);
        aiModeBean.setName("人脸面罩");
        mList.add(aiModeBean);
        aiModeBean = new AiModeBean();
        aiModeBean.setImage(R.drawable.icon_picturesegmentation);
        aiModeBean.setName("图像分割");
        mList.add(aiModeBean);
        aiModeBean = new AiModeBean();
        aiModeBean.setImage(R.drawable.icon_tag);
        aiModeBean.setName("目标识别");
        mList.add(aiModeBean);
    }
}
