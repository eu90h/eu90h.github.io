package com.example.passwordvault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView invalid;
    EditText password;
    String pwd;
    Button submit;

    /* access modifiers changed from: protected */
    @Override // androidx.core.app.ComponentActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        this.submit = (Button) findViewById(R.id.submitBtn);
        this.password = (EditText) findViewById(R.id.passwordTxt);
        this.invalid = (TextView) findViewById(R.id.invalidTxt);
        this.invalid.setVisibility(4);
        this.pwd = "test";
        this.submit.setOnClickListener(new View.OnClickListener() {
            /* class com.example.passwordvault.MainActivity.AnonymousClass1 */

            public void onClick(View view) {
                String obj = MainActivity.this.password.getText().toString();
                if (obj.equals("notthefl@g")) {
                    Intent intent = new Intent(view.getContext(), PwdContainer.class);
                    intent.putExtra("SECRET", obj);
                    MainActivity.this.invalid.setVisibility(4);
                    MainActivity.this.startActivity(intent);
                    return;
                }
                MainActivity.this.invalid.setVisibility(0);
            }
        });
    }
}
