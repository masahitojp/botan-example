package com.github.masahitojp.botan.handlers.keepalive;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

@SuppressWarnings("unused")
public class KeepAliveHandlers implements BotanMessageHandlers {
    private static Logger logger = LoggerFactory.getLogger(KeepAliveHandlers.class);
    // TODO add feature: robot.route.get / post method
    private App app;

    @Override
    public final void initialize(final Robot robot) {
        try {
            final int port = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("5353"));
            app = new App(port);
            app.start();
        } catch (IOException e) {
            logger.warn("", e);
        }
    }

    @Override
    public final void register(final Robot robot) {
    }

    @Override
    public final void beforeShutdown() {
        if(app != null) {
            app.stop();
        }
    }
}
