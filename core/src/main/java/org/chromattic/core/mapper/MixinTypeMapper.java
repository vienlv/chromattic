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
package org.chromattic.core.mapper;

import org.chromattic.api.NameConflictResolution;
import org.chromattic.spi.instrument.Instrumentor;

import java.util.Set;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class MixinTypeMapper extends TypeMapper {

  /** . */
  private final String mixinName;

  public MixinTypeMapper(
    Class<?> objectClass,
    Set<PropertyMapper> propertyMappers,
    Set<MethodMapper> methodMappers,
    NameConflictResolution onDuplicate,
    Instrumentor instrumentor,
    String mixinName) {
    super(
      objectClass,
      propertyMappers,
      methodMappers,
      onDuplicate,
      instrumentor);

    //
    this.mixinName = mixinName;
  }

  @Override
  public String getTypeName() {
    return mixinName;
  }
}
