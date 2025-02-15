package org.example.propagation;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.ContextKey;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;

import java.util.Collection;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Implementation of the B3 propagation protocol. See <a
 * href=https://github.com/openzipkin/b3-propagation>openzipkin/b3-propagation</a>.
 *
 * <p>Also see <a
 * href=https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/context/api-propagators.md#b3-requirements>B3
 * Requirements</a>
 *
 * <p>To register the default B3 propagator, which injects a single header, use:
 *
 * <pre>{@code
 * OpenTelemetry.setPropagators(
 *   DefaultContextPropagators
 *     .builder()
 *     .addTextMapPropagator(B3Propagator.injectingSingleHeader())
 *     .build());
 * }</pre>
 *
 * <p>To register a B3 propagator that injects multiple headers, use:
 *
 * <pre>{@code
 * OpenTelemetry.setPropagators(
 *   DefaultContextPropagators
 *     .builder()
 *     .addTextMapPropagator(B3Propagator.injectingMultiHeaders())
 *     .build());
 * }</pre>
 */
@Immutable
public final class B3Propagator implements TextMapPropagator {
  // Hack: Had to change these because the HttpServer exchange message always has an upper case X
  static final String TRACE_ID_HEADER = "X-b3-traceid";
  static final String SPAN_ID_HEADER = "X-b3-spanid";
  static final String PARENT_SPAN_ID_HEADER = "X-b3-parent-spanid";
  static final String SAMPLED_HEADER = "X-b3-sampled";
  static final String DEBUG_HEADER = "X-b3-flags";

  static final ContextKey<Boolean> DEBUG_CONTEXT_KEY = ContextKey.named("b3-debug");
  static final String MULTI_HEADER_DEBUG = "1";

  private static final B3Propagator MULTI_HEADERS_INSTANCE =
      new B3Propagator(new B3PropagatorInjectorMultipleHeaders());

  private final B3PropagatorExtractor multipleHeadersExtractor =
      new B3PropagatorExtractorMultipleHeaders();

  private final B3PropagatorInjector b3PropagatorInjector;

  private B3Propagator(B3PropagatorInjector b3PropagatorInjector) {
    this.b3PropagatorInjector = b3PropagatorInjector;
  }

  /**
   * Returns an instance of the {@link B3Propagator} that injects multi headers format.
   *
   * <p>This instance extracts both formats, in the order: single header, multi header.
   *
   * @return an instance of the {@link B3Propagator} that injects multi headers format.
   */
  public static B3Propagator injectingMultiHeaders() {
    return MULTI_HEADERS_INSTANCE;
  }


  @Override
  public Collection<String> fields() {
    return b3PropagatorInjector.fields();
  }

  @Override
  public <C> void inject(Context context, @Nullable C carrier, TextMapSetter<C> setter) {
    b3PropagatorInjector.inject(context, carrier, setter);
  }

  @Override
  public <C> Context extract(Context context, @Nullable C carrier, TextMapGetter<C> getter) {
    return multipleHeadersExtractor.extract(context, carrier, getter).orElse(context);
  }

  @Override
  public String toString() {
    return "B3Propagator{b3PropagatorInjector=" + b3PropagatorInjector + "}";
  }
}
