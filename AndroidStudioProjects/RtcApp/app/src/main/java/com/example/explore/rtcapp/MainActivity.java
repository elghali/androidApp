package com.example.explore.rtcapp;

import android.content.Context;
import android.media.MediaRecorder;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoCapturerAndroid;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;
import org.webrtc.videoengine.VideoCaptureAndroid;

public class MainActivity extends AppCompatActivity {
    PeerConnectionFactory peerConnectionFactory = new PeerConnectionFactory();
    Context context = getApplicationContext();
    boolean initializeAudio = true;
    boolean initializeVideo = true;
    boolean videoCodecHwAcceleration = true;
    GLSurfaceView videoView = (GLSurfaceView) findViewById(R.id.glview_call);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    //initiate peerconnectionfactory
        PeerConnectionFactory.initializeAndroidGlobals(
                context,
                initializeAudio,
                initializeVideo,
                videoCodecHwAcceleration,
                null
        );
    }

    public void videoRPC(View v){
        VideoCapturer capturer;
        MediaConstraints videoConstraints = new MediaConstraints();
        MediaConstraints audioConstraints = new MediaConstraints();

        videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                "OfferToReceiveVideo", "true"
        ));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                "OfferToReceiveAudio", "true"
        ));
       int nbDevices = VideoCapturerAndroid.getDeviceCount();
        String frontCameraName;

            frontCameraName = VideoCapturerAndroid.getNameOfFrontFacingDevice();
            capturer = VideoCapturerAndroid.create(frontCameraName);


            Toast.makeText(getApplicationContext(),"You don't have front camera",Toast.LENGTH_SHORT).show();

        VideoSource videoSource =
                peerConnectionFactory.createVideoSource(capturer, videoConstraints);
        VideoTrack localVideoTrack =
                peerConnectionFactory.createVideoTrack("this video track", videoSource);
        AudioSource audioSource =
                peerConnectionFactory.createAudioSource(audioConstraints);
        AudioTrack localAudioTrack =
                peerConnectionFactory.createAudioTrack("myAudio",audioSource);

        VideoRendererGui.setView(videoView, new Runnable() {
            @Override
            public void run() {

            }
        });
        VideoRenderer renderer = VideoRenderer.createGui(50,100);
        localVideoTrack.addRenderer(renderer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
