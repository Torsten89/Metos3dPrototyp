package masterproject.studentAtUniversityKiel.knauf.torsten.metos3dPrototyp;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Metos3dOptionFileParser {
	private final String PATH_TO_OPTION_FILE;
	private final int INDENTATION_OF_VALUES = 53;
	private Map<String, String> entries;

	public Metos3dOptionFileParser(String pathToOptionFile) throws IOException {
		this.PATH_TO_OPTION_FILE = pathToOptionFile;
		this.entries = this.parseOptionFile();
	}

	private Map<String, String> parseOptionFile() throws IOException {
		HashMap<String, String> result = new LinkedHashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader(this.PATH_TO_OPTION_FILE));
		String line = null;

		while ((line = br.readLine()) != null) {
			if (line.startsWith("-")) {
				String[] l = line.split("\\s+");
				if (l.length < 2)
					result.put(l[0], "");
				else
					result.put(l[0], l[1]);
			}
		}

		br.close();
		return result;
	}

    public String getKey(String line) {
        return line.split("\\s+")[0];
    }

    public String getValue(String line) {
        return line.split("\\s+")[1];
    }

	public void editEntry(String key, String value) {
		this.entries.put(key, value);
	}

	public Map<String, String> getEntries() {
		return this.entries;
	}

	public void writeBackOpitionFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(this.PATH_TO_OPTION_FILE));
		for (Map.Entry<String, String> e : this.entries.entrySet()) {
			String key = e.getKey();
			writer.write(key + this.indent(this.INDENTATION_OF_VALUES - key.length()) + e.getValue());
			writer.newLine();
		}

		writer.close();
	}

	private String indent(int times) {
		StringBuilder result = new StringBuilder();
		for(int i=0; i<times; i++) {
			result.append(" ");
		}

		return result.toString();
	}

}
