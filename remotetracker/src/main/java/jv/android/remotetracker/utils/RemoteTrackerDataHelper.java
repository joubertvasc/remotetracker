package jv.android.remotetracker.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import jv.android.remotetracker.commands.CommandTable;
import jv.android.utils.Logs;

public class RemoteTrackerDataHelper implements Serializable {

   /**
	 * 
	 */
	private static final long serialVersionUID = -1026753041397619301L;
	
   private static final String DATABASE_NAME = "remotetracker.db";
   private static final int DATABASE_VERSION = 2;
   private static final String COMMANDS = "commands";

   private Context context;
   private SQLiteDatabase db;

   // Manipulação do Banco de dados
   public RemoteTrackerDataHelper(Context context) {
      this.context = context;
      OpenHelper openHelper = new OpenHelper(this.context);
      this.db = openHelper.getWritableDatabase();
	      
      // Cria tabelas
      try {
          this.db.query(COMMANDS, new String[] { "id" }, "1=2", null, null, null, "id");
      }
      catch (Exception e) {
    	  createCommands(db);
      }
   }
	   
   public SQLiteDatabase getDB() {
	   return db;
   }
		   
   public String getDBFile() {
	   return "/data/jv.android.remotetracker/databases/" + DATABASE_NAME;
   }

   private static void createCommands(SQLiteDatabase db) {
	   try {
		   db.execSQL("CREATE TABLE " + COMMANDS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, command TEXT, data TEXT, sender TEXT, receipient TEXT, result TEXT)");
	   } catch (Exception e) {
		   Logs.errorLog("RemoteTrackerDataHelper.createCommands error", e);		   
	   }
   } 

   // Manipula��o de grupos de coordenadas
   public long insertCommand(String command, String data, String from, String to, String result) {
	   ContentValues values = new ContentValues();    
	   values.put("command", command);    
	   values.put("data", data);    
	   values.put("sender", from);    
	   values.put("receipient", to);    
	   values.put("result", result);    
	   
	   return db.insert(COMMANDS, null, values);
   }

   public long insertCommand(CommandTable ct) {
	   ContentValues values = new ContentValues();    
	   values.put("command", ct.getCommand());    
	   values.put("data", ct.getData());    
	   values.put("sender", ct.getFrom());    
	   values.put("receipient", ct.getTo());    
	   values.put("result", ct.getResult());    
	   
	   return db.insert(COMMANDS, null, values);
   }
   
   public List<CommandTable> selectAllCommands() {
	      List<CommandTable> list = new ArrayList<CommandTable>();
	      Cursor cursor = this.db.query(COMMANDS, new String[] { "id, command, data, sender, receipient, result" }, null, null, null, null, "id");
	      
	      if (cursor.moveToFirst()) {
	         do {
	        	 CommandTable ct = new CommandTable();
	        	 ct.setId(cursor.getInt(0));
	        	 ct.setCommand(cursor.getString(1));
	        	 ct.setData(cursor.getString(2));
	        	 ct.setFrom(cursor.getString(3));
	        	 ct.setTo(cursor.getString(4));
	        	 ct.setResult(cursor.getString(5));
	        	 list.add(ct);
	         } while (cursor.moveToNext());
	      }
	      
	      if (cursor != null && !cursor.isClosed()) {
	         cursor.close();
	      }
	      
	      return list;
	   }
   
   
   private static class OpenHelper extends SQLiteOpenHelper {
      OpenHelper(Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }

      @Override
      public void onCreate(SQLiteDatabase db) {
//       db.execSQL("CREATE TABLE " + COMMANDS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, data TEXT)");
   		 createCommands(db);
      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          db.execSQL("DROP TABLE IF EXISTS " + COMMANDS);
    	  onCreate(db);
      }
   }
}
