package com.example.promise;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.promise.retrofit.Model;
import com.example.promise.retrofit.RetrofitAPI;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Kakao_Login_Activity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private View loginButton, logoutButton;
    private TextView nickName;
    private ImageView profileImage;

    private EditText userId;
    private EditText userPass;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login);


        loginButton = findViewById(R.id.login);

        nickName = findViewById(R.id.nickname);

        userId = findViewById(R.id.user_id_login);
        userPass = findViewById(R.id.user_pass_login );
        login = findViewById(R.id.logon_btn);


        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String user_id = userId.getText().toString();
                String user_pass = userPass.getText().toString();

                Model model = new Model(user_id, user_pass, null);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://3.34.97.79:8080/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);



                Call<Model> call = retrofitAPI.postLogin(model);
                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        if (response.isSuccessful()) {
                            Model model = response.body();
                            Log.d(TAG, "ID: " + model.toString());

                            Intent intent = new Intent(Kakao_Login_Activity.this, MainActivity.class);


                            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("user_id", user_id);
                            editor.apply();




                            startActivity(intent);

                        }
                        else if(response.code() == 400){
                            Log.d(TAG, "ID: " + response.code());
                            //비번이 틀립니다
                            Toast.makeText(getApplicationContext(), "비번이 틀립니다", Toast.LENGTH_LONG).show();

                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {

                        Toast.makeText(getApplicationContext(), "비번이 틀립니다", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "에러메세지: " + t.getMessage());
                    }
                });



            }
        });

//        회원가입 으로 이동
        Button btn2 = (Button) findViewById(R.id.to_register_btn);
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Kakao_Login_Activity.this, Register.class);
                startActivity(intent);
            }
        });

        // 메인으로 이동
        Button btn = (Button) findViewById(R.id.button5);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        // 카카오가 설치되어 있는지 확인 하는 메서드또한 카카오에서 제공 콜백 객체를 이용함
        Function2<OAuthToken, Throwable, Unit> callback = new  Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                // 이때 토큰이 전달이 되면 로그인이 성공한 것이고 토큰이 전달되지 않았다면 로그인 실패
                if(oAuthToken != null) {

                }
                if (throwable != null) {

                }
                updateKakaoLoginUi();
                return null;
            }
        };
        // 로그인 버튼
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(Kakao_Login_Activity.this)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(Kakao_Login_Activity.this, callback);
                }else {
                    UserApiClient.getInstance().loginWithKakaoAccount(Kakao_Login_Activity.this, callback);
                }
            }
        });

        updateKakaoLoginUi();
    }
    private  void updateKakaoLoginUi(){
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                // 로그인이 되어있으면
                if (user!=null){

                    // 유저의 아이디
                    Log.d(TAG,"invoke: id" + user.getId());
                    // 유저의 어카운트정보에 이메일
                    Log.d(TAG,"invoke: nickname" + user.getKakaoAccount().getEmail());
                    // 유저의 어카운트 정보의 프로파일에 닉네임
                    Log.d(TAG,"invoke: email" + user.getKakaoAccount().getProfile().getNickname());
                    // 유저의 어카운트 파일의 성별
                    Log.d(TAG,"invoke: gerder" + user.getKakaoAccount().getGender());
                    // 유저의 어카운트 정보에 나이
                    Log.d(TAG,"invoke: age" + user.getKakaoAccount().getAgeRange());

                    nickName.setText(user.getKakaoAccount().getProfile().getNickname()+"님 환영합니다!");


                    loginButton.setVisibility(View.GONE);

                }else {
                    // 로그인이 되어 있지 않다면 위와 반대로
                    nickName.setText(null);

                    loginButton.setVisibility(View.VISIBLE);

                }
                return null;
            }
        });
    }
}





//   <meta-data
//            android:name="com.kakao.sdk.AppKey"
//            android:value=	"13fdc35e90c9a79646e3de36f199f936"/>

//
