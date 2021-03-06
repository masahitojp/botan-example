package com.github.masahitojp.botan.handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;
import com.github.masahitojp.nineteen.Reviewer;
import com.github.masahitojp.nineteen.Song;
import com.github.masahitojp.nineteen.Token;

import java.util.Optional;
import java.util.stream.Collectors;


@SuppressWarnings("unused")
public class HaikuMessageHandlers implements BotanMessageHandlers {

    public final String toSenryuString(final Optional<Song> songOpt) {
        return songOpt.map(song -> song.getPhrases().stream()
                .map(list -> list.stream().map(Token::toString).collect(Collectors.joining()))
                .collect(Collectors.joining(" "))).orElse("");
    }

    @Override
    public void register(final Robot robot) {
        robot.hear(
                "(?<body>.+)",
                "koko de ikku",
                message -> {
                        final Reviewer reviewer = new Reviewer();
                        final Optional<Song> phrases = reviewer.find(message.getBody());
                        if (phrases.isPresent()) {
                            final String result = "ここで一句: " + toSenryuString(phrases);
                            message.reply(result);
                        }
                });
    }


}
