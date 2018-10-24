/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.sql.expression.predicate;

import org.elasticsearch.common.io.stream.NamedWriteableRegistry;
import org.elasticsearch.common.io.stream.Writeable.Reader;
import org.elasticsearch.test.AbstractWireSerializingTestCase;
import org.elasticsearch.xpack.sql.expression.Literal;
import org.elasticsearch.xpack.sql.expression.function.scalar.Processors;
import org.elasticsearch.xpack.sql.expression.gen.processor.ConstantProcessor;
import org.elasticsearch.xpack.sql.expression.predicate.operator.comparison.InProcessor;

import java.util.Arrays;

import static org.elasticsearch.xpack.sql.tree.Location.EMPTY;

public class InProcessorTests extends AbstractWireSerializingTestCase<InProcessor> {

    private static final Literal ONE = L(1);
    private static final Literal TWO = L(2);
    private static final Literal THREE = L(3);

    public static InProcessor randomProcessor() {
        return new InProcessor(Arrays.asList(new ConstantProcessor(randomLong()), new ConstantProcessor(randomLong())));
    }

    @Override
    protected InProcessor createTestInstance() {
        return randomProcessor();
    }

    @Override
    protected Reader<InProcessor> instanceReader() {
        return InProcessor::new;
    }

    @Override
    protected NamedWriteableRegistry getNamedWriteableRegistry() {
        return new NamedWriteableRegistry(Processors.getNamedWriteables());
    }

    public void testEq() {
        assertEquals(true, new In(EMPTY, TWO, Arrays.asList(ONE, TWO, THREE)).makePipe().asProcessor().process(null));
        assertEquals(false, new In(EMPTY, THREE, Arrays.asList(ONE, TWO)).makePipe().asProcessor().process(null));
    }

    private static Literal L(Object value) {
        return Literal.of(EMPTY, value);
    }
}
