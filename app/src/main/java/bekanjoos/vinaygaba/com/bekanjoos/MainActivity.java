package bekanjoos.vinaygaba.com.bekanjoos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MainActivity";
    public static final String BASE_URL = "http://api.myservice.com";
    private static RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    static ArrayList<Product> productList;
    Retrofit retrofit;
    RetrofitEndpoints endpoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        productList = new ArrayList<Product>();

        //populate dummy product data
        populateProducts();
        // specify an adapter (see also next example)
        mAdapter = new CustomAdapter(productList,this);
        mRecyclerView.setAdapter(mAdapter);

        //Swipe to Dismiss
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                Product product = new Product("id");
                deleteProduct(product);
                int position = viewHolder.getAdapterPosition();
                productList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // prepare call in Retrofit 2.0
        endpoint = retrofit.create(RetrofitEndpoints.class);

        //Load products
        loadProducts();


        /*
        * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
        * performs a swipe-to-refresh gesture.
        */
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        loadProducts();
                    }
                }
        );

    }

    /**
     * Method to load products from the API
     */
    private void loadProducts() {

        Call<ArrayList<Product>> call = endpoint.getProducts("email_id");

        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Response<ArrayList<Product>> response, Retrofit retrofit) {
                productList = response.body();
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Method to delete a product from a user's list
     * @param product
     */
    private void deleteProduct(Product product) {

        Call<String> call = endpoint.deleteProduct("product_id");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }

    private void populateProducts() {

        Product p1 = new Product("Hill's Adult Oral Care","$21.59","Amazon","http://ecx.images-amazon.com/images/I/51jx7n5KIFL._SY300_.jpg");
        Product p2 = new Product("Samsung Galaxy 5","Rs. 20,000","Flipkart","http://s.tmocache.com/content/dam/tmo/en-p/cell-phones/samsung-galaxy-s-5/charcoal-black/stills/carousel-samsung-galaxy-s-5-charcoal-black-380x380-1.jpg");
        Product p3 = new Product("Sony Playstation 4","$ 349.99","BestBuy","http://cdn2.ubergizmo.com/wp-content/uploads/2015/06/sony-ps4-640x360.jpg");
        Product p4 = new Product("Joe Black Wayfarer","$199","Amazon","http://stat.homeshop18.com/homeshop18/images/productImages/921/joe-black-unisex-wayfarer-sunglasses-black-medium_a832884383553c32ed3e25401a63c733.jpg");

        productList.add(p1);
        productList.add(p2);
        productList.add(p3);
        productList.add(p4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            LoginManager.getInstance().logOut();


            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class ItemClickListener implements View.OnClickListener{

        Context context;

        ItemClickListener(Context context){
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            int position = mRecyclerView.indexOfChild(view);
            Toast.makeText(context,"Pos "+position,Toast.LENGTH_LONG).show();
            Product product = productList.get(position);

            Intent intent = new Intent(context.getApplicationContext(),DetailActivity.class);
            intent.putExtra("product_name",product.getProductName());
            intent.putExtra("product_id",product.getProductId());
            intent.putExtra("price",product.getPrice());
            intent.putExtra("image",product.getImageUrl());
            intent.putExtra("website",product.getWebsite());
            context.startActivity(intent);


        }
    }


}


