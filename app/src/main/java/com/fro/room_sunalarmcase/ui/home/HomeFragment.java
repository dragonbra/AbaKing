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
import com.fro.room_sunalarmcase.viewDrawer.MboardView;

public class HomeFragment extends Fragment {
    private Context context;

    private EditText sunIp_et;
    private EditText sunPort_et;
    private EditText tubeIp_et;
    private EditText tubePort_et;
    private EditText buzzerIp_et;
    private EditText buzzerPort_et;
    private EditText curtainIp_et;
    private EditText curtainPort_et;
    private EditText bodyIp_et;
    private EditText bodyPort_et;
    private EditText fanIp_et;
    private EditText fanPort_et;

    private EditText time_et;
    private TextView sun_tv;
    private EditText maxLim_et;
    private ToggleButton connect_tb;
    private TextView info_tv;
    private ProgressBar progressBar;

    private HomeViewModel homeViewModel;
    private MboardView dashboardview;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        View mhomeroot = inflater.inflate(R.layout.fragment_home,container,false);
        context = getActivity().getBaseContext();


        /**
         * 绑定setting控件
         */
        dashboardview = (MboardView) mhomeroot.findViewById(R.id.mboardView);
        bodyIp_et = (EditText) root.findViewById(R.id.bodyIp_et);
        bodyPort_et = (EditText) root.findViewById(R.id.bodyPort_et);
        sunIp_et = (EditText) root.findViewById(R.id.sunIp_et);
        sunPort_et = (EditText) root.findViewById(R.id.sunPort_et);
        tubeIp_et = (EditText) root.findViewById(R.id.tubeIp_et);
        tubePort_et = (EditText) root.findViewById(R.id.tubePort_et);
        buzzerIp_et = (EditText) root.findViewById(R.id.buzzerIp_et);
        buzzerPort_et = (EditText) root.findViewById(R.id.buzzerPort_et);
        curtainIp_et = (EditText) root.findViewById(R.id.curtainIp_et);
        curtainPort_et = (EditText) root.findViewById(R.id.curtainPort_et);
        fanIp_et = (EditText) root.findViewById(R.id.fanIp_et);
        fanPort_et = (EditText) root.findViewById(R.id.fanPort_et);
        time_et = (EditText) root.findViewById(R.id.time_et);
        maxLim_et = (EditText) root.findViewById(R.id.maxLim_et);
        /**
         * 绑定home的控件
         */
        connect_tb = (ToggleButton) mhomeroot.findViewById(R.id.connect_tb);
        info_tv = (TextView) mhomeroot.findViewById(R.id.info_tv);
        sun_tv = (TextView) mhomeroot.findViewById(R.id.sun_tv);
        progressBar = (ProgressBar) mhomeroot.findViewById(R.id.progressBar);
        // 初始化数据
        initData();
        // 事件监听
        initEvent();

        return mhomeroot;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        bodyIp_et.setText(Const.BODY_IP);
        bodyPort_et.setText(String.valueOf(Const.BODY_PORT));

        sunIp_et.setText(Const.SUN_IP);
        sunPort_et.setText(String.valueOf(Const.SUN_PORT));
        tubeIp_et.setText(Const.TUBE_IP);
        tubePort_et.setText(String.valueOf(Const.TUBE_PORT));
        buzzerIp_et.setText(Const.BUZZER_IP);
        buzzerPort_et.setText(String.valueOf(Const.BUZZER_PORT));
        curtainIp_et.setText(Const.CURTAIN_IP);
        curtainPort_et.setText(String.valueOf(Const.CURTAIN_PORT));
        fanIp_et.setText(Const.FAN_IP);
        fanPort_et.setText(String.valueOf(Const.FAN_PORT));

        time_et.setText(String.valueOf(Const.time));
        maxLim_et.setText(String.valueOf(Const.maxLim));

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
                    // 获取IP和端口
                    String BODY_IP = bodyIp_et.getText().toString().trim();
                    String BODY_PORT = bodyPort_et.getText().toString().trim();
                    String SUN_IP = sunIp_et.getText().toString().trim();
                    String SUN_PORT = sunPort_et.getText().toString().trim();
                    String TUBE_IP = tubeIp_et.getText().toString().trim();
                    String TUBE_PORT = tubePort_et.getText().toString().trim();
                    String BUZZER_IP = buzzerIp_et.getText().toString().trim();
                    String BUZZER_PORT = buzzerPort_et.getText().toString().trim();
                    String CURTAIN_IP = curtainIp_et.getText().toString().trim();
                    String CURTAIN_PORT = curtainPort_et.getText().toString().trim();
                    String FAN_IP = fanIp_et.getText().toString().trim();
                    String FAN_PORT = fanPort_et.getText().toString().trim();

                    if (checkIpPort(SUN_IP, SUN_PORT) && checkIpPort(TUBE_IP, TUBE_PORT) && checkIpPort(BUZZER_IP, BUZZER_PORT) && checkIpPort(CURTAIN_IP, CURTAIN_PORT) && checkIpPort(FAN_IP, FAN_PORT) && checkIpPort(BODY_IP, BODY_PORT)) {
                        Const.SUN_IP = SUN_IP;
                        Const.SUN_PORT = Integer.parseInt(SUN_PORT);
                        Const.TUBE_IP = TUBE_IP;
                        Const.TUBE_PORT = Integer.parseInt(TUBE_PORT);
                        Const.BUZZER_IP = BUZZER_IP;
                        Const.BUZZER_PORT = Integer.parseInt(BUZZER_PORT);
                        Const.CURTAIN_IP = CURTAIN_IP;
                        Const.CURTAIN_PORT = Integer.parseInt(CURTAIN_PORT);
                        Const.FAN_IP=FAN_IP;
                        Const.FAN_PORT=Integer.parseInt(FAN_PORT);
                    } else {
                        Toast.makeText(context, "配置信息不正确,请重输！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 获取其他参数
                    Const.time = Integer.parseInt(time_et.getText().toString().trim());
                    Const.maxLim = Integer.parseInt(maxLim_et.getText().toString().trim());

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


    /**
     * IP地址可用端口号验证，可用端口号（1024-65536）
     *
     * @param IP
     * @param port
     * @return
     */
    private boolean checkIpPort(String IP, String port) {
        boolean isIpAddress = false;
        boolean isPort = false;

        if (IP == null || IP.length() < 7 || IP.length() > 15 || "".equals(IP) || port == null || port.length() < 4
                || port.length() > 5 || "".equals(port)) {
            return false;
        }
        // 判断IP格式和范围
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(IP);

        isIpAddress = mat.find();

        // 判断端口
        int portInt = Integer.parseInt(port);
        if (portInt > 1024 && portInt < 65536) {
            isPort = true;
        }

        return (isIpAddress && isPort);
    }
}