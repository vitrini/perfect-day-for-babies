package br.com.perfect_day_babies.utils;

import java.util.LinkedList;
import java.util.List;

import android.app.Application;
import android.content.Context;

public class ApplicationContextProvider extends Application {

   private static Context sContext;
   public List<Integer> tabCallStack = new LinkedList<Integer>();

   @Override
   public void onCreate() {
       super.onCreate();

       sContext = getApplicationContext();

   }

   /**
    * Returns the application context
    *
    * @return application context
    */
   public static Context getContext() {
       return sContext;
   }   
   
}
