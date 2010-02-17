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

package org.chromattic.core.mapping;

import org.chromattic.api.RelationshipType;
import org.chromattic.core.bean.*;
import org.chromattic.core.mapping.jcr.JCRMemberMapping;
import org.chromattic.core.mapping.jcr.JCRNodeAttributeMapping;
import org.chromattic.core.mapping.jcr.JCRPropertyMapping;
import org.chromattic.core.mapping.value.*;
import org.reflext.api.ClassTypeInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class AbstractTypeMappingVisitor {

  /** . */
  private final Set<ClassTypeInfo> types;

  /** . */
  private final TypeMappingBuilder builder;

  /** . */
  private final Map<String, NodeTypeMapping> mappings;

  public AbstractTypeMappingVisitor() {
    types = new HashSet<ClassTypeInfo>();
    builder = new TypeMappingBuilder(new BeanInfoFactory());
    mappings = new HashMap<String, NodeTypeMapping>();
  }

  public void addTypeMapping(NodeTypeMapping mapping) {
    mappings.put(mapping.getObjectClass().getName(), mapping);
  }

  public void addType(ClassTypeInfo cti) {
    addTypeMapping(builder.build(cti));
  }

  protected NodeTypeMapping getMapping(ClassTypeInfo type) {
    return mappings.get(type.getName());
  }

  protected abstract void startMapping(NodeTypeMapping mapping);

  protected abstract void propertyMapping(JCRPropertyMapping propertyMapping, PropertyInfo<SimpleValueInfo> propertyInfo);

  protected abstract void propertyMapMapping();

  protected abstract void oneToManyByReference(String relatedName);

  protected abstract void oneToManyByPath(String relatedName);

  protected abstract void oneToManyHierarchic(ClassTypeInfo relatedType);

  protected abstract void manyToOneByReference(String name, ClassTypeInfo relatedType);

  protected abstract void manyToOneByPath(String name, ClassTypeInfo relatedType);

  protected abstract void manyToOneHierarchic(ClassTypeInfo relatedType);

  protected abstract void oneToOneHierarchic(String name, ClassTypeInfo relatedType);

  protected abstract void endMapping();

  public void generate() {

    for (NodeTypeMapping mapping : mappings.values()) {

      startMapping(mapping);

      //
      for (PropertyMapping<? extends ValueMapping> propertyMapping : mapping.getPropertyMappings()) {

        ValueMapping valueMapping = propertyMapping.getValueMapping();

        if (valueMapping instanceof SimpleMapping) {
          SimpleMapping<?> simpleMapping = (SimpleMapping)valueMapping;
          PropertyInfo<? extends ValueInfo> propertyInfo = propertyMapping.getInfo();
          JCRMemberMapping memberMapping = simpleMapping.getJCRMember();
          if (memberMapping instanceof JCRPropertyMapping) {
            ValueInfo valueInfo;
            if (propertyInfo instanceof SingleValuedPropertyInfo) {
              valueInfo = ((SingleValuedPropertyInfo<?>)propertyInfo).getValue();
            } else {
              valueInfo = ((MultiValuedPropertyInfo<?>)propertyInfo).getValue();
            }

            //
            if (valueInfo instanceof SimpleValueInfo) {
              propertyMapping((JCRPropertyMapping)memberMapping, (PropertyInfo<SimpleValueInfo>)propertyInfo);
            } else {
              // WTF ?
              throw new AssertionError();
            }
          } else if (memberMapping instanceof JCRNodeAttributeMapping) {
            if (propertyInfo instanceof SingleValuedPropertyInfo) {
              ValueInfo valueInfo = ((SingleValuedPropertyInfo)propertyInfo).getValue();
              if (valueInfo instanceof SimpleValueInfo) {
                SimpleValueInfo simpleValueInfo = (SimpleValueInfo)valueInfo;
                SimpleType simpleType = simpleValueInfo.getSimpleType();
                SimpleTypeKind simpleTypeKind = simpleType.getKind();
                if (simpleTypeKind instanceof SimpleTypeKind.STRING) {
                  // ok
                } else if (simpleTypeKind instanceof SimpleTypeKind.PATH) {
                  // ok
                } else {
                  throw new AssertionError(mapping.getObjectClass().toString() + " wrong simple kind "+ simpleTypeKind);
                }
              } else {
                throw new AssertionError();
              }
            } else {
              throw new AssertionError();
            }
          } else {
            throw new AssertionError(mapping.getObjectClass());
          }
        } else if (valueMapping instanceof RelationshipMapping) {
          RelationshipMapping relationshipMapping = (RelationshipMapping)valueMapping;
          RelationshipType type = ((RelationshipMapping)valueMapping).getType();
          if (valueMapping instanceof OneToManyMapping) {
            if (valueMapping instanceof NamedOneToManyMapping) {
              NamedOneToManyMapping namedOneToManyMapping = (NamedOneToManyMapping)valueMapping;
              switch (type) {
                case REFERENCE:
                  oneToManyByReference(namedOneToManyMapping.getName());
                  break;
                case PATH:
                  oneToManyByPath(namedOneToManyMapping.getName());
                  break;
                default:
                  throw new AssertionError();
              }
            } else {
              switch (type) {
                case HIERARCHIC:
                  oneToManyHierarchic(relationshipMapping.getRelatedType());
                  break;
                default:
                  throw new AssertionError();
              }
            }
          } else if (valueMapping instanceof ManyToOneMapping) {
            ClassTypeInfo relatedType = ((ManyToOneMapping)valueMapping).getRelatedType();
            if (valueMapping instanceof NamedManyToOneMapping) {
              NamedManyToOneMapping namedManyToOneMapping = (NamedManyToOneMapping)valueMapping;
              String name = namedManyToOneMapping.getRelatedName();
              switch (type) {
                case REFERENCE:
                  manyToOneByReference(name, relatedType);
                  break;
                case PATH:
                  manyToOneByPath(name, relatedType);
                  break;
                default:
                  throw new AssertionError();
              }
            } else {
              switch (type) {
                case HIERARCHIC:
                  manyToOneHierarchic(relatedType);
                  break;
                default:
                  throw new AssertionError();
              }
            }
          } else if (valueMapping instanceof OneToOneMapping) {
            if (valueMapping instanceof NamedOneToOneMapping) {
              NamedOneToOneMapping namedOneToOneMapping = (NamedOneToOneMapping)valueMapping;
              String name = namedOneToOneMapping.getName();
              switch (type) {
                case HIERARCHIC:
                  oneToOneHierarchic(name, relationshipMapping.getRelatedType());
                  break;
                case EMBEDDED:
                  // Nothing to do
                  break;
                default:
                  throw new AssertionError();
              }
            } else {
              switch (type) {
                case EMBEDDED:
                  // Nothing to do
                  break;
                default:
                  throw new AssertionError();
              }
            }
          } else {
            throw new AssertionError();
          }
        } else if (valueMapping instanceof PropertyMapMapping) {
          propertyMapMapping();
        } else {
          // WTF ?
          throw new AssertionError();
        }
      }

      //
      endMapping();
    }
  }
}