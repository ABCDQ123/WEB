package com.comic.mario.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

import com.comic.mario.R;
import com.comic.mario.util.recyclerview.AdapterManager;
import com.comic.mario.util.recyclerview.MultiData;
import com.comic.mario.util.recyclerview.MutiViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class LocalActivity extends AppCompatActivity implements MutiViewHolder {


    private ImageView ig_finish;

    private SwipeRefreshLayout srl_local;
    private RecyclerView rv_local;

    private RecyclerView.Adapter adapter;

    private List<MultiData> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        ig_finish = findViewById(R.id.ig_finish);
        srl_local = findViewById(R.id.srl_local);
        rv_local = findViewById(R.id.rv_local);

        ig_finish.setOnClickListener(v -> {
            finish();
        });
        srl_local.setOnRefreshListener(() -> {
            srl_local.setRefreshing(false);
        });
        rv_local.setLayoutManager(new LinearLayoutManager(this));
        rv_local.setAdapter(adapter = new AdapterManager().getLinearMultiAdapter(this, items = new ArrayList<>(), this));

        new Thread(() -> {
            File file = new File(Data_FilePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            File[] subFile = file.listFiles();
            for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                if (subFile[iFileLength].isDirectory()) { //判断为文件夹
                    String filename = subFile[iFileLength].getName();
                    items.add(new MultiData(0, R.layout.item_forld, filename));
                }
            }
            runOnUiThread(() -> {
                adapter.notifyItemRangeChanged(0, items.size());
            });
        }).start();
    }

    @Override
    public void onBindViewHolder(int tag, RecyclerView.ViewHolder holder, int position) {
        if (tag == 0) {
            TextView tv_ford_name = holder.itemView.findViewById(R.id.tv_ford_name);

            String data = (String) items.get(position).getData();
            tv_ford_name.setText("" + data);

            tv_ford_name.setOnClickListener(v -> {

            });
        }
    }

    private final String Data_FilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ComicMario/Local";

}
