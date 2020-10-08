package com.fro.room_sunalarmcase.ui.gallery;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fro.room_sunalarmcase.Const;
import com.fro.room_sunalarmcase.R;
import com.fro.util.ChartView;
import com.fro.util.Constant;
import com.fro.util.DataInput;

import org.xmlpull.v1.XmlPullParser;

import java.util.LinkedList;
import java.util.Queue;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Constant.point = new Point();
        //获取屏幕长宽
        //getActivity().getWindowManager().getDefaultDisplay().getSize(Constant.point);
        Constant.point = new Point(1080,2028);
        ChartView myView = new ChartView(getActivity().getBaseContext());
        //getActivity().setContentView(myView);

        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        ChartView tmp = root.findViewById(R.id.awsl);
        String[] Xlabel = new String[20];
        String[] data = new String[20];
        if(DataInput.Dataque.size() == 0) {
            DataInput.Dataque.offer("1");
            DataInput.Xque.offer("1");
            DataInput.cnt++;
        }
        Queue<String> tmp1 = new LinkedList<String>(DataInput.Xque);
        Queue<String> tmp2 = new LinkedList<String>(DataInput.Dataque);
        int sz = tmp1.size();
        for(int i=0;i<sz;i++) {
            Xlabel[i] = tmp1.poll();
            data[i] = tmp2.poll();
        }
        tmp.SetInfo(Xlabel,new String[] {"","200","400","600","800","1000"},data,// 数据
                "图标的标题",sz);

//        final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        return root;
    }

}