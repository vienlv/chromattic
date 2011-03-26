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

package org.chromattic.test.property;

import org.chromattic.test.AbstractTestCase;
import org.chromattic.api.ChromatticSession;

import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class PropertiesTestCase extends AbstractTestCase {

  protected void createDomain() {
    addClass(TP_B.class);
  }

  /** . */
  private ChromatticSession session;

  /** . */
  private TP_B b;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    //
    session = login();
    b = session.insert(TP_B.class, "ozoijeif");
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();

    //
    b = null;
    session.close();
    session = null;
  }

  public void testGetString() throws Exception {
    b.setString("bar");
    Map<String, Object> properties = b.getProperties();
    Object value = properties.get("string_property");
    assertEquals("bar", value);
  }

  public void testPutString() throws Exception {
    Map<String, Object> properties = b.getProperties();
    Object value = properties.put("string_property", "bar");
    assertEquals(null, value);
    assertEquals("bar", b.getString());
  }

  public void testRemoveString() throws Exception {
    b.setString("bar");
    Map<String, Object> properties = b.getProperties();
    Object value = properties.remove("string_property");
    assertEquals("bar", value);
    assertEquals(null, b.getString());
  }

  public void testPutWrongType() throws Exception {
    Map<String, Object> properties = b.getProperties();
    try {
      properties.put("string_property", 5);
      fail();
    }
    catch (ClassCastException ignore) {
    }
  }

  public void testGetInvalidKey() throws Exception {
    Map<String, Object> properties = b.getProperties();
    try {
      properties.get("/invalid");
      fail();
    }
    catch (IllegalArgumentException ignore) {
    }
  }

  public void testRemoveInvalidKey() throws Exception {
    Map<String, Object> properties = b.getProperties();
    try {
      properties.remove("/invalid");
      fail();
    }
    catch (IllegalArgumentException ignore) {
    }
  }

  public void testPutInvalidKey() throws Exception {
    Map<String, Object> properties = b.getProperties();
    try {
      properties.put("/invalid", "foo");
      fail();
    }
    catch (IllegalArgumentException ignore) {
    }
  }
}