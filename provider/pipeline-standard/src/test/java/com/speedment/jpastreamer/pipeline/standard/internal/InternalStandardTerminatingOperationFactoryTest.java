package com.speedment.jpastreamer.pipeline.standard.internal;

import com.speedment.jpastreamer.pipeline.TerminatingOperationFactory;
import com.speedment.jpastreamer.pipeline.terminating.TerminatingOperation;
import com.speedment.jpastreamer.pipeline.terminating.TerminatingOperationType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class InternalStandardTerminatingOperationFactoryTest {

    private final TerminatingOperationFactory factory = new InternalStandardTerminatingOperationFactory();

    @Test
    void createForEach() {
        final List<String> list = new ArrayList<>();
        final Consumer<String> consumer = list::add;

        final TerminatingOperation<Stream<String>, Void> forEach = factory.createForEach(consumer);

        assertEquals(TerminatingOperationType.FOR_EACH, forEach.type());
        assertEquals(Stream.class, forEach.streamType());

        assertEquals(void.class, forEach.returnType());
        assertEquals(1, forEach.arguments().length);
        assertEquals(consumer, forEach.arguments()[0]);

        forEach.consumer().accept(Stream.of("A", "B"));
        assertEquals(Arrays.asList("A", "B"), list);

        assertDoesNotThrow(forEach::consumer);
        assertThrows(ClassCastException.class, forEach::function);
        assertThrows(ClassCastException.class, forEach::predicate);
        assertThrows(ClassCastException.class, forEach::toDoubleFunction);
        assertThrows(ClassCastException.class, forEach::toIntFunction);
        assertThrows(ClassCastException.class, forEach::toDoubleFunction);


    }

}