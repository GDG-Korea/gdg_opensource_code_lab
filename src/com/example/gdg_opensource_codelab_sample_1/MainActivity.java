
package com.example.gdg_opensource_codelab_sample_1;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import net.simonvt.menudrawer.MenuDrawer;

public class MainActivity extends SherlockFragmentActivity {

    private MenuDrawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.hello_opensoruce_code_lab);
        
        mDrawer = MenuDrawer.attach(this);
        mDrawer.setContentView(R.layout.activity_main);
        mDrawer.setMenuView(R.layout.activity_menu);        
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
