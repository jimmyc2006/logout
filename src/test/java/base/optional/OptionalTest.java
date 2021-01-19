package base.optional;

import org.junit.Test;

import java.util.Optional;

public class OptionalTest {

    @Test
    public void test() {
        ta().ifPresent(str -> System.out.println(str));
    }

    private Optional<String> ttt() {
        return Optional.of("aa");
    }

    private Optional<String[]> ta() {
        return Optional.of(new String[]{"111", "222"});
    }
}
