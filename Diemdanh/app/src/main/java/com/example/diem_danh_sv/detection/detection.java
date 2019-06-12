package com.example.diem_danh_sv.detection;

import android.Manifest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.*;
import android.graphics.*;
import android.widget.*;
import android.provider.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.diem_danh_sv.Account;
import com.example.diem_danh_sv.R;

import dmax.dialog.SpotsDialog;
import edmt.dev.edmtdevcognitiveface.Contract.Face;
import edmt.dev.edmtdevcognitiveface.Contract.IdentifyResult;
import edmt.dev.edmtdevcognitiveface.Contract.Person;
import edmt.dev.edmtdevcognitiveface.Contract.TrainingStatus;
import edmt.dev.edmtdevcognitiveface.FaceServiceClient;
import edmt.dev.edmtdevcognitiveface.FaceServiceRestClient;
import edmt.dev.edmtdevcognitiveface.Rest.ClientException;
import edmt.dev.edmtdevcognitiveface.Rest.Utils;


public class detection extends AppCompatActivity {
    // Replace `<API endpoint>` with the Azure region associated with
    private final String apiEndpoint = "https://eastasia.api.cognitive.microsoft.com/face/v1.0";

    // Replace `<Subscription Key>` with your subscription key.
    private final String subscriptionKey = "33b1eaf1acdf47daae276f6b3011f5b8";

    private final FaceServiceClient faceServiceClient =
            new FaceServiceRestClient(apiEndpoint, subscriptionKey);

    //doi tuong personGroup
    private final String personGroupId = "myfriends";
    Face[] DetectedFace;

    private final int PICK_IMAGE = 1;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;
    private ProgressDialog detectionProgressDialog;

    private String pathToFile;
    private ImageView img;
    private TextView textName;
    private TextView textMSSV;

    //doi tuong account nhan du lieu
    private Account account;

    //doi tuong kiem tra dieu kien deteced va recognition
    private int n = 0;
    private int m = 0;

    //doi tuong insert du lieu
    public static final String url_sv = "https://diemdanhlophoc.000webhostapp.com/diem_danh_sv.php";

    public static final String KEY_So_lan = "Solan";
    public static final String KEY_ma_so = "ma_so";
    public static final String KEY_email_id_GV = "Email_ID_GV";

    //bien TAG
    public static final String TAG = Detection.class.getSimpleName();


    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.diem_danh_gv);

        //actionbar
        ActionBar actionBar = getSupportActionBar ();
        actionBar.setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

        if (Build.VERSION.SDK_INT > 23) {
            requestPermissions ( new String[]{Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE} , 2 );
        }



        // tham chieu toi id trong diem_danh_gv
        img = (ImageView) findViewById ( R.id.imageView1 );
        textName = (TextView) findViewById ( R.id.textName );
        textMSSV = (TextView) findViewById ( R.id.textMSSV );

        //bat su kien voi buttoon 2
        Button button2 = findViewById ( R.id.button2 );
        button2.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent ();
                n = n + 1;
            }
        } );

        detectionProgressDialog = new ProgressDialog ( this );

        // bat su kien voi button1 va lay anh tu thu vien ra
        Button button1 = findViewById ( R.id.button1 );
        button1.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (n > 0) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream ();
                    bitmap.compress ( Bitmap.CompressFormat.JPEG , 100 , outputStream );
                    ByteArrayInputStream inputStream = new ByteArrayInputStream ( outputStream.toByteArray () );
                    new Detection ().execute ( inputStream );
                    m = m + 1;
                } else {
                    Toast.makeText ( detection.this , "Ban chua chup anh" , Toast.LENGTH_LONG ).show ();
                }
            }
        } );
        // bat su kien voi button 3
        Button button3 = findViewById ( R.id.button3 );
        button3.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (n >= m && n > 0 && m > 0) {
                    //neu co khuon mat
                    if (DetectedFace.length > 0) {
                        final UUID[] faceIds = new UUID[DetectedFace.length];
                        for (int i = 0; i < DetectedFace.length; i++) {
                            faceIds[i] = DetectedFace[i].faceId;
                        }
                        //nhan dien khuon mat
                        new Recognition ().execute ( faceIds );
                    } else {
                        Toast.makeText ( detection.this , "Khong co khuon mat duoc phat hien" , Toast.LENGTH_LONG ).show ();
                    }
                } else if (n == 0) {
                    Toast.makeText ( detection.this , "Ban chua chup anh" , Toast.LENGTH_LONG ).show ();
                } else if (m == n) {
                    Toast.makeText ( detection.this , "Ban chua detection" , Toast.LENGTH_LONG ).show ();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //tao file anh chua anh
        File fileimg = new File(getFilesDir(), "image.jpg");
        if (requestCode == REQUEST_ID_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            //nhan du lieu va set anh len
            bitmap = BitmapFactory.decodeFile(pathToFile);
            img.setImageBitmap(bitmap);

            FileOutputStream outputStream = null;
            try {
                //du lieu ra outputstream
                outputStream = new FileOutputStream(fileimg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //chat luong anh
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            try {
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //chup anh tu camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent ( MediaStore.ACTION_IMAGE_CAPTURE );
        //dam bao chi co mot hoat dodng camera
        if (takePictureIntent.resolveActivity ( getPackageManager () ) != null) {
            // tao file ma anh gui den
            File photoFile = null;
            try {
                photoFile = createImageFile ();
            } catch (IOException ex) {
                //ERROR
            }
            // neu co file gui anh vao photoFIle
            if (photoFile != null) {
                pathToFile = photoFile.getAbsolutePath ();
                Uri photoURI = FileProvider.getUriForFile ( this ,
                        "com.example.diem_danh_sv.fileprovider" ,
                        photoFile );
                takePictureIntent.putExtra ( MediaStore.EXTRA_OUTPUT , photoURI );
                startActivityForResult ( takePictureIntent , REQUEST_ID_IMAGE_CAPTURE );
            }
        }
    }

    // luu hinh anh thu nho
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat ( "yyyyMMdd_HHmmss" ).format ( new Date () );
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir ( Environment.DIRECTORY_PICTURES );
        File image = File.createTempFile (
                imageFileName ,  /* prefix */
                ".jpg" ,         /* suffix */
                storageDir      /* directory */
        );

        // luu file: su dung ACTION_VIEW
        pathToFile = image.getAbsolutePath ();
        return image;
    }

    //phat hien khuon mat hinh anh
    class Detection extends AsyncTask <InputStream, String, Face[]> {

        AlertDialog alertDialog = new SpotsDialog.Builder ()
                .setContext ( detection.this )
                .setCancelable ( false )
                .build ();

        @Override
        protected void onPreExecute() {
            alertDialog.show ();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            alertDialog.setMessage ( values[0] );
        }

        @Override
        protected Face[] doInBackground(InputStream... inputStreams) {
            try {
                publishProgress ( "Detecting...." );
                Face[] result = faceServiceClient.detect ( inputStreams[0] , true , false , null );
                if (result == null) {
                    return null;
                } else {
                    return result;
                }
            } catch (ClientException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Face[] faces) {
            alertDialog.dismiss ();
            if (faces == null) {
                Toast.makeText ( detection.this , "No Face detected" , Toast.LENGTH_LONG ).show ();
            } else {
                img.setImageBitmap ( Utils.drawFaceRectangleOnBitmap ( bitmap , faces , Color.YELLOW ) );
                DetectedFace = faces;
            }
        }
    }

    //nhan dien hinh anh
    class Recognition extends AsyncTask <UUID, String, IdentifyResult[]> {
        AlertDialog alertDialog = new SpotsDialog.Builder ()
                .setContext ( detection.this )
                .setCancelable ( false )
                .build ();

        @Override
        protected void onPreExecute() {
            alertDialog.show ();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            alertDialog.setMessage ( values[0] );
        }

        @Override
        protected IdentifyResult[] doInBackground(UUID... uuids) {
            publishProgress ( "Getting person group status...." );
            try {
                //training face
                TrainingStatus trainingStatus = faceServiceClient.getPersonGroupTrainingStatus ( personGroupId );
                if (trainingStatus.status != TrainingStatus.Status.Succeeded) {
                    Log.d ( "ERROR" , "PersonGroup Training Status is " + trainingStatus.status );
                }

                publishProgress ( "Recognition...." );
                IdentifyResult[] results = faceServiceClient.identity ( personGroupId , uuids , 1 );
                return results;
            } catch (ClientException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
            return null;
        }

        @Override
        protected void onPostExecute(IdentifyResult[] identifyResults) {
            alertDialog.dismiss ();
            if (identifyResults != null) {
                for (IdentifyResult identifyResult : identifyResults)
                    if (new PersonDetectionTask ().execute ( identifyResult.candidates.get ( 0 ).personId ) == null) {
                        Toast.makeText ( detection.this , "Khong nhan duoc khuon mat nao" , Toast.LENGTH_LONG ).show ();
                    } else {
                        return;
                    }
            } else {
                Toast.makeText ( detection.this , "Khong nhan duoc khuon mat nao" , Toast.LENGTH_LONG ).show ();
            }
        }
    }

    class PersonDetectionTask extends AsyncTask <UUID, String, Person> {
        AlertDialog alertDialog = new SpotsDialog.Builder ()
                .setContext ( detection.this )
                .setCancelable ( false )
                .build ();

        @Override
        protected void onPreExecute() {
            alertDialog.show ();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            alertDialog.setMessage ( values[0] );
        }

        @Override
        protected Person doInBackground(UUID... uuids) {
            try {
                return faceServiceClient.getPerson ( personGroupId , uuids[0] );
            } catch (ClientException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Person person) {
            alertDialog.dismiss ();
            img.setImageBitmap ( Utils.drawFaceRectangleWithTextOnBitmap ( bitmap, DetectedFace,
                    person.name, Color.YELLOW, 100 ) );
            String nameStudent = person.name;
            if (nameStudent.length () > 0) {

                String[] output = nameStudent.split ( "-" );
                textName.setText ( output[0] );
                textMSSV.setText ( output[1] );

                //nhan du email_id_gv
                Bundle bundle = getIntent ().getExtras ();
                final String email_id_gv = bundle.getString ( "email_id" );

                //update diem danh
                String Ma_so = textMSSV.getText().toString().trim();
                UpdateData (Ma_so, email_id_gv ,"1");
            }
        }
    }


    private void UpdateData(final String ma_so , final String email_id_gv , final String solan) {
        StringRequest update = new StringRequest ( Request.Method.POST , url_sv ,
                new Response.Listener <String> () {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim ().equals ( "success" )) {
                            Toast.makeText ( getApplicationContext () , "Đã điểm danh" , Toast.LENGTH_LONG).show ();
                        } else {
                            Toast.makeText ( getApplicationContext () , "Không điểm danh" , Toast.LENGTH_LONG ).show ();
                        }
                    }
                } ,
                new Response.ErrorListener () {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d ( TAG , "Error: " + error.getMessage () );
                    }
                } ) {
            @Override
            protected Map <String, String> getParams() {
                Map <String, String> params = new HashMap <> ();
                params.put ( KEY_ma_so ,  ma_so );
                params.put ( KEY_email_id_GV , email_id_gv  );
                params.put ( KEY_So_lan , solan  );
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( update );
    }

    //   them menu vao khi khoi dong
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.menu_activity , menu );
        return super.onCreateOptionsMenu ( menu );
    }

    //   bat su kien nut back tren actonbar

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home:
                onBackPressed ();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected ( item );
    }
}