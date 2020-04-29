/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.commandler.exceptions.misuse;

import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;

import java.util.Objects;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public abstract class ParamException extends ActionException {

    /** The parameter metadata. */
    private final transient MetaParam metaParam;

    public ParamException(ActionEvent<?, ?> action, MetaParam metaParam) {
        super(action);
        this.metaParam = Objects.requireNonNull(metaParam);
    }

    public ParamException(ActionEvent<?, ?> action, MetaParam metaParam, String message) {
        super(action, message);
        this.metaParam = Objects.requireNonNull(metaParam);
    }

    public ParamException(ActionEvent<?, ?> action, MetaParam metaParam, String message, Throwable cause) {
        super(action, message, cause);
        this.metaParam = Objects.requireNonNull(metaParam);
    }

    public ParamException(ActionEvent<?, ?> action, MetaParam metaParam, Throwable cause) {
        super(action, cause);
        this.metaParam = Objects.requireNonNull(metaParam);
    }

    public MetaParam getMetaParam() {
        return metaParam;
    }
}
