/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */

package org.elasticsearch.xpack.watcher.rest.action;

import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.rest.action.RestBuilderListener;
import org.elasticsearch.xpack.core.watcher.transport.actions.get.GetWatchAction;
import org.elasticsearch.xpack.core.watcher.transport.actions.get.GetWatchRequest;
import org.elasticsearch.xpack.core.watcher.transport.actions.get.GetWatchResponse;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestStatus.NOT_FOUND;
import static org.elasticsearch.rest.RestStatus.OK;

public class RestGetWatchAction extends BaseRestHandler {

    public RestGetWatchAction(RestController controller) {
        controller.registerHandler(GET, "/_watcher/watch/{id}", this);
    }

    @Override
    public String getName() {
        return "watcher_get_watch";
    }

    @Override
    protected RestChannelConsumer prepareRequest(final RestRequest request, NodeClient client) {
        final GetWatchRequest getWatchRequest = new GetWatchRequest(request.param("id"));
        return channel -> client.execute(GetWatchAction.INSTANCE, getWatchRequest, new RestBuilderListener<>(channel) {
            @Override
            public RestResponse buildResponse(GetWatchResponse response, XContentBuilder builder) throws Exception {
                response.toXContent(builder, request);
                RestStatus status = response.isFound() ? OK : NOT_FOUND;
                return new BytesRestResponse(status, builder);
            }
        });
    }
}
