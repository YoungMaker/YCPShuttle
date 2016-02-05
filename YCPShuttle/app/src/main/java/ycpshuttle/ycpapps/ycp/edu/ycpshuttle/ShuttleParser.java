package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import android.support.*;

/**
 * Created by Aaron on 2/4/2016.
 */
public class ShuttleParser extends AsyncTask<String, Void, ArrayList<String[]>> {
        MainActivityFragment fragInstance;

        public ShuttleParser(MainActivityFragment frag ) {
            this.fragInstance = frag;
        }

        @Override
        protected ArrayList<String[]> doInBackground(String... uris) {
            ArrayList output = new ArrayList<String[]>();
            output.add(loadPage(103));
            return output;
        }

        @Override
        protected void onPostExecute(ArrayList<String[]> list){
            String output ="";
            for(String[] s : list) {
                for(int i=0; i<s.length; i++) {
                    if(s[i] != null) {
                        output += s[i] + " , ";
                    }
                }
                output+= "\n";
            }
            fragInstance.editTextView(output);
        }

        protected String[] loadPage(int pageID) {
            String times[] = new String[10];
            try {
                Document doc = Jsoup.connect("https://ycpapps.ycp.edu/transit/gettimes.php?sid=" + pageID).get(); //loads html via http
                Elements elements = doc.getElementsByTag("td");
                for(int i=0; i<elements.size(); i++) {
                    String text =  elements.get(i).text();
                    if(text.length() > 0 && !text.equals("Minutes")) {
                        times[i] = text;
                    }
                }
            }
            catch(IOException e) {
                Log.e("IO_EXCEPTION", e.toString());
            }
            return times;
        }
}
