package com.github.masahitojp.botan;

import com.github.masahitojp.botan.adapter.SlackRTMAdapter;
import com.github.masahitojp.botan.brain.mapdb.MapDBBrain;
import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.utils.BotanUtils;

import java.io.IOException;

public class SlackBot {
    static public void main(String[] Args) {

        final String apiToken = BotanUtils.envToOpt("SLACK_API_TOKEN").orElse("");
        Botan botan = null;

        try {
            botan = new Botan.BotanBuilder()
                    .setAdapter(new SlackRTMAdapter(apiToken))
                    .setBrain(new MapDBBrain())
                    .build();

            botan.start();
        } catch (final BotanException | IOException ex) {
            ex.printStackTrace();
        } finally {
            if(botan != null)botan.stop();
        }

    }
}
