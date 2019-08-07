package com.xheghun.data.repository;


import androidx.lifecycle.LiveData;

import com.xheghun.data.models.CoinModel;

import java.util.List;

public interface CryptoRepository {

    LiveData<List<CoinModel>> getCryptoCoinsData();
    LiveData<String> getErrorStream();
    LiveData<Double> getTotalMarketCapStream();
    void fetchData();
    }
