package io.hackathon.www.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.sns.SNS;
import com.avos.sns.SNSBase;
import com.avos.sns.SNSCallback;
import com.avos.sns.SNSException;
import com.avos.sns.SNSType;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import io.hackathon.www.R;
import io.hackathon.www.WeiboService;
import io.hackathon.www.WeiboUser;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ShareFragment extends Fragment {

    @InjectView(R.id.et_count)
    EditText countET;

    @InjectView(R.id.til_custom_share)
    TextInputLayout customSendTIL;

    @InjectView(R.id.mosquito)
    ImageButton mosquitoBtn;

    @InjectView(R.id.btn_send)
    Button sendBtn;

    String[] mosquitoAS;
    Random random = new Random();


    private OnFragmentInteractionListener mListener;

    public static ShareFragment newInstance() {
        ShareFragment fragment = new ShareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ShareFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_share, container, false);

        ButterKnife.inject(this, view);
        mosquitoAS = getResources().getStringArray(R.array.mosquito);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onSendFinished() {
        if (mListener != null) {
            mListener.onSendFinish();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onSendFinish();
    }

    @OnClick(R.id.mosquito)
    public void onMosQuittoClick(View view){
        countET.setText(String.valueOf(getCount()+1));
        if(Math.random()*10 > 6.0) {
            Toast.makeText(getActivity(), mosquitoAS[random.nextInt(mosquitoAS.length)], Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_send)
    public void onSendBtnClick(Button btn){
        if(getCount() == 0){
            return;
        }
        AVUser user = AVUser.getCurrentUser();
        if(user != null) {
            saveShare(user);
        } else {
            loginAndSaveShare();
        }
    }

    private void loginAndSaveShare() {
        try {
            SNS.setupPlatform(SNSType.AVOSCloudSNSSinaWeibo, "https://leancloud.cn/1.1/sns/goto/kri2k7ypwpd7p5se");
        } catch (AVException e) {
            e.printStackTrace();
        }
        SNS.loginWithCallback(getActivity(), SNSType.AVOSCloudSNSSinaWeibo, new SNSCallback() {
            @DebugLog
            @Override
            public void done(SNSBase base, SNSException e) {
                if (e == null) {
                    fetchWeiboUserInfo(base);

                } else {
                    onShareFailed(e);
                }
            }
        });

//        AVAnonymousUtils.logIn(new LogInCallback<AVUser>() {
//            @Override
//            public void done(AVUser user, AVException e) {
//                if(e == null) {
//                    saveShare(user);
//                } else {
//                    onShareFailed(e);
//                }
//            }
//        });
    }
    private void fetchWeiboUserInfo(final SNSBase base){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.weibo.com/2")
                .build();

        WeiboService service = restAdapter.create(WeiboService.class);
        service.userInfo(base.accessToken, base.userId, new Callback<WeiboUser>() {
            @Override
            public void success(final WeiboUser weiboUser, Response response) {
                SNS.loginWithAuthData(base.userInfo(), new LogInCallback<AVUser>() {
                    @Override
                    public void done(final AVUser user, AVException e) {
                        if(e == null) {
                            user.setUsername(weiboUser.screen_name);
                            user.put("avatar", weiboUser.profile_image_url);
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if(e == null) {
                                        saveShare(user);
                                    }else {
                                        onShareFailed(e);
                                    }
                                }
                            });
                        } else {
                            onShareFailed(e);
                        }
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    @DebugLog
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        SNS.onActivityResult(requestCode, resultCode, data, SNSType.AVOSCloudSNSSinaWeibo);
    }

    private void saveShare(AVUser user) {
        Toast.makeText(getActivity(), "提交中...", Toast.LENGTH_LONG).show();
        int count = getCount();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.fix_send_pre));
        sb.append(count);
        sb.append(getString(R.string.fix_send_end));
        sb.append("\n");
        sb.append(customSendTIL.getEditText().getText());

        AVObject object = new AVObject("FireLog");
        object.put("count", count);
        object.put("content", sb.toString());
        object.put("own", user);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null) {
                    onShareSuccess();
                }else {
                    onShareFailed(e);
                }
            }
        });
    }
    private void onShareSuccess(){
        countET.setText("0");
        customSendTIL.getEditText().setText("");
        onSendFinished();
        Toast.makeText(getActivity(), "发送成功~", Toast.LENGTH_LONG).show();
    }
    private void onShareFailed(Exception e){

        e.printStackTrace();
        Toast.makeText(getActivity(), "出错啦!", Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取被蚊子咬的次数
     * @return
     */
    private int getCount(){
        return Integer.parseInt(countET.getText().toString());
    }

}
