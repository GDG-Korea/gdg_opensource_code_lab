
package com.example.gdg_opensource_codelab_sample_1;

import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import net.simonvt.menudrawer.MenuDrawer;

import java.util.ArrayList;

public class MainActivity extends SherlockFragmentActivity {

    private MenuDrawer mDrawer;
    
    private ArrayList<JsonVideoResource> mVideoResoruces = new ArrayList<JsonVideoResource>();
    {
        mVideoResoruces.add(JsonVideoResource.generateMock());
        mVideoResoruces.add(JsonVideoResource.generateMock());
        mVideoResoruces.add(JsonVideoResource.generateMock());
        mVideoResoruces.add(JsonVideoResource.generateMock());
        mVideoResoruces.add(JsonVideoResource.generateMock());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.hello_opensoruce_code_lab);
        
        mDrawer = MenuDrawer.attach(this);
        mDrawer.setContentView(R.layout.activity_main);
        mDrawer.setMenuView(R.layout.activity_menu);
        
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new VideoResourceAdapter(this, mVideoResoruces));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.abs__home:
                //fall through
            case android.R.id.home:
                mDrawer.toggleMenu(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}//end of class
