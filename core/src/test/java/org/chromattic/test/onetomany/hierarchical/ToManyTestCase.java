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

package org.chromattic.test.onetomany.hierarchical;

import org.chromattic.test.AbstractTestCase;
import org.chromattic.core.DomainSession;
import org.chromattic.api.ChromatticSession;
import org.chromattic.api.Status;

import javax.jcr.Node;
import java.util.Collection;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ToManyTestCase extends AbstractToManyTestCase<TOTM_A_1, TOTM_B_1> {

  public Class<TOTM_A_1> getOneSideClass() {
    return TOTM_A_1.class;
  }

  public Class<TOTM_B_1> getManySideClass() {
    return TOTM_B_1.class;
  }

  public Collection<TOTM_B_1> getMany(TOTM_A_1 many) {
    return many.getChildren();
  }
}