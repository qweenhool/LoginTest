package com.ydl.logintest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.tv_register_account)
    TextView tvRegisterAccount;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        sp = getSharedPreferences("Token", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
    }

    @OnClick({R.id.btn_login, R.id.tv_forget_password, R.id.tv_register_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_forget_password:

                break;
            case R.id.tv_register_account:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void login() {

        if (NetworkState.networkConnected(this)) {
            String imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
            String account = etAccount.getText().toString();
            String password = etPassword.getText().toString();
            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {

                String loginInfo = imei + "#" + account + "#" + password;
                OkHttpUtils
                        .post()
                        .url(UrlConstants.LOGIN_URL)
                        .addParams(UrlConstants.ACCESS_KEY, Base64.getBase64(loginInfo))
                        .build()
                        .execute(new ResultJsonCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(ResultJson response, int id) {
                                switch (response.getErrorCode()) {
                                    case 0://成功，返回token
                                        Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                        String data = response.getData();
                                        String token = Base64.getBase64(data);
                                        sp.edit().putString("token", token).apply();
                                        break;
                                    case 1://服务器异常
                                        String errorString1 = response.getErrorString();
                                        Toast.makeText(MainActivity.this, errorString1, Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2://用户名不存在
                                        String errorString2 = response.getErrorString();
                                        Toast.makeText(MainActivity.this, errorString2, Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3://密码不正确
                                        String errorString3 = response.getErrorString();
                                        Toast.makeText(MainActivity.this, errorString3, Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;

                                }
                            }
                        });

            }
        }
    }
}
