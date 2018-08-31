package com.elypia.commandler.impl;

/**
 * This should implement the default build method.
 * The default method should be the standard or minimal way
 * to send messages in a chat channel, usually raw text.
 * Platform specific implementations may append more
 * methods according to the different types of messages.
 *
 * @param <O> The object to build, this should be the ouput from a {@link IParser}.
 * @param <M> The message we're returning.
 */
public interface IBuilder<CE extends ICommandEvent, O, M> {

    /**
     * The default build method, this builder should be the default
     * way to send an {@link O object} of this type as a message.
     * Platform specific implementations may implement more build
     * methods for the diffenet message formats, if so this build method
     * should return the basic {@link M message} that requires minimal
     * permissions.
     *
     * @param event The {@link ICommandEvent} which required building.
     * @param output The output from the {@link IParser} when parsing the input.
     * @return The message to response to the user.
     */
    M build(CE event, O output);
}
