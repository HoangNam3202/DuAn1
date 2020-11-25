package com.example.tinnhn.Call;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tinnhn.R;
import com.sinch.android.rtc.calling.Call;

public class GroupHoiThoaiActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_hoi_thoai);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.callgroup, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.groupchat) {
            //đang nhap sinchclient o day = UserEmail sau do goi cho EmailNguoiGui

            if (getGiaodiendichvu().isStarted()) {

                Hamchuyenidgroupquagroupcall();

            } else {
                Toast.makeText(this, "chua chay dich vu", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void Hamchuyenidgroupquagroupcall() {
        String username="idgroup";

        Intent callScreen = new Intent(this, CuocGoi_Screen.class);
        callScreen.putExtra(SinchServices.CALL_ID,username);
        startActivity(callScreen);

//        Intent mainActivity = new Intent(this, Dialer.class);
//        startActivity(mainActivity);
    }
}