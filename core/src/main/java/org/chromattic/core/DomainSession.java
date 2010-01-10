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

package org.chromattic.core;

import org.chromattic.api.ChromatticException;
import org.chromattic.api.UndeclaredRepositoryException;
import org.chromattic.core.jcr.LinkType;
import org.chromattic.core.jcr.SessionWrapper;
import org.chromattic.spi.instrument.MethodHandler;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Iterator;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class DomainSession {

  /** . */
  protected final EventBroadcaster broadcaster;

  /** . */
  final Domain domain;

  /** . */
  protected final SessionWrapper sessionWrapper;

  protected DomainSession(Domain domain, SessionWrapper sessionWrapper) {
    this.domain = domain;
    this.broadcaster = new EventBroadcaster();
    this.sessionWrapper = sessionWrapper;
  }

  protected abstract void _setName(EntityContext ctx, String name) throws RepositoryException;

  protected abstract String _persist(EntityContext ctx, String name) throws RepositoryException;

  protected abstract String _persist(EntityContext parentCtx, String name, EntityContext childCtx) throws RepositoryException;

  protected abstract <O> O _create(Class<O> clazz, String name) throws NullPointerException, IllegalArgumentException, RepositoryException;

  protected abstract <E> E _findById(Class<E> clazz, String id) throws RepositoryException;

  protected abstract <E> E _findByNode(Class<E> clazz, Node node) throws RepositoryException;

  protected abstract void _save() throws RepositoryException;

  protected abstract void _remove(EntityContext context) throws RepositoryException;

  protected abstract Object _getReferenced(EntityContext referentCtx, String name, LinkType linkType) throws RepositoryException;

  protected abstract boolean _setReferenced(EntityContext referentCtx, String name, EntityContext referencedCtx, LinkType linkType) throws RepositoryException;

  protected abstract <T> Iterator<T> _getReferents(EntityContext referencedCtx, String name, Class<T> filterClass, LinkType linkType) throws RepositoryException;

  protected abstract void _removeChild(EntityContext ctx, String name) throws RepositoryException;

  protected abstract EntityContext _getChild(EntityContext ctx, String name) throws RepositoryException;

  protected abstract <T> Iterator<T> _getChildren(EntityContext ctx, Class<T> filterClass) throws RepositoryException;

  protected abstract Object _getParent(EntityContext ctx) throws RepositoryException;

  protected abstract <E> E _findByPath(EntityContext ctx, Class<E> clazz, String relPath) throws RepositoryException;

  protected abstract void _orderBefore(EntityContext parentCtx, EntityContext srcCtx, EntityContext dstCtx) throws RepositoryException;

  protected abstract Node _getRoot() throws RepositoryException;

  protected abstract void _move(EntityContext srcCtx, EntityContext dstCtx) throws RepositoryException;

  protected abstract void _addMixin(EntityContext ctx, EmbeddedContext mixinCtx) throws RepositoryException;

  protected abstract EmbeddedContext _getEmbedded(EntityContext ctx, Class<?> embeddedClass) throws RepositoryException;

  protected abstract String _getName(EntityContext ctx) throws RepositoryException;

  protected abstract void _close() throws RepositoryException;

  public void close() {
    try {
      _close();
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public EmbeddedContext getEmbedded(EntityContext ctx, Class<?> embeddedClass) throws UndeclaredRepositoryException {
    try {
      return _getEmbedded(ctx, embeddedClass);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public void save() throws UndeclaredRepositoryException {
    try {
      _save();
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public <E> E findById(Class<E> clazz, String id) throws UndeclaredRepositoryException {
    try {
      return _findById(clazz, id);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public <E> E findByPath(EntityContext ctx, Class<E> clazz, String relPath) throws UndeclaredRepositoryException {
    try {
      return _findByPath(ctx, clazz, relPath);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public <E> E findByNode(Class<E> clazz, Node node) throws UndeclaredRepositoryException {
    try {
      return _findByNode(clazz, node);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public String persist(EntityContext ctx, String name) throws UndeclaredRepositoryException {
    try {
      return _persist(ctx, name);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public <O> O create(Class<O> clazz, String name) throws NullPointerException, IllegalArgumentException, UndeclaredRepositoryException {
    try {
      return _create(clazz, name);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public String getName(EntityContext ctx) throws UndeclaredRepositoryException {
    try {
      return _getName(ctx);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public void addMixin(EntityContext ctx, EmbeddedContext mixinCtx) throws ChromatticException {
    try {
      _addMixin(ctx, mixinCtx);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public final void setName(EntityContext ctx, String name) throws UndeclaredRepositoryException {
    try {
      _setName(ctx, name);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public final void orderBefore(EntityContext parentCtx, EntityContext srcCtx, EntityContext dstCtx) {
    try {
      _orderBefore(parentCtx, srcCtx, dstCtx);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public void move(EntityContext srcCtx, EntityContext dstCtx) throws UndeclaredRepositoryException {
    try {
      _move(srcCtx, dstCtx);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public final void remove(EntityContext context) throws UndeclaredRepositoryException {
    try {
      _remove(context);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public final Object getReferenced(EntityContext referentCtx, String name, LinkType linkType) throws UndeclaredRepositoryException {
    try {
      return _getReferenced(referentCtx, name, linkType);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public final boolean setReferenced(EntityContext referentCtx, String name, EntityContext referencedCtx, LinkType linkType) throws UndeclaredRepositoryException {
    try {
      return _setReferenced(referentCtx, name, referencedCtx, linkType);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public final void removeChild(EntityContext ctx, String name) throws UndeclaredRepositoryException {
    try {
      _removeChild(ctx, name);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public final EntityContext getChild(EntityContext ctx, String name) throws UndeclaredRepositoryException {
    try {
      return _getChild(ctx, name);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public final <T> Iterator<T> getChildren(EntityContext ctx, Class<T> filterClass) throws UndeclaredRepositoryException {
    try {
      return _getChildren(ctx, filterClass);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public final Object getParent(EntityContext ctx) throws UndeclaredRepositoryException {
    try {
      return _getParent(ctx);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public final <T> Iterator<T> getReferents(EntityContext referencedCtx, String name, Class<T> filterClass, LinkType linkType) throws UndeclaredRepositoryException {
    try {
      return _getReferents(referencedCtx, name, filterClass, linkType);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  /**
   * Unwraps the object to an entity context
   *
   * @param o the object to unwrap
   * @return the related entity context
   * @throws NullPointerException if the object is null
   * @throws IllegalArgumentException if the object is not a proxy
   */
  public final EntityContext unwrapEntity(Object o) throws NullPointerException, IllegalArgumentException {
    return unwrap(o, EntityContext.class);
  }

  /**
   * Unwraps the object to an embedded context
   *
   * @param o the object to unwrap
   * @return the related embedded context
   * @throws NullPointerException if the object is null
   * @throws IllegalArgumentException if the object is not a proxy
   */
  public final EmbeddedContext unwrapMixin(Object o) {
    return unwrap(o, EmbeddedContext.class);
  }

  private <T> T unwrap(Object o, Class<T> expectedClass) {
    if (o == null) {
      throw new NullPointerException("Cannot unwrap null object");
    }
    if (expectedClass == null) {
      throw new NullPointerException();
    }
    MethodHandler handler = domain.getInstrumentor().getInvoker(o);
    if (handler == null) {
      throw new IllegalArgumentException("The object with class " + o.getClass().getName() + " is not instrumented");
    }
    if (expectedClass.isInstance(handler)) {
      return expectedClass.cast(handler);
    } else {
      throw new AssertionError("The proxy " + o + " handler is not of the expected type");
    }
  }

  public final String persist(EntityContext parentCtx, EntityContext childCtx, String name) throws UndeclaredRepositoryException {
    try {
      return _persist(parentCtx, name, childCtx);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public final Node getRoot() {
    try {
      return _getRoot();
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public SessionWrapper getSessionWrapper() {
    return sessionWrapper;
  }
}
