package br.com.perfect_day_babies.controller;

import br.com.perfect_day_babies.Constants;
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
public class VitriniTabGroupActivity extends TabGroupActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.getBoolean(Constants.RENDER_JUST_FAVORITES_FLAG, false)) {
        	myTabIndex = 3;
        } else {
        	myTabIndex = 1;
        }
        Intent intent = new Intent(this, VitriniListActivity.class);
        if(extras != null) {
        	intent.putExtras(extras);
        }
        //Here we only starts the MenuActivity having this Activity as parent.
        startChildActivity("VitriniList", intent);
        
    }
    
}
