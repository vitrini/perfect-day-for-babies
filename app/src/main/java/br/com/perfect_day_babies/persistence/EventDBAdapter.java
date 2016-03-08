package br.com.perfect_day_babies.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import br.com.perfect_day_babies.entities.Event;



@SuppressWarnings("deprecation")
public class EventDBAdapter
{
	public static final String DATABASE_TABLE = "events";	
	//private final Context context;
	private SQLiteDatabase sqliteDb;
	private InsertHelper insertHelper;
	
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_DESCRIPTION= "description";
	public static final String KEY_IMAGE_PATH = "iconPath";
	
	public static final String TABLE_CREATE = "create table " +
	DATABASE_TABLE + " (" + KEY_ID + " text primary key, " +
	KEY_NAME + " text not null, " + KEY_DESCRIPTION + " text , " + KEY_IMAGE_PATH + " text);";
	
	public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;
	public static final String INSERT_STATEMENT = "INSERT INTO " + DATABASE_TABLE + " VALUES(?,?,?,?,?)";
	public static final int ID_COLUMN = 0;
	public static final int NAME_COLUMN = 1;
	public static final int DESCRIPTION_COLUMN= 2;
	public static final int IMAGE_PATH_COLUMN = 3;
	
	public EventDBAdapter(Context context, SQLiteDatabase db)
	{
		super();
	//	this.context = context;
		this.sqliteDb = db;
		insertHelper = new InsertHelper(sqliteDb, DATABASE_TABLE);
	}
	
	public long insertEventWithHelper(Event event)
	{
		
		insertHelper.prepareForInsert();
		insertHelper.bind(1, event.getId());
		insertHelper.bind(2, event.getName());		
		insertHelper.bind(3, event.getDescription());
		insertHelper.bind(4, event.getImage());
		return insertHelper.execute();
	}
	
	public long insertEventWithPreparedStatement(Event event)
	{
		SQLiteStatement insertStatement = sqliteDb.compileStatement(INSERT_STATEMENT);
		insertStatement.bindString(1, event.getId());
		insertStatement.bindString(2, event.getName());
		insertStatement.bindString(3, event.getDescription());
		insertStatement.bindString(4, event.getImage());
		return insertStatement.executeInsert();
	}
	
	public long insertEvent(Event event)
	{
		ContentValues neweventValues = new ContentValues();
		neweventValues.put(KEY_ID, event.getId());
		neweventValues.put(KEY_NAME, event.getName());
		neweventValues.put(KEY_DESCRIPTION, event.getDescription());
		neweventValues.put(KEY_IMAGE_PATH, event.getImage());
		return sqliteDb.insert(DATABASE_TABLE, null, neweventValues);	
	}	
	
	public boolean removeEvent(String eventId)
	{
		return sqliteDb.delete(DATABASE_TABLE, KEY_ID + "=" + eventId, null) > 0;
	}
	
	public Cursor getAllEventsCursor()
	{
		String[] eventColumns = new String[] {KEY_ID, KEY_NAME, KEY_DESCRIPTION, KEY_IMAGE_PATH};
		return sqliteDb.query(DATABASE_TABLE, eventColumns, null, null, null, null, null);
	}
	
	public Event findEvent(String eventId)
	{
		String[] eventColumns = new String[] {KEY_ID, KEY_NAME, KEY_DESCRIPTION, KEY_IMAGE_PATH};
		String whereClause = KEY_ID + "='" + eventId + "'";
		Cursor cursor = sqliteDb.query(true, DATABASE_TABLE, eventColumns, whereClause, 
				null, null, null, null, null);
		if((cursor.getCount() == 0) || !cursor.moveToFirst())
		{
			return null;
		}
		String id = cursor.getString(ID_COLUMN);
		String name = cursor.getString(NAME_COLUMN);
		String billCode = cursor.getString(DESCRIPTION_COLUMN);
		String logoPath = cursor.getString(IMAGE_PATH_COLUMN);
		return new Event(id, name, billCode, logoPath);		
	}
}
