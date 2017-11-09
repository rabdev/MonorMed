package com.monormed;

import com.monormed.ServerRequest;
import com.monormed.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by NYG on 2017. 11. 09..
 */

public interface Interface {
    @POST("homokozo/mm2/index.php")
    Call<ServerResponse> operation(@Body ServerRequest request);
}
