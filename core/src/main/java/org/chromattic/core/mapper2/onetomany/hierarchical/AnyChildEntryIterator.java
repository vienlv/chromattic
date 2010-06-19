/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.chromattic.core.mapper2.onetomany.hierarchical;

import org.chromattic.common.collection.AbstractFilterIterator;
import org.chromattic.core.EntityContext;

import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class AnyChildEntryIterator<E> extends AbstractFilterIterator<Map.Entry<String, E>, E> {

  /** . */
  private final AnyChildMap map;

  public AnyChildEntryIterator(AnyChildMap<E> map) throws NullPointerException {
    super(map.parentCtx.getChildren(map.relatedClass));

    //
    this.map = map;
  }

  protected Map.Entry<String, E> adapt(final E internal) {
    final EntityContext internalCtx = map.parentCtx.getSession().unwrapEntity(internal);
    return new Map.Entry<String, E>() {

      /** . */
      private final String name = map.parentCtx.getSession().getName(internalCtx);

      public String getKey() {
        return name;
      }

      public E getValue() {
        return internal;
      }

      public E setValue(E value) {
        throw new UnsupportedOperationException();
      }
    };
  }
}