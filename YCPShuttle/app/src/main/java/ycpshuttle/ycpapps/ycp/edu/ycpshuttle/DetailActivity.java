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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

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
//        s = new Stop(StopID.NSC, 3, 10); //TODO THIS IS FOR WEEKEND DEBUG ONLY
//        Route.getInstance().setStop(s);

        header.setText( s.getName());
        TextView dist = (TextView)findViewById(R.id.disp_dist);
        DecimalFormat f = new DecimalFormat("#.##");
        f.setRoundingMode(RoundingMode.FLOOR);

        if(Route.getInstance().getCurrentLoc().getLatitude() == 0.00 && Route.getInstance().getCurrentLoc().getLongitude() == 0.00) {
            dist.setText("");
        }
        else {
            dist.setText(f.format(s.getDistanceTo()) + " miles from your location");
        }


        map = (ImageButton) findViewById(R.id.image_map);
        map.setImageResource(getImage(s.getId().getId()));

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse(s.getId().getGeoString());
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
        if (id == R.id.action_refresh) {
            //TODO: Refresh current shuttle
            return true;
        }

        return super.onOptionsItemSelected(item);
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
