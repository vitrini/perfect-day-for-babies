package br.com.perfect_day_babies.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * 
 * @author AlessandroGurgel
 *
 */
public class DBVersionAdapter {

	public DBVersionAdapter(SQLiteDatabase sqliteDb) {
		super();
		this.sqliteDb = sqliteDb;
	}

	public DBVersionAdapter(DatabaseManager dbManager)
	{
		super();
		this.sqliteDb = dbManager.getDB();
		this.dbManager = dbManager;
	}

	public static final String DATABASE_TABLE = "db_version";	

	public static final String KEY_ID = "_id";
	public static final String KEY_VERSION = "version";

	private SQLiteDatabase sqliteDb;
	@SuppressWarnings("unused")
	private DatabaseManager dbManager;

	//sempre e 1 a mais do que o ultimo script de update. A ultima versao e atualizada com o script de cadastro do evento e estandes
	public static final int DATABASE_VERSION = 6; 
	public static final int DATABASE_COLUMN = 1;

	private String[] categoryColumns = new String[] { KEY_ID, KEY_VERSION };

	public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;


	public Cursor getCursorDatabaseVersion() {
		return sqliteDb.query(DATABASE_TABLE, categoryColumns, null, null, null,
				null, null);
	}

	public Integer getLastScriptVersion () 
	{
		return DATABASE_VERSION - 2;
	}

	public Integer getVersion()
	{
		String whereClause = KEY_ID + "='1'";
		try{

			if ( sqliteDb == null )
			{
				//nesse caso o banco ainda nao foi criado
				return null;
			}

			Cursor cursor = sqliteDb.query(true, DATABASE_TABLE, categoryColumns,
					whereClause, null, null, null, null, null);

			if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
				return null;
			}


			String version = cursor.getString(DATABASE_COLUMN);

			if ( version == null || version.isEmpty())
			{
				return null;
			}


			int versionInt = Integer.parseInt(version);
			return versionInt; 
		}
		catch( SQLiteException e)
		{
			Log.i("dbVersion", "There is no table dbVersion. The system will create it", e);
			return null;
		}


	}


	public boolean hasUpdatedDatabase() {
		final Integer version = getVersion();

		if ( version == null )
		{
			return false;
		}

		if ( version != DATABASE_VERSION )
		{
			return false;
		}

		return true;
	}	
}
