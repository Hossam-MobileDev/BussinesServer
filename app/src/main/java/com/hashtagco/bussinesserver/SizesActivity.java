package com.hashtagco.bussinesserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hashtagco.bussinesserver.databinding.ActivitySizesBinding;

public class SizesActivity extends AppCompatActivity {
ActivitySizesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySizesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
binding.btnUploadSizes.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       String smfromm = binding.sfroom.getText().toString();
      String stoo = binding.stoo.getText().toString();
     String mfroom =  binding.mfroom.getText().toString();
     String mtoo =  binding.mtoo.getText().toString();
     String mplusfrom =  binding.mplusfroom.getText().toString();
    String mplustoo =   binding.mplustoo.getText().toString();
    String lfroom =   binding.lfroom.getText().toString();
   String ltoo =    binding.ltoo.getText().toString();
     String lplusfroom =  binding.lplusfroom.getText().toString();
      String lplustoo = binding.lplustoo.getText().toString();
      String xlfroom = binding.xlfroom.getText().toString();
    String xltoo =   binding.xltoo.getText().toString();
        Intent intent = getIntent();
        intent.putExtra("smfromm", smfromm);
        intent.putExtra("stoo", stoo);
        intent.putExtra("mfroom", mfroom);
        intent.putExtra("mtoo", mtoo);
        intent.putExtra("mplusfrom", mplusfrom);
        intent.putExtra("mplustoo", mplustoo);
        intent.putExtra("lfroom", lfroom);
        intent.putExtra("ltoo", ltoo);
        intent.putExtra("lplusfroom", lplusfroom);
        intent.putExtra("lplustoo", lplustoo);
        intent.putExtra("xlfroom", xlfroom);
        intent.putExtra("xltoo", xltoo);
        setResult(RESULT_OK, intent);
        finish();
    }
});
    }
}