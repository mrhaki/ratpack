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

package ratpack.launch;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import ratpack.api.Nullable;
import ratpack.background.Background;
import ratpack.file.FileSystemBinding;
import ratpack.handling.Context;

import javax.inject.Provider;
import javax.net.ssl.SSLContext;
import java.net.InetAddress;
import java.net.URI;
import java.util.List;

/**
 * The configuration used to launch a server.
 *
 * @see LaunchConfigBuilder
 * @see LaunchConfigFactory
 * @see ratpack.server.RatpackServerBuilder#build(LaunchConfig)
 */
public interface LaunchConfig {

  /**
   * The default port for Ratpack applications, {@value}.
   */
  public static final int DEFAULT_PORT = 5050;

  /**
   * The default max content length.
   */
  public int DEFAULT_MAX_CONTENT_LENGTH = 65536;

  /**
   * The base dir of the application, which is also the initial {@link ratpack.file.FileSystemBinding}.
   *
   * @return The base dir of the application.
   */
  public FileSystemBinding getBaseDir();

  /**
   * The handler factory that can create the root handler for the application.
   *
   * @return The handler factory that can create the root handler for the application.
   */
  public HandlerFactory getHandlerFactory();

  /**
   * The port that the application should listen to requests on.
   * <p>
   * Defaults to {@value #DEFAULT_PORT}.
   *
   * @return The port that the application should listen to requests on.
   */
  public int getPort();

  /**
   * The address of the interface that the application should bind to.
   * <p>
   * A value of null causes all interfaces to be bound. Defaults to null.
   *
   * @return The address of the interface that the application should bind to.
   */
  @Nullable
  public InetAddress getAddress();

  /**
   * Whether or not the server is in "reloadable" (i.e. development) mode.
   * <p>
   * Different parts of the application may respond to this as they see fit.
   *
   * @return {@code true} if the server is in "reloadable" mode
   */
  public boolean isReloadable();

  /**
   * The number of threads for handling application requests.
   * <p>
   * If the value is greater than 0, a thread pool (of this size) will be created for servicing requests.
   * <p>
   * If the value is 0 or less, no thread pool will be used to handle requests. This means that the handler will be called on the
   * same thread that accepted the request. This means that handlers SHOULD NOT perform blocking IO or long operations in their operation.
   * <p>
   * The default value is 0.
   *
   * @return The size of the request handling thread pool, or 0 if a dedicated thread pool should not be used.
   */
  public int getMainThreads();

  /**
   * The executor service to use to perform foreground computation operations.
   *
   * @return the executor service to use to perform foreground computation operations
   */
  public EventLoopGroup getEventLoopGroup();

  /**
   * The “background”, for performing blocking operations.
   *
   * @see ratpack.handling.Context#background(java.util.concurrent.Callable)
   * @return the “background”, for performing blocking operations
   */
  public Background getBackground();

  /**
   * The allocator for buffers needed by the application.
   * <p>
   * Defaults to Netty's {@link io.netty.buffer.PooledByteBufAllocator}.
   *
   * @return The allocator for buffers needed by the application.
   */
  public ByteBufAllocator getBufferAllocator();

  /**
   * The public address of the site used for redirects.
   *
   * @return The url of the public address
   */
  public URI getPublicAddress();

  /**
   * The names of files that can be served if a request is made to serve a directory.
   *
   * @return The names of files that can be served if a request is made to serve a directory.
   */
  public List<String> getIndexFiles();

  /**
   * The SSL context to use if the application will serve content over HTTPS.
   *
   * @return The SSL context or <code>null</code> if the application does not use SSL.
   */
  @Nullable
  public SSLContext getSSLContext();

  /**
   * Provides access to any "other" properties that were specified.
   * <p>
   * Extensions and plugins can use other properties for their configuration.
   *
   * @param key The property key
   * @param defaultValue The value to return if the property was not set
   * @return The other property for {@code key}, or the {@code defaultValue} if it is not set
   */
  public String getOther(String key, String defaultValue);

  /**
   * The max content length to use for the HttpObjectAggregator.
   *
   * @return The max content length as an int.
   */
  public int getMaxContentLength();

  /**
   * A provider that always returns the current context for the current thread.
   * <p>
   * This DOES NOT always return <i>this</i> context.
   * The context returned by this provider is the context being used on the current thread.
   * That is, it acts like thread local storage of the current context.
   * Moreover, the provider returned by successive calls to this method on any context instance will provide a functionally identical provider.
   * <p>
   * If there is no context bound to the current thread, this will return {@code null}.
   * <p>
   * This method is primary provided for integration with dependency injection frameworks.
   *
   * @return A provider that always provides the context object for the current thread.
   */
  public Provider<Context> getContextProvider();

}
