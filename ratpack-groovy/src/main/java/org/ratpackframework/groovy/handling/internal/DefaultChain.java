/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ratpackframework.groovy.handling.internal;

import com.google.common.collect.ImmutableList;
import groovy.lang.Closure;
import org.ratpackframework.groovy.handling.Chain;
import org.ratpackframework.groovy.handling.ClosureHandlers;
import org.ratpackframework.handling.Handler;
import org.ratpackframework.handling.Handlers;
import org.ratpackframework.handling.internal.ChainBuilder;

import java.util.List;

import static org.ratpackframework.groovy.util.Closures.action;

public class DefaultChain implements Chain {

  private final List<Handler> handlers;

  public DefaultChain(List<Handler> handlers) {
    this.handlers = handlers;
  }

  public void handler(Closure<?> handler) {
    add(ClosureHandlers.handler(handler));
  }

  public void prefix(String prefix, Closure<?> chain) {
    add(Handlers.prefix(prefix, toHandlerList(chain)));
  }

  public void path(String path, Closure<?> handler) {
    add(Handlers.path(path, ClosureHandlers.handler(handler)));
  }

  public void get(String path, Closure<?> handler) {
    add(ClosureHandlers.get(path, handler));
  }

  public void get(Closure<?> handler) {
    get("", handler);
  }

  public void post(String path, Closure<?> handler) {
    add(ClosureHandlers.post(path, handler));
  }

  public void post(Closure<?> handler) {
    post("", handler);
  }

  public void assets(String path, String... indexFiles) {
    add(Handlers.assets(path, indexFiles));
  }

  public void context(Object object, Closure<?> handlers) {
    add(Handlers.context(object, toHandlerList(handlers)));
  }

  public <T> void context(Class<? super T> type, T object, Closure<?> handlers) {
    add(Handlers.context(type, object, toHandlerList(handlers)));
  }

  public void fileSystem(String path, Closure<?> handlers) {
    add(Handlers.fileSystem(path, toHandlerList(handlers)));
  }

  private ImmutableList<Handler> toHandlerList(Closure<?> handlers) {
    return ChainBuilder.INSTANCE.buildList(GroovyDslChainActionTransformer.INSTANCE, action(handlers));
  }

  public void add(Handler handler) {
    handlers.add(handler);
  }

}
