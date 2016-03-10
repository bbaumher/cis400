package utilities;

import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class StreamUtilities {
  private StreamUtilities() { }

  public static <T> Collector<T, ?, T> randomElementCollector() {
    return new Collector<T, RandomElement<T>, T>() {
      @Override
      public Supplier<RandomElement<T>> supplier() {
        return RandomElement::new;
      }

      @Override
      public BiConsumer<RandomElement<T>, T> accumulator() {
        return (randomElement, element) -> {
          if (ThreadLocalRandom.current().nextInt(randomElement.count + 1) == 0)
          {
            randomElement.t = element;
          }
          randomElement.count++;
        };
      }

      @Override
      public BinaryOperator<RandomElement<T>> combiner() {
        return (r1, r2) -> {
          RandomElement<T> result =
            ThreadLocalRandom.current().nextInt(r1.count + r2.count) < r1.count
              ? r1
              : r2;
          result.count = r1.count + r2.count;
          return result;
        };
      }

      @Override
      public Function<RandomElement<T>, T> finisher() {
        return randomElement -> randomElement.t;
      }

      @Override
      public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
      }
    };
  }

  private static final class RandomElement<T> {
    private T t;
    private int count;
  }
}
