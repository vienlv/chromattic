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

package org.chromattic.test.mixin;

import org.chromattic.test.AbstractTestCase;
import org.chromattic.api.ChromatticSession;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.nodetype.NodeType;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class MixinTestCase extends AbstractTestCase {

  protected void createDomain() {
    addClass(TM_A.class);
  }

  public void testPersistent() throws Exception {
    ChromatticSession session = login();

    //
    TM_A a = session.insert(TM_A.class, "tm_a");
    assertEquals(null, a.getMixinValue());
    a.setMixinValue("foo");
    Node rootNode = session.getJCRSession().getRootNode();
    Node aNode = rootNode.getNode("tm_a");
    assertNotNull(aNode);
    Set<String> mixinNames = new HashSet<String>();
    for (NodeType mixinNodeType : aNode.getMixinNodeTypes()) {
      mixinNames.add(mixinNodeType.getName());
    }
    assertTrue(mixinNames.contains("tm_mixin"));
    Property valueProperty = aNode.getProperty("value");
    assertEquals("foo", valueProperty.getString());
  }
}