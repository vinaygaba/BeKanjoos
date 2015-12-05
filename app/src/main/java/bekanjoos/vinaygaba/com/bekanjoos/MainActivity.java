package bekanjoos.vinaygaba.com.bekanjoos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://api.myservice.com";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Product> productList;
    Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        productList = new ArrayList<Product>();
        populateProducts();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CustomAdapter(productList,this);
        mRecyclerView.setAdapter(mAdapter);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // prepare call in Retrofit 2.0
        RetrofitEndpoints endpoint = retrofit.create(RetrofitEndpoints.class);

        Call<List<Product>> call = endpoint.getProducts("email_id");

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

