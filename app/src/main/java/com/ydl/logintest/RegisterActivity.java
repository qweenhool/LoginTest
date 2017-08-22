package com.ydl.logintest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_register)
    public void onViewClicked() {

        if (NetworkState.networkConnected(this)) {
            String account = etAccount.getText().toString();
            String password = etPassword.getText().toString();
            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {

                String info = account + "#" + password;
                OkHttpUtils
                        .post()
                        .url(UrlConstants.REG_URL)
                        .addParams(UrlConstants.ACCESS_KEY, Base64.getBase64(info))
                        .build()
                        .execute(new ResultJsonCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(ResultJson response, int id) {
                                switch (response.getErrorCode()) {
                                    case 0://成功
                                        String data = response.getData();
                                        Toast.makeText(RegisterActivity.this, data, Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1://服务器异常
                                        String errorString1 = response.getErrorString();
                                        Toast.makeText(RegisterActivity.this, errorString1, Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2://用户名格式不对
                                        String errorString2 = response.getErrorString();
                                        Toast.makeText(RegisterActivity.this, errorString2, Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3://密码格式不对
                                        String errorString3 = response.getErrorString();
                                        Toast.makeText(RegisterActivity.this, errorString3, Toast.LENGTH_SHORT).show();
                                        break;
                                    case 4://用户已存在
                                        String errorString4 = response.getErrorString();
                                        Toast.makeText(RegisterActivity.this, errorString4, Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;

                                }
                            }
                        });

            }
        } else {
            Toast.makeText(this, "网络不可用，请检查网络", Toast.LENGTH_SHORT).show();
        }
    }
}
