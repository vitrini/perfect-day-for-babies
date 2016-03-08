package br.com.perfect_day_babies.controller;

import android.app.Activity;
import br.com.perfect_day_babies.persistence.DatabaseManager;

public abstract class AbstractActivity extends Activity
{
	protected ActivityController controller;
	
	protected DatabaseManager databaseManager;
	
	protected void initDatabaseAndServices() {
		if(databaseManager == null){
			databaseManager = DatabaseManager.getInstance();
		}		
		controller = new ActivityController( this, databaseManager );
	}
	
	abstract protected void initView();
	
}
