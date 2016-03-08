package br.com.perfect_day_babies.controller;

import android.content.Intent;
import android.os.Bundle;
/**
 * 
 * @author tnunes
 * 
 * This class launches the first Activity of the group. 
 * We start MenuActivity, that renders the categories view, as a child of this group activity,
 * then the users may navigate from the MenuActivity to the MenuItemListActivity.
 * The last activity of the group is the ItemDetailsAcivity that can be achieved from MenuItemListActivity.
 * All the activities that pertains to the group must be started from startChildActivity from this Activity
 * that is the parent.
 *  
 */
public class MapTabGroupActivity extends TabGroupActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myTabIndex = 2;
        Bundle extras = getIntent().getExtras();
        Intent intent = new Intent(this, PhotoMapActivity.class);
        if(extras != null) {
        	intent.putExtras(extras);
        }
        //Here we only starts the MenuActivity having this Activity as parent.
        startChildActivity("MapActivity", 
        		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));        
    }    
}
