package com.lankr.tv_cloud.support.qiniu.storage;

import com.lankr.tv_cloud.support.qiniu.http.Response;


/**
 * Created by bailong on 15/10/8.
 */
public interface UpCompletionHandler {
    void complete(String key, Response r);
}
