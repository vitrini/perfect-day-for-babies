package br.com.perfect_day_babies;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;

public class ReadAssetFileUtils {

	public static String readFileFromAssets ( final Context context, final String fileName  ) throws IOException
	{
		AssetManager manager = context.getAssets();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];

		InputStream inputStream = null ;
		int len;

		try {
			inputStream = manager.open( fileName );
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			outputStream.close();
			inputStream.close();
			
			final String text = outputStream.toString();
			
			return text;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			throw e;
		}
		
	}
	
}
