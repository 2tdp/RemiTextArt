package com.textonphoto.texttophoto.quotecreator.pro.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.adapter.textadapter.QuotesAdapter;
import com.textonphoto.texttophoto.quotecreator.pro.callback.ICallBackItem;
import com.textonphoto.texttophoto.quotecreator.pro.data.text.DataQuotes;

public class QuotesFragment extends Fragment {

    private static final String NAME_QUOTES = "name_quotes";

    private String nameQuotes;
    private ICallBackItem callBack;

    private RecyclerView rcvQuotes;

    public QuotesFragment() {
    }

    public QuotesFragment(ICallBackItem callBack) {
        this.callBack = callBack;
    }

    public static QuotesFragment newInstance(String nameQuotes, ICallBackItem callBack) {
        QuotesFragment fragment = new QuotesFragment(callBack);
        Bundle args = new Bundle();
        args.putString(NAME_QUOTES, nameQuotes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nameQuotes = getArguments().getString(NAME_QUOTES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quotes, container, false);
        init(v);
        setUpData(nameQuotes);
        return v;
    }


    private void init(View view) {
        rcvQuotes = view.findViewById(R.id.rcvQuotes);
    }

    private void setUpData(String nameQuotes) {
        QuotesAdapter quotesAdapter = new QuotesAdapter(requireContext(), callBack);

        quotesAdapter.setData(DataQuotes.getListQuotesWithName(requireContext(), nameQuotes));
        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvQuotes.setLayoutManager(manager);
        rcvQuotes.setAdapter(quotesAdapter);
    }
}