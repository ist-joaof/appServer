package pt.ulisboa.tecnico.cmov.appServer;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.Iterator;

public class User {

	private String username;
	private String password;
	private Map<String,String> keyPairs;
	
	public User(String username , String password){
		this.username = username;
		this.password = password;
		keyPairs = new HashMap<String,String>();
	}
	
	public void changePassword(String newPassword){
		password = newPassword;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void newKeyPair(String key, String value){
		keyPairs.put(key, value);
	}
	
	public boolean hasKey(String key){
		return keyPairs.containsKey(key);
	}
	
	public String getValue(String key){
		return keyPairs.get(key);
	}
	
	public String[] getAllKeys(){
		if(!keyPairs.isEmpty()){
			String[] out = new String[keyPairs.size()];
			int ptr = 0;
			for(Map.Entry<String, String> entry : keyPairs.entrySet()){
				out[ptr] = entry.getKey() + "=" + entry.getValue();
				ptr++;
			}
			return out;
		}else{
			return null;
		}
	}
	
	public void deleteAllKeys(){
		keyPairs.clear();
	}
}
