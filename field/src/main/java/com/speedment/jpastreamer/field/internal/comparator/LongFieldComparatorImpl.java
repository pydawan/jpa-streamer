/*
 * JPAstreamer - Express JPA queries with Java Streams
 * Copyright (c) 2020-2020, Speedment, Inc. All Rights Reserved.
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * See: https://github.com/speedment/jpa-streamer/blob/master/LICENSE
 */
package com.speedment.jpastreamer.field.internal.comparator;

import com.speedment.jpastreamer.field.comparator.FieldComparator;
import com.speedment.jpastreamer.field.comparator.LongFieldComparator;
import com.speedment.jpastreamer.field.comparator.NullOrder;
import com.speedment.jpastreamer.field.trait.HasLongValue;

import java.util.Objects;

import static com.speedment.common.invariant.NullUtil.requireNonNulls;
import static java.util.Objects.requireNonNull;

/**
 * @param <ENTITY> entity type
 *
 * @author Emil Forslund
 * @since  3.0.0
 */
public final class LongFieldComparatorImpl<ENTITY>
extends AbstractFieldComparator<ENTITY> 
implements LongFieldComparator<ENTITY> {
    
    private final HasLongValue<ENTITY> field;
    private final boolean reversed;
    
    public LongFieldComparatorImpl(HasLongValue<ENTITY> field) {
        this(field, false);
    }
    
    LongFieldComparatorImpl(HasLongValue<ENTITY> field, boolean reversed) {
        this.field    = requireNonNull(field);
        this.reversed = reversed;
    }
    
    @Override
    public HasLongValue<ENTITY> getField() {
        return field;
    }
    
    @Override
    public NullOrder getNullOrder() {
        return NullOrder.NONE;
    }
    
    @Override
    public boolean isReversed() {
        return reversed;
    }
    
    @Override
    public LongFieldComparatorImpl<ENTITY> reversed() {
        return new LongFieldComparatorImpl<>(field, !reversed);
    }
    
    @Override
    public int compare(ENTITY first, ENTITY second) {
        requireNonNulls(first, second);
        final long a = field.getAsLong(first);
        final long b = field.getAsLong(second);
        return applyReversed(Long.compare(a, b));
    }
    
    @Override
    public int hashCode() {
        return (4049 + Objects.hashCode(this.field.table())) * 3109
            + Boolean.hashCode(reversed);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FieldComparator)) return false;
        
        @SuppressWarnings("unchecked")
        final FieldComparator<ENTITY> casted =
            (FieldComparator<ENTITY>) obj;
        
        return reversed == casted.isReversed()
            && Objects.equals(
                field.table(),
                casted.getField().table()
            );
    }
    
    @Override
    public String toString() {
        return "(order by " + field.table() + " " +
            (reversed ? "descending" : "ascending") + ")";
    }
    
    private int applyReversed(long compare) {
        if (compare == 0) {
            return 0;
        } else {
            if (reversed) {
                if (compare > 0) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                if (compare > 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
    }
}
