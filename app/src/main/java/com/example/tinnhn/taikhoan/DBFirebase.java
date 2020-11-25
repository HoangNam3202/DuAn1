package com.example.tinnhn.taikhoan;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.tinnhn.taikhoan.DangKiActivity.kiemTraTrungEmail;
import static com.example.tinnhn.taikhoan.DangKiActivity.kiemTraTrungSoDienThoai;
import static com.example.tinnhn.taikhoan.DangKiActivity.kiemTraTrungTenTaiKhoan;
import static com.example.tinnhn.taikhoan.LoginActivity.kiemTraDangNhap;
import static com.example.tinnhn.taikhoan.LoginActivity.tenUser;
import static com.example.tinnhn.taikhoan.LoginActivity.DiaChiUser;
import static com.example.tinnhn.taikhoan.QuenMatKhauActivity.idTaiKhoanQMK;
import static com.example.tinnhn.taikhoan.QuenMatKhauActivity.xacNhanTaiKhoan;

public class DBFirebase {
    public String TAG = "DBFirebase";
    DatabaseReference databaseReference;

    public void KhoiTaoFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void ThemTaiKhoan(TaiKhoan taiKhoan) {
        KhoiTaoFirebase();
        databaseReference.child("TaiKhoan").push().setValue(taiKhoan);
        SetKeyIdTaiKhoan(taiKhoan.getTenTaiKhoan(), taiKhoan.getEmail(), taiKhoan.getSoDienThoai());
    }

    private void SetKeyIdTaiKhoan(String tenTaiKhoan, String email, String soDienThoai) {
        KhoiTaoFirebase();
        databaseReference.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                if (taiKhoan.getTenTaiKhoan().equals(tenTaiKhoan) && taiKhoan.getEmail().equals(email) && taiKhoan.getSoDienThoai().equals(soDienThoai)) {
                    databaseReference.child("TaiKhoan").child(snapshot.getKey()).child("idTaiKhoan").setValue(snapshot.getKey());
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


    public void KiemTraTrung(String tenTaiKhoan, String email, String soDienThoai) {
        KhoiTaoFirebase();
        kiemTraTrungTenTaiKhoan = false;
        kiemTraTrungEmail = false;
        kiemTraTrungSoDienThoai = false;
        databaseReference.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                if (taiKhoan.getTenTaiKhoan().equals(tenTaiKhoan))
                    kiemTraTrungTenTaiKhoan = true;
                if (taiKhoan.getEmail().equals(email))
                    kiemTraTrungEmail = true;
                if (taiKhoan.getSoDienThoai().equals(soDienThoai))
                    kiemTraTrungSoDienThoai = true;
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


    boolean checkEmail;

    public void KiemTraDangNhap(String email, String matKhau) {
        KhoiTaoFirebase();
        kiemTraDangNhap = -1;
        checkEmail = false;
        databaseReference.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                if (taiKhoan.getEmail().equals(email)) {
                    checkEmail = true;
                    if (taiKhoan.getMatKhau().equals(matKhau)) {
                        kiemTraDangNhap = 0;
                        tenUser = taiKhoan.getTenTaiKhoan();
                        DiaChiUser = taiKhoan.getDiaChi();
                    } else {
                        kiemTraDangNhap = 2;
                    }
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
        if (!checkEmail) kiemTraDangNhap = 1;
    }

    public void KiemTraTaiKhoan(String tenTaiKhoan, String email, String soDienThoai) {
        KhoiTaoFirebase();
        xacNhanTaiKhoan = -1;
        idTaiKhoanQMK = "";
        databaseReference.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                if (taiKhoan.getTenTaiKhoan().equals(tenTaiKhoan) && taiKhoan.getEmail().equals(email) && taiKhoan.getSoDienThoai().equals(soDienThoai)) {
                    xacNhanTaiKhoan = 0;
                    idTaiKhoanQMK = taiKhoan.getIdTaiKhoan();
                }
                if (!taiKhoan.getTenTaiKhoan().equals(tenTaiKhoan) && taiKhoan.getEmail().equals(email) && taiKhoan.getSoDienThoai().equals(soDienThoai)) {
                    xacNhanTaiKhoan = 1;
                }
                if (taiKhoan.getTenTaiKhoan().equals(tenTaiKhoan) && !taiKhoan.getEmail().equals(email) && taiKhoan.getSoDienThoai().equals(soDienThoai)) {
                    xacNhanTaiKhoan = 2;
                }
                if (taiKhoan.getTenTaiKhoan().equals(tenTaiKhoan) && taiKhoan.getEmail().equals(email) && !taiKhoan.getSoDienThoai().equals(soDienThoai)) {
                    xacNhanTaiKhoan = 3;
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


    public void DoiMatKhau(String idTaiKhoanQMK, String matKhau) {
        KhoiTaoFirebase();
        databaseReference.child("TaiKhoan").child(idTaiKhoanQMK).child("matKhau").setValue(matKhau);
    }
}

//    public String LayKeyTaiKhoan(String email) {
//        KhoiTaoFirebase();
//        final String[] key = {""};
//        databaseReference.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                TaiKhoan taiKhoan;
//                taiKhoan = snapshot.getValue(TaiKhoan.class);
//                if (taiKhoan.getEmail().equals(email)) {
//                    key[0] = snapshot.getKey();
//                    Log.d(TAG, key[0]);
//                    databaseReference.child("TaiKhoan").child(key[0]).child("idTaiKhoan").setValue(key[0]);
//                }
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
//        return key[0];
//    }


//    public boolean KiemTraTrungTTK(String tenTaiKhoan) {
//        KhoiTaoFirebase();
//        kiemTraTrungTenTaiKhoan = false;
//        databaseReference.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
//                if (taiKhoan.getTenTaiKhoan().equals(tenTaiKhoan))
//                    kiemTraTrungTenTaiKhoan = true;
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
//        return false;
//    }


//    public ArrayList<TaiKhoan> LayDanhSachTaiKhoan() {
//        KhoiTaoFirebase();
//        final ArrayList<TaiKhoan> taiKhoans = new ArrayList<>();
//        databaseReference.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
//                taiKhoans.add(taiKhoan);
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
//        return taiKhoans;
//    }


//                Log.d(TAG, "onChildAdded: "+taiKhoan.getEmail());
//                if (taiKhoan.getMatKhau().equals(matKhau) && !taiKhoan.getEmail().equals(email))
//                    kiemTraTaiKhoan = 1;
//                if (!taiKhoan.getMatKhau().equals(matKhau) && taiKhoan.getEmail().equals(email))
//                    kiemTraTaiKhoan = 2;

//                if (!taiKhoan.getEmail().equals(email)) kiemTraTaiKhoan = 1;
//                if (!taiKhoan.getMatKhau().equals(matKhau)) kiemTraTaiKhoan = 2;


//    public void ThemTinhThanh(TinhThanh tinhThanh) {
//        KhoiTaoFirebase();
//        databaseReference.child("TinhThanh").push().setValue(tinhThanh);
//    }
//    public void ThemTinhThanh2(String tinhThanh) {
//        KhoiTaoFirebase();
//        databaseReference.child("TinhThanh2").push().setValue(tinhThanh);
//    }

//    public ArrayList<String> LayDanhSachTinhThanh2() {
//        KhoiTaoFirebase();
//        ArrayList<String> abc = new ArrayList<>();
//        databaseReference.child("TinhThanh2").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String tinh = snapshot.getValue().toString();
//                abc.add(tinh);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return abc;
//    }

//    public ArrayList<TinhThanh> LayDanhSachTinhThanh() {
//        KhoiTaoFirebase();
//        final ArrayList<TinhThanh> tinhThanhs = new ArrayList<>();
//        databaseReference.child("TinhThanh").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                TinhThanh tinhThanh = snapshot.getValue(TinhThanh.class);
//                tinhThanhs.add(tinhThanh);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return tinhThanhs;
//    }


//        databaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
//        Query query = databaseReference.orderByChild("tenTaiKhoan");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                Log.d(TAG,""+snapshot.toString());
//                TaiKhoan taiKhoan =new TaiKhoan();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    taiKhoan = dataSnapshot.getValue(TaiKhoan.class);
//                    if (taiKhoan.getEmail().equals(email)) {
//                        key[0] = snapshot.getKey();
//                        Log.d(TAG, taiKhoan.getTenTaiKhoan());
//                        break;
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d(TAG,""+error.toString());
//            }
//        });
