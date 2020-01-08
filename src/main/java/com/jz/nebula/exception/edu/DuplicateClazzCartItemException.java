/*
 * Copyright (c) 2020. iEuclid Technology
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.jz.nebula.exception.edu;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class DuplicateClazzCartItemException extends Exception implements Serializable {

    public DuplicateClazzCartItemException() {
        super("Duplicate class item was added into the cart!");
    }

    public DuplicateClazzCartItemException(@NotNull String msg) {
        super(msg);
    }
}
