package com.datnt.remitextart.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.adapter.decor.DecorAdapter;
import com.datnt.remitextart.callback.ICallBackItem;
import com.datnt.remitextart.data.DataDecor;
import com.datnt.remitextart.utils.Utils;

public class DecorFragment extends Fragment {

    private static final String NAME_DECOR = "nameDecor";

    private String nameDecor;
    private ICallBackItem callBack;

    private RecyclerView rcvDecor;

    public DecorFragment(){}

    public DecorFragment(ICallBackItem callBack) {
        this.callBack = callBack;
    }

    public static DecorFragment newInstance(String nameDecor, ICallBackItem callBack) {
        DecorFragment fragment = new DecorFragment(callBack);
        Bundle args = new Bundle();
        args.putString(NAME_DECOR, nameDecor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) nameDecor = getArguments().getString(NAME_DECOR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_decor, container, false);

        init(v);
        setUpData(nameDecor);
        return v;
    }

    private void init(View view) {
        rcvDecor = view.findViewById(R.id.rcvDecor);
    }

    private void setUpData(String nameDecor) {
        DecorAdapter decorAdapter = new DecorAdapter(requireContext(), callBack);

        decorAdapter.setData(DataDecor.getDataDecor(requireContext(), nameDecor));
        GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);
        rcvDecor.setLayoutManager(manager);
        rcvDecor.setAdapter(decorAdapter);
    }
}