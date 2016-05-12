package comp380.get2gether;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
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
    // this is for testing need to get this data from db of the user (who's data being encoded in QR CODE

    private final ParseUser CURRENTUSER =ParseUser.getCurrentUser();
    private String uName = CURRENTUSER.getUsername().toString();
    private String uPhone = "";
    private String uEmail = "";
    private String contact;




    //this is for data that will be decoded from the QR THIS NEED TO BE ADDED
    private String newContactName;
    private String newContactPhone;
    private String newContactEmail;
    private String scannedText;
    CountDownTimer timer;


    //scanner variables:
    private TextView contentTxt;
    IntentIntegrator scanIntegrator = new IntentIntegrator(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {


        if (CURRENTUSER.has("email"))
            uEmail = CURRENTUSER.getString("email");//email
        if (CURRENTUSER.has("phone"))
            uPhone = CURRENTUSER.getString("phone");//"8181234567"


        super.onCreate(savedInstanceState);
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
                                                          contact = combineStrings(uName, uPhone, uEmail);
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
//        final Button cancelButton = (Button) findViewById(R.id.cancelbutton);
//        cancelButton.setVisibility(View.INVISIBLE);
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                timer.cancel();
//            }
//        });

        // scan button:
        Button scanButton = (Button) findViewById(R.id.scanqrbutton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //make sure that scan button has been pressed:
                try {
                    if (v.getId() == R.id.scanqrbutton) {

                        scanIntegrator.initiateScan();

//                        timer = new CountDownTimer(10000, 10000){
//                            //add a new field of text: "If you dont want to add this contact press cancel"
//
//                          public void onTick(long millisUntilFinished) {
////                                mTextField.setText(millisUntilFinished / 1000);
//                              //display text:
//                            cancelButton.setVisibility(View.VISIBLE);
//                           }
//
//                            public void onFinish() {
//
//                            }
//                        }.start();


                    }
                } catch (ActivityNotFoundException anfe) {
                    Log.e("onCreate", "Scanner Not Found", anfe);
                }
            }
        });


    }

    //method to decode scanned data
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            scannedText = scanningResult.getContents();
            stringDecoder(scannedText);
            contentTxt = (TextView) findViewById(R.id.scan_content);
            contentTxt.setText("You added:"+"\nName: " + newContactName + "\nPhone: " + newContactPhone + "\nEmail: " + newContactEmail);
            insertNewContact();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public String combineStrings(String userName, String userPhone, String userEmail) {
        String separator = "|";
        String combinedText = separator + userName + separator + userPhone + separator + userEmail + separator;
        return combinedText;
    }

    public void stringDecoder(String contact) {
        int length = contact.length();
        int nextCharPtr = 0;
        String name = "";
        String phone = "";
        String email = "";

        if (length > 0) {
            //get name:
            for (int i = 1; i < length; i++) {
                char current = contact.charAt(i);
                nextCharPtr = i + 1;
                if (current == '|') {
                    i = length;
                } else
                    name += current;
            }
            //get phone:
            for (int i = nextCharPtr; i < length; i++) {
                char current = contact.charAt(i);
                nextCharPtr = i + 1;
                if (current == '|') {
                    i = length;
                } else
                    phone += current;
            }
            //get email:
            for (int i = nextCharPtr; i < length; i++) {
                char current = contact.charAt(i);
                nextCharPtr = i + 1;
                if (current == '|') {
                    i = length;
                } else
                    email += current;
            }

        }

        //add data to the private values:
        if (name != null)
            newContactName = name;
        if (phone != null)
            newContactPhone = phone;
        if (email != null)
            newContactEmail = email;
    }

    //insert a new contact to the phone:
    public void insertNewContact() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        // Sets the MIME type to match the Contacts Provider
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        if (newContactName != null)
            intent.putExtra(ContactsContract.Intents.Insert.NAME, newContactName);
        if (newContactEmail != null)
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, newContactEmail);
        if (newContactPhone != null)
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, newContactPhone);
        startActivity(intent);

    }
}






