package com.elypia.commandler.parsing;

import com.elypia.commandler.annotations.filter.Search;
import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.parsing.parsers.*;
import com.elypia.commandler.parsing.parsers.jda.*;
import net.dv8tion.jda.core.entities.*;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

public class Parser {

    protected Map<Class<?>, IParamParser> parsers;

    public Parser() {
        parsers = new HashMap<>();

        registerParser(boolean.class, new BooleanParser());
        registerParser(double.class, new DoubleParser());
        registerParser(int.class, new IntParser());
        registerParser(long.class, new LongParser());
        registerParser(String.class, new StringParser());
        registerParser(URL.class, new UrlParser());

        registerParser(Emote.class, new EmoteParser());
        registerParser(Guild.class, new GuildParser());
        registerParser(Role.class, new RoleParser());
        registerParser(TextChannel.class, new TextChannelParser());
        registerParser(User.class, new UserParser());
        registerParser(VoiceChannel.class, new VoiceChannelParser());
    }

    public <T> void registerParser(Class<T> t, IParamParser<T> parser) {
        if (parsers.put(t, parser) != null)
            System.err.printf("The parser for %s has been replaced.", t.getName());
    }

    /**
     * Take the String parameters from the message event and parse them into the required
     * format the commands method required to execute.
     *
     * @param event The message event to take parameters from.
     * @param metaCommand The method to imitate the fields of.
     * @return An Object[] array of all parameters parsed as required for the given method.
     * @throws IllegalArgumentException If one of the arguments could not be parsed in the required format.
     */

    public Object[] parseParameters(MessageEvent event, MetaCommand metaCommand) throws IllegalArgumentException {
        List<MetaParam> metaParams = metaCommand.getMetaParams(); // Parameter data
        List<Object> inputs = event.getParams(); // User input parameters
        Object[] objects = new Object[metaParams.size()]; // Parsed parameters to perform commands

        int offset = 0;

        for (int i = 0; i < metaParams.size(); i++) {
            MetaParam param = metaParams.get(i);

            if (!param.isInput()) {
                objects[i] = event;
                offset++;
                continue;
            }

            Object input = inputs.get(i - offset);
            Object object = parseParam(event, param, input);

            if (object == null)
                return null;

            objects[i] = parseParam(event, param, input);
        }

        return objects;
    }

    private Object parseParam(MessageEvent event, MetaParam param, Object object) throws IllegalArgumentException {
        Class<?> clazz = param.getParameter().getType(); // Class of type required
        boolean array = clazz.isArray(); // Is type required an array
        SearchScope scope; // The search scope if applicable

        Search search = param.getParameter().getAnnotation(Search.class);

        scope = search != null ? search.value() : SearchScope.GLOBAL;

        if (clazz.isArray()) {
            String[] input = object.getClass().isArray() ? (String[])object : new String[] {(String)object};
            Object[] objects = (Object[])Array.newInstance(clazz.getComponentType(), input.length);

            for (int i = 0; i < input.length; i++)
                objects[i] = parsers.get(clazz.getComponentType()).parse(event, scope, input[i]);

            return objects;
        } else {
            if (array)
                throw new IllegalArgumentException("Parameter `" + String.join(", ", object + "` can't be a list."));

            String input = (String)object;

            return parsers.get(clazz).parse(event, scope, input);
        }
    }
}
