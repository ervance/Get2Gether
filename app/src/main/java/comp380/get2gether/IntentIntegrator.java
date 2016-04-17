package comp380.get2gether;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by olgak on 4/16/16.
 */
public class IntentIntegrator {
    private static final String PACKAGE = "com.google.zxing.client.android";

    catch (ActivityNotFoundException e) {
        return showDownloadDialog(activity, stringTitle, stringMessage, stringButtonYes, stringButtonNo);
    }

    private static AlertDialog showDownloadDialog(final Activity activity,
                                                  CharSequence stringTitle,
                                                  CharSequence stringMessage,
                                                  CharSequence stringButtonYes,
                                                  CharSequence stringButtonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(activity);
        downloadDialog.setTitle(stringTitle);
        downloadDialog.setMessage(stringMessage);
        downloadDialog.setPositiveButton(stringButtonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + PACKAGE);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    activity.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                    // Hmm, market is not installed
                    Log.w(TAG, "Android Market is not installed; cannot install Barcode Scanner");
                }
            }
        });
        downloadDialog.setNegativeButton(stringButtonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        return downloadDialog.show();
    }

}
