package pt.ulisboa.tecnico.cmov.appServer;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.Iterator;

public class User {

	private String username;
	private String password;
	private Map<String,String> keyPairs;
	private Map<String, Message> messages;
	
	public User(String username , String password){
		this.username = username;
		this.password = password;
		keyPairs = new HashMap<String,String>();
		messages = new HashMap<String, Message>();
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
	
	public void addKeyPair(String key, String value){
		keyPairs.put(key, value);
	}
	
	public void removeKeyPair(String key){
		keyPairs.remove(key);
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
	
	public void addMessage(Message message){
		messages.put(message.getName(), message);
	}
	
	public void removeMessage(String name){
		messages.remove(name);
		}
	
	public Message getMessage(String name){
		return messages.get(name);
	}
	
	public String[] listMessages(){
		String[] out = new String[messages.size()];
		int i=0;
		for (Map.Entry<String, Message> entry: messages.entrySet()){
			out[i] = entry.getKey();
			i++;
		}
		return out;
	}
}
