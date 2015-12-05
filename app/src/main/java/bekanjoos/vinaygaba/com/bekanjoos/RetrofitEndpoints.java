package bekanjoos.vinaygaba.com.bekanjoos;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by vinaygaba on 12/5/15.
 */
public interface RetrofitEndpoints {

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("/api/products")
    Call<List<Product>> getProducts(@Query("email") String email);

    @POST("/api/product")
    Call<String> addProduct(@Body Product product);

    @DELETE("/api/product")
    Call<String> deleteProduct(@Body Product product);
}
