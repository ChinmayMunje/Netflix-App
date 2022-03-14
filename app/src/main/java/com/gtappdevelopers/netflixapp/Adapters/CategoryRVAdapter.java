package com.gtappdevelopers.netflixapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.gtappdevelopers.netflixapp.Models.CategoryRVModal;
import com.gtappdevelopers.netflixapp.Models.VideoRVModal;
import com.gtappdevelopers.netflixapp.R;
import com.gtappdevelopers.netflixapp.ViewHolder.CategoryViewHolder;

public class CategoryRVAdapter extends FirestorePagingAdapter<CategoryRVModal, CategoryViewHolder> {

    private TextView categoryTV;
    private Context context;
    private ProgressBar loadingPB;

    /**
     * Construct a new FirestorePagingAdapter from the given {@link FirestorePagingOptions}.
     *
     * @param options
     */
    public CategoryRVAdapter(@NonNull FirestorePagingOptions<CategoryRVModal> options, Context context, ProgressBar progressBar) {
        super(options);
        this.context = context;
        this.loadingPB = progressBar;
    }


    @Override
    protected void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i, @NonNull CategoryRVModal categoryRVModal) {
        categoryTV = categoryViewHolder.itemView.findViewById(R.id.idTVCategoryName);
        categoryViewHolder.setCategoryNameTV(categoryRVModal.getCategoryName());

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        firebaseFirestore.setFirestoreSettings(settings);
        FirestorePagingAdapter videoRVAdapter = new VideoRVAdapter(getFirestorePaging(firebaseFirestore, categoryRVModal.getCategoryName()));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);
        categoryViewHolder.setVideosRV(videoRVAdapter, layoutManager);

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_rv_item, parent, false);
        return new CategoryViewHolder(view);
    }


    private Query getchildQuery(FirebaseFirestore firebaseFirestore, String category) {
        Query query = firebaseFirestore.collection("Videos")
                .whereEqualTo("videoCategory", category);
        query.orderBy("timestamp", Query.Direction.DESCENDING);
        return query;
    }

    private FirestorePagingOptions<VideoRVModal> getFirestorePaging(
            FirebaseFirestore firebaseFirestore, String category) {
        Query baseQuery = getchildQuery(firebaseFirestore, category);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(15)
                .setPageSize(15)
                .build();

        return new FirestorePagingOptions.Builder<VideoRVModal>()
                .setLifecycleOwner((LifecycleOwner) context)
                .setQuery(baseQuery, config, VideoRVModal.class)
                .build();
    }

    private void toggleNoDataLayout(boolean noData, boolean initialLoad) {
        if (initialLoad) {
            //noDataLayout.setVisibility(View.VISIBLE);
            //noDataText.setText(R.string.loadingtext);
            loadingPB.setVisibility(View.VISIBLE);
        } else if (noData) {
            //noDataLayout.setVisibility(View.VISIBLE);
            //progressBar.setVisibility(View.GONE);
//            if (!isNetworkStatusAvailable(context)) {
//                noDataText.setText(R.string.no_internet_connection_text);
//            } else { //no data was found
//                noDataText.setText(R.string.no_fresh_videos_found);
//            }
            loadingPB.setVisibility(View.GONE);
            Toast.makeText(context, "No data found..", Toast.LENGTH_SHORT).show();
        } else {
            loadingPB.setVisibility(View.GONE);
            //  Log.e(TAG,"spin 1 = "+spin1+"spin 2 ="+spin2);
        }
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        switch (state) {
            case LOADING_INITIAL:
                // The initial load has begun
                toggleNoDataLayout(true, true); //show loading view
                break;
            case LOADING_MORE:
                // The adapter has started to load an additional page
                super.onLoadingStateChanged(state); //default implementation is desired
                break;
            case LOADED:
                // The previous load (either initial or additional) completed
                int count = getItemCount();

                if (count > 0) {
                    toggleNoDataLayout(false, false);
                } else {
                    toggleNoDataLayout(true, false);
                }
                break;
            case ERROR:
                // The previous load (either initial or additional) failed. Call
                // the retry() method in order to retry the load operation.
                retry();
                super.onLoadingStateChanged(state);
        }
    }
}
