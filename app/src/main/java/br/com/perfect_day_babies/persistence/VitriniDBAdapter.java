package br.com.perfect_day_babies.persistence;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import br.com.perfect_day_babies.entities.Vitrini;

public class VitriniDBAdapter {
	public static final String DATABASE_TABLE = "vitrinis";
	private SQLiteDatabase sqliteDb;

	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_LOGO_PATH = "smallImagePath";
	public static final String KEY_IMAGE_PATH = "largeImagePath";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_SEGMENT = "segment";
	public static final String KEY_FAVORITE = "favorite";
	public static final String KEY_SITE = "site";
	public static final String KEY_CITY = "city";
	public static final String KEY_ESTATE = "estate";

	// TODO colocar String.format
	// TODO inserir restri��es
	public static final String TABLE_CREATE = "create table " + DATABASE_TABLE
			+ " (" + KEY_ID + " text primary key, " + KEY_NAME
			+ " text not null," + KEY_LOGO_PATH + " text," + KEY_IMAGE_PATH
			+ " text," + KEY_DESCRIPTION + " text," + KEY_SEGMENT + " text," + KEY_SITE + " text);";

	// TODO colocar String.format
	public static final String TABLE_DROP = "DROP TABLE IF EXISTS "
			+ DATABASE_TABLE;
	public static final String INSERT_STATEMENT = "INSERT INTO "
			+ DATABASE_TABLE + " VALUES(?,?,?,?,?,?)";

	public static final int ID_COLUMN = 0;
	public static final int NAME_COLUMN = 1;
	public static final int LOGO_COLUMN = 2;
	public static final int IMAGE_COLUMN = 3;
	public static final int DESCRIPTION_COLUMN = 4;
	public static final int SEGMENT_COLUMN = 5;
	public static final int SITE_COLUMN = 6;
	public static final int FAVORITE_COLUMN = 7;
	public static final int CITY_COLUMN = 8;
	public static final int ESTATE_COLUMN = 9;
	
	private static String WHERE_IS_FAVORITE = KEY_FAVORITE + "=1"; 

	
	private String[] vitriniColumns = new String[] { KEY_ID, KEY_NAME,
			KEY_LOGO_PATH, KEY_IMAGE_PATH, KEY_DESCRIPTION, KEY_SEGMENT, 
			KEY_SITE, KEY_FAVORITE, KEY_CITY, KEY_ESTATE};

	public VitriniDBAdapter(SQLiteDatabase db) {
		super();
		this.sqliteDb = db;
	}

	public SQLiteStatement createInsertStatement() {
		return sqliteDb.compileStatement(INSERT_STATEMENT);
	}

	public long insertVitrini(Vitrini vitrini) {
		ContentValues newVitriniValues = new ContentValues();
		newVitriniValues.put(KEY_ID, vitrini.getId());
		newVitriniValues.put(KEY_NAME, vitrini.getName());
		newVitriniValues.put(KEY_LOGO_PATH, vitrini.getLogoPath());
		newVitriniValues.put(KEY_IMAGE_PATH, vitrini.getImagePath());
		newVitriniValues.put(KEY_DESCRIPTION, vitrini.getDescription());
		newVitriniValues.put(KEY_SEGMENT, vitrini.getSegment());
		newVitriniValues.put(KEY_SITE, vitrini.getSite());
		return sqliteDb.insert(DATABASE_TABLE, null, newVitriniValues);
	}


	public long insertVitriniWithPreparedStatement(Vitrini vitrini) {

		SQLiteStatement insertStmt = sqliteDb
				.compileStatement(INSERT_STATEMENT);
		insertStmt.bindString(1, vitrini.getId());
		insertStmt.bindString(2, vitrini.getName());
		insertStmt.bindString(3, vitrini.getLogoPath());
		insertStmt.bindString(4, vitrini.getImagePath());
		insertStmt.bindString(5, vitrini.getDescription());
		insertStmt.bindString(6, vitrini.getSite());
		return insertStmt.executeInsert();
	}

	public boolean removeCategory(String categoryId) {
		return sqliteDb.delete(DATABASE_TABLE,
				KEY_ID + "='" + categoryId + "'", null) > 0;
	}

	public Cursor getAllVitrinisCursor() {

		return sqliteDb.query(DATABASE_TABLE, vitriniColumns, null, null, null,
				null, null);
	}
	
	public Cursor filterVitrinisBySegment(String segment, boolean considerJustFavorites) {		

		StringBuilder whereClause = new StringBuilder(KEY_SEGMENT + "='" + segment + "'");
		if(considerJustFavorites) {
			whereClause.append(" AND " + WHERE_IS_FAVORITE);
		}
		Cursor cursor = sqliteDb.query(true, DATABASE_TABLE, vitriniColumns,
				whereClause.toString(), null, null, null, null, null);

		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			return null;
		}

		return cursor;
	}

	public Vitrini findVitrini(String vitriniId) {

		String whereClause = KEY_ID + "='" + vitriniId + "'";
		Cursor cursor = sqliteDb.query(true, DATABASE_TABLE, vitriniColumns,
				whereClause, null, null, null, null, null);

		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			return null;
		}

		Vitrini vitrini = getVitriniByCursor(cursor);
		return vitrini;
	}

	public Cursor filterVitrinisByInput(String input, boolean considerJustFavorites) {

		// String whereClauseFormat = "%s like '%s' OR %s like '%s'";
		// String whereClause =String.format(whereClauseFormat, KEY_NAME, input,
		// KEY_SEGMENT, input) ;

		StringBuilder whereClause = new StringBuilder(KEY_NAME + " like '%" + input + "%' OR "
				+ KEY_SEGMENT + " like '%" + input + "%' OR " + KEY_DESCRIPTION + " like '%" + input + "%' ");
		whereClause.append(" AND " + WHERE_IS_FAVORITE);

		Cursor cursor = sqliteDb.query(true, DATABASE_TABLE, vitriniColumns,
				whereClause.toString(), null, null, null, null, null);

		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			return null;
		}

		return cursor;
	}
	
	public void setAsFavorite(Vitrini vitrini){
		String where = KEY_ID +  "='" + vitrini.getId().replace("'", "''") + "'";				
		ContentValues newVitriniValues = new ContentValues();		
		newVitriniValues.put(KEY_FAVORITE, 1);		
		sqliteDb.update(DATABASE_TABLE, newVitriniValues, where, null);		
	}
	
	public void removeFavorite(Vitrini vitrini){
		String where = KEY_ID +  "='" + vitrini.getId().replace("'", "''") + "'";				
		ContentValues newVitriniValues = new ContentValues();		
		newVitriniValues.put(KEY_FAVORITE, 0);		
		sqliteDb.update(DATABASE_TABLE, newVitriniValues, where, null);		
	}
	
	public List<Vitrini> findFavorites(String[] segmentsToFilter){
		List<Vitrini> vitrinis = new ArrayList<Vitrini>();
		String whereClause = WHERE_IS_FAVORITE;
		if(segmentsToFilter != null && segmentsToFilter.length > 0) {
			whereClause += " AND (" + buildSegmentsFilter(segmentsToFilter) + ")";
		}
			
		Cursor vitrinisCursor = sqliteDb.query(true, DATABASE_TABLE, vitriniColumns,
				whereClause, null, null, null, null, null);

		if(vitrinisCursor.moveToFirst()) {
			do {
				Vitrini vitrini = getVitriniByCursor(vitrinisCursor);
				vitrinis.add( vitrini );
				
			} while(vitrinisCursor.moveToNext());
		}
		
		return vitrinis;		
	}

	public Vitrini findVitriniByName(String vitriniName) {
		
		String whereClause = KEY_NAME + "=" + DatabaseUtils.sqlEscapeString(vitriniName);
		Cursor cursor = sqliteDb.query(true, DATABASE_TABLE, vitriniColumns,
				whereClause, null, null, null, null, null);

		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			return null;
		}

		Vitrini vitrini = getVitriniByCursor(cursor);
		return vitrini;
	}

	private Vitrini getVitriniByCursor(Cursor cursor) {
		String id = cursor.getString(ID_COLUMN);
		String name = cursor.getString(NAME_COLUMN);
		String logoPath = cursor.getString(LOGO_COLUMN);
		String imagePath = cursor.getString(IMAGE_COLUMN);
		String description = cursor.getString(DESCRIPTION_COLUMN);
		String segment = cursor.getString(SEGMENT_COLUMN);
		String site = cursor.getString(SITE_COLUMN);
		int favorite = cursor.getInt(FAVORITE_COLUMN);
		String city = cursor.getString(CITY_COLUMN);
		String estate = cursor.getString(ESTATE_COLUMN);
		Vitrini vitrini = new Vitrini(id, name, logoPath, imagePath,
				description, segment, site, favorite, city, estate);
		return vitrini;
	}
	
	public List<Vitrini> listVitrinis() {
		return listVitrinis(null);
	}
	
	public List<Vitrini> listVitrinis(String segmentsToFilter[]) {
		
		String whereClause = "";
		if(segmentsToFilter != null && segmentsToFilter.length > 0) {
			whereClause += " " + buildSegmentsFilter(segmentsToFilter);
		}
			
		Cursor vitrinisCursor = sqliteDb.query(true, DATABASE_TABLE, vitriniColumns,
				whereClause, null, null, null, null, null);
		
		List<Vitrini> vitrinis = new ArrayList<Vitrini>();
		if(vitrinisCursor.moveToFirst()) {
			do {
				Vitrini vitrini = getVitriniByCursor(vitrinisCursor);
				vitrinis.add( vitrini );
				
			} while(vitrinisCursor.moveToNext());
		}
		
		
		return vitrinis;
	}	
	
	
	public List<Vitrini> filterVitrinis(String segment, boolean considerJustFavorites) {
		
		Cursor vitrinisCursor = filterVitrinisBySegment(segment, considerJustFavorites );
		
		List<Vitrini> vitrinis = new ArrayList<Vitrini>();
		
		if(vitrinisCursor == null) {
			return vitrinis;
		}
		
		if(vitrinisCursor != null && vitrinisCursor.moveToFirst()) {
			do {
				Vitrini vitrini = getVitriniByCursor(vitrinisCursor);
				vitrinis.add( vitrini );
				
			} while(vitrinisCursor.moveToNext());
		}
		
		return vitrinis;
	}
	
	private String buildSegmentsFilter(String[] segments) {
		StringBuilder segmentFilterBuilder = new StringBuilder();
		for (int i = 0; i < segments.length; i++) {
			String segment = segments[i];
			segmentFilterBuilder.append(KEY_SEGMENT + "='" + segment + "'");
			if(i != segments.length - 1) {
				segmentFilterBuilder.append(" OR ");
			}			
		}
		return segmentFilterBuilder.toString();
	}

	public Cursor listFavoriteSegments() {		
		Cursor cursor = sqliteDb.query(true, DATABASE_TABLE, new String[]{KEY_SEGMENT},
				WHERE_IS_FAVORITE, null, null, null, null, null);

		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			return null;
		}		
		return cursor;
	}
	
	public Cursor listAllSegments() {		
		Cursor cursor = sqliteDb.query(true, DATABASE_TABLE, new String[]{KEY_SEGMENT},
				null, null, null, null, null, null);

		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			return null;
		}		
		return cursor;
	}
}
