// Copyright 2008 The Apache Software Foundation
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

package org.apache.tapestry5.services;

/**
 * A closure over a call to {@link org.apache.tapestry5.services.Environment#peek(Class)} or {@link
 * org.apache.tapestry5.services.Environment#peekRequired(Class)} that caches the current value on the top of the stack.
 * A closure is created for some specific type of environmental object.
 *
 * @param <T>
 * @since 5.0.19
 */
public interface EnvironmentalClosure<T>
{

    /**
     * Peeks at the current top of the indicated stack.
     *
     * @return current value of type, or null.
     */
    T peek();

    /**
     * Peeks at the current top of the indicated stack.
     *
     * @return the current value
     * @throws RuntimeException if no environmental of that type has been added
     */
    T peekRequired();
}
