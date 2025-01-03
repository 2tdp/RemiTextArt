package com.textonphoto.texttophoto.quotecreator.pro.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.adapter.emoji.EmojiAdapter;
import com.textonphoto.texttophoto.quotecreator.pro.callback.ICallBackItem;
import com.textonphoto.texttophoto.quotecreator.pro.data.DataEmoji;

public class EmojiFragment extends Fragment {

    private static final String NAME_EMOJI = "name_emoji";

    private String nameEmoji;
    private ICallBackItem callBack;

    private RecyclerView rcvEmoji;

    public EmojiFragment(){}

    public EmojiFragment(ICallBackItem callBack) {
        this.callBack = callBack;
    }

    public static EmojiFragment newInstance(String nameEmoji, ICallBackItem callBack) {
        EmojiFragment fragment = new EmojiFragment(callBack);
        Bundle args = new Bundle();
        args.putString(NAME_EMOJI, nameEmoji);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nameEmoji = getArguments().getString(NAME_EMOJI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_emoji, container, false);
        init(v);
        setUpData(nameEmoji);
        return v;
    }


    private void init(View view) {
        rcvEmoji = view.findViewById(R.id.rcvEmoji);
    }

    private void setUpData(String nameEmoji) {
        EmojiAdapter emojiAdapter = new EmojiAdapter(requireContext(), callBack);

        emojiAdapter.setData(DataEmoji.getTitleEmoji(requireContext(), nameEmoji));
        GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);
        rcvEmoji.setLayoutManager(manager);
        rcvEmoji.setAdapter(emojiAdapter);
    }
}