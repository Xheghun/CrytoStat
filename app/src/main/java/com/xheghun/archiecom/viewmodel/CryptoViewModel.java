package com.xheghun.archiecom.viewmodel;

import android.app.Application;

import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.xheghun.data.models.CoinModel;
import com.xheghun.data.repository.CryptoRepository;
import com.xheghun.data.repository.CryptoRepositoryImpl;

import java.util.List;

/**
 * Created by omrierez on 10/1/17.
 */

public class  CryptoViewModel extends AndroidViewModel {
    private static final String TAG = CryptoViewModel.class.getSimpleName();
    private CryptoRepository mCryptoRepository;

    public LiveData<List<CoinModel>> getCoinsMarketData() {
        return mCryptoRepository.getCryptoCoinsData();
    }

    public LiveData<String> getErrorUpdates() {
        return mCryptoRepository.getErrorStream();
    }

    public CryptoViewModel(@NonNull Application application) {
        super(application);
        mCryptoRepository = CryptoRepositoryImpl.create(application);
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared() called");
        super.onCleared();
    }

    public void fetchData() {
        mCryptoRepository.fetchData();
    }

    public LiveData<Double> getTotalMarketCap() {
        return mCryptoRepository.getTotalMarketCapStream();
    }

    @VisibleForTesting
    public CryptoViewModel(@NonNull Application application,CryptoRepositoryImpl repo) {
        super(application);
        this.mCryptoRepository = repo; }
}

