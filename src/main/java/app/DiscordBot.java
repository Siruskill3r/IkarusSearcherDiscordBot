package app;

import app.model.app.VehicleData;
import app.model.utas.Location;
import app.service.UtasDataService;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DiscordBot {

    private static UtasDataService utas;

    static {
        List<Location> locations = new ArrayList<>();
        List<Double> latspans = new ArrayList<>();
        List<Double> lonspans = new ArrayList<>();
        locations.add(new Location(47.62815937109606,17.053415320775617));
        locations.add(new Location(47.6058025338198,18.625496688700164));
        locations.add(new Location(47.61474641584916,20.217477820775617));
        locations.add(new Location(47.98456923069974,21.285431830209586));
        locations.add(new Location(47.9934481492923,22.28705329247372));
        locations.add(new Location(47.215262683301745,21.006835132096363));
        locations.add(new Location(46.66272787667776,21.09970069813413));
        locations.add(new Location(46.519163291696465,19.542130629911533));
        locations.add(new Location(46.34415212007624, 18.017188019700576));
        locations.add(new Location(46.478958833380226,17.001980800449243));
        locations.add(new Location(47.219550705078305,16.73877892879149));
        locations.add(new Location(46.04222850237878,18.227749517026787));
        locations.add(new Location(47.4539776454333,21.70201422290914));
        locations.add(new Location(47.10826011631514,18.751805892958025));
        locations.add(new Location(46.98461809354887,17.818626169455708));
        locations.add(new Location(46.90940988500315,19.17404786964612));

        for(Location ignored : locations) {
            latspans.add(0.8);
            lonspans.add(1.0);
        }
        utas = new UtasDataService(locations,latspans,lonspans,vehicle -> vehicle.getModel().trim().toLowerCase().contains("ikarus"));
        Thread daemon = new Thread(utas);
        daemon.setDaemon(true);
        daemon.start();
    }


    public static void main(String[] args) throws IOException {
        // Replace with your bot token
        String token = "TOKEN";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        GatewayDiscordClient client = DiscordClientBuilder.create(token)
                .build()
                .login()
                .block();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    final User self = event.getSelf();
                    System.out.printf("Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
                    System.out.println("Debug:");
                    List<VehicleData> data;
                    while((data = utas.getResult(elem -> true)) == null) {}
                    for (int i = 0; i < data.size(); i++) {
                        System.out.println((i+1) + ". " + data.get(i).toString());
                    }
                });

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .subscribe(event -> {
                    Message message = event.getMessage();
                    String content = message.getContent();
                    System.out.println("Received message: " + content); // Debug statement

                    if (content.equalsIgnoreCase(".ping")) {
                        System.out.println("Ping command received"); // Debug statement
                        message.getChannel().flatMap(channel -> channel.createMessage("The bot is alive and well! \n Current time: " + LocalDateTime.now().format(formatter))).subscribe();
                    } else if(content.startsWith(".all")) {
                        StringBuilder messageString = new StringBuilder();
                        String[] parts = content.split(" ");
                        if(parts[0].equals(".allik") || parts[0].equals(".alleag")) {
                            System.out.println(parts[0] + " command received");
                            List<VehicleData> data = utas.getResult((parts[0].equals(".allik")) ? elem -> !(elem.getModel().contains("E") || elem.getModel().contains("EAG")) : elem -> (elem.getModel().contains("EAG")));
                            int page = (parts.length > 1) ? Integer.parseInt(parts[1]) : 1;
                            int pagesNum = data.size() / 10 + (data.size() % 10 == 0 ? 0 : 1);
                            if(page > pagesNum || page < 1) {
                                messageString.append("No such page.\n");
                                message.getChannel().flatMap(channel -> channel.createMessage(messageString.toString())).subscribe();
                            } else {
                                messageString.append("Last updated: " + utas.getLastUpdated().format(formatter) + "\n" + "Page " + page + " out of " + pagesNum + " pages.\n");
                                int cnt = 1;
                                for (int i = 0; i < data.size(); i++) {
                                    if (i < (page - 1) * 10 - 1) continue;
                                    if (cnt == 10) break;
                                    cnt++;
                                    messageString.append((i+1) + ". " + data.get(i).toString() + "\n");
                                }
                                message.getChannel().flatMap(channel -> channel.createMessage(messageString.toString())).subscribe();
                            }
                        }

                    }
                });
        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
