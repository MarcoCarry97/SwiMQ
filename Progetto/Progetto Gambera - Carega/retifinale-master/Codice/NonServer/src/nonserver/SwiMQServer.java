package nonserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

public class SwiMQServer {

	public static synchronized String signUp(JSONObject params) {
		String email = (String) params.getString("email");
		String pwd = (String) params.getString("password");
		String type = (String) params.getString("type");
		boolean found = false;
		try {

			FileReader reader = new FileReader("root/users.txt");
			BufferedReader buffReader = new BufferedReader(reader);
			String line = buffReader.readLine();
			while (!found && line != null) // controllo se email già presente
			{
				String[] parts = line.split(" ");
				if (email.equals(parts[0]))
					found = true; // se è così non scrivo nulla nel file
				else
					line = buffReader.readLine();
			}
			buffReader.close();
			if (!found) {
				FileWriter writer = new FileWriter("root/users.txt", true);
				BufferedWriter buffWriter = new BufferedWriter(writer);
				PrintWriter printer = new PrintWriter(buffWriter);
				String res = String.format("%s %s %s\n", email, pwd, type);
				printer.append(res);
				try {
					File dir = new File("root/" + type.toLowerCase() + "/" + email);
					boolean success = dir.mkdir();
					if (!success)
						System.out.println("Cartella creata");
				} catch (Exception e) {
					System.err.println("Errore:" + e.getMessage());
				}
				printer.close();
			} // fine if
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		JSONObject result = new JSONObject();
		result.put("find", found);
		return result.toString();
	}

	public static synchronized String logIn(JSONObject params) {
		String email = (String) params.get("email");
		String pwd = (String) params.get("password");
		String type = new String();
		boolean found = false;
		boolean correct = false;
		try {

			FileReader reader = new FileReader("root/users.txt");
			BufferedReader buffReader = new BufferedReader(reader);
			String line = buffReader.readLine();
			while (!found && line != null) // controllo se email già presente
			{
				String[] parts = line.split(" ");
				if (email.equals(parts[0])) {
					found = true;
					if (pwd.equals(parts[1])) {
						correct = true;
						type = parts[2];

					}
				} else
					line = buffReader.readLine();
			}
			buffReader.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		JSONObject result = new JSONObject();
		result.put("find", found);
		result.put("correct", correct);
		result.put("type", type);
		return result.toString();
	}

	public static synchronized String create(JSONObject params) {
		Date date = (Date) new Date(params.getLong("date"));
		String email = (String) params.getString("email");
		boolean create = false;// controlla che non ci siano due esercizi nella stessa data
		File check = new File("root/coach/" + email + "/" + date.toString() + ".json");
		if (!check.exists()) {
			try (FileWriter file = new FileWriter("root/coach/" + email + "/" + date.toString() + ".json")) {
				file.write(params.toString());
				file.close();
				create = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		JSONObject result = new JSONObject();
		result.put("success", create);
		return result.toString();
	}

	public static synchronized String newTraining() {
		JSONArray jsArrayTraining = new JSONArray();
		File folder = new File("root/coach");
		long millis = System.currentTimeMillis();
		Date currentDate = new Date(millis);
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				for (final File training : fileEntry.listFiles()) {
					try {
						String dateText = training.getName();
						dateText = dateText.substring(0, dateText.length() - 5);
						long mil2 = new SimpleDateFormat("yyyy-MM-dd").parse(dateText).getTime();
						Date date2 = new Date(mil2);
						System.out.println(date2.toString());
						if (currentDate.toString().equals(date2.toString()) || !currentDate.after(date2)) {
							FileReader reader = new FileReader(
									"root/coach/" + fileEntry.getName() + "/" + training.getName());
							BufferedReader buffReader = new BufferedReader(reader);
							String line = buffReader.readLine();
							JSONObject json = new JSONObject(line);
							jsArrayTraining.put(json);
							buffReader.close();
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}
			} // fine if
		} // fine for
		JSONObject json = new JSONObject();
		json.put("allenamenti", jsArrayTraining);
		return jsArrayTraining.toString();
	}

	public static synchronized String oldTraining(JSONObject params) {
		String email = (String) params.get("email");
		JSONArray jsArrayTraining = new JSONArray();
		File folder = new File("root/swimmer/" + email);
		for (final File fileEntry : folder.listFiles()) {
			try {
				FileReader reader = new FileReader("root/swimmer/" + email + "/" + fileEntry.getName());
				BufferedReader buffReader = new BufferedReader(reader);
				String line = buffReader.readLine();
				JSONObject json = new JSONObject(line);
				jsArrayTraining.put(json);
				buffReader.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		} // fine for
		return jsArrayTraining.toString();
	}

	public static String analyze(JSONObject params)
	{
		String email = (String) params.get("email");
		File folder=new File("root/swimmer/"+email);
		File last=null;
		boolean first=true;
		try
		{
			for(File training:folder.listFiles())
			{
				if(first)
				{
					last=training;
					first=false;
				}
				else
				{
					String lastName=last.getName();
					String currentName=training.getName();
					long lastMillis=new SimpleDateFormat("yyyy-MM-dd").parse(lastName.substring(0,lastName.length()-5)).getTime();
					long currentMillis=new SimpleDateFormat("yyyy-MM-dd").parse(currentName.substring(0,lastName.length()-5)).getTime();
					if(lastMillis<currentMillis) last=training;
				}
			}
			FileReader reader = new FileReader("root/swimmer/" + email + "/" + last.getName());
			BufferedReader buffReader = new BufferedReader(reader);
			String line = buffReader.readLine();
			JSONObject json = new JSONObject(line);
			return json.toString();
		}
		catch(Exception e)
		{
			return null;
		}
	}

}
