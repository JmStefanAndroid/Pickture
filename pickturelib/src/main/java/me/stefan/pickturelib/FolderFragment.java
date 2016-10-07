package me.stefan.pickturelib;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.stefan.pickturelib.adapter.PicFolderAdapter;
import me.stefan.pickturelib.domain.PicFolder;

/**
 * <h4>desc:文件目录展示</h4>
 * <p></p>
 * <h3>注：</h3>
 * <p/>
 * Created by stefan on 2016/9/2 16:26
 */
public class FolderFragment extends Fragment {

    private static final String PARAM_DATA = "PARAM_DATA";
    private OnFolderItemClickListener mListener;
    static View back, tabFrame;
    PicFolderAdapter mPicFolderAdapter;
    private ArrayList<PicFolder> datas = new ArrayList<>();

    public FolderFragment() {
    }


    public static FolderFragment newInstance(List<PicFolder> datas, View back, View tabFrame) {
        FolderFragment fragment = new FolderFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(PARAM_DATA, (ArrayList<? extends Parcelable>) datas);
        fragment.setArguments(args);
        FolderFragment.back = back;
        FolderFragment.tabFrame = tabFrame;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            datas.clear();
            datas.addAll(getArguments().<PicFolder>getParcelableArrayList(PARAM_DATA));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.fragment_picfolder_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.__picfolder_recyleview);

        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mPicFolderAdapter = new PicFolderAdapter(datas, mListener);
        recyclerView.setAdapter(mPicFolderAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFolderItemClickListener) {
            mListener = (OnFolderItemClickListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

        back.setAlpha(0.3f);
        tabFrame.setVisibility(View.VISIBLE);
//        ViewUtils.backgroundAlpha(activity,0.3f);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        tabFrame.setVisibility(View.INVISIBLE);
        back.setAlpha(1.0f);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFolderItemClickListener {
        // TODO: Update argument type and name
        void onFolderItemClick(PicFolder item);
    }


}
