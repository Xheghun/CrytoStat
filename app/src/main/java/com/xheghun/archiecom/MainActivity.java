package com.xheghun.archiecom;


import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.xheghun.archiecom.fragment.UILessFragment;
import com.xheghun.archiecom.recview.Divider;
import com.xheghun.archiecom.recview.MyCryptoAdapter;
import com.xheghun.archiecom.screens.MainScreen;
import com.xheghun.archiecom.viewmodel.CryptoViewModel;
import com.xheghun.data.models.CoinModel;

import java.util.List;


public class MainActivity extends LocationActivity implements MainScreen {
    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int DATA_FETCHING_INTERVAL=5*1000; //5 seconds
    private RecyclerView recView;
    private MyCryptoAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CryptoViewModel mViewModel;
    private long mLastFetchedDataTimeStamp;
    private ViewGroup root;

    private final Observer<List<com.xheghun.data.models.CoinModel>> dataObserver = coinModels -> updateData(coinModels);

    private final Observer<String> errorObserver = errorMsg -> setError(errorMsg);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        mViewModel = ViewModelProviders.of(this).get(CryptoViewModel.class);
        mViewModel.getCoinsMarketData().observe(this, dataObserver);
        mViewModel.getErrorUpdates().observe(this, errorObserver);
        mViewModel.fetchData();
        getSupportFragmentManager().beginTransaction()
                .add(new UILessFragment(),"UILessFragment").commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void bindViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        root = findViewById(R.id.root);
        recView = findViewById(R.id.recView);
        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (System.currentTimeMillis() - mLastFetchedDataTimeStamp < DATA_FETCHING_INTERVAL) {
                Log.d(TAG, "\tNot fetching from network because interval didn't reach");
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
            mViewModel.fetchData();
        });
        mAdapter = new MyCryptoAdapter();
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(RecyclerView.VERTICAL);
        recView.setLayoutManager(lm);
        recView.setAdapter(mAdapter);
        recView.addItemDecoration(new Divider(this));
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> recView.smoothScrollToPosition(0));
        fab=findViewById(R.id.fabExit);
        fab.setOnClickListener(view -> finish());
    }

    private void showErrorToast(String error) {
        Snackbar.make(root, "Error:" + error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void updateData(List<CoinModel> data) {
        mLastFetchedDataTimeStamp=System.currentTimeMillis();
        mAdapter.setItems(data);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setError(String msg) {
        showErrorToast(msg);
    }
}
