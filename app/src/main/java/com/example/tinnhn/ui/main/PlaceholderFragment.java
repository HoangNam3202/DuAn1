package com.example.tinnhn.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tinnhn.Friends;
import com.example.tinnhn.HoiThoai;
import com.example.tinnhn.HoiThoaiActivity;
import com.example.tinnhn.Message;
import com.example.tinnhn.MessageAdapter;
import com.example.tinnhn.R;
import com.example.tinnhn.TinNhanHienThi;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.tinnhn.HoiThoaiActivity.hoiThoaiAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private PageViewModel pageViewModel;
    int vitri;
    boolean clickcheck = false;
    private DatabaseReference mDatabase;
    public String NoiDung,TenUser;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
//        int index = 1;
//        if (getArguments() != null) {
//            index = getArguments().getInt(ARG_SECTION_NUMBER);
//        }
//        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ListView list_view_Message = view.findViewById(R.id.list_view_Message);
        ArrayList<TinNhanHienThi> messageArrayList = new ArrayList<>();
        ArrayList<TinNhanHienThi> messageArrayList_Message1 = new ArrayList<>();
        ArrayList<Friends> messageArrayList_check = new ArrayList<>();
        ArrayList<HoiThoai> messageArrayList_Message = new ArrayList<>();
        MessageAdapter messageAdapter = new MessageAdapter(getActivity(),R.layout.list_message_item,messageArrayList);
        list_view_Message.setAdapter(messageAdapter);
        sharedPreferences = getContext().getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String EmailUser = sharedPreferences.getString("tenTaiKhoan", "");
        TenUser = sharedPreferences.getString("tenUser", "");

        mDatabase.child("TinNhan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TinNhanHienThi message = snapshot.getValue(TinNhanHienThi.class);
                messageArrayList_Message1.clear();
                messageArrayList_Message1.add(message);
                for(int i = 0; i < messageArrayList_Message1.size(); i++){
                    if(messageArrayList_Message1.get(i).email_User.equals(EmailUser)){
                        String keyTinNhan = snapshot.getKey();
                        messageArrayList.add(new TinNhanHienThi(keyTinNhan,messageArrayList_Message1.get(i).message_User,messageArrayList_Message1.get(i).emailNguoiNhan,messageArrayList_Message1.get(i).email_User,messageArrayList_Message1.get(i).tenUser,messageArrayList_Message1.get(i).tenNguoiGui));
                    }
                    if(messageArrayList_Message1.get(i).emailNguoiNhan.equals(EmailUser)) {
                        String keyTinNhan = snapshot.getKey();
                        messageArrayList.add(new TinNhanHienThi(keyTinNhan,messageArrayList_Message1.get(i).message_User,messageArrayList_Message1.get(i).email_User,messageArrayList_Message1.get(i).emailNguoiNhan,messageArrayList_Message1.get(i).tenNguoiGui,messageArrayList_Message1.get(i).tenUser));
                    }
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        mDatabase.child("HoiThoai").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                HoiThoai hThoai = snapshot.getValue(HoiThoai.class);
//                messageArrayList_Message1.add(hThoai);
//                for(int i = 0; i < messageArrayList_Message.size(); i++){
//                    String last = messageArrayList_Message.get(messageArrayList_Message.size() - 1).message_User;
//                    if(messageArrayList_Message.get(i).email_User.equals(EmailUser) && messageArrayList_Message.get(i).message_User.equals(last)){
//                        NoiDung = messageArrayList_Message.get(i).message_User;
//
//                    }
//                }
//                messageAdapter.notifyDataSetChanged();
//                Toast.makeText(getContext(), ""+(messageArrayList_Message1.size() - 1), Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        mDatabase.child("BanBe").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Friends friends = snapshot.getValue(Friends.class);
//                messageArrayList_check.clear();
//                messageArrayList_check.add(friends);
//                for(int i = 0 ; i < messageArrayList_check.size(); i++){
//                    if(messageArrayList_check.get(i).EmailUser.equals(EmailUser)){
//                        messageArrayList.add(new Message(messageArrayList_check.get(i).idTaiKhoan,messageArrayList_check.get(i).tenTaiKhoan,messageArrayList_check.get(i).email,
//                                messageArrayList_check.get(i).diaChi,messageArrayList_check.get(i).hinhDaiDien,messageArrayList_check.get(i).EmailUser,NoiDung));
//                    }
////                    else if(NoiDung.equals(null)){
////                        messageArrayList.add(new Message(messageArrayList_check.get(i).idTaiKhoan,messageArrayList_check.get(i).tenTaiKhoan,messageArrayList_check.get(i).email,
////                                messageArrayList_check.get(i).diaChi,messageArrayList_check.get(i).hinhDaiDien,messageArrayList_check.get(i).EmailUser,"Hãy gửi lời chào đến người ấy !"));
////                    }
//                }
//                messageAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                Friends friends = snapshot.getValue(Friends.class);
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        list_view_Message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), HoiThoaiActivity.class);
                if (messageArrayList.get(i).email_User.equals(EmailUser)) {
                    intent.putExtra("TenNguoiGui", messageArrayList.get(i).tenUser);
                }
                else if (messageArrayList.get(i).emailNguoiNhan.equals(EmailUser)) {
                    intent.putExtra("TenNguoiGui", messageArrayList.get(i).tenNguoiGui);
                }
                intent.putExtra("EmailNguoiGui", messageArrayList.get(i).emailNguoiNhan);
                startActivity(intent);
            }
        });
        return view;
    }

}