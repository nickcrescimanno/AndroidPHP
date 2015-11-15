package am.dx.shilaoshiyongyuan.androidphp;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import am.dx.shilaoshiyongyuan.androidphp.name.HttpFileUpload;


public class UserPage extends Activity {

    private static final int SELECT_AUDIO = 2;
    String selectedPath = "";

    TextView user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userpage);


        String string;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            string = null;
        } else {
            string = extras.getString("EXTRA");
        }

        user = (TextView) findViewById(R.id.TextView01);
        user.setText("Welcome " + string + "!");



        // Get the reference of ListViewAnimals
        ListView songs=(ListView)findViewById(R.id.songList);

        //make list of songs
        final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myRecordings/");
        File[] filelist = dir.listFiles();
        final String[] theNamesOfFiles = new String[filelist.length];
        for (int i = 0; i < theNamesOfFiles.length; i++) {
            theNamesOfFiles[i] = filelist[i].getName();
        }

        //creates adapter, pass names of songs
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, theNamesOfFiles);

        // Set The Adapter
        songs.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item


        songs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                try {

                    //MediaPlayer myPlayer = new MediaPlayer();
                    //myPlayer.setDataSource(dir + "/" + theNamesOfFiles[position]);
                    //myPlayer.prepare();
                    //myPlayer.start();


                    selectedPath = dir.toString() + "/" + theNamesOfFiles[position];
                    String path=selectedPath;//it contain your path of image..im using a temp string..
                    final String filename=path.substring(path.lastIndexOf("/")+1);

                    new Thread(new Runnable() {
                        public void run() {
                            UploadFile(selectedPath, filename);
                            Log.d("Debug", filename);
                            //doFileUpload();

                        }
                    }).start();

                    //playBtn.setEnabled(false);
                    //stopPlayBtn.setEnabled(true);

                    Log.d("NICK", dir + "/" + theNamesOfFiles[position]);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                }

            }
        });


    }

    /*
    private void doFileUpload(){
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String lineEnd = "rn";
        String twoHyphens = "--";
        String boundary =  "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*1024;
        String responseFromServer = "";
        String urlString = "http://www.shilaoshiyongyuan.dx.am/upload_audio.php";
        String path=selectedPath;//it contain your path of image..im using a temp string..
        String filename=path.substring(path.lastIndexOf("/")+1);

        try
        {
            //------------------ CLIENT REQUEST
            FileInputStream fileInputStream = new FileInputStream(new File(selectedPath) );
            // open a URL connection to the Servlet
            URL url = new URL(urlString);
            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection();
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
            dos = new DataOutputStream( conn.getOutputStream() );
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + filename + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0)
            {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            Log.d("Debug","File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();
        }
        catch (MalformedURLException ex)
        {
            Log.d("Debug", "error: " + ex.getMessage(), ex);
        }
        catch (IOException ioe)
        {
            Log.d("Debug", "error: " + ioe.getMessage(), ioe);
        }
        //------------------ read the SERVER RESPONSE
        try {
            inStream = new DataInputStream ( conn.getInputStream() );
            String str;

            while (( str = inStream.readLine()) != null)
            {
                Log.d("Debug","Server Response "+str);
            }
            inStream.close();

        }
        catch (IOException ioex){
            Log.d("Debug", "error: " + ioex.getMessage(), ioex);
        }
    }
    */
    public void UploadFile(String location,String title){
        try {
            // Set your file path here
            FileInputStream fstrm = new FileInputStream(location);

            // Set your server page url (and the file title/description)
            HttpFileUpload hfu = new HttpFileUpload("http://www.shilaoshiyongyuan.dx.am/upload_audio.php", title, "stufffffz");
            Log.d("Debug", "sending to upload class...");
            hfu.Send_Now(fstrm);

        } catch (FileNotFoundException e) {
            // Error: File not found
            Log.d("Debug", "file not found...");
        }
    }
}