package com.cuk.seosom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity{
    LinearLayout linearLayout_list, linearLayout_header;
    TextView statement;
    final int NO=0, TITLE=1, LINK=2, IMAGE_LINK=3, AGE_UPPER=4, AGE_LOWWER=5, OLD=6, CITIZEN=7, KIDS=8, PREGNANT=9, DISABLE=10, LOW_INCOME=11, YOUTH=12, H_EDU=13, H_FIN=14, H_CUL=15, H_TNG=16, H_CON=17, H_HEL=18, H_HOU=19, H_JOB=20, H_FAL=21;
    boolean[] filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        linearLayout_list   = (LinearLayout) findViewById(R.id.layout_list);
        linearLayout_header = (LinearLayout) findViewById(R.id.layout_header);
        filter = new boolean[11];
        Arrays.fill(filter, true);
        System.out.println(filter);

        makeList();
    }

    private void makeList() {
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        String[] columnText;
        try{
            InputStream in = getResources().openRawResource(R.raw.data);
            InputStreamReader is = new InputStreamReader(in, "UTF-8");
            br = new BufferedReader(is);
            columnText = br.readLine().split(cvsSplitBy);
            while ((line = br.readLine()) != null) {
                String[] field = line.split(cvsSplitBy);
                System.out.print(field[1]);
                System.out.println(field[IMAGE_LINK]);
                linearLayout_list.addView(new ContentLayout(this, field, columnText));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

class ContentLayout extends LinearLayout implements View.OnClickListener{
    String title, link, imageLink, no;
    TextView textView,mainTextView1;
    ImageView imageView;
    LinearLayout mainLayout,footerLayout,mainHashTagLayout;
    TextView footer1, footer2;
    Context context;
    String[] row, columns;
    final int NO=0, TITLE=1, LINK=2, IMAGE_LINK=3, AGE_UPPER=4, AGE_LOWWER=5, OLD=6, MUTI_CUL=7, CITIZEN=8, KIDS=9, PREGNANT=10, DISABLE=11, LOW_INCOME=12, YOUTH=13, H_EDU=14, H_FIN=15, H_CUL=16, H_TNG=17, H_CON=18, H_HEL=19, H_HOU=20, H_JOB=21, H_FAL=22;
    final int H_START = 14, H_END=22;
    final int dip = getResources().getDimensionPixelSize(R.dimen.dip);

    public ContentLayout(Context context, String[] row, String[] columns) {
        super(context);
        this.context = context;
        this.no = row[NO];
        this.title = row[TITLE];
        this.link = row[LINK];
        this.imageLink = row[IMAGE_LINK];
        this.row = row;
        this.columns = columns;

        textView = new TextView(context);
        imageView = new ImageView(context);
        footerLayout = new LinearLayout(context);
        mainLayout = new LinearLayout(context);
        setOrientation(VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(3*dip,dip,3*dip,dip);
        setLayoutParams(params);

        textView.setText(title);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setPadding(dip,dip,dip,dip);
        textView.setTextSize(15);
        textView.setTextColor(0xffffffff);
        textView.setBackgroundColor(ContextCompat.getColor(getContext() ,R.color.colorPrimaryDark));
        textView.setGravity(Gravity.CENTER);
        textView.setOnClickListener(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 30*dip));//LinearLayout.LayoutParams.WRAP_CONTENT));

        setBackgroundColor(0xffffffff);
        if (!imageLink.equals("")) {
            try {
                URL url = new URL(imageLink);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        addView(textView);
        addView(imageView);

        setMainLayout();

        setFooterLayout();
    }

    private void setMainLayout(){
        mainLayout.setOrientation(VERTICAL);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 20*dip));

        mainTextView1 = new TextView(context);
        mainHashTagLayout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        params.weight = 5;
        mainTextView1.setBackgroundColor(0x88448844);
        mainTextView1.setLayoutParams(params);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        params2.weight = 2;
        mainHashTagLayout.setLayoutParams(params2);
        addHashTag();
        mainHashTagLayout.setOrientation(HORIZONTAL);
        mainHashTagLayout.setPadding(dip,dip,dip,dip);

        mainLayout.addView(mainTextView1);
        mainLayout.addView(mainHashTagLayout);
        addView(mainLayout);
    }

    private void addHashTag() {
        for(int i=H_START; i<=H_END; i++){
            if(Integer.parseInt(row[i])==1) {
                TextView hashTag = new TextView(context);
                hashTag.setPadding(dip,0,dip,0);
                hashTag.setText(columns[i]);
                mainHashTagLayout.addView(hashTag);
            }
        }
    }

    private void setFooterLayout() {
        footerLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        footerLayout.setOrientation(HORIZONTAL);
        footer1 = new TextView(context);
        footer2 = new TextView(context);
        footer1.setText("좋아요");
        footer2.setText("자세히 보기");
        footer1.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        footer2.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        footer1.setOnClickListener(this);
        footer2.setOnClickListener(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
        params.weight = 1;
        footer1.setPadding(dip, dip, dip, dip);
        footer2.setPadding(dip, dip, dip, dip);
        footer1.setLayoutParams(params);
        footer2.setLayoutParams(params);
        footerLayout.addView(footer1);
        footerLayout.addView(footer2);
        footerLayout.setBackgroundColor(0xff00ffff);
        this.addView(footerLayout);
    }

    @Override
    public void onClick(View v) {
        if (v == footer1){
            Toast.makeText(context,"LIKE!",Toast.LENGTH_LONG).show();
        }
        else if (v==footer2){
            Uri uri = Uri.parse(link);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
