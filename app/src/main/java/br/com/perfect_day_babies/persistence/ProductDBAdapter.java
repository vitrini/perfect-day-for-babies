package br.com.perfect_day_babies.persistence;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import br.com.perfect_day_babies.entities.Product;
import br.com.perfect_day_babies.entities.Vitrini;


@SuppressWarnings("deprecation")
public class ProductDBAdapter
{
	public static final String DATABASE_TABLE = "products";	
	private SQLiteDatabase sqliteDb;
	
	public static final String KEY_ID = "_id";	
	public static final String KEY_NAME = "name";
	public static final String KEY_SMALL_IMAGE_PATH = "smallImagePath";
	public static final String KEY_LARGE_IMAGE_PATH = "largeImagePath";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_PRICE = "price";
	public static final String KEY_VITRINI_ID = "vitriniId";
	
	
	public static final String TABLE_CREATE = "create table " +
	DATABASE_TABLE + " (" + KEY_ID + " text primary key, " +
	KEY_NAME + " text not null, "  + 
	KEY_SMALL_IMAGE_PATH + " text," +
	KEY_LARGE_IMAGE_PATH + " text," + 
	KEY_DESCRIPTION + " text," + 
	KEY_PRICE + " numeric not null," +
	KEY_VITRINI_ID + " text," +
	" FOREIGN KEY (" + KEY_VITRINI_ID + ") REFERENCES " + 
			VitriniDBAdapter.DATABASE_TABLE + " ("+ VitriniDBAdapter.KEY_ID + "));";
	
	public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;
	public static final String INSERT_STATEMENT = 
		"INSERT INTO " + DATABASE_TABLE + " VALUES(?,?,?,?,?,?,?)"; 
	
	public static final int ID_COLUMN = 0;
	private static final int NAME_COLUMN = 1;
	private static final int SMALL_IMAGE_COLUMN = 2;
	private static final int LARGE_IMAGE_COLUMN = 3;
	private static final int DESCRIPTION_COLUMN = 4;
	private static final int PRICE_COLUMN = 5;
	private static final int VITRINI_ID_COLUMN = 6;

	private DatabaseManager dbManager;
	private InsertHelper insertHelper;
	
	public ProductDBAdapter(DatabaseManager dbManager)
	{
		super();
		this.sqliteDb = dbManager.getDB();
		this.dbManager = dbManager;
		insertHelper = new InsertHelper(sqliteDb, DATABASE_TABLE);
	}
	
	public SQLiteStatement createInsertStatement()
	{
		return sqliteDb.compileStatement(INSERT_STATEMENT);
	}
	
	public long insertProduct(Product product)
	{
		ContentValues newMenuItemValues = new ContentValues();
		newMenuItemValues.put(KEY_ID, product.getId());
		newMenuItemValues.put(KEY_NAME, product.getName());		
		newMenuItemValues.put(KEY_SMALL_IMAGE_PATH, product.getLightPhotoPath());
		newMenuItemValues.put(KEY_LARGE_IMAGE_PATH, product.getHeavyPhotoPath());
		newMenuItemValues.put(KEY_DESCRIPTION, product.getDescription());
		newMenuItemValues.put(KEY_PRICE, product.getPrice().doubleValue());
		newMenuItemValues.put(KEY_VITRINI_ID, product.getVitrini().getId());
		return sqliteDb.insert(DATABASE_TABLE, null, newMenuItemValues);
	}
	
	public long insertProductWithHelper(Product product)
	{

		insertHelper.prepareForInsert();
		insertHelper.bind(1, product.getId());
		insertHelper.bind(2, product.getName());
		insertHelper.bind(3, product.getLightPhotoPath());
		insertHelper.bind(4, product.getHeavyPhotoPath());
		insertHelper.bind(5, product.getDescription());
		insertHelper.bind(6, product.getPrice().doubleValue());
		insertHelper.bind(7, product.getVitrini().getId());
		
		return insertHelper.execute();
	}
	
	public long insertProductWithPreparedStatement(Product product)
	{
		
		SQLiteStatement insertStmt = sqliteDb.compileStatement(INSERT_STATEMENT);
		insertStmt.bindString(1, product.getId());
		insertStmt.bindString(2, product.getName());
		insertStmt.bindString(3, product.getLightPhotoPath());
		insertStmt.bindString(4, product.getHeavyPhotoPath());
		insertStmt.bindString(5, product.getDescription());		
		insertStmt.bindDouble(6, product.getPrice().doubleValue());		
		insertStmt.bindString(7, product.getVitrini().getId());	
		return insertStmt.executeInsert();
	}
	
	public boolean removeProduct(String productId)
	{
		return sqliteDb.delete(DATABASE_TABLE, KEY_ID + "='" + productId + "'", null) > 0;
	}
	
	public Cursor getAllProductsCursor()
	{
		String[] menuItemColumns = new String[] {KEY_ID, KEY_NAME, KEY_SMALL_IMAGE_PATH, 
				KEY_LARGE_IMAGE_PATH, KEY_DESCRIPTION, KEY_PRICE, KEY_VITRINI_ID};

		return sqliteDb.query(DATABASE_TABLE, menuItemColumns, null, null, null, null, null);
	}
	
	public Product findProduct(String productId)
	{
		String[] categoryColumns = new String[] {KEY_ID, KEY_NAME, KEY_SMALL_IMAGE_PATH, 
				KEY_LARGE_IMAGE_PATH, KEY_DESCRIPTION, KEY_PRICE, KEY_VITRINI_ID};
		
		String whereClause = KEY_ID + "='" + productId + "'";
		Cursor cursor = sqliteDb.query(true, DATABASE_TABLE, categoryColumns, whereClause, 
				null, null, null, null, null);
		
		if((cursor.getCount() == 0) || !cursor.moveToFirst())
		{
			return null;
		}
		
		String id = cursor.getString(ID_COLUMN);
		String name = cursor.getString(NAME_COLUMN);
		String lightPhotoPath = cursor.getString(SMALL_IMAGE_COLUMN);
		String heavyPhotoPath = cursor.getString(LARGE_IMAGE_COLUMN);
		String description = cursor.getString(DESCRIPTION_COLUMN);
		BigDecimal price = new BigDecimal(cursor.getDouble(PRICE_COLUMN));
		String vitriniId = cursor.getString(VITRINI_ID_COLUMN);
		VitriniDBAdapter vitriniAdapter = dbManager.getVitriniAdapter();
		Vitrini vitrini = vitriniAdapter.findVitrini(vitriniId);
		Product product = new Product(id, name, lightPhotoPath, heavyPhotoPath, price, description);		
		product.setVitrini(vitrini);
		return product;
	}

	public List<Product> findByVitrini(Vitrini vitrini)
	{
		String[] categoryColumns = new String[] {KEY_ID, KEY_NAME, KEY_SMALL_IMAGE_PATH,
				KEY_LARGE_IMAGE_PATH, KEY_DESCRIPTION, KEY_PRICE, KEY_VITRINI_ID};
		String whereClause = KEY_VITRINI_ID + "='" + vitrini.getId() + "'";
		Cursor cursor = sqliteDb.query(DATABASE_TABLE, categoryColumns, whereClause, null, null, null, null);
		List<Product> productList = new ArrayList<Product>();
		if(cursor.moveToFirst())
		{
			do
			{
				String id = cursor.getString(ID_COLUMN);
				String name = cursor.getString(NAME_COLUMN);
				String lightPhotoPath = cursor.getString(SMALL_IMAGE_COLUMN);
				String heavyPhotoPath = cursor.getString(LARGE_IMAGE_COLUMN);
				String description = cursor.getString(DESCRIPTION_COLUMN);
				BigDecimal price = new BigDecimal(cursor.getDouble(PRICE_COLUMN));				
				Product product = new Product(id, name, lightPhotoPath, heavyPhotoPath, price, description);
				
				product.setVitrini(vitrini);
				productList.add(product);
				
			} while(cursor.moveToNext());
		}
		return productList;
	}

}
