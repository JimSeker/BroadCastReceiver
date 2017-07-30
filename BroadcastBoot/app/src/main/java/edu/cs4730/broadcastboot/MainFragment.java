package edu.cs4730.broadcastboot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * All this fragment does is display information.  The bulk of the example is in mainActivity and myService.
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
        logger = (TextView) myView.findViewById(R.id.textView2);
        settext(info);
        return myView;
    }

    public void settext(String i) {
        info = i;
        logger.setText(info);

    }

}
