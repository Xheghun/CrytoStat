package com.xheghun.archiecom.screens;


import com.xheghun.data.models.CoinModel;

import java.util.List;


public interface MainScreen {

    void updateData(List<CoinModel> data);
    void setError(String msg);
}
