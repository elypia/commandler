package com.elypia.commandler.impl.modules;

import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.builders.BuilderModuleBuilder;

@Builders(BuilderModuleBuilder.class)
@Module(id = "Builders", aliases = "builder", help = "Module for testing if builders are performing correctly.")
public class BuilderModule extends Handler<Void, String, String> {

    /**
     * @return Returns a new {@link BuilderModule} instance
     * this is good for this test as we have registered a builder called
     * {@link BuilderModuleBuilder} which will always return null upon building
     * this type of object. Builders should never return null!
     */
    @Command(id = "Builders Returned Null", aliases = "null", help = "When a builder returns null for input.")
    public BuilderModule builderReturnedNull() {
        return new BuilderModule();
    }
}