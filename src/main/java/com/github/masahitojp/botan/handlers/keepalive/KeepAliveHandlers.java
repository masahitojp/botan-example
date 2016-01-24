package com.github.masahitojp.botan.handlers.keepalive;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unused")
public class KeepAliveHandlers implements BotanMessageHandlers {
    private static Logger logger = LoggerFactory.getLogger(KeepAliveHandlers.class);

    @Override
    public final void register(final Robot robot) {
        robot.router.GET("/botan/keepalive", (req, resp) -> 200);
    }
}
