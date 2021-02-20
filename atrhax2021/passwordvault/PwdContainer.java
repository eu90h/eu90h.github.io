package com.example.passwordvault;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;

public class PwdContainer extends AppCompatActivity {
    ArrayAdapter adapter;
    ArrayList list = new ArrayList();
    ListView pwdDb;

    /* access modifiers changed from: protected */
    @Override // androidx.core.app.ComponentActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_pwd_container);
        Intent intent = getIntent();
        this.pwdDb = (ListView) findViewById(R.id.listView);
        ArrayList arrayList = new ArrayList(Arrays.asList("p93hSxPQInWeGPVcmMxewg==", "jGl5hpGvF4MNtCTSNlcJzYkEWaQAdSMwQKERffswFuk=", "ww/vxITH/93Ta/k5u5g1Pw==", "ISZotmRXFSOEmIVzUvv9bw==", "3KrWtdspWYCpVRJkmhbPZA==", "TC4ONjo91AAGhH0+aRYmEA==", "zj5LT5BVsgVjqHC1wZWPQhhXCk5jUilwx+4svDBSRD0=", "aM+aAy8qPDU6PPSe5Hqq3w==", "Gi56D0Jrn54PaZnlmFeaZA==", "BAW5c6F9UZiwdDlwm0udJQ==", "OYEX2V+vXODhONUXrlP+2Q==", "gJiMQqGKTFBQ6wPIVQoHTA==", "wFjm0l2DbL3z+i7Bgpf5l3gBbiHLVJx0++r0j7kB9mc=", "aYzMVpVlQpIAhKSud0AetA==", "a79PD2rXccFkWKLCusG4Lg==", "j8mKBy9MuNTGksZypCrGKg==", "vwu2UHf3gVdtG4lSQd5X5A==", "8BMV5V164qNZJWSqyBZ2MA==", "st3r4csKDHT/alCwUXTgAw==", "2DP0pPvEzadEAuaF08h479K+8csb48hlRghXOi3mw/Y="));
        String stringExtra = intent.getStringExtra("SECRET");
        for (int i = 0; i < arrayList.size(); i++) {
            new String();
            this.list.add(AES.decrypt(arrayList.get(i).toString(), stringExtra));
        }
        this.adapter = new ArrayAdapter(this, 17367043, this.list);
        this.pwdDb.setAdapter((ListAdapter) this.adapter);
    }
}
