package com.example.tinnhn.taikhoan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tinnhn.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class ThemAnhActivity extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://duan1-f124f.appspot.com");
    StorageReference storageRef = storage.getReference();
    ImageView ivHinh, ivHinhTaiVe;
    Button btnThem,btnTaiHinh;
    int REQUEST_CODE_IMAGE = 1;
    String linkURLImage = "";
    String TAG = "ThemAnhActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_anh);

        ThemHinh();
        HienHinh();
    }

    private void HienHinh() {
        ivHinhTaiVe = findViewById(R.id.ivHinhTaiVe);
        btnTaiHinh = findViewById(R.id.btnTaiHinh);
        btnTaiHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linkURLImage!=null){
                    new DownloadImageTask(findViewById(R.id.ivHinhTaiVe)).execute(linkURLImage);
                }
            }
        });

    }

    private void ThemHinh() {
        ivHinh = findViewById(R.id.ivHinh);
        ivHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // camera
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, REQUEST_CODE_IMAGE);
                // chọn hình từ bộ sưu tập
//                asdf;
                //
            }
        });
        btnThem = findViewById(R.id.btnThem);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                StorageReference mountainsRef = storageRef.child("hinhanh_" + calendar.getTimeInMillis());
                ivHinh.setDrawingCacheEnabled(true);
                ivHinh.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) ivHinh.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ThemAnhActivity.this, "NOT OK ", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        StorageMetadata metadata = taskSnapshot.getMetadata();
                        Log.d(TAG, "onSuccess: "+metadata);
//                        Toast.makeText(ThemAnhActivity.this, "OK: " + metadata, Toast.LENGTH_SHORT).show();
                    }
                }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return mountainsRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            linkURLImage = downloadUri.toString();
                            Toast.makeText(ThemAnhActivity.this, "downloadUri: " + linkURLImage, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: " + linkURLImage);
                        } else {
                            // ...
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ivHinh.setImageBitmap(bitmap);
        }
    }
}

//                    URL url = null;
//                    try {
//                        url = new URL(linkURLImage);
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    }
//                    Bitmap bmp = null;
//                    try {
//                        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    ivHinhTaiVe.setImageBitmap(bmp);

//                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                        if (!task.isSuccessful()) {
//                            throw task.getException();
//                        }
//
//                        // Continue with the task to get the download URL
//                        return mountainsRef.getDownloadUrl();
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        if (task.isSuccessful()) {
//                            Uri downloadUri = task.getResult();
//                            linkURLImage = downloadUri.toString();
//                            Toast.makeText(ThemAnhActivity.this, "downloadUri: " + linkURLImage, Toast.LENGTH_SHORT).show();
//                            Log.d(TAG, "onComplete: " + linkURLImage);
//                        } else {
//                            // Handle failures
//                            // ...
//                        }
//                    }
//                });