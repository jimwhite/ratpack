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

package org.ratpackframework.handling;

/**
 * A buildable strategy for processing an exchange based on HTTP method.
 * <p>
 * A by-method-chain is exposed by {@link org.ratpackframework.handling.Exchange#getMethods()}.
 * It is used to respond differently based on the HTTP method.
 * If there is no action registered with the chain before {@link #send()} is called, a {@code 405} will be issued to
 * the contextual {@link org.ratpackframework.error.ClientErrorHandler} (which by default will send back a HTTP 405 to the client).
 * <p>
 * This is useful when a given handler can respond to more than one HTTP method.
 * If a handler only needs to respond to one HTTP method it can be more convenient to use {@link Handlers#get(Handler)} and friends.
 * <pre class="tested">
 * import org.ratpackframework.handling.*;
 *
 * class MyHandler implements Handler {
 *   public void handle(final Exchange exchange) {
 *     // Do processing common to all methods …
 *
 *     exchange.getMethods().
 *       get(new Runnable() {
 *         public void run() {
 *           // GET handling logic
 *         }
 *       }).
 *       post(new Runnable() {
 *         public void run() {
 *           // POST handling logic
 *         }
 *       }).
 *       send(); // do the processing
 *   }
 * }
 * </pre>
 * <p>
 * If you are using Groovy, you can use closures as the definitions (because closures implement {@link Runnable}).
 * <pre class="tested">
 * import org.ratpackframework.handling.*
 *
 * class MyHandler implements Handler {
 *   void handle(Exchange exchange) {
 *     // Do processing common to all methods …
 *
 *     exchange.methods.
 *       get {
 *         // GET handling logic
 *       }.
 *       post {
 *         // POST handling logic
 *       }.
 *       send() // do the processing
 *   }
 * }
 * </pre>
 * <p>
 * You <b>must</b> call the {@link #send()} method to finalise the chain. Otherwise, the exchange will not be processed.
 * <p>
 * Only the last added runnable for a method will be used. Adding a subsequent runnable for the same method will replace the previous.
 */
public interface ByMethodChain {

  /**
   * Defines the action to to take if the request has a HTTP method of GET.
   *
   * @param runnable The action to take
   * @return this
   */
  ByMethodChain get(Runnable runnable);

  /**
   * Defines the action to to take if the request has a HTTP method of POST.
   *
   * @param runnable The action to take
   * @return this
   */
  ByMethodChain post(Runnable runnable);

  /**
   * Defines the action to to take if the request has a HTTP method of PUT.
   *
   * @param runnable The action to take
   * @return this
   */
  ByMethodChain put(Runnable runnable);

  /**
   * Defines the action to to take if the request has a HTTP method of DELETE.
   *
   * @param runnable The action to take
   * @return this
   */
  ByMethodChain delete(Runnable runnable);

  /**
   * Defines the action to to take if the request has a HTTP method of {@code methodName}.
   * <p>
   * The method name is case insensitive.
   *
   * @param methodName The HTTP method to map the given action to
   * @param runnable The action to take
   * @return this
   */
  ByMethodChain named(String methodName, Runnable runnable);

  /**
   * Finalise the chain and processes the exchange.
   * <p>
   * <b>Must be called to actually perform the action.</b>
   */
  void send();

}
