package com.fro.room_sunalarmcase.ui.home;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fro.room_sunalarmcase.ConnectTask;
import com.fro.room_sunalarmcase.Const;
import com.fro.room_sunalarmcase.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fro.room_sunalarmcase.viewDrawer.MboardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {
    private Context context;

    private TextView sun_tv;
    private ToggleButton connect_tb;
    private TextView info_tv;
    private ProgressBar progressBar;

    private HomeViewModel homeViewModel;
    private MboardView dashboardview;

    private FloatingActionButton button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        View mhomeroot = inflater.inflate(R.layout.fragment_home,container,false);
        context = getActivity().getBaseContext();

        button = mhomeroot.findViewById(R.id.fab);
        /**
         * 绑定home的控件
         */
        dashboardview = (MboardView) mhomeroot.findViewById(R.id.mboardView);
        connect_tb = (ToggleButton) mhomeroot.findViewById(R.id.connect_tb);
        info_tv = (TextView) mhomeroot.findViewById(R.id.info_tv);
        sun_tv = (TextView) mhomeroot.findViewById(R.id.sun_tv);
        progressBar = (ProgressBar) mhomeroot.findViewById(R.id.progressBar);
        // 初始化连接状态
        initStatus();
        // 事件监听
        initEvent();

        return mhomeroot;
    }

    private void initStatus() {
        Log.i(Const.TAG, Const.isLinking.toString());
        if (!Const.isLinking) {
            connect_tb.setChecked(false);
            info_tv.setText("请点击连接!");
        } else {
            connect_tb.setChecked(true);
            info_tv.setTextColor(context.getResources().getColor(R.color.green));
            info_tv.setText("连接正常！");
        }
    }

    /**
     * 按钮监听
     */
    private void initEvent() {

        // 连接
        connect_tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 进度条显示
                    progressBar.setVisibility(View.VISIBLE);

                    // 开启任务
                    Const.connectTask = new ConnectTask(context, sun_tv, info_tv, progressBar,dashboardview);
                    Const.connectTask.setCIRCLE(true);
                    Const.connectTask.execute();
                } else {
                    if (Const.connectTask != null && Const.connectTask.getStatus() == AsyncTask.Status.RUNNING) {
                        Const.connectTask.setCIRCLE(false);
                        try {
                            Thread.sleep(3000);
                            Const.isLinking = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 如果Task还在运行，则先取消它
                        Const.connectTask.cancel(true);
                        Const.connectTask.closeSocket();

                        // 进度条消失
                        progressBar.setVisibility(View.GONE);
                        info_tv.setText("请点击连接！");
                        info_tv.setTextColor(context.getResources().getColor(R.color.gray));
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_nav_home_self);
                Toast.makeText(context, "刷新成功！", Toast.LENGTH_SHORT).show();
            }
        });
    }

}