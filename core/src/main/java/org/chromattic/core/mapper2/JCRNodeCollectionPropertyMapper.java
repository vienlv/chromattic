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

package org.chromattic.core.mapper2;

import org.chromattic.core.ObjectContext;
import org.chromattic.metamodel.bean2.BeanValueInfo;
import org.chromattic.metamodel.bean2.MultiValuedPropertyInfo;
import org.chromattic.metamodel.mapping2.PropertyMapping;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class JCRNodeCollectionPropertyMapper<P extends MultiValuedPropertyInfo<BeanValueInfo>, O extends ObjectContext>
  extends RelatedPropertyMapper<P, BeanValueInfo, O> {

  /** . */
  private final Class relatedClass;

  public JCRNodeCollectionPropertyMapper(Class<O> contextType, PropertyMapping<P, BeanValueInfo> info) throws ClassNotFoundException {
    super(contextType, info);

    //
    relatedClass = Thread.currentThread().getContextClassLoader().loadClass(info.getValue().getClassType().getName());
  }

  public Class<?> getRelatedClass() {
    return relatedClass;
  }
}