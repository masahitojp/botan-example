package com.github.masahitojp.botan.adapter;

import com.github.masahitojp.botan.Botan;
import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.message.BotanMessage;

public class MockAdapter implements BotanAdapter {
    private Botan botan;

    @Override
    public void run() throws BotanException {

    }

    @Override
    public void say(BotanMessage message) {

    }

    @Override
    public void initialize(Botan botan) {
        this.botan = botan;
    }

    @Override
    public void beforeShutdown() {

    }
}
