package com.elypia.commandler.parsers;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.impl.*;

import java.net.*;

public class UrlParser implements IParser<ICommandEvent, URL> {

    @Override
    public URL parse(ICommandEvent event, Class<? extends URL> type, String input) {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
