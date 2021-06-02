import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.security.auth.login.LoginException;
 
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class BunterBot extends ListenerAdapter {
    private static final String TOKEN = getToken();
    public static JDA jda;
    public static void main(String[] args) throws LoginException {
        jda = JDABuilder.createDefault(TOKEN).build();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.watching("Mr. Inbetween")); 
        jda.addEventListener(new BunterBot());
    }
    
    
    public static String getToken() {
        try {
            File tokenFile = new File("./src/token.txt");
            Scanner read = new Scanner(tokenFile);
            String token = read.nextLine();
            System.out.println(token);
            read.close();
            return token;
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
            return "";
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        System.out.println(msg.getContentRaw());
        if (msg.getContentRaw().equals("!ping"))
        {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis(); 
            channel.sendMessage("Pong!") /* => RestAction<Message> */
                   .queue(response /* => Message */ -> {
                       response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
                   });
        }
        else if (event.isFromType(ChannelType.PRIVATE))
        {
            System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
                                    event.getMessage().getContentDisplay());
        }
        else
        {
            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
                        event.getTextChannel().getName(), event.getMember().getEffectiveName(),
                        event.getMessage().getContentDisplay());
        }
    }
}

