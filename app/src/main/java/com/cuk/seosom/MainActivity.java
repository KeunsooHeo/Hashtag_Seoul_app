package com.cuk.seosom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
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
import android.view.WindowManager;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout linearLayout_list, linearLayout_header;
    ImageView imageView_all, imageView_like, imageView_hash, imageView_exit;
    ArrayList<ContentLayout> contentLayouts;
    String id;
    ScrollView scrollView;
    DBHelper dbHelper;
    boolean loading;
    static final int NONE=-2,ALL=-1, NO=0, TITLE=1, LINK=2, IMAGE_LINK=3, DISCRIP=4, AGE_UPPER=5, AGE_LOWWER=6,  CITIZEN=7, OLD=8, MULTI=9 ,KIDS=10,PREGNANT=11, DISABLE=12, LOW_INCOME=13, YOUTH=14, H_EDU=15, H_FIN=16, H_CUL=17, H_TNG=18, H_CON=19, H_HEL=20, H_HOU=21, H_JOB=22, H_FAL=23;
    static final int H_START=7, H_END=23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        contentLayouts = new ArrayList<ContentLayout>();
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        linearLayout_list   = (LinearLayout) findViewById(R.id.layout_list);
        linearLayout_header = (LinearLayout) findViewById(R.id.layout_header);
        imageView_all = (ImageView) findViewById(R.id.imageView_all);
        imageView_all.setOnClickListener(this);
        imageView_like = (ImageView) findViewById(R.id.imageView_like);
        imageView_like.setOnClickListener(this);
        imageView_hash = (ImageView) findViewById(R.id.imageView_hash);
        imageView_hash.setOnClickListener(this);
        imageView_exit = (ImageView) findViewById(R.id.imageView_exit);
        imageView_exit.setOnClickListener(this);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    private void makeList(int col) {
        loading = true;
        linearLayout_list.removeAllViews();
        for(ContentLayout contentLayout:contentLayouts){
            if (col == NONE);
            else if(col==ALL ||contentLayout.isSelected(col)){
                //if(contentLayout.getParent() != null) ((ViewGroup)contentLayout.getParent()).removeView(contentLayout); // <- fix

                linearLayout_list.addView(contentLayout);
            }
        }
        loading = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_CANCELED && data != null) {
            if(requestCode == 101){
                int index = data.getExtras().getInt("index");
                makeList(index);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(loading) {
            Toast.makeText(this, "목록을 불러오고 있습니다. 잠시만 기다려주십시오.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(v == imageView_hash){
            Intent intent = new Intent(this, SelectActivity.class);
            startActivityForResult(intent, 101);
        }
        else if (v == imageView_all){
            makeList(ALL);
        }
        else if (v == imageView_like){
            makeLikeList();
        }
        else if (v == imageView_exit){
            finish();
            Toast.makeText(this, "성공적으로 로그아웃하였습니다.",Toast.LENGTH_LONG).show();
            return;
        }
        scrollView.scrollTo(0,0);
    }

    private void makeLikeList() {
        loading=true;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct num from userlike where id=?",new String[]{id});
        linearLayout_list.removeAllViews();
        while (cursor.moveToNext()){
            for(ContentLayout contentLayout:contentLayouts){
                if (cursor.getString(0).equals(contentLayout.getNo())) {
                    if (contentLayout.getParent() != null) {
                        ((ViewGroup) contentLayout.getParent()).removeView(contentLayout);
                    }
                    linearLayout_list.addView(contentLayout);
                }
            }
        }
        loading=false;
    }

    class MyAsyncTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params){
            loading = true;Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ProgressDialog asyncDialog = new ProgressDialog(MainActivity.this);
                    asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    asyncDialog.setMessage("정보를 받아오고 있습니다. 잠시만 기다려주세요.");
                    asyncDialog.show();
                    while(loading){}
                        asyncDialog.dismiss();
                }
            }, 0);
            boolean isEmpty = false;
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select distinct num from userlike where id=?",new String[]{id});
            if(!cursor.moveToNext()){
                isEmpty = true;
            }

            BufferedReader br = null;
            String line;
            String cvsSplitBy = ",";
            String[] columnText;
            InputStream in = getResources().openRawResource(R.raw.data);
            try{
                InputStreamReader is = new InputStreamReader(in, "UTF-8");
                br = new BufferedReader(is);
                columnText = br.readLine().split(cvsSplitBy);
                while ((line = br.readLine()) != null) {
                    String[] field = line.split(cvsSplitBy);
                    contentLayouts.add(new ContentLayout(MainActivity.this, id, field, columnText, isEmpty));
                }
            } catch(Exception e){
                e.printStackTrace();
                return -1;
            }
            loading = false;
            return 0;
        }
    }
}

class ContentLayout extends LinearLayout implements View.OnClickListener{
    String title, link, imageLink, no, discription, id;
    TextView textView,mainTextView1;
    ImageView imageView;
    LinearLayout mainLayout,footerLayout,mainHashTagLayout;
    TextView footer1, footer2;
    Context context;
    String[] row, columns;
    DBHelper dbHelper;
    final int NONE=-2,ALL=-1, NO=0, TITLE=1, LINK=2, IMAGE_LINK=3, DISCRIP=4, AGE_UPPER=5, AGE_LOWWER=6,  CITIZEN=7, OLD=8, MULTI=9 ,KIDS=10,PREGNANT=11, DISABLE=12, LOW_INCOME=13, YOUTH=14, H_EDU=15, H_FIN=16, H_CUL=17, H_TNG=18, H_CON=19, H_HEL=20, H_HOU=21, H_JOB=22, H_FAL=23;
    final int H_START=7, H_END=23;
    final int dip = getResources().getDimensionPixelSize(R.dimen.dip);

    public ContentLayout(Context context,String id,String[] row, String[] columns, boolean isEmpty) {
        super(context);
        this.context = context;
        this.no = row[NO];
        this.title = row[TITLE];
        this.link = row[LINK];
        this.imageLink = row[IMAGE_LINK];
        this.row = row;
        this.columns = columns;
        this.discription = row[DISCRIP];
        this.id = id;

        dbHelper = new DBHelper(getContext());

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
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 30*dip));

        setBackgroundColor(0xffffffff);
        if (!imageLink.equals("")) {
            try {
                URL url = new URL(imageLink);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(3 * 1000);
                connection.connect();

                InputStream is = new BufferedInputStream(connection.getInputStream());
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(isEmpty){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select hash from userinfo where id=?",new String[]{id});
            while(cursor.moveToNext()){
                if (row[Integer.parseInt(cursor.getString(0))].equals("1")){
                    db.execSQL("insert into userlike (id, num) values (?,?)",new String[] {id, no});
                    db.close();
                    break;
                }
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
        mainTextView1.setText(discription);
        mainTextView1.setTextColor(0xff000000);
        mainTextView1.setPadding(dip,dip,dip,dip);
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
                hashTag.setPadding((int) (0.5*dip),0,(int) (0.5*dip),0);
                hashTag.setText(columns[i]);
                hashTag.setTextColor(ContextCompat.getColor(getContext(),R.color.hashtag));
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
        footerLayout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.footer));
        if (isLike()){
            footer1.setText("좋아요 취소");
            footer1.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.likeText));
            footer1.setTextColor(0xffffffff);
        }
        else{
        }
        this.addView(footerLayout);
    }

    public boolean isSelected(int col){
        return (row[col].equals("1"));
    }

    public String getNo() {
        return no;
    }

    @Override
    public void onClick(View v) {
        if (v == footer1){
            if (!isLike()){
                like();
            }
            else{
                dislike();
            }
        }
        else if (v==footer2){
            Uri uri = Uri.parse(link);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public boolean isLike() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct num from userlike where id=? and num=?",new String[]{id, this.no});
        if(cursor.moveToNext()){
            return true;
        }
        else{
            return false;
        }
    }

    void like(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        footer1.setText("좋아요 취소");
        footer1.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.likeText));
        footer1.setTextColor(0xffffffff);
        db.execSQL("insert into userlike (id, num) values (?, ?)",new String[]{id,no});
    }

    void dislike(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        footer1.setText("좋아요");
        footer1.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.footer));
        footer1.setTextColor(0xff000000);
        db.execSQL("delete from userlike where id=? and num=?",new String[]{id,no});
    }
}