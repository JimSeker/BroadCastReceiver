package edu.cs4730.broadcastboot;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * All this fragment does is display information.
 * <p>
 * It also has a button to "jump start" the process.  Basically the first message.
 */

public class MainFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    String info = "";
    TextView logger;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            info = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_main, container, false);
        logger = myView.findViewById(R.id.textView2);
        logger.setText(info);

        myView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.ACTION);
                intent.setPackage("edu.cs4730.broadcastboot"); //in API 26, it must be explicit now.
                getActivity().sendBroadcast(intent);
                getActivity().finish();
            }
        });
        return myView;
    }

}
