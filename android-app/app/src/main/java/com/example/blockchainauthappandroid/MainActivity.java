package com.example.blockchainauthappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void signIn(View view){
        EditText profileId_tv = findViewById(R.id.profileID); //user's profile id from respective field
        EditText address_tv = findViewById(R.id.address); //user's address from respective field
        String apiKey = "<apikey>"; //Moralis api key, required to use the Api

        OkHttpClient client = new OkHttpClient(); //a tool that allows to perform http requests for verification of profile id,
        //it requires implementation 'com.squareup.okhttp3:okhttp:4.10.0' in app gradle file and need imports

        //the following request calls https://authapi.moralis.io/ and asks for information on the given profile id using given api key,
        //if the user exists, it returns the user's address
        Request request = new Request.Builder()
                .url("https://authapi.moralis.io/profile/" + profileId_tv.getText() + "/addresses")
                .addHeader("accept", "application/json")
                .addHeader("X-API-Key", apiKey)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(), "Error: "+e.toString(), Toast.LENGTH_SHORT).show(); //throws exception on error
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle response
                final String responseBody = response.body().string(); //the response given by https://authapi.moralis.io/

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //looks if the given response contains provided address, if there is no address corresponding to the given one, throws an error,
                        //if it exists, the user is logged in
                        if(responseBody.contains(address_tv.getText())){
                            Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_SHORT).show();
                            setContentView(R.layout.logged_in);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Error: No such Address found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void signUp(View view){
        String url = "<url>";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
    public void logOFF(View view){
        setContentView(R.layout.activity_main);
    }
}
