package com.example.mhike;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhike.Hike;
import com.example.mhike.R;
import com.example.mhike.SQLiteHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DetailActivity extends AppCompatActivity {

    TextInputEditText txtName, txtPosition, txtDate, txtNote, txtObserve, txtObserveDate, txtNoteOther, txtLength;
    Button btnAdd;
    int yearHike, dayhike, monthHike;

    RadioGroup radioGroupParking;

    String[] levelDropdown = {"Khó", "Trung Bình", "Dễ"};
    String[] weatherDropDown = {"Nắng", "Mưa", "Dễ chịu", "Mát mẻ"};

    AutoCompleteTextView autoCompleteLevel, autoCompleteWeather;

    ArrayAdapter<String> adapterLevel, adapterWeather;

    Uri selectedImageUri;

    ImageView imgHike;

    private int mode = 1;

    private String fileName;

    private Hike hike;

    private static final int REQUEST_IMAGE_PICK = 1;
    int yearObserve, dayObserve, monthObserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thêm mới chuyến đi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addData();
        addMethods();
        getIntentData();
    }

    private void getIntentData() {
        if (getIntent().hasExtra("hike")) {
            hike = (Hike) getIntent().getSerializableExtra("hike");
            txtName.setText(hike.getName());
            txtPosition.setText(hike.getPosition());
            txtObserve.setText(hike.getObserve());
            txtNoteOther.setText(hike.getNote_other());
            txtNote.setText(hike.getNote());
            radioGroupParking.check(hike.getParking());
            txtDate.setText(hike.getDate());
            txtLength.setText(hike.getLength() + "");
            txtObserveDate.setText(hike.getObserve_date());
            autoCompleteWeather.setText(hike.getWeather(), false);
            autoCompleteLevel.setText(hike.getLevel(), false);
            File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), hike.getImage());
            String imagePath = imageFile.getAbsolutePath();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imgHike.setImageBitmap(bitmap);
        }
        this.mode = getIntent().getIntExtra("mode", 1);
        if (mode == 1) {
            getSupportActionBar().setTitle("Thêm mới chuyến đi");
            btnAdd.setText("Thêm mới");
        } else if (mode == 2) {
            getSupportActionBar().setTitle("Chỉnh sửa chuyến đi");
            btnAdd.setText("Chỉnh sửa");
        }
    }

    private void addMethods() {
        //ngày đi bộ
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                yearHike = calendar.get(Calendar.YEAR);
                monthHike = calendar.get(Calendar.MONTH);
                dayhike = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i, i1, i2);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        txtDate.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, yearHike, monthHike, dayhike);
                datePickerDialog.show();
            }
        });

        //ngày quan sát
        txtObserveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                yearObserve = calendar.get(Calendar.YEAR);
                monthObserve = calendar.get(Calendar.MONTH);
                dayObserve = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i, i1, i2);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        txtObserveDate.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, yearObserve, monthObserve, dayObserve);
                datePickerDialog.show();
            }
        });

        //btnImage
        imgHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hike.setName(txtName.getText().toString());
                hike.setPosition(txtPosition.getText().toString());
                hike.setDate(txtDate.getText().toString());
                hike.setLength(Double.parseDouble(txtLength.getText().toString()));
                hike.setObserve(txtObserve.getText().toString());
                hike.setWeather(String.valueOf(autoCompleteWeather.getText()));
                hike.setLevel(String.valueOf(autoCompleteLevel.getText()));
                hike.setObserve_date(txtObserveDate.getText().toString());
                hike.setNote(txtNote.getText().toString());
                hike.setNote_other(txtNoteOther.getText().toString());
                hike.setParking(radioGroupParking.getCheckedRadioButtonId());
                if (selectedImageUri != null) {
                    // Lưu ảnh vào thư mục ứng dụng
                    saveImageToAppDirectory(selectedImageUri);
                    hike.setImage(fileName);
                } else {
                    hike.setImage("default.jpg");
                }
                if (hike.validate(DetailActivity.this)) {
                    SQLiteHelper myDB = new SQLiteHelper(DetailActivity.this);
                    //thêm mới
                    if (mode == 1) {
                        myDB.addHike(hike);
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                    //cập nhật
                    else if (mode == 2) {
                        myDB.updateHike(hike);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();

            // hiển thị ảnh trên ImageView:
            ImageView imgHike = findViewById(R.id.imgHike);
            imgHike.setImageURI(selectedImageUri);
        }
    }

    private void saveImageToAppDirectory(Uri imageUri) {
        try {
            // Lấy đường dẫn thư mục lưu trữ của ứng dụng
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            // Tạo tên tệp tin
            fileName = generateUniqueFileName("jpg");
            // Tạo tệp tin cho ảnh
            File imageFile = new File(storageDir, fileName);
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            OutputStream outputStream = new FileOutputStream(imageFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
        } catch (Throwable e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu ảnh.", Toast.LENGTH_LONG).show();
        }
    }

    //    tạo ten file anh theo time
    private static String generateUniqueFileName(String extension) {
        // Lấy thời gian hiện tại dưới dạng timestamp
        long timestamp = System.currentTimeMillis();

        // Tạo định dạng ngày tháng giờ phút giây
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        // Lấy ngày tháng giờ phút giây hiện tại
        String formattedDate = dateFormat.format(new Date(timestamp));

        // Tạo một số ngẫu nhiên để đảm bảo tính duy nhất
        Random random = new Random();
        int randomInt = random.nextInt(10000);

        // Kết hợp thông tin để tạo tên tập tin duy nhất
        return formattedDate + "_" + randomInt + "." + extension;
    }

    private void addData() {
        hike = new Hike();
        txtDate = findViewById(R.id.txtDate);
        txtObserveDate = findViewById(R.id.txtObserveDate);
        txtLength = findViewById(R.id.txtLength);
        txtName = findViewById(R.id.txtName);
        txtNote = findViewById(R.id.txtNote);
        txtNoteOther = findViewById(R.id.txtNoteOther);
        txtObserve = findViewById(R.id.txtObserve);
        txtPosition = findViewById(R.id.txtPosition);
        imgHike = findViewById(R.id.imgHike);
        btnAdd = findViewById(R.id.btnAdd);
        radioGroupParking = findViewById(R.id.radioGroupParking);

        autoCompleteLevel = findViewById(R.id.auto_conplete_level);
        autoCompleteWeather = findViewById(R.id.auto_conplete_weather);


        adapterLevel = new ArrayAdapter<String>(this, R.layout.drop_down, levelDropdown);
        adapterWeather = new ArrayAdapter<String>(this, R.layout.drop_down, weatherDropDown);
        autoCompleteLevel.setAdapter(adapterLevel);
        autoCompleteWeather.setAdapter(adapterWeather);
        autoCompleteLevel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                hike.setLevel(item);
            }
        });

        autoCompleteWeather.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                hike.setWeather(item);
            }
        });

    }
}