package app.com.example.teddy.labb2_android;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameSaver {
    private static String FILENAME = "gameFile";
    NineMenMorrisRules game;
    Context context;


    public GameSaver(Context context){
        game = null;
        this.context = context;
    }

    public NineMenMorrisRules loadGame() {
        FileInputStream fis = null;
        ObjectInputStream objectStream = null;
        game = null;
        boolean exists = fileExists(fis);
        Log.i("retrieveStoredData() ", "exists: " +exists );
        if(exists) {
            game = getStoredData(fis, objectStream);
            Log.i("retrieveStoredData() ", "storedData is null: " +(game==null));
        }
        else {
            return null; //Or throw an exception
        }
        return game;
    }
    private boolean fileExists(FileInputStream fis) {
        try {
            fis = context.openFileInput(FILENAME);
            fis.close();
        } catch (FileNotFoundException e) {
            try {
                fis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }finally {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    private NineMenMorrisRules getStoredData(FileInputStream fis, ObjectInputStream objectStream) {
        NineMenMorrisRules savedState = null;
        try {
            fis = context.openFileInput(FILENAME);
            objectStream = new ObjectInputStream(fis);
            savedState = (NineMenMorrisRules) objectStream.readObject();
            if(savedState==null) Log.i("getStoredData(): ", "sd = null");
        } catch (Exception e) {
            e.printStackTrace();

                Log.i("getStoredData()", " exception");
            try {if(objectStream != null) {objectStream.close();}} catch(Exception e2){e2.printStackTrace();}
            try {if(fis != null) { fis.close();}} catch (Exception e2) {e2.printStackTrace();}
            return null;
        }
        return savedState;
    }

    public boolean saveGame(NineMenMorrisRules game) {
        this.game = game;
        boolean success = false;
        String path = context.getFilesDir().getPath().toString() + "/" + FILENAME;
        File storedFile = new File(path);

        try {
            storedFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectOutputStream os = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            os = new ObjectOutputStream(fos);

            os.writeObject(game);
            success = true;
        }
        catch (FileNotFoundException e) { e.printStackTrace();}
        catch (IOException e) { e.printStackTrace();}
        finally {
                try {if (os != null) os.close();} catch (Exception e) {}
                try {if (fos != null) fos.close();} catch (Exception e) {}
                return success;
        }
    }
}

