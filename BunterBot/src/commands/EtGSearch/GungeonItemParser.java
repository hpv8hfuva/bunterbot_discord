package commands.EtGSearch;

import java.lang.reflect.Type;
import java.util.HashMap; 
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GungeonItemParser { 
     
    private HashMap<String, ParseItem> map;

    public class ParseItem {
        private String title;
        private int pageid;
        private WikiText wikitext;
 
        public String getTitle() {
            return this.title;
        }

        public int getPageId() {
            return this.pageid;
        }

        public WikiText getWikiText() {
            return this.wikitext;
        }

        @Override 
        public String toString() {
            String result = "";
            result += "{title : " + this.title + "}\n";
            result += "{pageid : " + this.pageid + "}\n";
            result += "{wikitext : " + this.wikitext + "}\n";
            return result;
        }
    }

    public class WikiText {
        private String content;

        public String getContent() {
            return this.content;
        }

        @Override 
        public String toString() {
            return this.content;
        } 
    } 

    public GungeonItemParser(String json) {
        Gson gson = new Gson();
        Type parseItemType = new TypeToken<HashMap<String, ParseItem>>(){}.getType();
        this.setMap(gson.fromJson(json, parseItemType));
    }

    public HashMap<String, ParseItem> getMap() {
        return this.map;
    }

    public void setMap(HashMap<String, ParseItem> nmap) {
        this.map = nmap; 
    }

    @Override
    public String toString() {
        String result = "";
        for(Map.Entry<String, ParseItem> item: this.map.entrySet()) {
            result = result + "{" + item.getKey() + ": " + item.getValue().toString() + "} ";
        }
        return result;
    }
}
 