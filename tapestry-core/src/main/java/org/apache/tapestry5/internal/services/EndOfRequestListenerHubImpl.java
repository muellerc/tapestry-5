//  Copyright 2008 The Apache Software Foundation
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

import org.apache.tapestry5.internal.events.EndOfRequestEvent;
import org.apache.tapestry5.internal.events.EndOfRequestListener;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.Request;

import java.util.List;

public class EndOfRequestListenerHubImpl implements EndOfRequestListenerHub
{
    private final List<EndOfRequestListener> listeners = CollectionFactory.newThreadSafeList();

    public void addEndOfRequestListener(EndOfRequestListener listener)
    {
        listeners.add(listener);
    }

    public void removeEndOfRequestListener(EndOfRequestListener listener)
    {
        listeners.remove(listener);
    }

    public void fire(Request request)
    {
        EndOfRequestEvent event = new EndOfRequestEvent(request);

        for (EndOfRequestListener l : listeners)
        {
            l.requestDidComplete(event);
        }
    }
}
