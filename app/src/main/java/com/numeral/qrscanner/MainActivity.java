package com.numeral.qrscanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    InterstitialAd mInterstitialAd;
    ImageButton share_button;
    private Button buttonScan;
    private TextView textViewName, textViewAddress;
    //qr code scanner object
    private IntentIntegrator qrScan;
    private AdView mBannerAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        mBannerAd = (AdView) findViewById(R.id.banner_AdView);

        share_button = findViewById(R.id.share_button);

        share_button.setVisibility(View.GONE);


        AdRequest request = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("1B701F31A2BBEB5B2AF3464EB02DF279")
                .build();


        mBannerAd.loadAd(request);

        mBannerAd.setAdListener(new AdListener(){
        @Override
        public void onAdLoaded() {


        }

        @Override
        public void onAdClosed() {

        }

        @Override
        public void onAdFailedToLoad(int i) {
            super.onAdFailedToLoad(i);

        }

        @Override
        public void onAdLeftApplication() {
            super.onAdLeftApplication();
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
        }
    });


        //intializing scan object
        qrScan = new IntentIntegrator(this);

        buttonScan.setOnClickListener(this);
        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, textViewAddress.getText().toString());
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "QRCode Link");
                startActivity(Intent.createChooser(intent, "Share link"));
            }
        });




    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
//                    textViewName.setText(obj.getString("name"));
                    textViewAddress.setText(obj.getString("address"));
                } catch (JSONException e) {
                    e.printStackTrace();
//                    Log.e("error",e.toString());

                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                    textViewAddress.setText(result.getContents());
                    share_button.setVisibility(View.VISIBLE);

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        qrScan.setPrompt(this.getString(R.string.scan_bar_code));
        qrScan.setBarcodeImageEnabled(true);
        qrScan.setOrientationLocked(false);//"additional property"

        qrScan.initiateScan();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toc) {

            Intent intent = new Intent(getApplicationContext(),
                    PrivacyPolicyActivity.class);
            startActivity(intent);        }


        return true;
    }


}
