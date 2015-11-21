package com.github.masahitojp.botan;

import com.github.masahitojp.botan.adapter.SlackRTMAdapter;
import com.github.masahitojp.botan.brain.redis.RedisBrain;
import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.utils.BotanUtils;

public class SlackBot {
    static public void main(String[] Args) {

        final String apiToken = BotanUtils.envToOpt("SLACK_API_TOKEN").orElse("");
        Botan botan;

        try {
            botan = new Botan.BotanBuilder()
                    .setAdapter(new SlackRTMAdapter(apiToken))
                    .setBrain(new RedisBrain())
                    .build();

            botan.start();
        } catch (final BotanException ex) {
            ex.printStackTrace();
        }

    }
}
