// THIS IS GENERATED CODE, WHEN YOU COMPLETE YOUR HAND EDITS REMOVE THIS LINE
package edu.vanderbilt.isis.ammo.all_types.provider;

import android.content.Context;

/**
 * Implements and overrides those elements not completed
 * 
 * @author <yourself\>
 *    
 */
public class AllTypesProvider extends AllTypesProviderBase {
   public final static String VERSION = "1.7.0";
   protected class AllTypesDatabaseHelper extends AllTypesProviderBase.AllTypesDatabaseHelper {
      protected AllTypesDatabaseHelper(Context context) { 
         super(context, AllTypesSchema.DATABASE_NAME, AllTypesSchema.DATABASE_VERSION);
      }

/**
   @Override
   protected void preloadTables(SQLiteDatabase db) {
      db.execSQL("INSERT INTO \""+Tables.*_TBL+"\" (" + *Schema.*+") "+"VALUES ('" + *TableSchema.* + "');");
   }
*/
   }

   // ===========================================================
   // Content Provider Overrides
   // ===========================================================

   @Override
   public synchronized boolean onCreate() {
       super.onCreate();
       this.openHelper = new AllTypesDatabaseHelper(getContext());

       return true;
   }

   @Override
   protected synchronized boolean createDatabaseHelper() {
      return false;
   }

}