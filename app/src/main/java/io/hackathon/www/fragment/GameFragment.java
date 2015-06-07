package io.hackathon.www.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.hackathon.www.R;
import io.hackathon.www.android.GameLauncher;
import io.hackathon.www.activity.GameOverActivity;
import io.hackathon.www.activity.PropsActivity;


public class GameFragment extends Fragment {
    public static final int REQUEST_GAME = 0x1111;
    @InjectView(R.id.btn_single)
    ImageButton singleBtn;
    @InjectView(R.id.btn_double)
    ImageButton doubleBtn;
    @InjectView(R.id.btn_props)
    ImageButton propsBtn;

    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public GameFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick({R.id.btn_single, R.id.btn_double})
    public void onSingleBtnClick(ImageButton btn){
        Intent intent = new Intent(getActivity(), GameLauncher.class);
        startActivityForResult(intent, REQUEST_GAME);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GAME && resultCode== Activity.RESULT_OK) {
            Intent intent = new Intent(getActivity(), GameOverActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.btn_props)
    public void onPropsBtnClick(ImageButton btn) {
        Intent intent = new Intent(getActivity(), PropsActivity.class);
        startActivity(intent);
    }
}
