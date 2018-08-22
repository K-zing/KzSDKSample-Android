package com.kzingsdksample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.kzingsdk.core.KzingException;
import com.kzingsdk.entity.RegParam;
import com.kzingsdk.requests.KzingAPI;
import com.kzingsdk.requests.RegAccountAPI;
import com.kzingsdk.requests.RegParamAPI;

public class RegActivity extends Activity {

    private EditText vcodeEditText;
    private ImageView
            memberRegCodeImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        Button regParamButton = findViewById(R.id.regParamButton);
        Button regButton = findViewById(R.id.regButton);
        vcodeEditText = findViewById(R.id.vcodeEditText);
        memberRegCodeImageview = findViewById(R.id.memberRegCodeImageview);

        regParamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KzingAPI.getRegParam()
                        .addRegParamCallBack(new RegParamAPI.RegParamCallBack() {
                            @Override
                            public void onSuccess(RegParam regParam) {
                                memberRegCodeImageview.setImageBitmap(regParam.getVerifyCodeBitmap());
                            }

                            @Override
                            public void onFailure(KzingException kzingException) {

                            }
                        })
                        .request(RegActivity.this);

            }
        });
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KzingAPI.regAccount()
                        .setParamAgentCode(RegAccountAPI.NO_AGENT)
                        .setParamLoginName("testing")
                        .setParamPassword("abc123")
                        .setParamWithdrawPassword("abc123")
                        .setParamRealName("testing")
                        .setParamEmail("testingtestingtesting@testingtestingtesting.com")
                        .setParamPhone("152" + (int) (Math.random() * 1000000000))
                        .setParamQq("")
                        .setParamBirthdayYear(1990)
                        .setParamBirthdayDay(1)
                        .setParamBirthdayMonth(1)
                        .setParamVerifycode(vcodeEditText.getText().toString())
                        .request(RegActivity.this);
            }
        });
    }


}
