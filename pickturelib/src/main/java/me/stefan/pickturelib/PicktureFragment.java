package me.stefan.pickturelib;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import me.stefan.pickturelib.adapter.PicRecyclerViewAdapter;
import me.stefan.pickturelib.constant.Constant;
import me.stefan.pickturelib.domain.Pic;
import me.stefan.pickturelib.domain.PicFolder;
import me.stefan.pickturelib.interf.OnPickListener;
import me.stefan.pickturelib.utils.PicLoader;

/**
 * @description 用于显示照片列表的frag
 */
public class PicktureFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_BUILDER = "ARG_BUILDER";
    // TODO: Customize parameters


    private OnPickListener mListener;
    private RequestManager mGlideRequestManager;

    private List<PicFolder> mFolderList;
    private PicRecyclerViewAdapter mPicAdapter;
    private RecyclerView mPicRecyclerView;

    private PickBuilder pickBuilder;
    private String TAG=this.getClass().getSimpleName();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PicktureFragment() {
    }

    // TODO: Customize parameter initialization
    public static PicktureFragment newInstance(PickBuilder builder) {
        PicktureFragment fragment = new PicktureFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BUILDER, builder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pickBuilder = (PickBuilder) getArguments().getSerializable(ARG_BUILDER);
            if (pickBuilder == null) pickBuilder = new PickBuilder();
        }

//        setRetainInstance(true);

        mGlideRequestManager = Glide.with(this);

        mFolderList = new ArrayList<>();

        int scWidth = getResources().getDisplayMetrics().widthPixels;
        mPicAdapter = new PicRecyclerViewAdapter(mGlideRequestManager, pickBuilder, (ArrayList<PicFolder>) mFolderList, mListener, scWidth / pickBuilder.getColumn());

        PicLoader.getPhotoDirs(getActivity(), null, new PicLoader.PhotosResultCallback() {
            @Override
            public void onResultCallback(List<PicFolder> directories) {
                mFolderList.clear();
                mFolderList.addAll(directories);
                if (mPicAdapter != null) {
                    mPicAdapter.setHasSelected(pickBuilder.getSelectedStrList());
                    mPicAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pic_list, container, false);


        // Set the adapter
        Context context = view.getContext();
        mPicRecyclerView = (RecyclerView) view;
        mPicRecyclerView.setLayoutManager(new GridLayoutManager(context, pickBuilder.getColumn()));
        mPicRecyclerView.setAdapter(mPicAdapter);
        mPicRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Log.d(">>> Picker >>>", "dy = " + dy);
                if (Math.abs(dy) > Constant.SCROLL_LIMIT) {
                    mGlideRequestManager.pauseRequests();
                } else {
                    mGlideRequestManager.resumeRequests();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mGlideRequestManager.resumeRequests();
                }
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPickListener) {
            mListener = (OnPickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public PicRecyclerViewAdapter getPicAdapter() {
        return mPicAdapter;
    }

    public List<PicFolder> getFolderList() {
        return mFolderList;
    }

    /**
     * invalide the data and fragment
     */
    public void invalide(Pic pic) {
        if (mFolderList != null && mFolderList.size() != 0)
            mFolderList.get(PicLoader.INDEX_ALL_PHOTOS).addPhoto(pic);
        if (mPicAdapter != null) {
            mPicAdapter.setHasSelected(pickBuilder.getSelectedStrList());
            mPicAdapter.notifyDataSetChanged();
        }
    }

}
