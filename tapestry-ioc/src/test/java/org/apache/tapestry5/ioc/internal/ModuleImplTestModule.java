// Copyright 2006, 2007, 2009, 2012 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.ioc.internal;

import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.annotations.InjectService;

/**
 * Module class used by {@link ioc.specs.ModuleImplSpec}.
 */
public class ModuleImplTestModule
{
    public UpcaseService buildUpcase()
    {
        return new UpcaseServiceImpl();
    }

    public FieService buildOtherFie()
    {
        return null;
    }

    public ToStringService buildToString(ServiceResources resources)
    {
        final String serviceId = resources.getServiceId();

        return new ToStringService()
        {
            @Override
            public String toString()
            {
                return "<ToStringService: " + serviceId + ">";
            }
        };
    }

    public FieService buildFie()
    {
        return null;
    }

    public FoeService buildRecursiveFoe(@InjectService("RecursiveFoe")
                                        FoeService self)
    {
        // While constructing self, we invoke a method on self.

        self.foe();

        return null;
    }
}
