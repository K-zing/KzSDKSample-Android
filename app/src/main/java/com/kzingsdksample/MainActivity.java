package com.kzingsdksample;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kzingsdk.core.BaseAppGame;
import com.kzingsdk.core.KzingException;
import com.kzingsdk.core.KzingSDK;
import com.kzingsdk.entity.ClientInfo;
import com.kzingsdk.entity.MemberInfo;
import com.kzingsdk.entity.gameplatform.GamePlatform;
import com.kzingsdk.entity.gameplatform.GamePlatformContainer;
import com.kzingsdk.entity.gameplatform.GamePlatformCustom;
import com.kzingsdk.entity.gameplatform.GamePlatformType;
import com.kzingsdk.entity.gameplatform.Playable;
import com.kzingsdk.entity.gameplatform.gameapps.GameAppHelper;
import com.kzingsdk.requests.GetBasicEncryptKeyAPI;
import com.kzingsdk.requests.GetClientInfoAPI;
import com.kzingsdk.requests.GetEncryptKeyAPI;
import com.kzingsdk.requests.GetGameListAPI;
import com.kzingsdk.requests.GetMemberInfoAPI;
import com.kzingsdk.requests.KzingAPI;
import com.kzingsdk.requests.LoginAPI;
import com.kzingsdk.requests.LogoutAPI;
import com.kzingsdk.requests.TransferToGameAPI;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends Activity {

    //TODO
    final String APIKEY = "input your own API key";
    final String MD5KEY = "input your own MD5 key";

    final String TAG = "MainActivity";

    TextView loginStatus;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO Step 1) init with APIKEY
        KzingSDK.getInstance().init(APIKEY);
        //TODO Step 2) setMd5Key
        KzingSDK.getInstance().setMd5Key(MD5KEY);
        final ListView listView = findViewById(R.id.listview);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        username.setText("");
        password.setText("");
        loginStatus = findViewById(R.id.textview);
        loginStatus.setText("Logouted");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, apiListItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (apiListItems[position]) {
                    case GetBasicKey:
                        //TODO Step 3) getBasicKey()
                        getBasicKey();
                        break;
                    case GetDataKey:
                        //TODO Step 4) getDataKey()
                        getDataKey();
                        break;
                    case Login:
                        login();
                        break;
                    case Logout:
                        logout();
                        break;
                    case GetMemberInfo:
                        getMemberInfo();
                        break;
                    case GetActivityList:
                        getActivityList();
                        break;
                    case GetActivityContent:
                        getActivityContent();
                        break;
                    case ApplyActivity:
                        applyActivity();
                        break;
                    case GetHistoryList:
                        getHistoryList();
                        break;
                    case GetMessageList:
                        getMessageList();
                        break;
                    case GetSiteInfo:
                        getSiteInfo();
                        break;
                    case GetGameListSimple:
                        getGameList(false, false);
                        break;
                    case GetGameListWithSubGames:
                        getGameList(false, true);
                        break;
                    case GetGameListWithStatusCheck:
                        getGameList(true, false);
                        break;
                    case GetGameListWithStatusCheckAndSubGames:
                        getGameList(true, true);
                        break;
                    case RegAccount:
                        Intent intent = new Intent(MainActivity.this, RegActivity.class);
                        startActivity(intent);
                        break;
                    case GetRedPocketInfo:
                        getRedPocketInfo();
                        break;
                    case RedeemRedPocket:
                        redeemRedPocket();
                        break;
                    case TransferToGame:
                        transferToGame();
                        break;
                    case TransferBack:
                        transferBack();
                        break;
                    case GetDepositRecord:
                        getDepositRecord();
                        break;
                    case GetWithdrawRecord:
                        getWithdrawRecord();
                        break;
                    case GetTransferRecord:
                        getTransferRecord();
                        break;
                    case DownloadAPPList:
                        getDownloadAppList();
                        break;
                    case GetWithdrawBankList:
                        getWithdrawBankList();
                        break;
                    case addBankCard:
                        addBankCard();
                        break;
                    case submitWithdraw:
                        submitWithdraw();
                        KzingAPI.submitWithdraw()
                                .request(MainActivity.this);
                        break;
                    case GetBounsList:
                        getBounsList();
                        break;
                    case playGame:
                        playGame();
                        break;
                    case playappgame:
                        playGameApp();
                        break;
                    case loadGameFromCache:
                        loadGameFromCache();
                        break;
                }
            }
        });
    }

    private void getBasicKey() {
        KzingAPI.getBasicEncryptKey()
                .addGetBasicEncryptKeyCallBack(new GetBasicEncryptKeyAPI.GetBasicEncryptKeyCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        KzingSDK.getInstance().setBasicRsaKey(response);
                        //TODO Optional Step 3.1) You can cache the key when it success. So you don't need to load it every time start APP.
                        KzingSDK.getInstance().cacheBasicRsaKey(MainActivity.this);
                    }

                    @Override
                    public void onFailure(KzingException kzingException) {

                    }
                })
                .request(MainActivity.this);
    }

    private void getDataKey() {
        KzingAPI.getEncryptKey()
                .addEncryptKeyCallBack(new GetEncryptKeyAPI.GetEncryptKeyCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        KzingSDK.getInstance().setDataRsaKey(response);
                        //TODO Optional Step 4.1) You can cache the key when it success. So you don't need to load it every time start APP.
                        KzingSDK.getInstance().cacheDataRsaKey(MainActivity.this);
                    }

                    @Override
                    public void onFailure(KzingException kzingException) {

                    }
                })
                .request(MainActivity.this);
    }

    private void login() {
        final String user = username.getText().toString();
        KzingAPI.login()
                .setParamLoginName(user)
                .setParamPassword(password.getText().toString())
                .addLoginCallBack(new LoginAPI.LoginCallBack() {
                    @Override
                    public void onSuccess(MemberInfo memberInfo) {
                        loginStatus.setText("Logined - " + user);
                    }

                    @Override
                    public void onFailure(KzingException kzingException) {
                    }
                })
                .request(MainActivity.this);
    }

    private void logout() {
        KzingAPI.logout()
                .addLogoutCallBack(new LogoutAPI.LogoutCallBack() {
                    @Override
                    public void onSuccess() {
                        loginStatus.setText("Logouted");
                    }

                    @Override
                    public void onFailure(KzingException kzingException) {
                    }
                })
                .request(MainActivity.this);
    }

    private void getMemberInfo() {
        KzingAPI.getMemberInfo()
                .addGetMemberInfoCallBack(new GetMemberInfoAPI.GetMemberInfoCallBack() {
                    @Override
                    public void onSuccess(MemberInfo memberInfo) {
                        MainActivity.memberInfo = memberInfo;
                    }

                    @Override
                    public void onFailure(KzingException kzingException) {

                    }
                })
                .request(MainActivity.this);
    }

    private void getActivityList() {
        KzingAPI.getActivityList()
                .request(MainActivity.this);
    }

    private void getSiteInfo() {
        KzingAPI.getSiteInfo()
                .addGetSiteInfoCallBack(new GetClientInfoAPI.GetSiteInfoCallBack() {
                    @Override
                    public void onSuccess(ClientInfo clientInfo) {
                        MainActivity.clientInfo = clientInfo;
                    }

                    @Override
                    public void onFailure(KzingException kzingException) {
                        Log.d(TAG, kzingException.toString());
                    }
                })
                .request(this)
        ;
    }

    private void getMessageList() {
        Calendar start = Calendar.getInstance();
        start.set(2017, 1, 1, 0, 0, 0);
        Calendar end = Calendar.getInstance();
        end.set(2018, 6, 1, 0, 0, 0);
        KzingAPI.getMessageList()
                .setParamStartDateCalendar(start)
                .setParamEndDateCalendar(end)
                .request(MainActivity.this);
    }

    private void getHistoryList() {
        Calendar start = Calendar.getInstance();
        start.set(2018, 1, 1, 0, 0, 0);
        Calendar end = Calendar.getInstance();
        end.set(2018, 6, 1, 0, 0, 0);
        KzingAPI.getHistoryList()
                .setParamStartDateCalendar(start)
                .setParamEndDateCalendar(end)
                .request(MainActivity.this);
    }

    private void getActivityContent() {
        KzingAPI.getActivityContent()
                .setParamActid("")
                .request(MainActivity.this);
    }

    private void applyActivity() {
        KzingAPI.getActivityContent()
                .setParamActid("")
                .request(MainActivity.this);
    }

    private void getGameList(boolean availableOnly, boolean requestSubGame) {
        GetGameListAPI.GetGameListCallBack getGameListCallBack = new GetGameListAPI.GetGameListCallBack() {
            @Override
            public void onSuccess(ArrayList<GamePlatformContainer> gamePlatformContainerList) {
                logGameList(gamePlatformContainerList);
            }

            @Override
            public void onFailure(KzingException kzingException) {

            }
        };
        KzingAPI.getGameList()
                .setRequestSubGame(requestSubGame)
                .addGetGameListCallBack(getGameListCallBack)
                .request(MainActivity.this);
    }

    private void getRedPocketInfo() {
        KzingAPI.getRedPocketInfo()
                .request(MainActivity.this);
    }

    private void redeemRedPocket() {
        KzingAPI.redeemRedPocket()
                .request(MainActivity.this);
    }

    private void transferToGame() {
        KzingAPI.transferToGame()
                .setParamGpAccountId("")
                .request(MainActivity.this);
    }

    private void transferBack() {
        KzingAPI.transferToGame()
                .setParamGpAccountId(TransferToGameAPI.TRANSFER_BACK)
                .request(MainActivity.this);
    }

    private void getDepositRecord() {
        Calendar start = Calendar.getInstance();
        start.set(2018, 1, 1, 0, 0, 0);
        Calendar end = Calendar.getInstance();
        end.set(2018, 6, 1, 0, 0, 0);
        KzingAPI.getDepositRecord()
                .setParamStartDateCalendar(start)
                .setParamEndDateCalendar(end)
                .request(MainActivity.this);
    }

    private void getWithdrawRecord() {
        Calendar start = Calendar.getInstance();
        start.set(2018, 1, 1, 0, 0, 0);
        Calendar end = Calendar.getInstance();
        end.set(2018, 6, 1, 0, 0, 0);
        KzingAPI.getWithdrawRecord()
                .setParamStartDateCalendar(start)
                .setParamEndDateCalendar(end)
                .request(MainActivity.this);
    }

    private void getTransferRecord() {
        Calendar start = Calendar.getInstance();
        start.set(2018, 1, 1, 0, 0, 0);
        Calendar end = Calendar.getInstance();
        end.set(2018, 6, 1, 0, 0, 0);
        KzingAPI.getTransferRecord()
                .setParamStartDateCalendar(start)
                .setParamEndDateCalendar(end)
                .request(MainActivity.this);
    }

    private void getDownloadAppList() {
        KzingAPI.getDownloadAppList()
                .request(MainActivity.this);
    }

    private void getWithdrawBankList() {
        KzingAPI.getWithdrawBankList()
                .request(MainActivity.this);
    }

    private void addBankCard() {
        KzingAPI.addBankCard()
                .setParamBankCode("")
                .setParamAccountRealName("")
                .setParamCardNumber("")
                .setParamAccountBankName("")
                .request(MainActivity.this);
    }

    private void submitWithdraw() {
        KzingAPI.submitWithdraw()
                .request(MainActivity.this);
    }

    private void getBounsList() {
        Calendar start = Calendar.getInstance();
        start.set(2018, 1, 1, 0, 0, 0);
        Calendar end = Calendar.getInstance();
        end.set(2018, 6, 1, 0, 0, 0);
        KzingAPI.getBounsList()
                .setParamStartDateCalendar(start)
                .setParamEndDateCalendar(end)
                .request(MainActivity.this);
    }

    private void logGameList(ArrayList<GamePlatformContainer> gamePlatformContainerList) {
        for (GamePlatformContainer c : gamePlatformContainerList) {
            Log.d(TAG, c.getType() + " : " + c.getGamePlatformList().size());
            for (GamePlatform gp : c.getGamePlatformList()) {
                Log.d(TAG, gp.toString());
            }
            if (c.getType() == GamePlatformType.GAME) {
                for (GamePlatform gp : c.getGamePlatformList()) {
                    if (gp instanceof GamePlatformCustom) {
                        GamePlatformCustom gpC = (GamePlatformCustom) gp;
                        for (Playable gpCc : gpC.getPlayableArrayList()) {
                            Log.d(TAG, "---" + gpCc.toString());
                        }
                    }
                }
            }
        }
    }

    private void playGame() {
        Intent intentPlay = new Intent(MainActivity.this, WebviewActivity.class);
        startActivity(intentPlay);
    }

    private void playGameApp() {
        if (clientInfo == null || memberInfo == null) {
            return;
        }
        BaseAppGame baseAppGame = GameAppHelper.getInstance(null);
        if (baseAppGame != null) {
            baseAppGame.setClientInfo(clientInfo);
            baseAppGame.setMemberInfo(memberInfo);
            baseAppGame
                    .addGameAppCallBack(new BaseAppGame.GameAppCallBack() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(KzingException kzingException) {
                            Log.d(TAG, kzingException.toString());
                        }
                    })
                    .launchApp(this);
        } else {// no APP for the game, go webview instead
            playGame();
        }
    }

    private void loadGameFromCache() {
        ArrayList<GamePlatformContainer> gamePlatformContainerList = KzingSDK.getInstance()
                .loadGamePlatformFromCache(MainActivity.this, true);
        logGameList(gamePlatformContainerList);
    }


    private final String GetBasicKey = "GetBasicKey";
    private final String GetDataKey = "GetDataKey";
    private final String Login = "Login";
    private final String Logout = "Logout";
    private final String GetMemberInfo = "GetMemberInfo";
    private final String GetActivityList = "GetActivityList";
    private final String GetActivityContent = "GetActivityContent";
    private final String ApplyActivity = "ApplyActivity";
    private final String GetHistoryList = "GetHistoryList";
    private final String GetMessageList = "GetMessageList";
    private final String GetSiteInfo = "GetSiteInfo";
    private final String GetGameListSimple = "GetGameListSimple";
    private final String GetGameListWithSubGames = "GetGameListWithSubGames";
    private final String GetGameListWithStatusCheck = "GetGameListWithStatusCheck";
    private final String GetGameListWithStatusCheckAndSubGames = "GetGameListWithStatusCheckAndSubGames";
    private final String RegAccount = "RegAccount";
    private final String TransferToGame = "TransferToGame";
    private final String TransferBack = "TransferBack";
    private final String GetDepositRecord = "GetDepositRecord";
    private final String GetWithdrawRecord = "GetWithdrawRecord";
    private final String GetTransferRecord = "GetTransferRecord";
    private final String DownloadAPPList = "DownloadAPPList";
    private final String GetWithdrawBankList = "GetWithdrawBankList";
    private final String addBankCard = "AddBankCard";
    private final String submitWithdraw = "SubmitWithdraw";
    private final String GetBounsList = "GetBounsList";
    private final String GetRedPocketInfo = "GetRedPocketInfo";
    private final String RedeemRedPocket = "RedeemRedPocket";
    private final String playGame = "PlayGame";
    private final String playappgame = "Playappgame";
    private final String loadGameFromCache = "LoadGameFromCache";

    private static ClientInfo clientInfo = null;
    private static MemberInfo memberInfo = null;

    final String[] apiListItems = new String[]{
            GetBasicKey,
            GetDataKey,
            Login,
            Logout,
            GetSiteInfo,
            GetMemberInfo,
            GetActivityList,
            GetActivityContent,
            ApplyActivity,
            GetHistoryList,
            GetMessageList,
            GetBounsList,
            GetGameListSimple,
            GetGameListWithSubGames,
            GetGameListWithStatusCheck,
            GetGameListWithStatusCheckAndSubGames,
            RegAccount,
            GetRedPocketInfo,
            RedeemRedPocket,
            TransferToGame,
            TransferBack,
            GetDepositRecord,
            GetWithdrawRecord,
            GetTransferRecord,
            DownloadAPPList,
            GetWithdrawBankList,
            addBankCard,
            submitWithdraw,
            playGame,
            playappgame,
            loadGameFromCache,
    };
}
