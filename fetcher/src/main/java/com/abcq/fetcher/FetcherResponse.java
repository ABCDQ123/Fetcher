package com.abcq.fetcher;

import org.json.JSONObject;

import java.util.List;

public interface FetcherResponse {
    void response(List<JSONObject> response);
}
