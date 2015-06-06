package io.hackathon.www;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.hackathon.www.android.AndroidLauncher;


public class GameFragment extends Fragment {

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

    @OnClick(R.id.btn_single)
    public void onSingleBtnClick(ImageButton btn){
        Intent intent = new Intent(getActivity(), AndroidLauncher.class);
        startActivity(intent);
    }

}
