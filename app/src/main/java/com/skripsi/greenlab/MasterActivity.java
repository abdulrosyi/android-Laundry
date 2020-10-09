package com.skripsi.greenlab;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.gridlayout.widget.GridLayout;

import com.skripsi.greenlab.master.MasterJasaList;
import com.skripsi.greenlab.master.MasterPelangganList;

public class MasterActivity extends AppCompatActivity {
    private GridLayout gridLayout;
    private Toolbar tulbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        gridLayout = findViewById(R.id.grid_menu_master);
        setSingleEvent(gridLayout);
        tulbar = findViewById(R.id.tulbar);
        setSupportActionBar(tulbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        final ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setSingleEvent(GridLayout mainGrid) {
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {
                        Intent intent = new Intent(MasterActivity.this, MasterPelangganList.class);
                        startActivity(intent);
                    } else if (finalI == 1) {
                        Intent intent = new Intent(MasterActivity.this, MasterJasaList.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
