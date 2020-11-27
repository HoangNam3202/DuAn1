package com.example.tinnhn.ui.main;

import android.animation.TimeAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import com.example.tinnhn.taikhoan.HihNgNhanTrogMessArrLst;
import com.example.tinnhn.taikhoan.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.tinnhn.HoiThoaiActivity.hoiThoaiAdapter;
import static com.example.tinnhn.MainActivity.hihNgNhanTrogMessArrLsts;
import static com.example.tinnhn.taikhoan.LoginActivity.dbFirebase;

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
    public String NoiDung, TenUser;
    ArrayList<TinNhanHienThi> messageArrayList;
    MessageAdapter messageAdapter;
    String EmailUser;
    ArrayList<TinNhanHienThi> messageArrayList_Message1;
    boolean check_search = false;

    String TAG = "PlaceholderFragment";

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        EditText tv_Search_TinNhan = view.findViewById(R.id.title);
        ListView list_view_Message = view.findViewById(R.id.list_view_Message);
        messageArrayList = new ArrayList<>();
        messageArrayList_Message1 = new ArrayList<>();
//        ArrayList<Friends> messageArrayList_check = new ArrayList<>();
//        ArrayList<HoiThoai> messageArrayList_Message = new ArrayList<>();
        messageAdapter = new MessageAdapter(getActivity(), R.layout.list_message_item, messageArrayList);
        list_view_Message.setAdapter(messageAdapter);
        sharedPreferences = getContext().getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        EmailUser = sharedPreferences.getString("tenTaiKhoan", "");
        TenUser = sharedPreferences.getString("tenUser", "");

        if (!check_search) {
            hihNgNhanTrogMessArrLsts.clear();
            mDatabase.child("TinNhan").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    TinNhanHienThi message = snapshot.getValue(TinNhanHienThi.class);
                    if (message.email_User.equals(EmailUser)) {
                        String keyTinNhan = snapshot.getKey();
                        messageArrayList.add(new TinNhanHienThi(keyTinNhan, message.message_User, message.emailNguoiNhan, message.email_User, message.tenUser, message.tenNguoiGui));
                        // lấy url hình từ mail người dùng
                        LayUrlTuEmail(message.emailNguoiNhan);
                        //
                    }
                    if (message.emailNguoiNhan.equals(EmailUser)) {
                        String keyTinNhan = snapshot.getKey();
                        messageArrayList.add(new TinNhanHienThi(keyTinNhan, message.message_User, message.email_User, message.emailNguoiNhan, message.tenNguoiGui, message.tenUser));
                        // lấy url hình từ mail người dùng
                        LayUrlTuEmail(message.email_User);
                        //
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
        }


        list_view_Message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), HoiThoaiActivity.class);
                if (messageArrayList.get(i).email_User.equals(EmailUser)) {
                    intent.putExtra("TenNguoiGui", messageArrayList.get(i).tenUser);
                } else if (messageArrayList.get(i).emailNguoiNhan.equals(EmailUser)) {
                    intent.putExtra("TenNguoiGui", messageArrayList.get(i).tenNguoiGui);
                }
                intent.putExtra("EmailNguoiGui", messageArrayList.get(i).emailNguoiNhan);
                startActivity(intent);
//                container.removeView(view);
            }
        });
        tv_Search_TinNhan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                messageArrayList.clear();
                messageAdapter.notifyDataSetChanged();
                mDatabase.child("TinNhan").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        TinNhanHienThi message = snapshot.getValue(TinNhanHienThi.class);
                        if (tv_Search_TinNhan.getText().toString().contains(message.tenUser) && message.tenUser.equals(TenUser)
                                || tv_Search_TinNhan.getText().toString().contains(message.tenUser) && message.tenNguoiGui.equals(TenUser)
                                || tv_Search_TinNhan.getText().toString().contains(message.tenNguoiGui) && message.email_User.equals(EmailUser)) {
                            String keyTinNhan = snapshot.getKey();
                            messageArrayList.add(new TinNhanHienThi(keyTinNhan, message.message_User, message.emailNguoiNhan, message.email_User, message.tenUser, message.tenNguoiGui));
                        }
                        if (tv_Search_TinNhan.getText().toString().equals("")) {
                            if (message.email_User.equals(EmailUser)) {
                                String keyTinNhan = snapshot.getKey();
                                messageArrayList.add(new TinNhanHienThi(keyTinNhan, message.message_User, message.emailNguoiNhan, message.email_User, message.tenUser, message.tenNguoiGui));
                            }
                            if (message.emailNguoiNhan.equals(EmailUser)) {
                                String keyTinNhan = snapshot.getKey();
                                messageArrayList.add(new TinNhanHienThi(keyTinNhan, message.message_User, message.email_User, message.emailNguoiNhan, message.tenNguoiGui, message.tenUser));
                            }
                            check_search = true;
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
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // code của trường, hàm lấy url hình từ email người nhận
//        final Dialog dialog = new Dialog(getContext());
//        dialog.setContentView(R.layout.dialog_loading);
//        dialog.show();
        new CountDownTimer(1300, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
//                dialog.dismiss();
                if (hihNgNhanTrogMessArrLsts.size() != 0) {
                    for (int i = 0; i < hihNgNhanTrogMessArrLsts.size(); i++) {
                        Log.d(TAG, "235: " + hihNgNhanTrogMessArrLsts.get(i).getUrlHinhNguoiNhan());
                    }
                } else Log.d(TAG, "238: " + hihNgNhanTrogMessArrLsts.size());
//                LayDanhSachUrlTuEmailNguoiNhan(messageArrayList);
            }
        }.start();
        //
        return view;
    }

    // lấy url hình từ mail người dùng
    private void LayUrlTuEmail(String emailNguoiNhan) {
        mDatabase.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                if (taiKhoan.getEmail().equals(emailNguoiNhan)) {
                    hihNgNhanTrogMessArrLsts.add(new HihNgNhanTrogMessArrLst(emailNguoiNhan, taiKhoan.getHinhDaiDien()));
                }
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
    }
    //

//    1sec messageArrayList
//            1sec hihNgNhanTrogMessArrLsts
//                1sec messageArrayList

//    private void LayDanhSachUrlTuEmailNguoiNhan(ArrayList<TinNhanHienThi> messageArrayList) {
//        if (messageArrayList.size() != 0) {
////            hihNgNhanTrogMessArrLsts.clear();
//            for (int i = 0; i < messageArrayList.size(); i++) {
////                dbFirebase.LayUrlHinhNguoiNhanTuEmail(messageArrayList.get(i).emailNguoiNhan);
//                int finalI = i;
//                mDatabase.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                        TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
//                        if (taiKhoan.getEmail().equals(messageArrayList.get(finalI).emailNguoiNhan)) {
//                            hihNgNhanTrogMessArrLsts.add(new HihNgNhanTrogMessArrLst(messageArrayList.get(finalI).emailNguoiNhan, taiKhoan.getHinhDaiDien()));
//                            Log.d(TAG, "hihNgNhanTrogMessArrLsts: " + hihNgNhanTrogMessArrLsts.size());
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//
//            }
//
//        } else Log.e(TAG, "LayDanhSachUrlTuEmailNguoiNhan: Danh sách tin nhắn trống");
////        Log.d(TAG, "hihNgNhanTrogMessArrLsts: " + hihNgNhanTrogMessArrLsts.size());
//
//    }

//    public void GoiDanhSachTinNhan() {
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        messageArrayList.clear();
//        messageAdapter.notifyDataSetChanged();
//        mDatabase.child("TinNhan").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                TinNhanHienThi message = snapshot.getValue(TinNhanHienThi.class);
//                messageArrayList_Message1.clear();
//                messageArrayList_Message1.add(message);
//                for (int i = 0; i < messageArrayList_Message1.size(); i++) {
//                    if (messageArrayList_Message1.get(i).email_User.equals(EmailUser)) {
//                        String keyTinNhan = snapshot.getKey();
//                        messageArrayList.add(new TinNhanHienThi(keyTinNhan, messageArrayList_Message1.get(i).message_User, messageArrayList_Message1.get(i).emailNguoiNhan, messageArrayList_Message1.get(i).email_User, messageArrayList_Message1.get(i).tenUser, messageArrayList_Message1.get(i).tenNguoiGui));
//                    }
//                    if (messageArrayList_Message1.get(i).emailNguoiNhan.equals(EmailUser)) {
//                        String keyTinNhan = snapshot.getKey();
//                        messageArrayList.add(new TinNhanHienThi(keyTinNhan, messageArrayList_Message1.get(i).message_User, messageArrayList_Message1.get(i).email_User, messageArrayList_Message1.get(i).emailNguoiNhan, messageArrayList_Message1.get(i).tenNguoiGui, messageArrayList_Message1.get(i).tenUser));
//                    }
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
//    }
}