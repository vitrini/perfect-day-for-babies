package br.com.perfect_day_babies.persistence;

import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.com.perfect_day_babies.ReadAssetFileUtils;
import br.com.perfect_day_babies.utils.ApplicationContextProvider;

public class DatabaseManager extends SQLiteOpenHelper
{
	//private static final String DB_SCRIPTS_RIO_FRANCHISING_TEST_SQL = "db_scripts/rio_franchising_test.sql";
	private static final String DB_SCRIPTS_CREATE_DATABASE_SQL = "db_scripts/create_database.sql";


	private static final String DB_SCRIPTS_EVENT = "db_scripts/a_perfect_day_for_babies2014.sql";
	private static final String DB_SCRIPTS_LOCATIONS_EVENT = "db_scripts/locations_a_perfect_day_for_babies2014.sql";

	private static final String DB_SCRIPT_UPDATE_FORMAT ="db_scripts/update_db_to_version_%s.sql";

	public static final String DATABASE_NAME = "vitrini.db";	
	private static final int DATABASE_VERSION = 8;
	private Context context;

	private SQLiteDatabase db;
	private static DatabaseManager dbManager;

	private EventDBAdapter eventAdapter;
	private VitriniDBAdapter vitriniAdapter;
	private ProductDBAdapter productAdapter;
	private DBVersionAdapter dbVersionAdapter;
	private LocationDBAdapter locationAdapter;

	public DatabaseManager(Context context)
	{
		this(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public DatabaseManager(Context context, String name, CursorFactory factory,
			int version)
	{		
		super(context, name, factory, version);
		this.context = context;
		open();
	}


	public void beginTransaction()
	{
		db.beginTransaction();
	}

	public void commitTransaction()
	{
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		//a criacao do db esta no create script
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w("VitriniDB", "Upgrading from version " +
				oldVersion + " to " +
				newVersion + ", which will destroy all old data");
		//checkAndCreateDatabase();
	}

	private void open()
	{
		if (db == null)
		{		
			try
			{
				db = getWritableDatabase();			
			} 
			catch(SQLiteException ex)
			{
				db = getReadableDatabase();
			}
		}

	}

	public void close()
	{
		db.close();
	}	

	public EventDBAdapter getEventAdapter()
	{
		if(eventAdapter == null)
		{
			eventAdapter = new EventDBAdapter(context, db); 
		}
		return eventAdapter;
	}

	public void checkAndCreateDatabase()
	{		
		executeCreateScript();
	}

	/**
	 * Verifica a necessidade de criar o database ou nao faz nada caso ja esteja criado
	 */
	private void executeCreateScript() 
	{
		try {

			DBVersionAdapter adapter = getDBVersionAdapter();

			if ( !adapter.hasUpdatedDatabase())
			{
				executeSQLStatementsFromSQLText( context, DB_SCRIPTS_CREATE_DATABASE_SQL );
			}

		} catch (IOException e) {
			Log.e("", String.format( "It was not possible to open the file:%s. Check the assets directory", DB_SCRIPTS_CREATE_DATABASE_SQL));
			e.printStackTrace();
		}	
	}

	private void executeSQLStatementsFromSQLText(final Context context, final String filePath) throws IOException {
		String sqlFileTxt = ReadAssetFileUtils.readFileFromAssets(context, filePath);

		String[] sqlStatements = sqlFileTxt.split( ";" );

		for (int i = 0; i < sqlStatements.length; i++) {
			String sqlStatement = sqlStatements[i].trim();

			try{
				if (sqlStatement.length() > 0) 
				{
					db.execSQL(sqlStatement + ";");
				}
			}
			catch ( Exception e )
			{
				e.printStackTrace();
				Log.e("MERDA", sqlStatement);
			}
		}

	}

	public void purge()
	{
		DBVersionAdapter adapter = getDBVersionAdapter();

		if (!adapter.hasUpdatedDatabase() )
		{
			//algumas tabelas foram removidas
			db.execSQL(EventDBAdapter.TABLE_DROP);
			db.execSQL(VitriniDBAdapter.TABLE_DROP);
			db.execSQL(ProductDBAdapter.TABLE_DROP);
			db.execSQL( DBVersionAdapter.TABLE_DROP);
			db.execSQL( "DROP TABLE IF EXISTS vitrinis_x_locations");
			db.execSQL( "DROP TABLE IF EXISTS places_x_locations");
			db.execSQL( LocationDBAdapter.TABLE_DROP_LOCATIONS);
			db.execSQL( "DROP TABLE IF EXISTS places");
		}
	}

	public VitriniDBAdapter getVitriniAdapter() 
	{
		if(vitriniAdapter == null)
		{
			vitriniAdapter = new VitriniDBAdapter(db); 
		}
		return vitriniAdapter;
	}

	public ProductDBAdapter getProductAdapter() 
	{
		if(productAdapter == null)
		{
			productAdapter = new ProductDBAdapter(this);
		}
		return productAdapter;
	}

	public LocationDBAdapter getLocationAdapter()
	{
		if ( locationAdapter == null )
		{
			locationAdapter = new LocationDBAdapter(this);
		}
		return locationAdapter;
	}

	public DBVersionAdapter getDBVersionAdapter() 
	{
		if(dbVersionAdapter == null)
		{
			dbVersionAdapter = new DBVersionAdapter(this);
		}
		return dbVersionAdapter;
	}

	public void createInitialDataForEvent() {
		try {
			DBVersionAdapter adapter = getDBVersionAdapter();
			boolean hasDatabase = adapter.hasUpdatedDatabase();

			if ( !hasDatabase )
			{
				executeSQLStatementsFromSQLText(context, DB_SCRIPTS_EVENT);
				executeSQLStatementsFromSQLText(context, DB_SCRIPTS_LOCATIONS_EVENT);
			}
		} catch (IOException e) {
			Log.e("", "It was not possible to open the file:%s. Check the assets directory");
			e.printStackTrace();
		}
	}

	public void applyScripts() 
	{
		DBVersionAdapter adapter = getDBVersionAdapter();
		int dbVersion = adapter.getVersion();

		final Integer lastScriptVersion = adapter.getLastScriptVersion();
		while (dbVersion < lastScriptVersion )
		{
			try {
				int versionToGet = dbVersion + 1;
				executeSQLStatementsFromSQLText(context, String.format(DB_SCRIPT_UPDATE_FORMAT, versionToGet));
				dbVersion = adapter.getVersion();
			} 
			catch (IOException e) 
			{
				Log.e("", "It was not possible to open the file:%s. Check the assets directory");
				e.printStackTrace();
				dbVersion = lastScriptVersion;
			}
		}
	}
	public SQLiteDatabase getDB()
	{
		return db;
	}

	public static DatabaseManager getInstance() {
		if(dbManager == null) 
		{
			dbManager = new DatabaseManager(ApplicationContextProvider.getContext());			
			dbManager.checkAndCreateDatabase();
			dbManager.applyScripts();
			dbManager.createInitialDataForEvent();
						
			Log.i("CARGA DE DADOS", "CRIADO");
		}
		return dbManager;
	}
	
	public boolean dataAlreadyLoaded() {
		return getVitriniAdapter().getAllVitrinisCursor().moveToFirst();
	}

}
