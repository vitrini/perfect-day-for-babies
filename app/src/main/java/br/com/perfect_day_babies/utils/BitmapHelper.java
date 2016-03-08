package br.com.perfect_day_babies.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import br.com.perfect_day_babies.Constants;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class BitmapHelper {
	
	public final static String IMAGES_SDCARD_PATH = "/images/";
	public final static String APP_SDCARD_PATH = "/vitrini_data/";
	public static HashMap<String, Bitmap> segmentImages;
	
	/*
	 * Estrutura que associa cada segmento a sua respectiva cor
	 */
	public static final HashMap<String, String> segmentColors;
    static
    {
    	
    	segmentColors = new HashMap<String, String>();
    	segmentColors.put("moda", "#EB42C3");
    	segmentColors.put("decoracao_de_festas", "#F16633");
    	segmentColors.put("alimento_e_bebida", "#FF0000");
    	segmentColors.put("entretenimento", "#40CC54");
    	segmentColors.put("moveis", "#754C24");
    	segmentColors.put("lembrancas", "#C8A700");
    	segmentColors.put("servicos_e_produtos", "#662D91");
    	segmentColors.put("blogueiras", "#0061A1");
    }

	public static void initializeSegmentImages( Context context )
	{
		String[] segmentNames;
		try {
			segmentNames = context.getAssets().list(Constants.SEGMENT_IMAGES_PATH);
			int numImages = segmentNames.length;
			segmentImages = new HashMap<String, Bitmap>(numImages);
			for (int i=0; i<numImages; i++){
				
				// Retira a extensão do nome da imagem, para servir como chave do hashmap
				String hashKey = segmentNames[i].replace(Constants.SEGMENT_IMAGES_EXTENSION, "");
				
				// Converte para letras minúsculas e remove espaços
				hashKey = StringUtils.normalize(hashKey);				
				
				segmentImages.put( hashKey, readBitmapFromAssets(Constants.SEGMENT_IMAGES_PATH + "/" + segmentNames[i], context));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("ERROR ", "It was not possible to load segments in assets folder: " + Constants.SEGMENT_IMAGES_PATH);
		}
	}
	
	public static boolean canReadSDCard() 
	{
		boolean mExternalStorageAvailable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) 
		{
			mExternalStorageAvailable = true;
			
			Log.i("isSdReadable", "External storage card is readable.");
		}
		else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) 
		{
			Log.i("isSdReadable", "External storage card is readable and writable.");
			mExternalStorageAvailable = true;
		} 
		else 
		{
			mExternalStorageAvailable = false;
		}

		return mExternalStorageAvailable;
	}
	
	public static boolean canWriteSDCard() 
	{
		boolean mExternalStorageAvailable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) 
		{
			mExternalStorageAvailable = true;			
			Log.i("isSdReadable", "External storage card is writable.");
		} 
		else 
		{
			mExternalStorageAvailable = false;
		}

		return mExternalStorageAvailable;
	}
	
	public static boolean saveBitmap(String vitriniId, String name, Bitmap image, Context context) 
	{
		if(canWriteSDCard())
		{
			return saveImageToExternalStorage(vitriniId, name, image, context);
		}
		else
		{
			return saveImageToInternalStorage(name, image, context);
		}		
	}
	
	public static boolean removeAllImages(Context context)
	{
		boolean returnValue = true;
		if(canWriteSDCard())
		{
			String sdCardAppImagespath = Environment.getExternalStorageDirectory().getAbsolutePath() + IMAGES_SDCARD_PATH + APP_SDCARD_PATH;
			File imagesDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + IMAGES_SDCARD_PATH);
			File sdcardAppImagesDir = new File(sdCardAppImagespath);
			if(sdcardAppImagesDir.exists())
			{
				File[] imageFiles = sdcardAppImagesDir.listFiles();
				for (File imageFile : imageFiles)
				{
					imageFile.delete();
				}
				imagesDir.delete();
				returnValue = sdcardAppImagesDir.delete();				
			}
			
		}		
			
		File internalImagesDir = context.getDir("vitrini", Context.MODE_PRIVATE);			
		File[] images = internalImagesDir.listFiles();
		for (File image : images)
		{
			image.delete();
		}		
		return internalImagesDir.delete() && returnValue;
	}
	
	public static boolean saveImageToInternalStorage(String name, Bitmap image, Context context) 
	{
		try 
		{			
			File vitriniDir = context.getDir("vitrini", Context.MODE_PRIVATE);
			File fileWithinMyDir = new File(vitriniDir, name);
			FileOutputStream fos = new FileOutputStream(fileWithinMyDir);	
			image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.close();
			return true;
		} 
		catch (Exception e)
		{
//			Log.e("saveToInternalStorage()", e.getMessage());
			return false;
		}
	}

	public static boolean saveImageToExternalStorage(String vitriniId, String name, Bitmap image, Context context) 
	{
		String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_SDCARD_PATH + "/" + vitriniId + IMAGES_SDCARD_PATH;
	
		try 
		{
			File dir = new File(fullPath);
			if (!dir.exists()) 
			{
				dir.mkdirs();
			}
			
			OutputStream fOut = null;
			File file = new File(fullPath, name);
			file.createNewFile();
			fOut = new FileOutputStream(file);
		
			image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
		
//			MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
		
			return true;		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}	
	}
	
	public static Bitmap readBitmap(String vitriniId, String filename, Context context) 
	{
		String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_SDCARD_PATH + "/" + vitriniId + IMAGES_SDCARD_PATH;
				
		return readBitmap(fullPath, context);
	}
	
	public static Bitmap readBitmap(String fullPath, Context context) 
	{

		Bitmap bitmap = null;
		
		try 
		{
			if (canReadSDCard() == true) 
			{
				bitmap = BitmapFactory.decodeFile(fullPath);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			Log.d("**Test Load from SDcard  -- ", "Failed:" + fullPath);			
		}
		
		if (bitmap == null)
		{
			try
			{
				String filename = fullPath.substring( fullPath.lastIndexOf("/"), (fullPath.lastIndexOf(".") - 1) );
				File filePath = new File(context.getDir("vitrini", Context.MODE_PRIVATE), filename);
				FileInputStream fi = new FileInputStream(filePath);
				bitmap = BitmapFactory.decodeStream(fi);
			} 
			catch (Exception ex)
			{
				Log.e("Error on internal storage", ex.getMessage());
			}
		}
		
		return bitmap;
	}

	/**
	 * Realiza a leitura de uma imagem que esteja dentro da pasta assets
	 * @param relativePath Path relativo a partir da pasta assets
	 * @param context contexto
	 * @return Bitmap da imagem
	 */
	public static Bitmap readBitmapFromAssets( String relativePath, Context context ) 
	{
		Bitmap bitmap = null;

		AssetManager manager = context.getAssets();	
		try {
			InputStream stream =  manager.open(relativePath);
			
			bitmap = BitmapFactory.decodeStream( stream );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("ERROR ", "It was not possible to open " + relativePath );
		}
				
		return bitmap;
	}
	
	/**
	 * Função que realiza o blur de um bitmap
	 * From: http://stackoverflow.com/questions/2067955/fast-bitmap-blur-for-android-sdk/2068981#2068981
	 * @param sentBitmap Bitmap de entrada
	 * @param radius raio
	 * @return Bitmap de saída
	 */
	public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
	
}
