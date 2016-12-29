package com.android.joseantonio.fb;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnvideo;
    

    //variables para la llamda de fb
    private CallbackManager cM;
    private LoginButton lb;

    //variables para llamada de anincio
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Cargar fb
        FacebookSdk.sdkInitialize(getApplicationContext());
        cM = CallbackManager.Factory.create();

        getFbKeyHash("PqmE3CoQ+WWvzLURzyIN29wG5CU=");//clave de developer de fb
        setContentView(R.layout.activity_main);

        lb = (LoginButton)findViewById(R.id.login_facebook);
        btnvideo = (Button)findViewById(R.id.video);
        btnvideo.setOnClickListener(this);

        lb.registerCallback(cM, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this,"Inicio de sesion exitoso!",Toast.LENGTH_SHORT).show(); //mensaje de secion exitosa
                btnvideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       videoYoutube();
                    }
                });
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this,"Inicio de sesión cancelado!",Toast.LENGTH_SHORT).show(); //mensaje de sesion cancelada

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this,"Inicio de sesion NO exitoso!",Toast.LENGTH_SHORT).show();//mensaje de q no inicio la sesion
            }
        });

        //llamar el anuncio
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);


    }

    private void getFbKeyHash(String packgeName) {
        try{
            PackageInfo info = getPackageManager().getPackageInfo(
                    packgeName, PackageManager.GET_SIGNATURES);
            for (Signature signature: info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash :", Base64.encodeToString(md.digest(),Base64.DEFAULT)); //hacemos log del keyhash
                System.out.println("KeyHash: " + Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        }catch (PackageManager.NameNotFoundException e){

        }catch (NoSuchAlgorithmException e){

        }
    }

    protected void onActivityResult(int reqCode, int resCode, Intent i){
        cM.onActivityResult(reqCode,resCode,i);
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

    @Override
    public void onClick(View v) {

    }

    public void videoYoutube (){
        Intent youtube = new Intent(this,Video.class);
        startActivity(youtube);
    }
}
