package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView header;
    private ImageButton map;
    private Stop s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        header = (TextView)findViewById(R.id.detail_Title);

        s = Route.getInstance().getStop(
                getIntent().getIntExtra("ROUTE_STOP_REQUESTED", 0));

        header.setText( s.getName());
        map = (ImageButton) findViewById(R.id.image_map);
        map.setImageResource(getImage(s.getId().getId()));

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:39.949733,-76.733897?q=39.949733,-76.733897(Northside Commons)");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private Integer getImage(int stopid) {
        switch(stopid) {
            case 100:
                return R.drawable.m100;
            case 101:
                return R.drawable.m101;
            case 1100:
                return R.drawable.m100;
            case 102:
                return R.drawable.m102;
            case 103:
                return R.drawable.m103;
            case 104:
                return R.drawable.m104;
            case 105:
                return R.drawable.m105;
            case 106:
                return R.drawable.m106;
            default:
                return R.drawable.def;
        }
    }

}
