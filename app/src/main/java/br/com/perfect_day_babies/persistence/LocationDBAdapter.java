package br.com.perfect_day_babies.persistence;

import java.util.ArrayList;
import java.util.List;

import br.com.perfect_day_babies.entities.Location;
import br.com.perfect_day_babies.entities.Vitrini;
import br.com.perfect_day_babies.utils.StringUtils;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LocationDBAdapter {

	public static final String DATABASE_LOCATION_TABLE = "locations";


	private SQLiteDatabase sqliteDb;

	public static final String KEY_ID = "_id";
	public static final String KEY_VITRINI_ID = "_vitrini_id";

	public static final String TABLE_DROP_LOCATIONS = "DROP TABLE IF EXISTS " + DATABASE_LOCATION_TABLE;


	public static final int ID_COLUMN = 0;
	public static final int VITRINI_ID_COLUMN = 1;

	private String[] locationColumns  = new String[] { KEY_ID, KEY_VITRINI_ID };


	@SuppressWarnings("unused")
	private DatabaseManager dbManager;


	public LocationDBAdapter(SQLiteDatabase db) 
	{
		super();
		this.sqliteDb = db;
	}

	public LocationDBAdapter(DatabaseManager dbManager)
	{
		super();
		this.sqliteDb = dbManager.getDB();
		this.dbManager = dbManager;
	}



	public Cursor getAllLocationsCursor () 
	{
		return sqliteDb.query(DATABASE_LOCATION_TABLE, locationColumns, null, null, null,
				null, null);
	}



	private Cursor getCursorByTableAndColumns( final String databaseTable, final String[] columns, final String whereClause )
	{
		Cursor cursor = sqliteDb.query(true, databaseTable, columns,
				whereClause, null, null, null, null, null);

		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			return null;
		}

		return cursor;
	}

	public Cursor getLocationsByVitrini ( final Vitrini vitrini) 
	{
		String whereClause = KEY_VITRINI_ID + "='" + StringUtils.normalize(vitrini.getId()) + "'";

		final Cursor cursor = getCursorByTableAndColumns(DATABASE_LOCATION_TABLE, locationColumns, whereClause);
		return cursor;

	}


	public Location getLocationById ( final String locationId )
	{
		String whereClause = KEY_ID + "='" + locationId + "'";
		Cursor cursor = sqliteDb.query(true, DATABASE_LOCATION_TABLE, locationColumns,
				whereClause, null, null, null, null, null);
		
		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			return null;
		}
		
		return getLocationByCursor(cursor);
	}
	
	public List<Location> getLocationsListByVitrini ( Vitrini vitrini )
	{
		Cursor cursor = getLocationsByVitrini(vitrini);
		List<Location> locations = new ArrayList<Location>();
		
		if(cursor.moveToFirst())
		{
			do
			{
				String locationId = cursor.getString( ID_COLUMN );
				Location location = getLocationById(locationId);
				locations.add( location );

			} while(cursor.moveToNext());
		}
		return locations;
		
	}
	
	public Location getLocationByCursor( Cursor cursor )
	{
		String id = cursor.getString(ID_COLUMN);
		String vitriniId = cursor.getString( VITRINI_ID_COLUMN );

		final Location location = new Location(id, vitriniId );
		return location;
	}

	public List<Location> listLocations() {

		Cursor cursor = this.getAllLocationsCursor();

		return getLocationListByCursor(cursor);
	}

	public List<Location> getLocationListByCursor(Cursor cursor) {
		List<Location> locations = new ArrayList<Location>();
		if(cursor.moveToFirst())
		{
			do
			{
				Location location = getLocationByCursor(cursor);
				locations.add( location );

			} while(cursor.moveToNext());
		}


		return locations;
	}


}
