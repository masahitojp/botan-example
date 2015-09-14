package com.github.masahitojp.botan;

import com.github.masahitojp.botan.adapter.SlackAdapter;
import com.github.masahitojp.botan.brain.RedisBrain;
import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.utils.BotanUtils;

public class SlackBot {


    static public void main(String[] Args) {

        final String team = BotanUtils.envToOpt("SLACK_TEAM").orElse("");
        final String user = BotanUtils.envToOpt("SLACK_USERNAME").orElse("");
        final String pswd = BotanUtils.envToOpt("SLACK_PASSWORD").orElse("");
        final String channel = BotanUtils.envToOpt("SLACK_ROOM").orElse("");
        final String redisHost = BotanUtils.envToOpt("REDIS_HOST").orElse("localhost");
        final String redisPort = BotanUtils.envToOpt("REDIS_PORT").orElse("");
        int port;
        try {
            port = Integer.parseInt(redisPort);
        } catch (final NumberFormatException ex) {
            port = 6379;
        }
        final Botan botan = new Botan.BotanBuilder(new SlackAdapter(team, user, pswd, channel))
                .setBrain(new RedisBrain(redisHost, port))
                .build();

        try {
            botan.start();
        } catch (final BotanException ex) {
            ex.printStackTrace();
        }

    }
}
