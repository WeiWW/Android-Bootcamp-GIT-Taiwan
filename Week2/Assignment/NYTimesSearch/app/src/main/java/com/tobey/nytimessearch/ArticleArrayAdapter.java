package com.tobey.nytimessearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import static android.R.attr.resource;
import static android.R.attr.text;

/**
 * Created by tobeyyang on 12/12/2016.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article>{
    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data item for position
        Article article = this.getItem(position);
        //check to see if exsiting view being reused
        //not using a recycled view -> inflate the layout
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);

        }

        //find the image view
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        //clear out recycled image from convertView from last time
        imageView.setImageResource(0);

        TextView textView = (TextView) convertView.findViewById(R.id.tvTitle);
        textView.setText(article.getHeadline());

        //populate the thumbnail image
        //remote download the image in the background

        String thumbnail = article.getThumbNail();
        if(!TextUtils.isEmpty(thumbnail)){
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }
        return convertView;
    }
}
