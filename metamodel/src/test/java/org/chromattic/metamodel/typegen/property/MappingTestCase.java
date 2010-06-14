/*
 * Copyright (C) 2010 eXo Platform SAS.
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

package org.chromattic.metamodel.typegen.property;

import org.chromattic.metamodel.mapping.jcr.PropertyDefinitionMapping;
import org.chromattic.metamodel.mapping.jcr.PropertyMetaType;
import org.chromattic.metamodel.mapping2.BeanMapping;
import org.chromattic.metamodel.mapping2.ValueMapping;
import org.chromattic.metamodel.typegen.AbstractMappingTestCase;

import java.util.Arrays;
import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class MappingTestCase extends AbstractMappingTestCase {


  public void testA() throws Exception {
    Map<Class<?>, BeanMapping> mappings = assertValid(A.class);
    BeanMapping mapping = mappings.get(A.class);
    ValueMapping<?> stringMapping = (ValueMapping<?>) mapping.getPropertyMapping("string");
    PropertyDefinitionMapping propertyDefinition = stringMapping.getPropertyDefinition();
    assertEquals("string", propertyDefinition.getName());
    assertEquals(null, propertyDefinition.getDefaultValue());
    assertEquals(PropertyMetaType.STRING, propertyDefinition.getMetaType());
    assertEquals("string", stringMapping.getName());
  }

  public void testB() throws Exception {
    Map<Class<?>, BeanMapping> mappings = assertValid(B.class);
    BeanMapping mapping = mappings.get(B.class);
    ValueMapping<?> stringMapping = (ValueMapping<?>) mapping.getPropertyMapping("string");
    PropertyDefinitionMapping propertyDefinition = stringMapping.getPropertyDefinition();
    assertEquals("string", propertyDefinition.getName());
    assertEquals(Arrays.asList("foo"), propertyDefinition.getDefaultValue());
    assertEquals(PropertyMetaType.STRING, propertyDefinition.getMetaType());
    assertEquals("string", stringMapping.getName());
  }
}
