package com.xheghun.archiecom.viewmodel;

import android.app.Application;

import android.content.Context;


import androidx.lifecycle.Observer;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.xheghun.archiecom.CoinEntityGenerator;
import com.xheghun.archiecom.entities.CryptoCoinEntity;
import com.xheghun.archiecom.recview.CoinModel;
import com.xheghun.data.mappers.CryptoMapper;
import com.xheghun.data.repository.CryptoRepositoryImpl;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@RunWith(AndroidJUnit4.class)

public class CryptoViewModelTest {

    private final int NUM_OF_INSERT_COINS = 100;
    private CryptoRepositoryImpl repo;

    @Mock
    private Observer<Double> observer;
    private CryptoViewModel mViewModel;
    private Double totalMarketCap;

    @Before
    public void init() throws Exception

    {

        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getTargetContext();
        repo = CryptoRepositoryImpl.createImpl(context);
        mViewModel = new CryptoViewModel((Application) context.getApplicationContext(), repo);

    }

    @Test
    public void testTotalMarketCap() throws InterruptedException {
        List<com.xheghun.data.entities.CryptoCoinEntity> coins = createRandomCoins();
        CryptoMapper mapper=new CryptoMapper();
        CountDownLatch latch=new CountDownLatch(1);
        totalMarketCap = calculateTotalMarketCap(mapper.mapEntityToModel(coins));
        mViewModel.getTotalMarketCap().observeForever(observer);
        repo.deleteAllCoins();
        repo.insertAllCoins(coins);
        latch.await(50, TimeUnit.MILLISECONDS);
        verify(observer,atLeastOnce()).onChanged(totalMarketCap);

    }

    private List<com.xheghun.data.entities.CryptoCoinEntity> createRandomCoins() {
        List<CryptoCoinEntity> coins = new ArrayList<>();
        CryptoCoinEntity entity;
        for (int i = 0; i < NUM_OF_INSERT_COINS; i++) {
            entity = CoinEntityGenerator.createRandomEntity();
            coins.add(entity);
        }
        return coins;
    }

    private double calculateTotalMarketCap(List<CoinModel> coins) {
        double totalMK = 0;
        for (int i = 0; i < NUM_OF_INSERT_COINS; i++) {
            totalMK += coins.get(i).marketCap;
        }
        return totalMK;
    }

}
