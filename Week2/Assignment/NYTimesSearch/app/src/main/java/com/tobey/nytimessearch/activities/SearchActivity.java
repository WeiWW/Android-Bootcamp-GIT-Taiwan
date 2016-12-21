package com.tobey.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tobey.nytimessearch.Article;
import com.tobey.nytimessearch.ArticleArrayAdapter;
import com.tobey.nytimessearch.DatePickerFragment;
import com.tobey.nytimessearch.Filter;
import com.tobey.nytimessearch.FilterDialogFragment;
import com.tobey.nytimessearch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.FilterItemDialogListener, DatePickerDialog.OnDateSetListener{

    EditText etQuery;
    GridView gvResult;
    Button btnSearch;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    Filter filter;
    String query;
    FilterDialogFragment filterItemDialogFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
        Log.d("Debug", "onCreate");
    }

    public void setupViews(){
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResult = (GridView) findViewById(R.id.gvResult);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<Article>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResult.setAdapter(adapter);

        //hook up listener for grid click
        gvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // create an intent to display the article
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                //get the article to display
                 Article article = articles.get(pos);
                //pass in that article into intent
                intent.putExtra("article", article);
                //launch the activity
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
        else if(id == R.id.action_filter){
//            Intent i = new Intent(getApplicationContext(), );
//
//            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    public void onFilterClick(MenuItem men){
        FragmentManager fm = getSupportFragmentManager();
        filterItemDialogFragment = FilterDialogFragment.newInstance(filter);
        // SETS the target fragment for use later when sending results
        filterItemDialogFragment.show(fm, "fragment_filter_item");

    }
    public void onCalendarClick(){
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void onArticleSearch(View view) {
        query = etQuery.getText().toString();
        queryNews(0);
        queryNews(1);
    }
    public void queryNews(int page){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", "140f0f5ccb674bad857b3dee07015f74");
        params.put("page",page);
        params.put("q", query);
        if(filter != null){
            if(!filter.get_beginDate().equals("")){
                params.put("begin_date", filter.get_beginDate());
            }
            params.put("sort", filter.get_sortingOrder());
            if (filter.get_newsType() != ""){
                params.put("fq", "news_desk:(" +filter.get_newsType()+ ")");
            }
        }
        Log.d("Debug", params.toString());
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Debug", response.toString());
                JSONArray articleJsonResults = null;
                try{
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.fromJsonArray(articleJsonResults));
                    Log.d("Debug", articles.toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure (int statusCode, Header[] headers,Throwable t, JSONObject response){
                Log.e("statusCode", Integer.toString(statusCode));
                Log.e("headers", headers.toString());
                Log.e("response", response.toString());
                Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
            }

        });
    }
    @Override
    public void onFinishFilterDialog(Filter savedFilter){
        filter = savedFilter;
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance

        if(filterItemDialogFragment != null){
            String day = dayOfMonth >= 10 ? Integer.toString(dayOfMonth) : "0" + Integer.toString(dayOfMonth);
            filterItemDialogFragment.updateSelectedDate(Integer.toString(year) + "/" + Integer.toString(monthOfYear+1) + "/" + day);
        }
    }
}
