package comp380.get2gether;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.Intents;

public class QRGenerator extends AppCompatActivity {

    private String LOG_TAG = "GenerateQRCode";
    // this is for testing need to get this data from db
    private String name = "COMP";
    private String phone = "380";
    private String email = "email@gmail.com";
    private String contact = "addalldatahere (create a method that combines all data together";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_layout);

        Intent intent = new Intent(Intents.Encode.ACTION);


        Button button1 = (Button) findViewById(R.id.generateqrbutton);
        button1.setOnClickListener(new View.OnClickListener() {

                                       public void onClick(View v) {
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

                                               // More buttons go here (neew to add scan? or maybe make it on a different screen?) ...
                                           }
                                       }

                                   }

        );

    }

}



