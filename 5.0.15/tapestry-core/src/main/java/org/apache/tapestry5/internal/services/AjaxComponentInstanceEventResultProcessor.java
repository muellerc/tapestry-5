// Copyright 2007, 2008 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.internal.services;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.services.ComponentEventResultProcessor;

import java.io.IOException;

/**
 * Performs a partial page render based on a root component.
 */
public class AjaxComponentInstanceEventResultProcessor implements ComponentEventResultProcessor<Component>
{
    private final RequestPageCache cache;

    private final PageRenderQueue pageRenderQueue;

    public AjaxComponentInstanceEventResultProcessor(RequestPageCache cache, PageRenderQueue pageRenderQueue)
    {
        this.cache = cache;
        this.pageRenderQueue = pageRenderQueue;
    }

    public void processResultValue(Component value) throws IOException
    {
        ComponentResources resources = value.getComponentResources();

        String pageName = resources.getPageName();

        Page page = cache.get(pageName);

        String nestedId = resources.getNestedId();

        // The user may return a complete page instance, which isn't really a partial render, I guess.
        // Depends on the structure of the page returned.

        RenderCommand command = nestedId == null ? page.getRootElement() : page.getComponentElementByNestedId(nestedId);

        pageRenderQueue.initializeForPartialPageRender(command);
    }
}
