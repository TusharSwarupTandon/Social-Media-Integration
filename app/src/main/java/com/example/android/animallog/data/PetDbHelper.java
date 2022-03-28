package com.example.android.animallog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.animallog.data.PetContract.PetEntry;


public class PetDbHelper extends SQLiteOpenHelper
{
    /** Name of the Database File **/
    private static final String DATABASE_NAME = "shelter.db";
    /** Database Version **/
    private static final int DATABASE_VERSION = 1;

    public PetDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE "+ PetEntry.TABLE_NAME + " ( "
                + PetEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetEntry.COLUMN_PET_TYPE+ " TEXT NOT NULL, "
                + PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                + PetEntry.COLUMN_PET_BREED + " TEXT, "
                + PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                + PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0 );";
        db.execSQL(SQL_CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(SQLiteDatabase db, String petType, String petName, String petBreed, int petGender, int petWeight)
    {
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_TYPE, petType);
        values.put(PetEntry.COLUMN_PET_NAME, petName);
        values.put(PetEntry.COLUMN_PET_BREED, petBreed);
        values.put(PetEntry.COLUMN_PET_GENDER, petGender);
        values.put(PetEntry.COLUMN_PET_WEIGHT, petWeight);

        // Insert a new row for Toto in the database, returning the ID of that new row.
        // The first argument for db.insert() is the pets table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Toto.
        long res = -1;
        try
        {
            res = db.insert(PetEntry.TABLE_NAME, null, values);
//            db.delete(PetEntry.TABLE_NAME,null,null);
        }
        catch (SQLException e)
        {
            res = -1;
        }
        return res;
    }
}
