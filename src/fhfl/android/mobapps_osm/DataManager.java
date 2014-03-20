package fhfl.android.mobapps_osm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class DataManager {
	
	SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy_kk-mm-ss");
	
	//Konstanten
	private String FOLDER_NAME = "/GPS_LOGS";
	
	/* Konstruktor */
	public DataManager(){
		createDirOnSD(FOLDER_NAME);
	}
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}

	// Erzeugt das Verzeichniss auf der SD-Karte
	private boolean createDirOnSD(String path){
		File Folder = new File(Environment.getExternalStorageDirectory() + path);
		
		if(Folder.exists())
		{
			return false;
		}
		else
		{
			if(Folder.mkdir())
				return true;
			else
				return false;
		}
	}
	
	// Erzeugt eine neue Datei und gibt diese zurück
	public File createNewGPSLogFile(){
		File file = new File(Environment.getExternalStorageDirectory() + "/GPS_LOGS/" , s.format(new Date()) + ".gpx" );
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	// Erzeugt eine neue Datei und gibt diese zurück
	public File createSettingsFile(){
		File file = new File(Environment.getExternalStorageDirectory() + "/GPS_LOGS/" , "Settings.xml" );
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
	
	// Gibt eine Liste der Dateien zurück
	public ArrayList<String> getAllFiles(){
		ArrayList<String> Files = new ArrayList<String>();
		
		// get all the files from a directory
		File[] fList = new File(Environment.getExternalStorageDirectory() + FOLDER_NAME).listFiles();
		for (File f : fList) {			
			if(f.isFile() && f.getName().endsWith(".gpx"))
			{
				Files.add(f.getName());
			}
		}
		return Files;
	}

	// Löscht die Datei
	public boolean deleteGPSLogFile(String FileName){
		File file = new File(Environment.getExternalStorageDirectory() + FOLDER_NAME + "/" + FileName);
		return file.delete();
	}
	
	// Schreibt den String in die Angegebene Datei
	public boolean writeToGPSLogFile(String FileName,String Value){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(Environment.getExternalStorageDirectory() + FOLDER_NAME + "/" + FileName, true));
			
			bw.write(Value);
			bw.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	// Überschreibt die Settings.xml
	public boolean rewriteSettingsFile(String Value){
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(Environment.getExternalStorageDirectory() + FOLDER_NAME + "/Settings.xml", false));
				
				bw.write(Value);
				bw.close();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	
	// Gibt den Inhalt der Datei als String zurück
	public String readGPSLogFile(String FileName){
   
	    BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(Environment.getExternalStorageDirectory() + FOLDER_NAME + "/" + FileName.trim() ))));
			StringBuilder sb = new StringBuilder();
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		      sb.append(line).append("\n");
		    }
		    reader.close();
		    return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "ERROR_readGPSLogFile";
		}
	    
	}

	// Gibt den Inhalt der Datei als String zurück
		public String readSettingsFile(){
	   
		    BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(Environment.getExternalStorageDirectory() + FOLDER_NAME + "/Settings.xml"))));
				StringBuilder sb = new StringBuilder();
			    String line = null;
			    while ((line = reader.readLine()) != null) {
			      sb.append(line).append("\n");
			    }
			    reader.close();
			    return sb.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "ERROR_readStettingsFile";
			}
		    
		}
}
