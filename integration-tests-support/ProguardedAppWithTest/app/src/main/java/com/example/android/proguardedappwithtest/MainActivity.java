package com.example.android.proguardedappwithtest;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.proguardedappwithtest.vo.User;
import com.example.android.proguardedappwithtest.databinding.ActivityMainBinding;
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        User user = new User();
        user.name = "foo";
        binding.setUser(user);
        binding.executePendingBindings();
        CharSequence text = binding.nameText.getText();
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
