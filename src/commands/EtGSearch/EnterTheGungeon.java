package commands.EtGSearch;
 
import net.dv8tion.jda.api.EmbedBuilder; 
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.Color; 
 
import java.io.IOException; 

import commands.EtGSearch.GungeonItemParser.ParseItem;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EnterTheGungeon extends ListenerAdapter { 
    OkHttpClient client = new OkHttpClient(); 
    private static final String etgURL = "https://enterthegungeon.gamepedia.com/api.php?action=parse&prop=wikitext&format=json&page=";
    public void onGuildMessageReceived(GuildMessageReceivedEvent gevent) {
        String[] args = gevent.getMessage().getContentRaw().split(" "); 
        if(args[0].equalsIgnoreCase("!etg")) {
            //invalid command call
            if(args.length == 1) {  
                gevent.getChannel().sendMessage(getErrorEmbed().build()).queue();
                return;
            }   
            
            //read in the user item input
            String searchItem = "";
            for(int i = 1; i < args.length; i++) { 
                searchItem = searchItem + args[i].substring(0,1).toUpperCase() + args[i].substring(1);
                if(i != args.length - 1) searchItem += "_";
            } 
             
            //produces json from Enter the Gungeon URL with searchItem using OkHttp library
            String json = "";
            String finalPath = etgURL + searchItem;
            try { 
                json  = this.run(finalPath);  
            } catch(IOException e) {
                //Error for invalid item search
                gevent.getChannel().sendMessage(getErrorEmbed().build()).queue();
                return;
            }   

            //parses through json and puts all attributes into GungeonItemParser class using GSON library
            GungeonItemParser parser = new GungeonItemParser(formatJson(json));   

            ParseItem gitem = parser.getMap().get("parse");
            
            String infoBox = this.getInfoBox(gitem);
            String[] attributes = infoBox.split("\\n\\|"); 

            //produces new embed
            EmbedBuilder embed = this.getEmbed(gitem, finalPath, searchItem, attributes);

            //retrieves channel and sends out embed to the channel
            MessageChannel channel = gevent.getChannel();
            channel.sendMessage(embed.build()).queue();
        }  
    }
    /*
        creates small embed for errors found when failing to correctly utilize the enter the gungeon command
    */
    private EmbedBuilder getErrorEmbed() {
        EmbedBuilder errorEmbed = new EmbedBuilder();
        errorEmbed.setColor(Color.RED);
        errorEmbed.setTitle("\u2757 Error \u2757"); 
        errorEmbed.setDescription("Invalid parameters. \nFor more clarification please see !help for addition commands");
        return errorEmbed;
    }
    /*
        parses through the GungeonItemParser and retrieves info relkating to the infobox in the json 
    */
    private String getInfoBox(ParseItem item) {
        String content = item.getWikiText().getContent();
        int start = content.indexOf("{{") + 2;
        int end = content.indexOf("}}", content.indexOf("desc")); 
        return content.substring(start, end);
    }

    /*
        retrieves an EmbedBuilder object from given information relating to the input item data found in the etg wiki
    */
    private EmbedBuilder getEmbed(ParseItem item, String itemURL, String inputString, String[] infoBoxStats) {
        EmbedBuilder embed = new EmbedBuilder();
            //create hyperlink for title
            embed.setTitle(item.getTitle(), itemURL);

            embed.setColor(new Color(0xff3d9f)); 

            //create subsections for each stat 
            for(int i = 1; i < infoBoxStats.length; i++) { 
                String[] partition = infoBoxStats[i].split(" = "); 
                String subtitle = partition[0].replace(" ", "");
                String text = partition[1];
                //produces a description of the item instead of a subsection
                if(subtitle.equals("desc")) {
                    embed.setDescription(text);
                    continue;
                }
                embed.addField(subtitle, text, true); 
            }  
            //if available, provides an image from the wiki gallery for the item
            embed.setImage("https://enterthegungeon.fandom.com/api.php?action=imageserving&prop=text&format=json&wisTitle="+inputString);

            return embed;
    }

    /*
        Prepares json string for utilization in GSON library
    */
    private String formatJson(String json) {
        int indexOf = json.indexOf("*");
        return json.substring(0, indexOf) + "content" + json.substring(indexOf + 1);  
    }

    private String run(String url) throws IOException {
        Request request = new Request.Builder()
            .url(url)
            .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } 
    } 
}
