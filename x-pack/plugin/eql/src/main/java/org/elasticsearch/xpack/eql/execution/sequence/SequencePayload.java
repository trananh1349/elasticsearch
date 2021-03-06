/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */

package org.elasticsearch.xpack.eql.execution.sequence;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.xpack.eql.action.EqlSearchResponse.Event;
import org.elasticsearch.xpack.eql.execution.payload.AbstractPayload;

import java.util.ArrayList;
import java.util.List;

class SequencePayload extends AbstractPayload {

    private final List<org.elasticsearch.xpack.eql.action.EqlSearchResponse.Sequence> values;

    SequencePayload(List<Sequence> sequences, List<List<GetResponse>> docs, boolean timedOut, TimeValue timeTook) {
        super(timedOut, timeTook);
        values = new ArrayList<>(sequences.size());
        
        for (int i = 0; i < sequences.size(); i++) {
            Sequence s = sequences.get(i);
            List<GetResponse> hits = docs.get(i);
            List<Event> events = new ArrayList<>(hits.size());
            for (GetResponse hit : hits) {
                events.add(new Event(hit.getIndex(), hit.getId(), hit.getSourceAsBytesRef()));
            }
            values.add(new org.elasticsearch.xpack.eql.action.EqlSearchResponse.Sequence(s.key().asList(), events));
        }
    }

    @Override
    public Type resultType() {
        return Type.SEQUENCE;
    }

    @Override
    public List<org.elasticsearch.xpack.eql.action.EqlSearchResponse.Sequence> values() {
        return values;
    }
}
