package com.android.joseantonio.fb;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by Jose Antonio on 28/12/2016.
 */

public class Video extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    //variables para llamada de anincio
    private AdView mAdView;

    private static final int RECOVERY_DIALOG_REQUES = 1;
    private YouTubePlayerView yPV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video);

        yPV = (YouTubePlayerView) findViewById(R.id.youtube);
        yPV.initialize("AIzaSyDhRya9S5pLlh7_zuIVWkI5IA9KdvNZpng",this); //con este inicializamos nuestro player

        //llamar el anuncio
        mAdView = (AdView)findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b){
            youTubePlayer.loadVideo("4r8edAU8d3U");

            //youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        if (youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this,RECOVERY_DIALOG_REQUES).show();
        }
        else {
            //String errorMessage = String.format(getString(R.string.error_reproduccion), youTubeInitializationResult.toString());
            Toast.makeText(this,"Error de Reproduccion",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUES){
            getYouPlayerProvider().initialize("AIzaSyDhRya9S5pLlh7_zuIVWkI5IA9KdvNZpng",this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private YouTubePlayer.Provider getYouPlayerProvider(){
        return (YouTubePlayerView)findViewById(R.id.youtube);
    }

    /** llamada cuando se deja la actividad */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** llamada cuando corre la actividad */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** llamada antes que la actividad se destulla */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
