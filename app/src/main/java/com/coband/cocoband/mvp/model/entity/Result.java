package com.coband.cocoband.mvp.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 17-5-8.
 */

public class Result<T> {
    private List<T> results = new ArrayList<>();

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
