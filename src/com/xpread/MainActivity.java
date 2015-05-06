package com.xpread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.uc.base.wa.WaEntry;
import com.xpread.control.Controller;
import com.xpread.control.Controller.NetworkStateChangeListener;
import com.xpread.control.Controller.ProgressChangeListener;
import com.xpread.control.WifiAdmin;
import com.xpread.control.WifiApAdmin;
import com.xpread.file.FilePickActivity;
import com.xpread.provider.UserInfo;
import com.xpread.util.BitmapUtil;
import com.xpread.util.Const;
import com.xpread.util.LaboratoryData;
import com.xpread.util.LogUtil;
import com.xpread.util.ScreenUtil;
import com.xpread.util.Utils;
import com.xpread.wa.WaKeys;
import com.xpread.widget.CircleAnimation;
import com.xpread.widget.CircleTrackView;
import com.xpread.widget.RoundImageButton;
import com.xpread.widget.RoundImageView;
import com.xpread.widget.RoundProgressBar;

public class MainActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = "MainActivity";

    private Controller mController;

    private RoundImageButton mSendButton;

    private RoundImageButton mReceiveButton;

    private RoundImageButton mDisconnectButton;

    private RelativeLayout mUserInfoLayout;

    private RelativeLayout mConnectedUserInfoLayout;

    RoundImageView mFaceImage;

    TextView mUserName;

    ImageView mUserHistory;

    PopupWindow mPopupWindow;

    List<Drawable> mPhotos;

    int iconId;

    DrawerLayout mDrawerLayout;

    ImageView mMenu;

    RelativeLayout mDrawerContent;

    ListView mDrawerListView;

    TextView mDrawerBottomText;

    ImageView mShare;

    RoundImageView mOwerIcon;

    RoundImageView mGuestIcon;

    TextView mGuestName;

    CircleAnimation mAnimationSend = null;

    Animation mScaleAnimation;

    RelativeLayout mContentLayout;

    private RoundProgressBar mOwnerRoundProgressBar;

    private RoundProgressBar mGuestRoundProgressBar;

    private static final float COLUM_NUMBER = 4.5f;

    private CircleTrackView mOwnerTrackView1, mOwnerTrackView2, mOwnerTrackView3, mOwnerTrackView4,
            mOwnerTrackView5, mGuestTrackView1, mGuestTrackView2, mGuestTrackView3,
            mGuestTrackView4, mGuestTrackView5;

    private CircleTrackView[] mOwnerTrackViewArray = {mOwnerTrackView1, mOwnerTrackView2,
            mOwnerTrackView3, mOwnerTrackView4, mOwnerTrackView5};

    private CircleTrackView[] mGuestTrackViewArray = {mGuestTrackView1, mGuestTrackView2,
            mGuestTrackView3, mGuestTrackView4, mGuestTrackView5};

    private Animation mOwnerAnimation1, mOwnerAnimation2, mOwnerAnimation3, mOwnerAnimation4,
            mOwnerAnimation5, mGuestAnimation1, mGuestAnimation2, mGuestAnimation3,
            mGuestAnimation4, mGuestAnimation5;

    private Animation[] mOwnerAnimationArray = {mOwnerAnimation1, mOwnerAnimation2,
            mOwnerAnimation3, mOwnerAnimation4, mOwnerAnimation5};

    private Animation[] mGuestAnimationArray = {mGuestAnimation1, mGuestAnimation2,
            mGuestAnimation3, mGuestAnimation4, mGuestAnimation5};

    private ImageView mWaitConnectCircleView1, mWaitConnectCircleView2, mWaitConnectCircleView3,
            mWaitConnectCircleView4, mWaitConnectCircleView5;

    private Bitmap mWaitConnectCircleBmp1, mWaitConnectCircleBmp2, mWaitConnectCircleBmp3,
            mWaitConnectCircleBmp4, mWaitConnectCircleBmp5;

    private Bitmap[] mWaitConnectCircleBmpArray = {mWaitConnectCircleBmp1, mWaitConnectCircleBmp2,
            mWaitConnectCircleBmp3, mWaitConnectCircleBmp4, mWaitConnectCircleBmp5};

    class WaitConnectIndex {

        int index;

        boolean flag;
    }

    private WaitConnectIndex mWaitConnectIndex1, mWaitConnectIndex2, mWaitConnectIndex3,
            mWaitConnectIndex4, mWaitConnectIndex5;

    private WaitConnectIndex[] mWaitConnectIndexArray = {mWaitConnectIndex1, mWaitConnectIndex2,
            mWaitConnectIndex3, mWaitConnectIndex4, mWaitConnectIndex5};

    static AnimationRunnable mAr1, mAr2, mAr3, mAr4, mAr5;

    static AnimationRunnable[] mAnimationRunnables = {mAr1, mAr2, mAr3, mAr4, mAr5};

    private Animation mProgressBarScaleAnimation;

    private static final int PROGRESS_MAX = 100;

    public static final int WIFI_AP_STATE_DISABLING = 10;

    public static final int WIFI_AP_STATE_DISABLED = 11;

    public static final int WIFI_AP_STATE_ENABLING = 12;

    public static final int WIFI_AP_STATE_ENABLED = 13;

    public static final int WIFI_AP_STATE_FAILED = 14;

    private static final String GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending";

    private static final String GOOGLE_PLAY_ACTIVITY_NAME =
            "com.android.vending.AssetBrowserActivity";

    private boolean mIsNameValid = false;

    private boolean mIsWaitConnected = false;

    private final String INTENT_TYPE = "type";

    private Timer mWaitConnectTimer = new Timer();

    private TimerTask mWaitConnectTask;

    private boolean mIsOwnerTrackAnimationStart = false;

    private boolean mIsDisconnected = false;

    private boolean mIsSenderFirstSetProgress = true;

    private boolean mIsReceiverFirstSetProgress = true;

    NetworkStateChangeListener mNetworkStateChangeListener = new NetworkStateChangeListener() {

        @Override
        public void stateChangeListener(int state) {
            if (state == Const.REFRESH_USER_INFO) {
                UserInfo targetUserInfo = mController.getTargetInfo();
                if (targetUserInfo != null) {
                    mGuestIcon.setImageDrawable(mPhotos.get(targetUserInfo.getPictureID()));
                    mGuestName.setText(targetUserInfo.getUserName());
                }
                return;
            }
            if (state == Const.REFRESH_DISCONNECTION) {

                if (LogUtil.isLog) {
                    Log.e(TAG, "--------->recieve the disconnect message");
                }
                resetAllTrackView();

                resetDefaultState();

            } else if (state == Const.REFRESH_ESTIBALE) {

                if (LogUtil.isLog) {
                    Log.e(TAG, "receive the establist message, begin to refresh UI");
                }
                mHandler.removeMessages(CONNECT_TIMEOUT);
                mIsWaitConnected = false;

                if (mWaitConnectTask != null) {
                    mWaitConnectTask.cancel();
                }

                resetAllTrackView();
                resetProgressBarState();

                // second step, set all the button enable
                // and set the wait connected circle view gone
                changeButtonState(true);
                changeWaitCircleState(false);

                // third step, refresh the UI
                mReceiveButton.setVisibility(View.GONE);
                mDisconnectButton.setVisibility(View.VISIBLE);
                mUserInfoLayout.setVisibility(View.GONE);
                mConnectedUserInfoLayout.setVisibility(View.VISIBLE);
                mOwerIcon.setImageDrawable(mPhotos.get(Utils.getOwerIcon(MainActivity.this)));
                UserInfo targetUserInfo = mController.getTargetInfo();
                if (targetUserInfo != null) {
                    mGuestIcon.setImageDrawable(mPhotos.get(targetUserInfo.getPictureID()));
                    mGuestName.setText(targetUserInfo.getUserName());
                }

                // fourth step, start the animation
                int role = mController.getRole();
                if (role != -1) {
                    if (role == Const.SENDER) {
                        mGuestRoundProgressBar.setVisibility(View.VISIBLE);
                        mOwnerRoundProgressBar.setVisibility(View.GONE);

                        // start the owner track animation if not
                        boolean isOwner = true;
                        startTrackAnimation(isOwner);
                    } else if (role == Const.RECEIVER) {
                        mOwnerRoundProgressBar.setVisibility(View.VISIBLE);
                        mGuestRoundProgressBar.setVisibility(View.GONE);

                        // start the guest track animation if not start
                        boolean isOwner = false;
                        startTrackAnimation(isOwner);
                    }

                } else {
                    if (LogUtil.isLog) {
                        Log.e(TAG, "unkown role, hide the progress bar and the track");
                    }
                    resetProgressBarState();
                    resetAllTrackView();
                }

                WaEntry.statEpv(WaKeys.CATEGORY_XPREAD, WaKeys.KEY_XPREAD_CONNECT_SUCESS);
            }
        }
    };

    ProgressChangeListener mProgressChangeListener = new ProgressChangeListener() {

        @Override
        public void setProgress(int progress, int role) {

            switch (role) {
                case Const.RECEIVER: {

                    if (mIsReceiverFirstSetProgress && !mIsDisconnected) {
                        mIsReceiverFirstSetProgress = false;

                        if (mOwnerRoundProgressBar != null) {
                            if (LogUtil.isLog) {
                                Log.e(TAG, "receiver : update progress");
                            }

                            if (mOwnerRoundProgressBar.getVisibility() == View.GONE) {
                                mOwnerRoundProgressBar.setVisibility(View.VISIBLE);
                            }

                            // set the guest send circle track visiable if not
                            boolean isOwner = false;
                            startTrackAnimation(isOwner);

                        } else {
                            if (LogUtil.isLog) {
                                Log.e(TAG, "null point----->owner RoundProgressBar is null");
                            }
                        }
                    }

                    if (LogUtil.isLog) {
                        Log.e(TAG, "receiver: progress is " + progress);
                    }

                    if (progress < 100) {
                        mOwnerRoundProgressBar.setProgress(progress);
                    } else {
                        if (LogUtil.isLog) {
                            Log.e(TAG, "receiver : progress is 100");
                        }
                        progress = 100;
                        mOwnerRoundProgressBar.setProgress(progress);

                        if (LogUtil.isLog) {
                            Log.e(TAG, "start the progress bar scale animation");
                        }

                        if (!mIsDisconnected) {
                            if (LogUtil.isLog) {
                                Log.e(TAG, "progress listener ------------> not disconnect");
                            }
                            boolean isOwner = false;
                            stopTrackAnimation(isOwner);

                            mOwnerRoundProgressBar.startAnimation(mProgressBarScaleAnimation);
                        } else {
                            if (LogUtil.isLog) {
                                Log.e(TAG, "progress listener ------------> disconnect");
                            }

                        }
                        mIsDisconnected = false;
                        mIsReceiverFirstSetProgress = true;
                    }

                    break;
                }

                case Const.SENDER: {

                    if (mIsSenderFirstSetProgress && !mIsDisconnected) {
                        mIsSenderFirstSetProgress = false;

                        if (mGuestRoundProgressBar != null) {
                            if (LogUtil.isLog) {
                                Log.e(TAG, "send : update progress");
                            }

                            if (mGuestRoundProgressBar.getVisibility() == View.GONE) {
                                mGuestRoundProgressBar.setVisibility(View.VISIBLE);
                            }

                            // set the owner send cirlce track visiable if not
                            boolean isOwner = true;
                            startTrackAnimation(isOwner);

                        } else {
                            if (LogUtil.isLog) {
                                Log.e(TAG, "null point----->guest RoundProgressBar is null");
                            }
                        }
                    }

                    if (LogUtil.isLog) {
                        Log.e(TAG, "progress is " + progress);
                    }

                    if (progress < 100) {
                        mGuestRoundProgressBar.setProgress(progress);
                    } else {
                        progress = 100;
                        mGuestRoundProgressBar.setProgress(progress);

                        if (!mIsDisconnected) {
                            if (LogUtil.isLog) {
                                Log.e(TAG, "progress listener ------------> not disconnect");
                            }
                            boolean isOwner = true;
                            stopTrackAnimation(isOwner);
                            mGuestRoundProgressBar.startAnimation(mProgressBarScaleAnimation);
                        } else {
                            if (LogUtil.isLog) {
                                Log.e(TAG, "progress listener ------------> disconnect");
                            }
                        }
                        mIsDisconnected = false;
                        mIsSenderFirstSetProgress = true;
                    }

                }
                default:
                    break;
            }
        }
    };

    private static final int CONNECT_TIMEOUT = 0x0100;

    private final int WAIT_CONNECTTED = 0x0010;

    private final int SEND_FILE = 0x0110;

    private final int WAIT_CONNECTTED_ANIMATION = 0x0101;

    private static final int TRACK_TIME_GAP = 50;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CONNECT_TIMEOUT:
                    Toast.makeText(MainActivity.this, R.string.exception_connect_fail,
                            Toast.LENGTH_SHORT).show();
                    mController.resumeToDefault();
                    mIsWaitConnected = false;

                    changeButtonState(true);

                    mReceiveButton.setVisibility(View.VISIBLE);
                    mDisconnectButton.setVisibility(View.GONE);
                    mUserInfoLayout.setVisibility(View.VISIBLE);
                    mConnectedUserInfoLayout.setVisibility(View.GONE);

                    if (mWaitConnectTask != null) {
                        mWaitConnectTask.cancel();
                    }

                    WaEntry.statEpv(WaKeys.CATEGORY_XPREAD, WaKeys.KEY_XPREAD_CONNECT_FAILURE);

                    break;

                case WAIT_CONNECTTED_ANIMATION:
                    for (int i = 0; i < mWaitConnectIndexArray.length; ++i) {

                        if ((mWaitConnectIndexArray[i].index + 1) >= 5) {
                            mWaitConnectIndexArray[i].flag = false;
                            mWaitConnectIndexArray[i].index = 3;

                            if (i == 0) {
                                mWaitConnectIndexArray[1].flag = true;
                                mWaitConnectIndexArray[2].flag = true;
                                mWaitConnectIndexArray[3].flag = true;
                                mWaitConnectIndexArray[4].flag = true;
                            }

                        } else if ((mWaitConnectIndexArray[i].index - 1) <= -1) {
                            mWaitConnectIndexArray[i].flag = true;
                            mWaitConnectIndexArray[i].index = 1;

                            if (i == 0) {
                                mWaitConnectIndexArray[1].flag = true;
                                mWaitConnectIndexArray[2].flag = true;
                                mWaitConnectIndexArray[3].flag = true;
                                mWaitConnectIndexArray[4].flag = true;
                            }

                        } else {
                            if (mWaitConnectIndexArray[i].flag == true) {
                                mWaitConnectIndexArray[i].index++;
                            } else {
                                mWaitConnectIndexArray[i].index--;
                            }
                        }
                    }

                    mWaitConnectCircleView1
                            .setImageBitmap(mWaitConnectCircleBmpArray[mWaitConnectIndexArray[4].index]);
                    mWaitConnectCircleView2
                            .setImageBitmap(mWaitConnectCircleBmpArray[mWaitConnectIndexArray[3].index]);
                    mWaitConnectCircleView3
                            .setImageBitmap(mWaitConnectCircleBmpArray[mWaitConnectIndexArray[2].index]);
                    mWaitConnectCircleView4
                            .setImageBitmap(mWaitConnectCircleBmpArray[mWaitConnectIndexArray[1].index]);
                    mWaitConnectCircleView5
                            .setImageBitmap(mWaitConnectCircleBmpArray[mWaitConnectIndexArray[0].index]);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        // TODO
        // 机型和Android版本号--------------------------------------------------------------
        // 实验室数据
        LaboratoryData.gAppBeginCPUTime = LaboratoryData.getAppCpuTime();
        LaboratoryData.gTotalAPPBeginCPUTime = LaboratoryData.getTotalCpuTime();
        WaEntry.handleMsg(WaEntry.MSG_UPLOAD_FILE);
        // -------------------------------------------------------------------------------------
        this.mController = Controller.getInstance(getApplicationContext());
        iconId = Utils.getOwerIcon(getApplicationContext());

        initIcons();

        initView();

        initAnimation();

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    private void initView() {
        mSendButton = (RoundImageButton) findViewById(R.id.send_button);
        mSendButton.setOnClickListener(this);

        mReceiveButton = (RoundImageButton) findViewById(R.id.receive_button);
        mReceiveButton.setOnClickListener(this);

        mDisconnectButton = (RoundImageButton) findViewById(R.id.disconnect_button);
        mDisconnectButton.setOnClickListener(this);

        mFaceImage = (RoundImageView) findViewById(R.id.user_icon);
        mFaceImage.setImageDrawable(mPhotos.get(Utils.getOwerIcon(this)));
        mFaceImage.setOnClickListener(this);

        mUserName = (TextView) findViewById(R.id.user_name);
        mUserName.setText(Utils.getOwnerName(this));
        mUserName.setOnClickListener(this);

        mUserHistory = (ImageView) findViewById(R.id.user_history);
        mUserHistory.setOnClickListener(this);

        mUserInfoLayout = (RelativeLayout) findViewById(R.id.user_info);
        mConnectedUserInfoLayout = (RelativeLayout) findViewById(R.id.user_info_connected);
        mOwerIcon = (RoundImageView) findViewById(R.id.owner_icon);
        mGuestIcon = (RoundImageView) findViewById(R.id.guest_icon);
        mGuestName = (TextView) findViewById(R.id.guest_name);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerStateChanged(int arg0) {

            }

            @Override
            public void onDrawerSlide(View arg0, float arg1) {

            }

            @Override
            public void onDrawerOpened(View arg0) {
                mMenu.setImageResource(R.drawable.back);

                String content = null;
                float size = mController.getTotalTransferDataSize() / Const.KILO;
                if (size > Const.KILO) {
                    size /= Const.KILO;

                    if (size > Const.KILO) {
                        size /= Const.KILO;
                        content =
                                String.format(
                                        getResources().getString(R.string.drawer_bottom_text_gb),
                                        size);
                    } else {
                        content =
                                String.format(
                                        getResources().getString(R.string.drawer_bottom_text_mb),
                                        size);
                    }
                } else {
                    content =
                            String.format(getResources().getString(R.string.drawer_bottom_text_kb),
                                    size);
                }

                SpannableString ss = new SpannableString(content);
                int start = content.indexOf(" ") + 1;
                int end = content.lastIndexOf(" ");
                ss.setSpan(
                        new ForegroundColorSpan(getResources().getColor(
                                R.color.drawer_bottom_text_color)), start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mDrawerBottomText.setText(ss);
            }

            @Override
            public void onDrawerClosed(View arg0) {
                mMenu.setImageResource(R.drawable.menu);
            }
        });

        mMenu = (ImageView) findViewById(R.id.menu);
        mMenu.setOnClickListener(this);
        mDrawerContent = (RelativeLayout) findViewById(R.id.drawer_content);

        mDrawerListView = (ListView) findViewById(R.id.drawer_listvew);
        String[] from = {"icon", "text"};
        int[] to = {R.id.drawer_opra_icon, R.id.drawer_opra_text};
        ArrayList<HashMap<String, Object>> list = initDrawerListData();

        SimpleAdapter adapter =
                new SimpleAdapter(this, list, R.layout.drawer_listview_item, from, to);
        mDrawerListView.setAdapter(adapter);
        mDrawerListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        WaEntry.statEpv(WaKeys.CATEGORY_XPREAD, WaKeys.KEY_XPREAD_MENU_UPDATE);

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (isGooglePlayInstall()) {
                            intent.setClassName(GOOGLE_PLAY_PACKAGE_NAME, GOOGLE_PLAY_ACTIVITY_NAME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        intent.setData(Uri
                                .parse("https://play.google.com/store/apps/details?id=com.xpread"));
                        startActivity(intent);
                        WaEntry.statEpv(WaKeys.CATEGORY_XPREAD, WaKeys.KEY_XPREAD_MENU_UPDATE);
                        break;

                    case 1:
                        WaEntry.statEpv(WaKeys.CATEGORY_XPREAD, WaKeys.KEY_XPREAD_MENU_ABOUT);
                        mDrawerLayout.closeDrawer(mDrawerContent);
                        Intent intentAboutUs = new Intent();
                        intentAboutUs.setClass(MainActivity.this, AboutUsActivity.class);
                        startActivity(intentAboutUs);
                        break;
                    default:
                        break;
                }
            }
        });

        mDrawerBottomText = (TextView) findViewById(R.id.drawer_bottom_text);

        mShare = (ImageView) findViewById(R.id.share);
        mShare.setOnClickListener(this);

        mContentLayout = (RelativeLayout) findViewById(R.id.content);

        mOwnerRoundProgressBar = (RoundProgressBar) findViewById(R.id.owner_progress);
        mGuestRoundProgressBar = (RoundProgressBar) findViewById(R.id.guest_progress);

        mOwnerTrackViewArray[0] = (CircleTrackView) findViewById(R.id.owner_circle_track_1);
        mOwnerTrackViewArray[1] = (CircleTrackView) findViewById(R.id.owner_circle_track_2);
        mOwnerTrackViewArray[2] = (CircleTrackView) findViewById(R.id.owner_circle_track_3);
        mOwnerTrackViewArray[3] = (CircleTrackView) findViewById(R.id.owner_circle_track_4);
        mOwnerTrackViewArray[4] = (CircleTrackView) findViewById(R.id.owner_circle_track_5);

        mGuestTrackViewArray[0] = (CircleTrackView) findViewById(R.id.guest_circle_track_1);
        mGuestTrackViewArray[1] = (CircleTrackView) findViewById(R.id.guest_circle_track_2);
        mGuestTrackViewArray[2] = (CircleTrackView) findViewById(R.id.guest_circle_track_3);
        mGuestTrackViewArray[3] = (CircleTrackView) findViewById(R.id.guest_circle_track_4);
        mGuestTrackViewArray[4] = (CircleTrackView) findViewById(R.id.guest_circle_track_5);

        mWaitConnectCircleView1 = (ImageView) findViewById(R.id.wait_connect_circle_view1);
        mWaitConnectCircleView2 = (ImageView) findViewById(R.id.wait_connect_circle_view2);
        mWaitConnectCircleView3 = (ImageView) findViewById(R.id.wait_connect_circle_view3);
        mWaitConnectCircleView4 = (ImageView) findViewById(R.id.wait_connect_circle_view4);
        mWaitConnectCircleView5 = (ImageView) findViewById(R.id.wait_connect_circle_view5);

        Bitmap bitmapSrc = BitmapFactory.decodeResource(getResources(), R.drawable.circle_blue);
        mWaitConnectCircleBmpArray[0] = BitmapUtil.toRoundBitmap(setAlpha(bitmapSrc, 10));
        mWaitConnectCircleBmpArray[1] = BitmapUtil.toRoundBitmap(setAlpha(bitmapSrc, 30));
        mWaitConnectCircleBmpArray[2] = BitmapUtil.toRoundBitmap(setAlpha(bitmapSrc, 50));
        mWaitConnectCircleBmpArray[3] = BitmapUtil.toRoundBitmap(setAlpha(bitmapSrc, 70));
        mWaitConnectCircleBmpArray[4] = BitmapUtil.toRoundBitmap(setAlpha(bitmapSrc, 90));

        mWaitConnectIndexArray[0] = new WaitConnectIndex();
        mWaitConnectIndexArray[0].index = 0;
        mWaitConnectIndexArray[0].flag = true;

        mWaitConnectIndexArray[1] = new WaitConnectIndex();
        mWaitConnectIndexArray[1].index = 1;
        mWaitConnectIndexArray[1].flag = true;

        mWaitConnectIndexArray[2] = new WaitConnectIndex();
        mWaitConnectIndexArray[2].index = 2;
        mWaitConnectIndexArray[2].flag = true;

        mWaitConnectIndexArray[3] = new WaitConnectIndex();
        mWaitConnectIndexArray[3].index = 3;
        mWaitConnectIndexArray[3].flag = true;

        mWaitConnectIndexArray[4] = new WaitConnectIndex();
        mWaitConnectIndexArray[4].index = 4;
        mWaitConnectIndexArray[4].flag = true;
    }

    private void initAnimation() {
        mScaleAnimation =
                new ScaleAnimation(0.1f, 15.0f, 0.1f, 15.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setDuration(500);
        mScaleAnimation.setRepeatCount(0);
        mScaleAnimation.setFillAfter(false);
        mScaleAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimationSend.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });

        // owner animation array
        mOwnerAnimationArray[0] =
                new TranslateAnimation(0f, 0f, 0f, getResources().getDimension(
                        R.dimen.track_distance));
        mOwnerAnimationArray[0].setDuration(750);
        mOwnerAnimationArray[0].setFillAfter(false);
        mOwnerAnimationArray[0].setInterpolator(new LinearInterpolator());
        mOwnerAnimationArray[0].setRepeatCount(0);
        mOwnerAnimationArray[0].setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (LogUtil.isLog) {
                    Log.e(TAG, "animation listener end");
                }

                for (int i = 0; i < mOwnerTrackViewArray.length; ++i) {
                    mAnimationRunnables[i] = new AnimationRunnable(Const.SENDER, i);
                    mOwnerAnimationArray[i].setStartOffset(0);
                    mOwnerTrackViewArray[i].postDelayed(mAnimationRunnables[i], i * TRACK_TIME_GAP);
                }
            }
        });

        mOwnerAnimationArray[1] =
                new TranslateAnimation(0f, 0f, 0f, getResources().getDimension(
                        R.dimen.track_distance));
        mOwnerAnimationArray[1].setDuration(750);
        mOwnerAnimationArray[1].setFillAfter(false);
        mOwnerAnimationArray[1].setStartOffset(TRACK_TIME_GAP);
        mOwnerAnimationArray[1].setInterpolator(new LinearInterpolator());
        mOwnerAnimationArray[1].setRepeatCount(0);

        mOwnerAnimationArray[2] =
                new TranslateAnimation(0f, 0f, 0f, getResources().getDimension(
                        R.dimen.track_distance));
        mOwnerAnimationArray[2].setDuration(750);
        mOwnerAnimationArray[2].setFillAfter(false);
        mOwnerAnimationArray[2].setStartOffset(TRACK_TIME_GAP * 2);
        mOwnerAnimationArray[2].setInterpolator(new LinearInterpolator());
        mOwnerAnimationArray[2].setRepeatCount(0);

        mOwnerAnimationArray[3] =
                new TranslateAnimation(0f, 0f, 0f, getResources().getDimension(
                        R.dimen.track_distance));
        mOwnerAnimationArray[3].setDuration(750);
        mOwnerAnimationArray[3].setFillAfter(false);
        mOwnerAnimationArray[3].setStartOffset(TRACK_TIME_GAP * 3);
        mOwnerAnimationArray[3].setInterpolator(new LinearInterpolator());
        mOwnerAnimationArray[3].setRepeatCount(0);

        mOwnerAnimationArray[4] =
                new TranslateAnimation(0f, 0f, 0f, getResources().getDimension(
                        R.dimen.track_distance));
        mOwnerAnimationArray[4].setDuration(750);
        mOwnerAnimationArray[4].setFillAfter(false);
        mOwnerAnimationArray[4].setStartOffset(TRACK_TIME_GAP * 4);
        mOwnerAnimationArray[4].setInterpolator(new LinearInterpolator());
        mOwnerAnimationArray[4].setRepeatCount(0);

        // guest animation array
        mGuestAnimationArray[0] =
                new TranslateAnimation(0f, 0f, 0f, -getResources().getDimension(
                        R.dimen.track_distance));
        mGuestAnimationArray[0].setDuration(750);
        mGuestAnimationArray[0].setFillAfter(false);
        mGuestAnimationArray[0].setInterpolator(new LinearInterpolator());
        mGuestAnimationArray[0].setRepeatCount(0);
        mGuestAnimationArray[0].setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                for (int i = 0; i < mGuestTrackViewArray.length; ++i) {
                    mAnimationRunnables[i] = new AnimationRunnable(Const.RECEIVER, i);
                    mGuestAnimationArray[i].setStartOffset(0);
                    mGuestTrackViewArray[i].postDelayed(mAnimationRunnables[i], i * TRACK_TIME_GAP);
                }
            }
        });

        mGuestAnimationArray[1] =
                new TranslateAnimation(0f, 0f, 0f, -getResources().getDimension(
                        R.dimen.track_distance));
        mGuestAnimationArray[1].setDuration(750);
        mGuestAnimationArray[1].setFillAfter(false);
        mGuestAnimationArray[1].setStartOffset(TRACK_TIME_GAP);
        mGuestAnimationArray[1].setInterpolator(new LinearInterpolator());
        mGuestAnimationArray[1].setRepeatCount(0);

        mGuestAnimationArray[2] =
                new TranslateAnimation(0f, 0f, 0f, -getResources().getDimension(
                        R.dimen.track_distance));
        mGuestAnimationArray[2].setDuration(750);
        mGuestAnimationArray[2].setFillAfter(false);
        mGuestAnimationArray[2].setStartOffset(TRACK_TIME_GAP * 2);
        mGuestAnimationArray[2].setInterpolator(new LinearInterpolator());
        mGuestAnimationArray[2].setRepeatCount(0);

        mGuestAnimationArray[3] =
                new TranslateAnimation(0f, 0f, 0f, -getResources().getDimension(
                        R.dimen.track_distance));
        mGuestAnimationArray[3].setDuration(750);
        mGuestAnimationArray[3].setFillAfter(false);
        mGuestAnimationArray[3].setStartOffset(TRACK_TIME_GAP * 3);
        mGuestAnimationArray[3].setInterpolator(new LinearInterpolator());
        mGuestAnimationArray[3].setRepeatCount(0);

        mGuestAnimationArray[4] =
                new TranslateAnimation(0f, 0f, 0f, -getResources().getDimension(
                        R.dimen.track_distance));
        mGuestAnimationArray[4].setDuration(750);
        mGuestAnimationArray[4].setFillAfter(false);
        mGuestAnimationArray[4].setStartOffset(TRACK_TIME_GAP * 4);
        mGuestAnimationArray[4].setInterpolator(new LinearInterpolator());
        mGuestAnimationArray[4].setRepeatCount(0);

        this.mProgressBarScaleAnimation =
                new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
        this.mProgressBarScaleAnimation.setFillAfter(false);
        this.mProgressBarScaleAnimation.setDuration(300);
        this.mProgressBarScaleAnimation.setRepeatCount(0);
        this.mProgressBarScaleAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                /*
                 * if (mOwnerRoundProgressBar.getVisibility() == View.VISIBLE &&
                 * mOwnerRoundProgressBar.getProgress() == PROGRESS_MAX) { boolean isOwner = false;
                 * resetTrackView(isOwner); } if (mGuestRoundProgressBar.getVisibility() ==
                 * View.VISIBLE && mGuestRoundProgressBar.getProgress() == PROGRESS_MAX) { boolean
                 * isOwner = true; resetTrackView(isOwner); }
                 */

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mOwnerRoundProgressBar.getVisibility() == View.VISIBLE
                        && mOwnerRoundProgressBar.getProgress() == PROGRESS_MAX) {

                    if (LogUtil.isLog) {
                        Log.e(TAG, "owner progress bar is max");
                    }

                    mOwnerRoundProgressBar.setProgress(0);
                    mOwnerRoundProgressBar.setVisibility(View.GONE);

                }

                if (mGuestRoundProgressBar.getVisibility() == View.VISIBLE
                        && mGuestRoundProgressBar.getProgress() == PROGRESS_MAX) {

                    if (LogUtil.isLog) {
                        Log.e(TAG, "guest progress bar is max");
                    }

                    mGuestRoundProgressBar.setProgress(0);
                    mGuestRoundProgressBar.setVisibility(View.GONE);

                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mController.isConnected() || this.mIsWaitConnected) {
            if (LogUtil.isLog) {
                Log.e(TAG, "MainActivity ----> isConnect, refresh the view");
            }

            mReceiveButton.setVisibility(View.GONE);
            mDisconnectButton.setVisibility(View.VISIBLE);
            mUserInfoLayout.setVisibility(View.GONE);
            mConnectedUserInfoLayout.setVisibility(View.VISIBLE);

            if (this.mIsWaitConnected) {
                resetProgressBarState();
            }

            mOwerIcon.setImageDrawable(mPhotos.get(Utils.getOwerIcon(this)));
            UserInfo targetUserInfo = mController.getTargetInfo();
            if (targetUserInfo != null) {
                mGuestIcon.setImageDrawable(mPhotos.get(targetUserInfo.getPictureID()));
                mGuestName.setText(targetUserInfo.getUserName());
            }

        } else {
            if (LogUtil.isLog) {
                Log.e(TAG, "MainActivity ----> not Connect");
            }
            mReceiveButton.setVisibility(View.VISIBLE);
            mDisconnectButton.setVisibility(View.GONE);
            mUserInfoLayout.setVisibility(View.VISIBLE);
            mConnectedUserInfoLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus == true) {

            if (!mController.isConnected() && mIsWaitConnected) {
                // notice , now the role is sender

                // first step, set all the visible button disable
                changeButtonState(false);

                // second step, set the wait connected circle view visible
                changeWaitCircleState(true);

                mWaitConnectCircleView1
                        .setImageBitmap(mWaitConnectCircleBmpArray[mWaitConnectIndexArray[4].index]);
                mWaitConnectCircleView2
                        .setImageBitmap(mWaitConnectCircleBmpArray[mWaitConnectIndexArray[3].index]);
                mWaitConnectCircleView3
                        .setImageBitmap(mWaitConnectCircleBmpArray[mWaitConnectIndexArray[2].index]);
                mWaitConnectCircleView4
                        .setImageBitmap(mWaitConnectCircleBmpArray[mWaitConnectIndexArray[1].index]);
                mWaitConnectCircleView5
                        .setImageBitmap(mWaitConnectCircleBmpArray[mWaitConnectIndexArray[0].index]);
                // third step, start the animation
                if (this.mWaitConnectTimer == null) {
                    mWaitConnectTimer = new Timer();
                }

                if (mWaitConnectTask != null) {
                    mWaitConnectTask.cancel();
                }

                mWaitConnectTask = new WaitConnectTask();

                mWaitConnectTimer.schedule(mWaitConnectTask, 500, 500);

            } else {
                changeWaitCircleState(false);
            }

            // start the owner track animation if connect
            if (mController.isConnected()) {
                // first step, remove the timeout message and the timertask
                mHandler.removeMessages(CONNECT_TIMEOUT);
                mIsWaitConnected = false;

                if (mWaitConnectTask != null) {
                    mWaitConnectTask.cancel();
                }

                // second step, set all the button enable
                // and set the wait connected circle view gone
                changeButtonState(true);

                int currentSendProgress = mController.getCurrentSendProgress();
                int currentReceiveProgress = mController.getCurrentReceiverProgress();

                if (currentSendProgress != -1) {
                    mGuestRoundProgressBar.setVisibility(View.VISIBLE);
                    mGuestRoundProgressBar.setProgress(currentSendProgress);
                    mController.setCurrentSendProgress(currentSendProgress);
                } else {
                    mGuestRoundProgressBar.setProgress(0);
                    mGuestRoundProgressBar.setVisibility(View.GONE);

                    boolean isOwner = true;
                    stopTrackAnimation(isOwner);
                }

                if (currentReceiveProgress != -1) {
                    mOwnerRoundProgressBar.setVisibility(View.VISIBLE);
                    mOwnerRoundProgressBar.setProgress(currentReceiveProgress);
                    mController.setCurrentReceiveProgress(currentReceiveProgress);
                } else {
                    mOwnerRoundProgressBar.setProgress(0);
                    mOwnerRoundProgressBar.setVisibility(View.GONE);

                    boolean isOwner = false;
                    stopTrackAnimation(isOwner);
                }

                // 这里只有从搜索好友界面跳转过来会进去，并且是在已经连接的情况下
                if (mIsOwnerTrackAnimationStart) {
                    mIsOwnerTrackAnimationStart = false;
                    if (mGuestRoundProgressBar.getVisibility() == View.GONE) {
                        mGuestRoundProgressBar.setVisibility(View.VISIBLE);

                        boolean isOwner = true;
                        startTrackAnimation(isOwner);
                    }
                }
            }
        }
    }

    public void initIcons() {
        mPhotos = new ArrayList<Drawable>(8);
        mPhotos.add(getResources().getDrawable(R.drawable.male_01));
        mPhotos.add(getResources().getDrawable(R.drawable.male_02));
        mPhotos.add(getResources().getDrawable(R.drawable.male_03));
        mPhotos.add(getResources().getDrawable(R.drawable.male_04));

        mPhotos.add(getResources().getDrawable(R.drawable.female_01));
        mPhotos.add(getResources().getDrawable(R.drawable.female_02));
        mPhotos.add(getResources().getDrawable(R.drawable.female_03));
        mPhotos.add(getResources().getDrawable(R.drawable.female_04));

    }

    private ArrayList<HashMap<String, Object>> initDrawerListData() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("text", getResources().getString(R.string.drawer_update));
        map1.put("icon", R.drawable.update);
        list.add(map1);

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("icon", R.drawable.about_us);
        map2.put("text", getResources().getString(R.string.drawer_about_us));
        list.add(map2);

        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button:
                if (!mController.isConnected()) {
                    WaEntry.statEpv(WaKeys.CATEGORY_XPREAD, WaKeys.KEY_XPREAD_SEND_DISCONNECT);
                } else {
                    WaEntry.statEpv(WaKeys.CATEGORY_XPREAD, WaKeys.KEY_XPREAD_SEND_CONNECT);
                }
                Intent sendIntent = new Intent(MainActivity.this, FilePickActivity.class);

                startActivityForResult(sendIntent, Const.PICK_FILE_REQUEST_CODE);

                break;

            case R.id.receive_button:
                WaEntry.statEpv(WaKeys.CATEGORY_XPREAD, WaKeys.KEY_XPREAD_RECEIVE);
                Intent receiveIntent = new Intent(this, WaitFriendActivity.class);
                startActivity(receiveIntent);

                break;

            case R.id.user_icon:
                WaEntry.statEpv(WaKeys.CATEGORY_XPREAD, WaKeys.KEY_XPREAD_USER_SET);
                doChangeUserInfo();

                break;

            case R.id.user_name:
                WaEntry.statEpv(WaKeys.CATEGORY_XPREAD, WaKeys.KEY_XPREAD_USER_SET);
                doChangeUserInfo();

                break;

            case R.id.user_history:
                WaEntry.statEpv(WaKeys.CATEGORY_XPREAD, WaKeys.KEY_XPREAD_RECORD_ENTRANCE);
                Intent historyIntent = new Intent(this, RecordsActivity.class);
                startActivity(historyIntent);

                break;

            case R.id.menu:
                if (mDrawerLayout.isDrawerOpen(mDrawerContent)) {
                    mDrawerLayout.closeDrawer(mDrawerContent);
                } else {
                    WaEntry.statEpv(WaKeys.CATEGORY_XPREAD, WaKeys.KEY_XPREAD_HAMBURGER);
                    mDrawerLayout.openDrawer(mDrawerContent);
                }
                break;

            case R.id.share:
                WaEntry.statEpv(WaKeys.CATEGORY_XPREAD, WaKeys.KEY_XPREAD_SHARE);
                Intent shareIntent = new Intent(this, ShareActivity.class);
                startActivity(shareIntent);

                break;

            case R.id.disconnect_button:
                WaEntry.statEpv(WaKeys.CATEGORY_XPREAD, WaKeys.KEY_XPREAD_DISCONNECT);

                mController.disconnect();
                mIsDisconnected = true;

                if (mIsWaitConnected) {
                    resetDefaultState();
                }

                break;

            default:
                break;
        }
    }

    private void doChangeUserInfo() {
        final String userName = Utils.getOwnerName(this);
        final int iconIndex = Utils.getOwerIcon(this);
        if (mPopupWindow == null) {
            View contentView =
                    LayoutInflater.from(this).inflate(R.layout.change_user_info, null, false);
            GridView gridView = (GridView) contentView.findViewById(R.id.user_icon_grid);
            IconAdapter adapter = new IconAdapter(this, mPhotos);
            gridView.setAdapter(adapter);

            int colmunWidth = (int) (ScreenUtil.getScreenWidth(this) / COLUM_NUMBER);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(adapter.getCount() * colmunWidth,
                            LayoutParams.WRAP_CONTENT);
            gridView.setLayoutParams(params);
            gridView.setColumnWidth(colmunWidth);
            gridView.setStretchMode(GridView.NO_STRETCH);
            int count = adapter.getCount();
            gridView.setNumColumns(count);

            final RoundImageView userIcon =
                    (RoundImageView) contentView.findViewById(R.id.user_icon);
            userIcon.setImageDrawable(mPhotos.get(Utils.getOwerIcon(this)));
            final EditText name = (EditText) contentView.findViewById(R.id.user_name);
            name.setText(mUserName.getText());

            name.addTextChangedListener(new TextWatcher() {

                int selectionStart = 0;

                int selectionEnd = 0;

                CharSequence temp = null;

                final int MAX_LEN = 14;

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    this.temp = s;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    mIsNameValid = true;
                    this.selectionStart = name.getSelectionStart();
                    this.selectionEnd = name.getSelectionEnd();

                    if (LogUtil.isLog) {
                        Log.e(TAG, "selection start is " + this.selectionStart);
                    }

                    String reg = "^[a-zA-Z0-9_\\-\\s]*$";
                    Pattern pattern = Pattern.compile(reg);
                    Matcher matcher = null;

                    if (this.temp.length() > MAX_LEN) {
                        mIsNameValid = false;
                        s.delete(this.selectionStart - 1, this.selectionEnd);
                        int tempSelection = this.selectionStart;
                        name.setText(s);
                        name.setSelection(tempSelection);

                        matcher = pattern.matcher(name.getText().toString());
                        if (!matcher.matches()) {
                            Toast.makeText(MainActivity.this,
                                    getResources().getString(R.string.name_illegal_and_length),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this,
                                    getResources().getString(R.string.name_length_limit),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        matcher = pattern.matcher(name.getText().toString());
                        if (!matcher.matches()) {
                            mIsNameValid = false;
                            Toast.makeText(MainActivity.this,
                                    getResources().getString(R.string.name_illegal_hint),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            gridView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    userIcon.setImageDrawable(mPhotos.get(position));
                    iconId = position;
                    mFaceImage.setImageDrawable(mPhotos.get(iconId));
                }
            });

            mPopupWindow =
                    new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                    | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.headedit_bg));
            mPopupWindow.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss() {
                    String user_name = name.getText().toString();

                    if (mIsNameValid) {
                        if (!TextUtils.isEmpty(user_name)) {
                            mUserName.setText(user_name);
                            if (!user_name.equals(userName)) {
                                WaEntry.statEpv(WaKeys.CATEGORY_XPREAD,
                                        WaKeys.KEY_XPREAD_PROFILE_NAME);
                            }
                        } else {
                            user_name = null;
                        }
                    } else {
                        user_name = null;
                    }
                    if (iconIndex != iconId) {
                        WaEntry.statEpv(WaKeys.CATEGORY_XPREAD, WaKeys.KEY_XPREAD_PROFILE_ICON);
                    }
                    Utils.saveUserInfo(MainActivity.this, user_name, iconId);
                    mController.setUserInfo(user_name, null, iconId);
                }
            });

            userIcon.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mPopupWindow.isShowing()) mPopupWindow.dismiss();
                }
            });
        } else {
            View contentView = mPopupWindow.getContentView();
            final EditText name = (EditText) contentView.findViewById(R.id.user_name);
            name.setText(mUserName.getText());
        }

        mPopupWindow.showAsDropDown(this.mMenu, 0,
                getResources().getDimensionPixelSize(R.dimen.select_icon_top));
    }

    class IconAdapter extends BaseAdapter {

        private LayoutInflater mLayoutInflater;

        private List<Drawable> mList;

        public IconAdapter(Context context, List<Drawable> list) {
            mLayoutInflater = LayoutInflater.from(context);
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.icon_item, parent, false);
            }

            RoundImageView icon = (RoundImageView) convertView.findViewById(R.id.icon);
            icon.setImageDrawable(mList.get(position));

            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return;
        }
        mController.disconnect();

        super.onBackPressed();
    }

    public int px2dip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @Override
    protected void onDestroy() {
        // 实验室数据
        LaboratoryData.gAppEndCPUTime = LaboratoryData.getAppCpuTime();
        LaboratoryData.gTotalAPPEndCPUTime = LaboratoryData.getTotalCpuTime();
        LaboratoryData.print();
        // ---------------------------------

        WaEntry.handleMsg(WaEntry.MSG_EXITED);
        WaEntry.handleMsg(WaEntry.MSG_UPLOAD_FILE);

        restoreWifiState();

        if (mWaitConnectTask != null) {
            mWaitConnectTask.cancel();
        }

        mHandler.removeCallbacksAndMessages(null);

        super.onDestroy();

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        mController.setNetworkStateChangeListener(mNetworkStateChangeListener);
        mController.setProgressChangeListener(mProgressChangeListener);

        saveWifiStateBeforeOpen();
        resumeWifiStateToDefault();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        mController.unRegisterNetworkStateChangeListener(mNetworkStateChangeListener);
        mController.unRegisterProgressChangeListener();
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        super.onPause();
    }

    private boolean openWifi() {
        WifiAdmin wifiAdmin = this.mController.getWifiAdmin();
        WifiApAdmin wifiApAdmin = this.mController.getWifiApAdmin();

        int wifiState = wifiAdmin.getWifiState();
        if (wifiState == WifiManager.WIFI_STATE_ENABLED
                || wifiState == WifiManager.WIFI_STATE_ENABLING) {
            if (LogUtil.isLog) {
                Log.d(TAG, "wifi is enbale or enbaling, it's ok");
            }
            return true;
        } else {
            if (wifiApAdmin.getWifiApState() == WIFI_AP_STATE_ENABLING
                    || wifiApAdmin.getWifiApState() == WIFI_AP_STATE_ENABLED) {
                if (LogUtil.isLog) {
                    Log.d(TAG, "wifi ap is enbale , close it");
                }
                WifiConfiguration wcg = wifiApAdmin.getWifiApConfiguration();
                if (!wifiApAdmin.setWifiApEnabled(wcg, false)) {
                    if (LogUtil.isLog) {
                        Log.e(TAG, "close wifi ap fail");
                        return false;
                    }
                }
            }
            if (!wifiAdmin.openWifi()) {
                if (LogUtil.isLog) {
                    Log.e(TAG, "open wifi fail");
                }
                return false;
            }
            return true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntent();
    }

    private void processIntent() {
        Intent intent = getIntent();
        if (intent.getIntExtra(INTENT_TYPE, -1) == WAIT_CONNECTTED) {
            this.mIsWaitConnected = true;

            mHandler.sendEmptyMessageDelayed(CONNECT_TIMEOUT, 30 * 1000);
        } else if (intent.getIntExtra(INTENT_TYPE, -1) == SEND_FILE) {
            this.mIsOwnerTrackAnimationStart = true;
        }
    }

    public Bitmap setAlpha(Bitmap sourceImg, int number) {
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg.getWidth(),
                sourceImg.getHeight());
        number = number * 255 / 100;
        for (int i = 0; i < argb.length; i++) {
            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
        }
        sourceImg =
                Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg.getHeight(),
                        Config.ARGB_8888);

        return sourceImg;
    }

    private class WaitConnectTask extends TimerTask {

        @Override
        public void run() {
            mHandler.sendEmptyMessage(WAIT_CONNECTTED_ANIMATION);
        }
    }

    private class AnimationRunnable implements Runnable {

        int index;

        int role;

        public AnimationRunnable(int role, int i) {
            this.index = i;
            this.role = role;
        }

        @Override
        public void run() {
            switch (role) {
                case Const.SENDER:
                    mOwnerTrackViewArray[index].startAnimation(mOwnerAnimationArray[index]);
                    break;

                case Const.RECEIVER:
                    mGuestTrackViewArray[index].startAnimation(mGuestAnimationArray[index]);
                    break;

                default:
                    break;
            }

        }
    }

    private boolean isGooglePlayInstall() {
        PackageManager pm = getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                if (packageInfo.applicationInfo.packageName.contains("com.android.vending")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void startTrackAnimation(boolean isOwner) {
        if (LogUtil.isLog) {
            Log.e(TAG, "start track animation");
        }

        if (isOwner) {
            for (int i = 0; i < mOwnerTrackViewArray.length; ++i) {

                if (mOwnerTrackViewArray[i].getAnimation() == null
                        || !mOwnerTrackViewArray[i].getAnimation().hasStarted()) {
                    if (mOwnerTrackViewArray[i].getVisibility() == View.GONE) {
                        mOwnerTrackViewArray[i].setVisibility(View.VISIBLE);
                    }
                    mOwnerTrackViewArray[i].startAnimation(mOwnerAnimationArray[i]);
                } else {
                    if (LogUtil.isLog) {
                        Log.e(TAG, "owner track animation have start already, not start again");
                    }
                }
            }
        } else {
            for (int i = 0; i < mGuestTrackViewArray.length; ++i) {
                if (mGuestTrackViewArray[i].getAnimation() == null
                        || !mGuestTrackViewArray[i].getAnimation().hasStarted()) {
                    if (mGuestTrackViewArray[i].getVisibility() == View.GONE) {
                        mGuestTrackViewArray[i].setVisibility(View.VISIBLE);
                    }
                    mGuestTrackViewArray[i].startAnimation(mGuestAnimationArray[i]);
                } else {
                    if (LogUtil.isLog) {
                        Log.e(TAG, "guest track animation have start already, not start again");
                    }
                }
            }
        }
    }

    private void stopTrackAnimation(boolean isOwner) {
        if (LogUtil.isLog) {
            Log.e(TAG, "stop track animation");
        }

        if (isOwner) {
            for (int i = 0; i < mOwnerAnimationArray.length; ++i) {
                mOwnerAnimationArray[i].cancel();
                mOwnerAnimationArray[i].setStartOffset(i * TRACK_TIME_GAP);
                mOwnerAnimationArray[i].reset();

                mOwnerTrackViewArray[i].removeCallbacks(mAnimationRunnables[i]);
                mOwnerTrackViewArray[i].clearAnimation();
                mOwnerTrackViewArray[i].setVisibility(View.GONE);
            }
        } else {
            for (int i = 0; i < mGuestAnimationArray.length; ++i) {
                mGuestAnimationArray[i].cancel();
                mGuestAnimationArray[i].setStartOffset(i * TRACK_TIME_GAP);
                mGuestAnimationArray[i].reset();

                mGuestTrackViewArray[i].removeCallbacks(mAnimationRunnables[i]);
                mGuestTrackViewArray[i].clearAnimation();
                mGuestTrackViewArray[i].setVisibility(View.GONE);
            }
        }
    }

    private void resetAllTrackView() {
        stopTrackAnimation(true);
        stopTrackAnimation(false);
    }

    private void resetDefaultState() {

        // first step, refresh UI, remove the timeout message and the wait
        // animation timer task

        mIsDisconnected = false;
        mIsSenderFirstSetProgress = true;
        mIsReceiverFirstSetProgress = true;

        mHandler.removeMessages(CONNECT_TIMEOUT);
        mIsWaitConnected = false;

        if (mWaitConnectTask != null) {
            mWaitConnectTask.cancel();
        }

        changeWaitCircleState(false);
        changeButtonState(true);
        resetProgressBarState();

        mReceiveButton.setVisibility(View.VISIBLE);
        mDisconnectButton.setVisibility(View.GONE);
        mUserInfoLayout.setVisibility(View.VISIBLE);
        mConnectedUserInfoLayout.setVisibility(View.GONE);

        // second, disconnect the current connection
        mController.getWifiAdmin().disconnectCurrentWifi();

        // second step, remove the configuration
        String connectedFriendSsid = mController.getUerInfo().getConnectedFriendSsid();
        if (android.os.Build.VERSION.SDK_INT != 21) {
            if (connectedFriendSsid != null) {
                mController.getWifiAdmin().removeWifiConfiguration(connectedFriendSsid);
            }
        } else {
            mController.getWifiAdmin().disableNetWork(connectedFriendSsid);
        }

        // third step, resume to the default state
        mController.resumeToDefault();

        // fourth step, open the wifi if wifi not open
        resumeWifiStateToDefault();
    }

    /*
     * save the wifi state before open the app it will save the first time, other time will not save
     * again
     */
    private void saveWifiStateBeforeOpen() {

        UserInfo userInfo = this.mController.getUerInfo();

        if (userInfo.getIsWifiConnectedBefore() == -1) { // have not save the
                                                         // state
            WifiAdmin wifiAdmin = this.mController.getWifiAdmin();
            int state = wifiAdmin.getWifiState();

            if (state == WifiManager.WIFI_STATE_ENABLED || state == WifiManager.WIFI_STATE_ENABLING) {

                if (LogUtil.isLog) {
                    Log.e(TAG, "wifi is open before open xpread");
                }

                userInfo.setIsWifiConnectedBefore(1); // save the state
            } else {
                if (LogUtil.isLog) {
                    Log.e(TAG, "wifi is not open before open xpread");
                }
                userInfo.setIsWifiConnectedBefore(0); // save the state
            }
        }

    }

    /*
     * change the wifi state in the onResume if role is default , reset the wifi state to
     * default(open) else , the user is connecting or transfer file, so not keep the wifi state
     * under that case
     */
    private void resumeWifiStateToDefault() {
        if (this.mController.getRole() == -1) { // resume to default state
            if (openWifi()) {
                if (LogUtil.isLog) {
                    Log.e(TAG, "onResume---->open wifi success ");
                }
            } else {
                if (LogUtil.isLog) {
                    Log.e(TAG, "onResume---->open wifi fail ");
                }
            }
        } else {
            if (LogUtil.isLog) {
                String role = (this.mController.getRole() == Const.SENDER ? "sender" : "receiver");
                Log.e(TAG, "connect to friend and the role is " + role
                        + "do not change wifi state now");
            }
        }
    }

    /*
     * restore the wifi state the method will call when quit the app in MainActivity
     */
    private void restoreWifiState() {

        final UserInfo userInfo = this.mController.getUerInfo();
        WifiAdmin wifiAdmin = this.mController.getWifiAdmin();
        int isWifiConnectedBefore = userInfo.getIsWifiConnectedBefore();
        if (isWifiConnectedBefore == 1) {
            if (openWifi()) {
                if (LogUtil.isLog) {
                    Log.e(TAG, "onDestroy, wifi open success");
                }
            } else {
                if (LogUtil.isLog) {
                    Log.e(TAG, "onDestroy, wifi open fail");
                }
            }
            wifiAdmin.reconnect();
        } else if (isWifiConnectedBefore == 0) {
            wifiAdmin.closeWifi();
        }

        userInfo.setIsWifiConnectedBefore(-1);

    }

    /*
     * reset the progress bar state
     */
    private void resetProgressBarState() {
        if (mOwnerRoundProgressBar != null) {
            mOwnerRoundProgressBar.setProgress(0);
            mOwnerRoundProgressBar.clearAnimation();
        }
        mOwnerRoundProgressBar.setVisibility(View.GONE);

        if (mGuestRoundProgressBar != null) {
            mGuestRoundProgressBar.setProgress(0);
            mGuestRoundProgressBar.clearAnimation();
        }
        mGuestRoundProgressBar.setVisibility(View.GONE);
    }

    /*
     * change the button state if connecting, set the buttons disabled, if receive the message of
     * establish, set the buttons enabled
     * 
     * @param isEnable indicate the button should be enabled or disabled
     */
    private void changeButtonState(boolean isEnabled) {
        if (isEnabled) {
            mSendButton.setEnabled(true);
            mMenu.setEnabled(true);
            mShare.setEnabled(true);
            mUserHistory.setEnabled(true);
            mFaceImage.setEnabled(true);
        } else {
            mSendButton.setEnabled(false);
            mMenu.setEnabled(false);
            mShare.setEnabled(false);
            mUserHistory.setEnabled(false);
            mFaceImage.setEnabled(false);
        }
    }

    private void changeWaitCircleState(boolean isVisible) {
        if (isVisible) {
            this.mWaitConnectCircleView1.setVisibility(View.VISIBLE);
            this.mWaitConnectCircleView2.setVisibility(View.VISIBLE);
            this.mWaitConnectCircleView3.setVisibility(View.VISIBLE);
            this.mWaitConnectCircleView4.setVisibility(View.VISIBLE);
            this.mWaitConnectCircleView5.setVisibility(View.VISIBLE);
        } else {
            this.mWaitConnectCircleView1.setVisibility(View.GONE);
            this.mWaitConnectCircleView2.setVisibility(View.GONE);
            this.mWaitConnectCircleView3.setVisibility(View.GONE);
            this.mWaitConnectCircleView4.setVisibility(View.GONE);
            this.mWaitConnectCircleView5.setVisibility(View.GONE);
        }
    }

}
