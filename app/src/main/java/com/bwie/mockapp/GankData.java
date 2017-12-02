package com.bwie.mockapp;

import java.util.List;

/**
 * Created by liqy on 2017/12/2.
 */

public class GankData<T> {
    public boolean error;
    public List<T> results;

    @Override
    public String toString() {
        return "GankData{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
