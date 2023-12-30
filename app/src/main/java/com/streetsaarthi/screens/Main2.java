package com.streetsaarthi.screens;

import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.streetsaarthi.R;
import com.streetsaarthi.utils.CustomTimePickerDialog;

import java.util.Calendar;

public class Main2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);

//        CustomTimePickerDialog.OnTimeSetListener timeSetListener = new CustomTimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                Log.e("TAG", "AAAA "+hourOfDay + " "+minute);
//            }
//        };

//        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(Main2.this, timeSetListener,
//                Calendar.getInstance().get(Calendar.HOUR),
//                CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL),
//                false
//        );
//        timePickerDialog.setTitle("2. Select Time");
//        timePickerDialog.show();
    }
}
