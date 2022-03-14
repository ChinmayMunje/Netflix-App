package com.gtappdevelopers.netflixapp;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.gtappdevelopers.netflixapp.Adapters.CategoryRVAdapter;
import com.gtappdevelopers.netflixapp.Models.CategoryRVModal;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.idRVCategories);
        loadingPB = findViewById(R.id.idPBLoading);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        //adding persistence settings
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        //adding the settings to Firestore instance
        firebaseFirestore.setFirestoreSettings(settings);

        FirestorePagingAdapter categoryAdapter = new CategoryRVAdapter(
                getFirestorePagingOptions(firebaseFirestore), this,loadingPB);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(categoryAdapter);

    }

    private Query getQuery(FirebaseFirestore firebaseFirestore) {
        return firebaseFirestore.collection("Categories")
                .orderBy("categoryName");
    }

    private FirestorePagingOptions<CategoryRVModal> getFirestorePagingOptions(
            FirebaseFirestore firebaseFirestore) {
        Query baseQuery = getQuery(firebaseFirestore);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(15)
                .setPageSize(15)
                .build();

        return new FirestorePagingOptions.Builder<CategoryRVModal>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, CategoryRVModal.class)
                .build();
    }

}