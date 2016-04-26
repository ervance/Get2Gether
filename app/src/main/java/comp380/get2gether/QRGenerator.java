package comp380.get2gether;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.parse.ParseUser;


public class QRGenerator extends AppCompatActivity {

    private String LOG_TAG = "GenerateQRCode";
    // this is for testing need to get this data from db
    private final ParseUser currentUser = ParseUser.getCurrentUser();
    private String contact;// = "addalldatahere (create a method that combines all data together";

    //scanner variables:
    private TextView formatTxt, contentTxt;
    IntentIntegrator scanIntegrator = new IntentIntegrator(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (currentUser == null){
            //send them to log in
            Intent intent = new Intent(QRGenerator.this, LoginActivity.class);
            startActivity(intent);
        }
        else {

            contact = createContactInfo();

            setContentView(R.layout.qr_layout);

            Intent intent = new Intent(Intents.Encode.ACTION);

            Button generateButton = (Button) findViewById(R.id.generateqrbutton);
            generateButton.setOnClickListener(new View.OnClickListener() {

                                                  public void onClick(View v) {
                                                      // make sure that generate qr has been presed:
                                                      switch (v.getId()) {
                                                          case R.id.generateqrbutton:

                                                              //Find screen size
                                                              WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                                                              Display display = manager.getDefaultDisplay();
                                                              Point point = new Point();
                                                              display.getSize(point);
                                                              int width = point.x;
                                                              int height = point.y;
                                                              int smallerDimension = width < height ? width : height;
                                                              smallerDimension = smallerDimension * 3 / 4;

                                                              //Encode with a QR Code image
                                                              QRGen qrCodeEncoder = new QRGen(contact,
                                                                      null,
                                                                      Contents.Type.TEXT,
                                                                      BarcodeFormat.QR_CODE.toString(),
                                                                      smallerDimension);
                                                              try {
                                                                  Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                                                                  ImageView myImage = (ImageView) findViewById(R.id.imageViewqr);
                                                                  myImage.setImageBitmap(bitmap);

                                                              } catch (WriterException e) {
                                                                  e.printStackTrace();
                                                              }


                                                              break;
                                                      }
                                                  }

                                              }

            );

            // scan button:
            Button scanButton = (Button) findViewById(R.id.scanqrbutton);
            scanButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //make sure that scan button has been pressed:
                    try {
                        if (v.getId() == R.id.scanqrbutton) {

                            scanIntegrator.initiateScan();


                        }
                    } catch (ActivityNotFoundException anfe) {
                        Log.e("onCreate", "Scanner Not Found", anfe);
                    }
                }
            });

        }//end login check
    }

    //method to decode scanend data
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            contentTxt = (TextView)findViewById(R.id.scan_content);
            contentTxt.setText("CONTENT: " + scanContent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    private String createContactInfo(){
        //creates contactinfo for qr generator
        String name = "";
        String phone;
        String email;

        if (currentUser.has("firstName"))
            name = currentUser.getString("firstName");
        if (currentUser.has("lastName"))
            name = name + currentUser.getString("lastName");

        if (currentUser.has("email"))
            email = currentUser.getString("email");
        else
            email = "N/A";
        if (currentUser.has("phone"))
            phone = currentUser.getString("phone");
        else
            phone = "N/A";

        return "Name: " + name + "\nEmail: " + email + "\nPhone Number: " + phone;

    }
}



