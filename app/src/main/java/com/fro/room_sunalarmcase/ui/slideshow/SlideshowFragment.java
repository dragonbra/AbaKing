package com.fro.room_sunalarmcase.ui.slideshow;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fro.room_sunalarmcase.ConnectTask;
import com.fro.room_sunalarmcase.Const;
import com.fro.room_sunalarmcase.MainActivity;
import com.fro.room_sunalarmcase.R;
import com.fro.room_sunalarmcase.viewDrawer.MboardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    private FloatingActionButton button;

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
    private Button submit_b;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        button = root.findViewById(R.id.fab);
        context = getActivity().getBaseContext();
        /**
         * 绑定setting控件
         */
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
        submit_b = root.findViewById(R.id.submit_b);

        initData();
        initEvent();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_nav_slideshow_self);
                Toast.makeText(context, "刷新成功！", Toast.LENGTH_SHORT).show();
            }
        });
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
    }

    /**
     * 按钮监听
     */
    private void initEvent() {

        // 保存
        submit_b.setOnClickListener( new Button.OnClickListener() {
             @Override
             public void onClick(View v) {
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
                     Toast.makeText(context, "保存成功！", Toast.LENGTH_SHORT).show();
                 } else {
                     Snackbar.make(root, "配置信息不正确,请重输！", Snackbar.LENGTH_LONG)
                             .setAction("Action", null).show();
                     return;
                 }
                 // 获取其他参数
                 Const.time = Integer.parseInt(time_et.getText().toString().trim());
                 Const.maxLim = Integer.parseInt(maxLim_et.getText().toString().trim());

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