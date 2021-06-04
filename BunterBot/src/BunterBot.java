import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.awt.Color;

import javax.security.auth.login.LoginException;

import commands.EnterTheGungeon;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
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
        jda.addEventListener(new EnterTheGungeon());
    } 
    
    /*
        retrieves discord bot token from private token text file 
    */
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
        }
        return "";
    }

    /*
        Simple activitation notification when BunterBot starts up
    */
    @Override
    public void onReady(ReadyEvent event) { 
        System.out.println("BunterBot is now live!");
    } 
}

