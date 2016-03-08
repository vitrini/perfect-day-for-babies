package br.com.perfect_day_babies.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.Locale;

import android.content.Context;

public class StringUtils {
	
	/*
	 * Substitui os espaços por inderline(_), remove os acentos e converte a string para letras minúsculas
	 * @param stringToNormalize String a ser normalizada
	 */
	public static String normalize(String stringToNormalize){
		Locale locale = ApplicationContextProvider.getContext().getResources().getConfiguration().locale;
		String normalizedString = Normalizer.normalize(stringToNormalize, Normalizer.Form.NFD);
		normalizedString = normalizedString.replaceAll("[^\\p{ASCII}]", "").toLowerCase(locale).trim().replaceAll(" ", "_").replace("'", "");
		return normalizedString;
	}
	
	/**
	 * Realiza a conversão de um arquivo html na pasta assets, para uma string
	 * @param filePath Caminho do arquivo a partir da pasta assets
	 * @return String com o conteúdo do arquivo
	 */
	public static String htmlFileFromAssetsToString( String filePath, Context context )
	{
		String content = "";
		InputStream is;
		try {
			is = context.getAssets().open(filePath);
			int size = is.available();

			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			
			content = new String(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    return content;
	}

	/**
	 * Realiza a conversão de um arquivo html na pasta assets, para uma string
	 * @param filePath Caminho do arquivo a partir da pasta assets
	 * @return String com o conteúdo do arquivo
	 */
	public static String htmlFileFromAssetsToString2( String filePath, Context context )
	{
		String content = "";
	    try {
	        BufferedReader in = new BufferedReader(new InputStreamReader(context.getAssets().open(filePath)));
	        String str;
	        while ((str = in.readLine()) != null) {
	            content +=str;
	        }
	        in.close();
	    } catch (IOException e) {
	    }
	    
	    return content;
	}

}
