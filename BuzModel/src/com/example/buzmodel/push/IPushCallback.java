package com.example.buzmodel.push;

import com.example.buzmodel.model.TBuz;

import java.util.List;

/**
 * Created by Administrator on 2014/7/20.
 */
public interface IPushCallback {

    public void onError();

    public void onComplete(List<TBuz> records);

}
