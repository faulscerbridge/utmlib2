package com.example.frazatas.utmlib2;

/**0196987396
 * Created by frazatas on 29/4/2016.
 */
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

public class MainBarcode extends AppCompatActivity {

    public static final String BARCODE_KEY = "BARCODE";

    private Barcode barcodeResult;
    ProgressDialog pDialogb;
    private TextView result;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        result = (TextView) findViewById(R.id.testhaha);
        final ImageButton fab = (ImageButton) findViewById(R.id.borbut);
        assertNotNull(result);
        assertNotNull(fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScan();
            }
        });
        if(savedInstanceState != null){
            Barcode restoredBarcode = savedInstanceState.getParcelable(BARCODE_KEY);

            if(restoredBarcode != null){
                result.setText(restoredBarcode.rawValue);
                String result1= result.getText().toString();
                barcodeResult = restoredBarcode;
               // bar(result1);
            }

            }


    }

    private void startScan() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(MainBarcode.this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withCenterTracker()
                .withText("Scanning...")
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        barcodeResult = barcode;
                        result.setText(barcode.rawValue);
                        bar(barcode.rawValue);
                        //String BARCODE = barcode.rawValue.toString();
                        //Toast.makeText(getApplicationContext(), barcode.rawValue, Toast.LENGTH_LONG).show();
                       // getDataBorrow(BARCODE);

                    }
                })

                .build();
        materialBarcodeScanner.startScan();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BARCODE_KEY, barcodeResult);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MaterialBarcodeScanner.RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScan();
            return;
        }
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(android.R.string.ok, listener)
                .show();
    }

    public void bar(final String result){


        String user = getIntent().getStringExtra("userid");
        pDialogb = new ProgressDialog(MainBarcode.this);
        pDialogb.setMessage("Processing...");
        pDialogb.setIndeterminate(false); pDialogb.setCancelable(true);
        pDialogb.show();
       // Toast.makeText(getApplicationContext(), user, Toast.LENGTH_LONG).show();

        getDataBorrow(result, user);
    }

    public void getDataBorrow(final String sbarcode, final String suserid){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                // Depends on your web service


                InputStream inputStream = null;
                String result1 = null;


                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("sbarcode", sbarcode));
                    nameValuePairs.add(new BasicNameValuePair("suserid", suserid));

                    DefaultHttpClient httpclient = new DefaultHttpClient();//(new BasicHttpParams());
                    HttpPost httppost = new HttpPost("http://domain/borrow.php");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    //httppost.setHeader("Content-type", "application/json");
                    HttpResponse response = httpclient.execute(httppost);

                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result1 = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish) {
                    }
                }
                return result1;
            }

            @Override
            protected void onPostExecute(String result1){
                Toast.makeText(MainBarcode.this, result1, Toast.LENGTH_SHORT).show();
                pDialogb.dismiss();

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
}
